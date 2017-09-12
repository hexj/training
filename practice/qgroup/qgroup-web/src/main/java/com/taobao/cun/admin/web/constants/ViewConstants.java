package com.taobao.cun.admin.web.constants;

public class ViewConstants {
	public static final String IS_EDIT = "y"; // 编辑
	public static final String INFO_BASIC = "1"; // 合伙人申请中info_level 为基本信息
	public static final String INFO_ADVANCE = "2";// 合伙人申请中info_level 为详细信息
	public static final String STEP_BASIC = "step1"; // 页面显示步骤序号，基本信息
	public static final String STEP_ADVANCE = "step2";// 页面显示步骤序号，详细信息
	public static final String STEP_BEFORE_EXAM = "step3";// 页面显示步骤序号，报名成功
	
	public static final String PAGE_DETAIL = "/my/copartnerApplyDetail.htm"; // 查看报名进度, 合伙人报名申请详情页

	/** 已经是合伙人 */
	public static final String REPEAT_APPLY_BE_PARTNER = "alreadyBePartner";
	/** 未申请 */
	public static final String REPEAT_APPLY_NOT_APPLY = "notApply";

	/** attchment biz type 身份证正面 */
	public static final String IDCARD_IMGS_FACE_BIZTYPE = "partner_idcard_imgs_face";
	/** attchment biz type 身份证反面 */
	public static final String IDCARD_IMGS_BACK_BIZTYPE = "partner_idcard_imgs_back";
	/** attchment biz type 身份证正反面合成照 */
	public static final String IDCARD_JOINTIMG_BIZTYPE = "partner_idcard_jointimg";

	/** 身份证正面 json 标识 */
	public static final String FACE_IMG_TYPE = "face";
	/** 身份证反面 json 标识 */
	public static final String BACK_IMG_TYPE = "back";
	/** 身份证正反面合成照 json 标识 */
	public static final String JOINT_IMG_TYPE = "joint";

	
	private ViewConstants() {

	}
}
