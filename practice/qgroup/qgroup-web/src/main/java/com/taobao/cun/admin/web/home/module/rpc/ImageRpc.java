package com.taobao.cun.admin.web.home.module.rpc;

import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.citrus.extension.rpc.response.MimeResult;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.adapter.cuntao.center.TrainTpServiceAdapter;
import com.taobao.cun.admin.alipay.util.StringUtils;
import com.taobao.cun.admin.dto.UploadFileDto;
import com.taobao.cun.admin.dto.UploadResultDto;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.service.TfsService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.crius.attachement.domain.Attachement;
import com.taobao.cun.crius.attachement.service.AttachementService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.train.dto.ApplyDetailODto;
import com.taobao.media.tools.ToolsService;
import com.taobao.media.tools.filter.ImageResult;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.service.requestcontext.parser.ParserRequestContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by Fe on 14/11/12.
 */
@WebResource("image")
public class ImageRpc {
	public static final Logger logger = LoggerFactory.getLogger(ImageRpc.class);

	@Resource
	private StationApplyService stationApplyService;
	@Autowired
	private ParserRequestContext parser;
	@Autowired
	private ToolsService pictureTools;
	@Autowired
	private TfsService tfsService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private AttachementService attachmentService;

	/**
	 * 下载第三方招募相关图片 验证是否申请者本人下载
	 * 
	 * @param fileName
	 * @param fileType
	 * @return
	 */
	@ResourceMapping(value = "/doDownLoadImage")
	public MimeResult doDownLoadImage(@RequestParam(name = "fileName") String fileName,
			@RequestParam(name = "fileType") String fileType) {
		if (!checkPermission(fileName, fileType)) {
			return null;
		}
		byte[] bt = stationApplyService.doGetFileFromTfs(fileName, fileType);
		if (bt != null) {
			return new ImageMimeResult(bt, fileType);
		} else {
			return null;
		}
	}

	@ResourceMapping(value = "/doUploadImage", responseContentType = "text/html;charset=gbk")
	public MimeResult doUploadImage() {
		UploadFileDto file = getUploadFileData("filedata");
		ImageUploadResult imageUploadResult = new ImageUploadResult();
		if (file == null || file.getDatas() == null || file.getDatas().length == 0) {
			imageUploadResult.setStatus(ImageUploadResult.STATUS_ERROR);
			return imageUploadResult;
		}
		try {
			ImageResult imageResult = pictureTools.check(new ByteArrayInputStream(file.getDatas()));
			logger.info("pictureTools.check result  : {}", imageResult.isSuccess());
			if (imageResult.isSuccess()) {
				if (!file.isAccordImageType() || file.isGtMaxSize()) {
					imageUploadResult.setStatus(ImageUploadResult.STATUS_ERROR);
				} else {
					UploadResultDto urd = tfsService.uploadFile(imageResult.getData(), file.getType());
					imageUploadResult.setStatus(ImageUploadResult.STATUS_SUCC);
					imageUploadResult.setFsId(urd.getFileName());
					imageUploadResult.setFileType(file.getType());
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
					imageUploadResult.setAttachementId(attRm.getResult());
				}
			} else {
				imageUploadResult.setStatus(ImageUploadResult.STATUS_ERROR);
			}
		} catch (Exception e) {
			logger.error(" check image ioexception ", e);
			imageUploadResult.setStatus(ImageUploadResult.STATUS_ERROR);
		}

		return imageUploadResult;
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
					throw new RuntimeException("getAttachmentByTfsId failed, fsid = " + fileName+", type = " + fileType);
				}
			}
			if (null == attacheRm.getResult()) {
				return false;
			}
			//是否本人上传
			if (attacheRm.getResult().getCreator().equals(String.valueOf(taobaoUserId))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error("checkPermission", e);
			return false;
		}
	}

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

	class ImageMimeResult implements MimeResult {
		public static final String DEFAULT_CHARSET = "UTF-8";

		private String fileType;

		private byte[] data;

		@Override
		public String getCharset() {
			return DEFAULT_CHARSET;
		}

		public ImageMimeResult(byte[] data, String fileType) {
			this.data = data;
			if (fileType.equals("jpg")) {
				fileType = "jpeg";
			}
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
			return "image/" + fileType;
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
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	class ImageUploadResult implements MimeResult {
		public static final String DEFAULT_CHARSET = "UTF-8";
		public static final String DOLWAN_IMAGE_URI = "/image/doDownLoadImage.json";

		public static final int STATUS_SUCC = 1;
		public static final int STATUS_ERROR = 0;
		private int status;
		private String fileType;
		private String fsId;
		private String title;
		private String url;
		private Long attachementId;

		public ImageUploadResult() {
		}

		public ImageUploadResult(int status, String fileType, String fsId, String title, String url) {
			this.status = status;
			this.fileType = fileType;
			this.fsId = fsId;
			this.title = title;
			this.url = url;
		}

		public ImageUploadResult(int status, String fileType, String fsId) {
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
			return "image/" + fileType;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
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
			return DOLWAN_IMAGE_URI + "?fileName=" + fsId + "&fileType=" + fileType;
		}

		public void setUrl(String url) {
			this.url = url;
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
					logger.error(" error , e : {}", e);
				}
			}
		}
	}
}
