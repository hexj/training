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
	 * 资产列表
	 * @param pageNum 开始页
	 * @param pageSize  每页总数
	 * @param stationApplyId 村点id
	 * @param stationStatus 村点状态
	 * @return
	 */
	@ResourceMapping("/getAssetList")
	public AjaxPagedResult<List<CuntaoAssetDto>> getAssetList(@RequestParam(name = "pageNum") int pageNum, 
			@RequestParam(name = "pageSize") int pageSize,
			@RequestParam(name = "stationApplyId") String stationApplyId,
			@RequestParam(name = "stationStatus") String stationStatus) {
		if (StringUtils.isEmpty(stationApplyId)|| StringUtils.isEmpty(stationStatus)) {
			return AjaxPagedResult.unSuccess("参数异常！");
		}
		AjaxPagedResult<List<CuntaoAssetDto>> result = new AjaxPagedResult<List<CuntaoAssetDto>>();
		//合伙人状态为“已冻结待处理-待退出审批“之间所有状态，且村点有对应资产则展示
		if ( !checkStatus(stationStatus)) {
			return AjaxPagedResult.unSuccess("合伙人状态为“已冻结待处理-待退出审批”之间状态时，才显示资产信息！");
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
				return AjaxPagedResult.unSuccess("查询资产信息异常！");
			}

			List<CuntaoAssetDto> list = assetDtoList.getResult();
			result.setData(list);
			Long totalPage = (assetDtoList.getTotalResultSize() - 1) / pageSize + 1;
			result.setTotalPage(totalPage);
			result.setPage(pageNum);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			logger.error("getAssetsList error！param:"+JSON.toJSONString(cuntaoAssetQueryCondition) +" errorMessage:"+e.getMessage());
			return AjaxPagedResult.unSuccess("查询资产信息异常！");
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
	 * 资产签收
	 * @param assetId 资产表主键id
	 * @param applierName 合伙人名字
	 * @return boolean 成功失败
	 */
	@Security(checkCSRF=true)
	@ResourceMapping("/signAsset")
	public AjaxResult<Boolean> signAsset(@RequestParam(name = "assetId") String assetId,@RequestParam(name = "applierName") String applierName) {
		if (StringUtils.isEmpty(assetId) || StringUtils.isEmpty(applierName)) {
			return AjaxResult.unSuccess("参数异常！");
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
				return AjaxResult.unSuccess("资产签收异常！");
			}
			return AjaxResult.success(true);
		} catch (Exception e) {
			logger.error("signAsset error : assetId: " + assetId +" errorMessage:"+e.getMessage());
			return AjaxResult.unSuccess("资产签收异常！");
		}
	}
	
	/**
	 * 资产盘点
	 * @param assetId 资产表主键id
	 * @return boolean 成功失败
	 */
	@Security(checkCSRF=true)
	@ResourceMapping("/checkAsset")
	public AjaxResult<Boolean> checkAsset(@RequestParam(name = "assetId") String assetId, @RequestParam(name = "applierName") String applierName) {
		if (StringUtils.isEmpty(assetId)) {
			return AjaxResult.unSuccess("参数异常！");
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
				return AjaxResult.unSuccess("资产盘点异常！");
			}
			return AjaxResult.success(true);
		} catch (Exception e) {
			logger.error("assetCheck error : assetId: " + assetId +" errorMessage:"+e.getMessage());
			return AjaxResult.unSuccess("资产盘点异常！");
		}
	}
	
	/**
	 * 修改资产
	 * @param assetNum 大阿里编号或者序列号
	 * @param stationApplyId 村点id
	 * @param applierName 合伙人名字
	 * @return String noData：没有数据 前端提示是否上报，notMyAsset：不是自己的资产，前端提示重新输入。success：您添加的资产已经在下方展示，不能重复添加！
	 *                 hasCommit:已经上报， setDataToMe:历史数据归属给我
	 */
	@Security(checkCSRF=true)
	@ResourceMapping("/modifyAsset")
	public AjaxResult<String> modifyAsset(@RequestParam(name = "assetNum") String assetNum,
			@RequestParam(name = "stationApplyId") String stationApplyId,
			@RequestParam(name = "stationName") String stationName,
			@RequestParam(name = "applierName") String applierName,
			@RequestParam(name = "countyId") String countyId) {
		if (StringUtils.isEmpty(stationApplyId) || StringUtils.isEmpty(assetNum)|| StringUtils.isEmpty(applierName)|| StringUtils.isEmpty(stationName)|| StringUtils.isEmpty(countyId)) {
			return AjaxResult.unSuccess("参数异常！");
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
				return AjaxResult.unSuccess("系统异常！");
			}
			if (assetDtoList.getResult()== null || assetDtoList.getResult().size() <= 0){
				cuntaoAssetQueryCondition.setAliNo(null);
				cuntaoAssetQueryCondition.setSerialNo(assetNum);
				assetDtoList = cuntaoAssetService.queryByPage(cuntaoAssetQueryCondition, contextDto);
				if(!assetDtoList.isSuccess()) {
					logger.error("modifyAsset.queryByPage  assetNum: " + assetNum +" errorMessage:",assetDtoList.getException());
					return AjaxResult.unSuccess("系统异常！");
				}
				if (assetDtoList.getResult()== null || assetDtoList.getResult().size() <= 0){
					return AjaxResult.success("noData");
				}
			}
		} catch (Exception e) {
			logger.error("modifyAsset.queryByPage  assetNum: " + assetNum +" errorMessage:",e);
			return AjaxResult.unSuccess("系统异常！");
		}
		CuntaoAssetDto asset = assetDtoList.getResult().get(0);
		//已经上报 不在重复插入数据
		if(CuntaoAssetEnum.UNMATCH.getCode().equals(asset.getStatus()) && applierName.equals(asset.getOperator())) {
			return AjaxResult.success("hasCommit");
		}
		//不是自己的数据，不能操作
		if (asset.getStationId() != null && !stationApplyId.equals(asset.getStationId())) {
			return AjaxResult.success("notMyAsset");
		}
		if (StringUtils.isEmpty(asset.getStationId())) {//已资产表中，没有stationId
			CuntaoAssetDto assetDto = new CuntaoAssetDto();
			assetDto.setId(asset.getId());
			assetDto.setStationId(stationApplyId);
			assetDto.setStationName(stationName);
			try {
				ResultModel<Integer> resModel = cuntaoAssetService.updateCuntaoAsset(assetDto, contextDto);
				if (!resModel.isSuccess()) {
					logger.error("modifyAsset.updateCuntaoAsset error : assetId: " + asset.getId() ,resModel.getException());
					return AjaxResult.unSuccess("资产编辑异常！");
				}
				return AjaxResult.success("setDataToMe");
			} catch (Exception e) {
				logger.error("modifyAsset.updateCuntaoAsset error : assetId: " + asset.getId() +" errorMessage:"+e.getMessage());
				return AjaxResult.unSuccess("资产编辑异常！");
			}
		}
		return AjaxResult.success("success");
	}
	
	/**
	 * 增加资产
	 * @param assetNum 大阿里编号或者序列号
	 * @param applierName 合伙人名字
	 * @param countyName 县点名字
	 * @param stationName 村点名字
	 * @param stationApplyId 村点id
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
			return AjaxResult.unSuccess("参数异常！");
		}
		
		ContextDto contextDto = assemberContext(getUserId());
		CuntaoAssetDto cuntaoAssetDto = new CuntaoAssetDto();
		//存储信息：填写的标号，村服务点，县运营中心，合伙人姓名，操作时间，操作人角色：合伙人，资产状态：未匹配。
		cuntaoAssetDto.setAliNo(assetNum);//大阿里编号
		cuntaoAssetDto.setStatus(CuntaoAssetEnum.UNMATCH.getCode());//资产状态：未匹配。
		cuntaoAssetDto.setCheckStatus(CuntaoAssetEnum.UNCHECKED.getCode());//资产状态：未匹配。
		cuntaoAssetDto.setOperateTime(new Date());//操作时间
		cuntaoAssetDto.setOperatorRole(CuntaoAssetEnum.PARTNER.getCode());//操作人角色
		//cuntaoAssetDto.setStationId(stationApplyId);//村点申请单id
		cuntaoAssetDto.setStationName(stationName);//服务点名称
		cuntaoAssetDto.setCounty(countyName);
		cuntaoAssetDto.setOperator(applierName);//合伙人姓名
		cuntaoAssetDto.setOrgId(countyId);
		try {
			ResultModel<Long> resModel = cuntaoAssetService.saveCuntaoAsset(cuntaoAssetDto, contextDto);
			if (!resModel.isSuccess()) {
				logger.error("addAsset.saveCuntaoAsset error : cuntaoAssetDto: " + JSON.toJSONString(contextDto),resModel.getException());
				return AjaxResult.unSuccess("资产增加异常！");
			}
			return AjaxResult.success(resModel.getResult());
		} catch (Exception e) {
			logger.error("addAsset.saveCuntaoAsset error : cuntaoAssetDto: " + JSON.toJSONString(contextDto),e);
			return AjaxResult.unSuccess("资产增加异常！");
		}
	}
}
