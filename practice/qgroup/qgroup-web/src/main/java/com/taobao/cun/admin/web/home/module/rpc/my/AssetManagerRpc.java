package com.taobao.cun.admin.web.home.module.rpc.my;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.common.ajax.AjaxPagedResult;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.common.PageConditionDto;
import com.taobao.cun.common.resultmodel.PagedResultModel;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.SystemTypeEnum;
import com.taobao.cun.dto.UserTypeEnum;
import com.taobao.cun.dto.asset.CuntaoAssetDto;
import com.taobao.cun.dto.asset.CuntaoAssetEnum;
import com.taobao.cun.dto.asset.CuntaoAssetQueryCondition;
import com.taobao.cun.service.asset.CuntaoAssetService;

@WebResource("assets")
public class AssetManagerRpc extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AssetManagerRpc.class);
	
	@Resource
	CuntaoAssetService cuntaoAssetService;
	
	@Autowired
	private StationApplyService stationApplyService;
	
	/**
	 * �ʲ��б�
	 * @param pageNum ��ʼҳ
	 * @param pageSize  ÿҳ����
	 * @param stationApplyId ���id
	 * @param stationStatus ���״̬
	 * @return
	 */
	@ResourceMapping("/getAssetList")
	public AjaxPagedResult<List<CuntaoAssetDto>> getAssetList(@RequestParam(name = "pageNum") int pageNum, 
			@RequestParam(name = "pageSize") int pageSize,
			@RequestParam(name = "stationApplyId") String stationApplyId,
			@RequestParam(name = "stationStatus") String stationStatus) {
		if (StringUtils.isEmpty(stationApplyId)|| StringUtils.isEmpty(stationStatus)) {
			return AjaxPagedResult.unSuccess("�����쳣��");
		}
		AjaxPagedResult<List<CuntaoAssetDto>> result = new AjaxPagedResult<List<CuntaoAssetDto>>();
		//�ϻ���״̬Ϊ���Ѷ��������-���˳�������֮������״̬���Ҵ���ж�Ӧ�ʲ���չʾ
		if ( !checkStatus(stationStatus)) {
			return AjaxPagedResult.unSuccess("�ϻ���״̬Ϊ���Ѷ��������-���˳�������֮��״̬ʱ������ʾ�ʲ���Ϣ��");
		}
		PageConditionDto page = new PageConditionDto();
		if (pageNum <= 0)
			pageNum = 1;
		if (pageSize <= 0)
			pageSize = 3;
		page.setSize(pageSize);
		page.setStart((pageNum - 1) * pageSize);
		
		CuntaoAssetQueryCondition cuntaoAssetQueryCondition = new CuntaoAssetQueryCondition();
		cuntaoAssetQueryCondition.setStationId(stationApplyId);
		cuntaoAssetQueryCondition.setPage(page);
		List<CuntaoAssetEnum> statusList = new ArrayList<CuntaoAssetEnum>();
		statusList.add(CuntaoAssetEnum.WAIT_STATION_SIGN);
		statusList.add(CuntaoAssetEnum.STATION_SIGN);
		cuntaoAssetQueryCondition.setStates(statusList);
		ContextDto contextDto = assemberContext(getUserId());
		try {
			PagedResultModel<List<CuntaoAssetDto>> assetDtoList = cuntaoAssetService.queryByPage(cuntaoAssetQueryCondition, contextDto);
			if (!assetDtoList.isSuccess()) {
				logger.error("getAssetsList error! queryCondition:" + JSON.toJSONString(cuntaoAssetQueryCondition),assetDtoList.getException());
				return AjaxPagedResult.unSuccess("��ѯ�ʲ���Ϣ�쳣��");
			}

			List<CuntaoAssetDto> list = assetDtoList.getResult();
			result.setData(list);
			Long totalPage = (assetDtoList.getTotalResultSize() - 1) / pageSize + 1;
			result.setTotalPage(totalPage);
			result.setPage(pageNum);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			logger.error("getAssetsList error��param:"+JSON.toJSONString(cuntaoAssetQueryCondition) +" errorMessage:"+e.getMessage());
			return AjaxPagedResult.unSuccess("��ѯ�ʲ���Ϣ�쳣��");
		}
	}
	
	private boolean  checkStatus(String status) {
		if (StringUtils.isEmpty(status)) {
			return false;
		}
		List<String>  statusList  = new ArrayList<String>();
		statusList.add("FROZEN");
		statusList.add("DECORATING");
		statusList.add("SERVICING");
		statusList.add("QUIT_APPLYING");
		statusList.add("QUIT_APPLY_CONFIRMED");
		statusList.add("QUITAUDITING");
		if (statusList.contains(status)) {
			return true;
		}
		return false;
	}
	
	private static ContextDto assemberContext(Long loginId) {
		ContextDto context = new ContextDto();

		context.setLoginId(String.valueOf(loginId));
		context.setAppId(String.valueOf(loginId));
		context.setUserType(UserTypeEnum.HAVANA);
		context.setSystemType(SystemTypeEnum.CUNTAO_ADMIN);
		return context;
	}
	
	/**
	 * �ʲ�ǩ��
	 * @param assetId �ʲ�������id
	 * @param applierName �ϻ�������
	 * @return boolean �ɹ�ʧ��
	 */
	@Security(checkCSRF=true)
	@ResourceMapping("/signAsset")
	public AjaxResult<Boolean> signAsset(@RequestParam(name = "assetId") String assetId,@RequestParam(name = "applierName") String applierName) {
		if (StringUtils.isEmpty(assetId) || StringUtils.isEmpty(applierName)) {
			return AjaxResult.unSuccess("�����쳣��");
		}
		CuntaoAssetDto assetDto = new CuntaoAssetDto();
		assetDto.setId(Long.parseLong(assetId));
		assetDto.setStatus(CuntaoAssetEnum.STATION_SIGN.getCode());
		assetDto.setReceiver(applierName);
		assetDto.setOperator(applierName);
		assetDto.setOperatorRole(CuntaoAssetEnum.PARTNER.getCode());
		assetDto.setOperateTime(new Date());
		ContextDto contextDto = assemberContext(getUserId());
		try {
			ResultModel<Integer> resModel = cuntaoAssetService.updateCuntaoAsset(assetDto, contextDto);
			if (!resModel.isSuccess()) {
				logger.error("signAsset error : assetId: " + assetId ,resModel.getException());
				return AjaxResult.unSuccess("�ʲ�ǩ���쳣��");
			}
			return AjaxResult.success(true);
		} catch (Exception e) {
			logger.error("signAsset error : assetId: " + assetId +" errorMessage:"+e.getMessage());
			return AjaxResult.unSuccess("�ʲ�ǩ���쳣��");
		}
	}
	
	/**
	 * �ʲ��̵�
	 * @param assetId �ʲ�������id
	 * @return boolean �ɹ�ʧ��
	 */
	@Security(checkCSRF=true)
	@ResourceMapping("/checkAsset")
	public AjaxResult<Boolean> checkAsset(@RequestParam(name = "assetId") String assetId, @RequestParam(name = "applierName") String applierName) {
		if (StringUtils.isEmpty(assetId)) {
			return AjaxResult.unSuccess("�����쳣��");
		}
		CuntaoAssetDto assetDto = new CuntaoAssetDto();
		assetDto.setId(Long.parseLong(assetId));
		assetDto.setCheckStatus(CuntaoAssetEnum.CHECKED.getCode());
		assetDto.setCheckTime(new Date());
		assetDto.setCheckOperator(applierName);
		assetDto.setCheckRole(CuntaoAssetEnum.PARTNER.getCode());
		ContextDto contextDto = assemberContext(getUserId());
		try {
			ResultModel<Integer> resModel = cuntaoAssetService.updateCuntaoAsset(assetDto, contextDto);
			if (!resModel.isSuccess()) {
				logger.error("assetCheck error : assetId: " + assetId ,resModel.getException());
				return AjaxResult.unSuccess("�ʲ��̵��쳣��");
			}
			return AjaxResult.success(true);
		} catch (Exception e) {
			logger.error("assetCheck error : assetId: " + assetId +" errorMessage:"+e.getMessage());
			return AjaxResult.unSuccess("�ʲ��̵��쳣��");
		}
	}
	
	/**
	 * �޸��ʲ�
	 * @param assetNum �����Ż������к�
	 * @param stationApplyId ���id
	 * @param applierName �ϻ�������
	 * @return String noData��û������ ǰ����ʾ�Ƿ��ϱ���notMyAsset�������Լ����ʲ���ǰ����ʾ�������롣success������ӵ��ʲ��Ѿ����·�չʾ�������ظ���ӣ�
	 *                 hasCommit:�Ѿ��ϱ��� setDataToMe:��ʷ���ݹ�������
	 */
	@Security(checkCSRF=true)
	@ResourceMapping("/modifyAsset")
	public AjaxResult<String> modifyAsset(@RequestParam(name = "assetNum") String assetNum,
			@RequestParam(name = "stationApplyId") String stationApplyId,
			@RequestParam(name = "stationName") String stationName,
			@RequestParam(name = "applierName") String applierName,
			@RequestParam(name = "countyId") String countyId) {
		if (StringUtils.isEmpty(stationApplyId) || StringUtils.isEmpty(assetNum)|| StringUtils.isEmpty(applierName)|| StringUtils.isEmpty(stationName)|| StringUtils.isEmpty(countyId)) {
			return AjaxResult.unSuccess("�����쳣��");
		}
		CuntaoAssetQueryCondition cuntaoAssetQueryCondition = new CuntaoAssetQueryCondition();
		cuntaoAssetQueryCondition.setAliNo(assetNum);
		List<CuntaoAssetEnum> statusList = new ArrayList<CuntaoAssetEnum>();
		statusList.add(CuntaoAssetEnum.WAIT_STATION_SIGN);
		statusList.add(CuntaoAssetEnum.STATION_SIGN);
		statusList.add(CuntaoAssetEnum.UNMATCH);
		cuntaoAssetQueryCondition.setStates(statusList);
		cuntaoAssetQueryCondition.setOrgId(Long.parseLong(countyId));
		ContextDto contextDto = assemberContext(getUserId());
		
		PagedResultModel<List<CuntaoAssetDto>> assetDtoList;
		try {
			assetDtoList = cuntaoAssetService.queryByPage(cuntaoAssetQueryCondition, contextDto);
			if(!assetDtoList.isSuccess()) {
				logger.error("modifyAsset.queryByPage  assetNum: " + assetNum +" errorMessage:",assetDtoList.getException());
				return AjaxResult.unSuccess("ϵͳ�쳣��");
			}
			if (assetDtoList.getResult()== null || assetDtoList.getResult().size() <= 0){
				cuntaoAssetQueryCondition.setAliNo(null);
				cuntaoAssetQueryCondition.setSerialNo(assetNum);
				assetDtoList = cuntaoAssetService.queryByPage(cuntaoAssetQueryCondition, contextDto);
				if(!assetDtoList.isSuccess()) {
					logger.error("modifyAsset.queryByPage  assetNum: " + assetNum +" errorMessage:",assetDtoList.getException());
					return AjaxResult.unSuccess("ϵͳ�쳣��");
				}
				if (assetDtoList.getResult()== null || assetDtoList.getResult().size() <= 0){
					return AjaxResult.success("noData");
				}
			}
		} catch (Exception e) {
			logger.error("modifyAsset.queryByPage  assetNum: " + assetNum +" errorMessage:",e);
			return AjaxResult.unSuccess("ϵͳ�쳣��");
		}
		CuntaoAssetDto asset = assetDtoList.getResult().get(0);
		//�Ѿ��ϱ� �����ظ���������
		if(CuntaoAssetEnum.UNMATCH.getCode().equals(asset.getStatus()) && applierName.equals(asset.getOperator())) {
			return AjaxResult.success("hasCommit");
		}
		//�����Լ������ݣ����ܲ���
		if (asset.getStationId() != null && !stationApplyId.equals(asset.getStationId())) {
			return AjaxResult.success("notMyAsset");
		}
		if (StringUtils.isEmpty(asset.getStationId())) {//���ʲ����У�û��stationId
			CuntaoAssetDto assetDto = new CuntaoAssetDto();
			assetDto.setId(asset.getId());
			assetDto.setStationId(stationApplyId);
			assetDto.setStationName(stationName);
			try {
				ResultModel<Integer> resModel = cuntaoAssetService.updateCuntaoAsset(assetDto, contextDto);
				if (!resModel.isSuccess()) {
					logger.error("modifyAsset.updateCuntaoAsset error : assetId: " + asset.getId() ,resModel.getException());
					return AjaxResult.unSuccess("�ʲ��༭�쳣��");
				}
				return AjaxResult.success("setDataToMe");
			} catch (Exception e) {
				logger.error("modifyAsset.updateCuntaoAsset error : assetId: " + asset.getId() +" errorMessage:"+e.getMessage());
				return AjaxResult.unSuccess("�ʲ��༭�쳣��");
			}
		}
		return AjaxResult.success("success");
	}
	
	/**
	 * �����ʲ�
	 * @param assetNum �����Ż������к�
	 * @param applierName �ϻ�������
	 * @param countyName �ص�����
	 * @param stationName �������
	 * @param stationApplyId ���id
	 * @return Long assetId
	 */
	@Security(checkCSRF=true)
	@ResourceMapping("/addAsset")
	public AjaxResult<Long> addAsset(@RequestParam(name = "assetNum") String assetNum,
			@RequestParam(name = "applierName") String applierName,
			@RequestParam(name = "countyName") String countyName,
			@RequestParam(name = "stationName") String stationName,
			@RequestParam(name = "stationApplyId") String stationApplyId,
			@RequestParam(name = "countyId") String countyId) {
		if (StringUtils.isEmpty(assetNum) || StringUtils.isEmpty(applierName)|| StringUtils.isEmpty(countyName)
				|| StringUtils.isEmpty(stationName)|| StringUtils.isEmpty(stationApplyId)) {
			return AjaxResult.unSuccess("�����쳣��");
		}
		
		ContextDto contextDto = assemberContext(getUserId());
		CuntaoAssetDto cuntaoAssetDto = new CuntaoAssetDto();
		//�洢��Ϣ����д�ı�ţ������㣬����Ӫ���ģ��ϻ�������������ʱ�䣬�����˽�ɫ���ϻ��ˣ��ʲ�״̬��δƥ�䡣
		cuntaoAssetDto.setAliNo(assetNum);//������
		cuntaoAssetDto.setStatus(CuntaoAssetEnum.UNMATCH.getCode());//�ʲ�״̬��δƥ�䡣
		cuntaoAssetDto.setCheckStatus(CuntaoAssetEnum.UNCHECKED.getCode());//�ʲ�״̬��δƥ�䡣
		cuntaoAssetDto.setOperateTime(new Date());//����ʱ��
		cuntaoAssetDto.setOperatorRole(CuntaoAssetEnum.PARTNER.getCode());//�����˽�ɫ
		//cuntaoAssetDto.setStationId(stationApplyId);//������뵥id
		cuntaoAssetDto.setStationName(stationName);//���������
		cuntaoAssetDto.setCounty(countyName);
		cuntaoAssetDto.setOperator(applierName);//�ϻ�������
		cuntaoAssetDto.setOrgId(countyId);
		try {
			ResultModel<Long> resModel = cuntaoAssetService.saveCuntaoAsset(cuntaoAssetDto, contextDto);
			if (!resModel.isSuccess()) {
				logger.error("addAsset.saveCuntaoAsset error : cuntaoAssetDto: " + JSON.toJSONString(contextDto),resModel.getException());
				return AjaxResult.unSuccess("�ʲ������쳣��");
			}
			return AjaxResult.success(resModel.getResult());
		} catch (Exception e) {
			logger.error("addAsset.saveCuntaoAsset error : cuntaoAssetDto: " + JSON.toJSONString(contextDto),e);
			return AjaxResult.unSuccess("�ʲ������쳣��");
		}
	}
}
