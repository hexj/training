package com.taobao.cun.admin.web.vo;

public class FileUploadVo {
	public static final String DOLWAN_ATTACHMENT_URI = "/attachment/doDownLoadAttachment.json";
	public static final String DOLWAN_IMAGE_URI = "/image/doDownLoadImage.json";

	private Long attachementId; // ��������Id
	private String title;// ��������
	private String fileType;// ��������
	private String fsId;// ����id

	private String url;
	private String downloadUrl;

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
		this.url = DOLWAN_IMAGE_URI + "?fileName=" + fsId + "&fileType=" + fileType + "&title=" + title;
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDownloadUrl() {
		this.downloadUrl = DOLWAN_ATTACHMENT_URI + "?fileName=" + fsId + "&fileType=" + fileType + "&title=" + title;
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public Long getAttachementId() {
		return attachementId;
	}

	public void setAttachementId(Long attachementId) {
		this.attachementId = attachementId;
	}
}
