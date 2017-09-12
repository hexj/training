package com.taobao.cun.admin.web.home.module.rpc;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.citrus.extension.rpc.response.MimeResult;
import com.alibaba.citrus.service.requestcontext.parser.ParserRequestContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.adapter.cuntao.center.TrainTpServiceAdapter;
import com.taobao.cun.admin.alipay.util.StringUtils;
import com.taobao.cun.admin.dto.UploadFileDto;
import com.taobao.cun.admin.dto.UploadResultDto;
import com.taobao.cun.admin.service.TfsService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.train.dto.ApplyDetailODto;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taobao.cun.crius.attachement.domain.Attachement;
import com.taobao.cun.crius.attachement.service.AttachementService;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;

@WebResource("attachment")
public class AttachmentRpc {

	public static final Logger logger = LoggerFactory.getLogger(AttachmentRpc.class);

	@Autowired
	private ParserRequestContext parser;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private TfsService tfsService;
	@Autowired
	private AttachementService attachmentService;

	/**
	 * 上传附件
	 * 
	 * @return
	 */
	@ResourceMapping(value = "/doUploadAttachment", responseContentType = "text/html;charset=gbk")
	public MimeResult doUploadAttachment() {
		UploadFileDto file = getUploadFileData("filedata");
		AttachmentUploadMimeResult attachmentMimeResult = new AttachmentUploadMimeResult();
		if (file == null || file.getDatas() == null || file.getDatas().length == 0) {
			attachmentMimeResult.setStatus(AttachmentUploadMimeResult.STATUS_ERROR);
			return attachmentMimeResult;
		}
		try {
			if (!file.isAccordFileType()) {
				attachmentMimeResult.setStatus(AttachmentUploadMimeResult.STATUS_ERROR);
				attachmentMimeResult.setMsg(AttachmentUploadMimeResult.MSG_EXT);
			} else if (file.isGtMaxSize()) {
				attachmentMimeResult.setStatus(AttachmentUploadMimeResult.STATUS_ERROR);
				attachmentMimeResult.setMsg(AttachmentUploadMimeResult.MSG_MAX);
			} else {
				UploadResultDto urd = tfsService.uploadFile(file.getDatas(), file.getType());
				Long taobaoUserId = WebUtil.getUserId(request.getSession());
				//save attachment to db
				Attachement att = new Attachement();
				att.setCreator(String.valueOf(taobaoUserId));
				att.setModifier(String.valueOf(taobaoUserId));
				att.setFsId(urd.getFileName());
				att.setFileType(file.getType());
				att.setTitle(file.getFileName());
				Date now = new Date();
				att.setGmtCreate(now);
				att.setGmtModified(now);
				att.setIsDeleted("n");
				ResultModel<Long> attRm = attachmentService.addAttachement(att);
				if (!attRm.isSuccess()) {
					throw new RuntimeException("addAttachement failed" + JSON.toJSONString(attRm));
				}
				attachmentMimeResult.setAttachementId(attRm.getResult());
				attachmentMimeResult.setStatus(AttachmentUploadMimeResult.STATUS_SUCC);
				attachmentMimeResult.setFsId(urd.getFileName());
				attachmentMimeResult.setFileType(file.getType());
				attachmentMimeResult.setTitle(file.getFileName());
			}
		} catch (Exception e) {
			logger.error("doUploadAttachment error , e :{}", e);
			attachmentMimeResult.setStatus(AttachmentUploadMimeResult.STATUS_ERROR);
		}

		return attachmentMimeResult;
	}

	/**
	 * 下载第三方招募相关附件 验证是否申请者本人的请求
	 * 
	 * @param fileName
	 * @param fileType
	 * @param title
	 * @return
	 */
	@ResourceMapping(value = "/doDownLoadAttachment")
	public MimeResult doDownLoadTpAttachment(@RequestParam(name = "fileName") String fileName,
			@RequestParam(name = "fileType") String fileType, @RequestParam(name = "title") String title) {
		logger.info("tfsService.doGetFileFromTfs param : fileName :{} , fileType : {}", fileName, fileType);
		byte[] bt = tfsService.doGetFileFromTfs(fileName, fileType);
		if (null == bt) {
			logger.info("tfsService.doGetFileFromTfs result is null! tfsId = {?}, fileType = {?}", fileName, fileType);
			return null;
		}
		AttachmentMimeResult attachmentMimeResult = null;
		try {
			if (!checkPermission(fileName, fileType)) {
				return null;
			}
			response.addHeader("Content-Disposition", "attachment;filename=" + getFileName(title));
			attachmentMimeResult = new AttachmentMimeResult(bt, fileType);

		} catch (Exception e) {
			logger.error("doDownLoadAttachment error , e :", e);
		}
		return attachmentMimeResult;
	}

	/**
	 * 验证权限
	 * 
	 * @param fileName
	 * @param fileType
	 * @return
	 */
	private boolean checkPermission(String fileName, String fileType) {
		try {
			if (StringUtils.isEmpty(fileName)) {
				return false;
			}
			Long taobaoUserId = WebUtil.getUserId(request.getSession());
			ResultModel<Attachement> attacheRm = attachmentService.getAttachmentByTfsId(fileName, fileType);
			if (!attacheRm.isSuccess()) {
				if (null != attacheRm.getException()) {
					throw attacheRm.getException();
				} else {
					throw new RuntimeException(
							"getAttachmentByTfsId failed, fsid = " + fileName + ", type = " + fileType);
				}
			}
			if (null == attacheRm.getResult()) {
				return false;
			}
			// 是否本人上传
			if (attacheRm.getResult().getCreator().equals(String.valueOf(taobaoUserId))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error("checkPermission", e);
			return false;
		}
	}

	/**
	 * 获取文件名
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private String getFileName(String fileName) throws Exception {
		String Agent = request.getHeader("User-Agent");
		String name = "";
		if (Agent.indexOf("Firefox") != -1 || Agent.indexOf("Safari") != -1) {
			name = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		} else {
			name = URLEncoder.encode(fileName, "UTF-8");
		}
		return name;
	}

	/**
	 * 从请求中获得UploadFileDto对象
	 * 
	 * @param type
	 * @return
	 */
	private UploadFileDto getUploadFileData(String type) {
		UploadFileDto file = null;
		try {
			FileItem fileItem = parser.getParameters().getFileItem(type);
			if (fileItem != null) {
				file = new UploadFileDto();
				byte[] datas = IOUtils.toByteArray(fileItem.getInputStream());
				file.setDatas(datas);
				file.setFileName(fileItem.getName());
				file.setType(FilenameUtils.getExtension(fileItem.getName()).toLowerCase());
			}
		} catch (Exception e1) {
			logger.error("getUploadFileData IOException,", e1);
		}
		return file;
	}

	class AttachmentUploadMimeResult implements MimeResult {
		public static final String DEFAULT_CHARSET = "UTF-8";
		public static final String DOLWAN_ATTACHMENT_URI = "/attachment/doDownLoadAttachment.json";

		public static final int STATUS_SUCC = 1;
		public static final int STATUS_ERROR = 0;

		public static final String MSG_MAX = "文件超出最大限制，上传失败！";
		public static final String MSG_EXT = "文件格式错误，上传失败！";

		private int status;
		private String fileType;
		private String fsId;
		private String title;
		private String url;
		private String msg;
		private Long attachementId;

		AttachmentUploadMimeResult() {
		}

		AttachmentUploadMimeResult(int status, String fileType, String fsId) {
			this.status = status;
			this.fileType = fileType;
			this.fsId = fsId;
		}

		@Override
		@JSONField(serialize = false)
		public String getCharset() {
			return DEFAULT_CHARSET;
		}

		@Override
		@JSONField(serialize = false)
		public String getContentType() {
			return "application/octet-stream";
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}

		public String getFsId() {
			return fsId;
		}

		public void setFsId(String fsId) {
			this.fsId = fsId;
		}

		public String getUrl() {
			return DOLWAN_ATTACHMENT_URI + "?fileName=" + fsId + "&fileType=" + fileType;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Long getAttachementId() {
			return attachementId;
		}

		public void setAttachementId(Long attachementId) {
			this.attachementId = attachementId;
		}

		@Override
		public void write(OutputStream out) {
			try {
				out.write(JSONObject.toJSONString(this).getBytes());
				out.flush();
			} catch (Exception e) {
				logger.error(" error , e : {}", e);
			} finally {
				try {
					out.close();
				} catch (Exception e) {
					logger.error("close error , e : {}", e);
				}
			}
		}
	}

	class AttachmentMimeResult implements MimeResult {
		public static final String DEFAULT_CHARSET = "UTF-8";

		private String fileType;

		private byte[] data;

		@Override
		public String getCharset() {
			return DEFAULT_CHARSET;
		}

		public AttachmentMimeResult(byte[] data, String fileType) {
			this.data = data;
			this.fileType = fileType;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.alibaba.citrus.extension.rpc.response.MimeResult#getContentType()
		 */
		@Override
		public String getContentType() {
			return fileType;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.alibaba.citrus.extension.rpc.response.MimeResult#write(java.io
		 * .OutputStream)
		 */
		@Override
		public void write(OutputStream out) {
			try {
				out.write(data);
				out.flush();
			} catch (Exception e) {
				logger.error(" error , e : {}", e);
			} finally {
				try {
					out.close();
				} catch (Exception e) {
					logger.error("close error , e : {}", e);
				}
			}
		}
	}

}
