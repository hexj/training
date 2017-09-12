package com.taobao.cun.admin.web.home.module.rpc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.agentorder.AgentOrderAgentServiceAdapter;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.station.StationServiceAdapter;
import com.taobao.cun.admin.stationbuyer.StationBuyerServiceAdapter;
import com.taobao.cun.admin.vo.AgentAgentOrderCountVo;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.admin.web.vo.agentorder.AegentOrderListDetailVo;
import com.taobao.cun.admin.web.vo.agentorder.AgentOrderPagedVo;
import com.taobao.cun.common.PageConditionDto;
import com.taobao.cun.common.exception.BusinessException;
import com.taobao.cun.common.exception.ServiceException;
import com.taobao.cun.common.resultmodel.PagedResultModel;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.HavanaContext;
import com.taobao.cun.dto.StationHavanaContext;
import com.taobao.cun.dto.agentorder.AgentOrderAgentPagedQueryConditionDto;
import com.taobao.cun.dto.agentorder.AgentOrderDetailDto;
import com.taobao.cun.dto.agentorder.AgentOrderUpdateDto;
import com.taobao.cun.dto.agentorder.enums.AgentOrderBuyerCountEnum;
import com.taobao.cun.dto.agentorder.enums.AgentOrderStateEnum;
import com.taobao.cun.dto.order.CuntaoConfirmOrderDto;
import com.taobao.cun.dto.order.CuntaoConfirmOrderResult;
import com.taobao.cun.dto.stationbuyer.StationBuyerDetailDto;
import com.taobao.cun.service.order.CuntaoOrderService;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;
import com.taobao.util.CollectionUtil;

/**
 * Created by wangbowangb on 15-1-6.
 */
@WebResource("agentorder")
public class AgentOrderRpc {

    public static final Logger logger = LoggerFactory.getLogger(AgentOrderRpc.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpSession session;
    @Autowired
    private AgentOrderAgentServiceAdapter agentOrderAgentServiceAdapter;
    @Autowired
    private StationBuyerServiceAdapter stationBuyerServiceAdapter;
    @Resource
    private UicReadServiceClient uicReadServiceClient;
    private final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
	private CuntaoOrderService cuntaoOrderService;
    @Resource
	private StationServiceAdapter stationServiceAdapter;
    

    /**
     * 代购单确认服务
     */
    @Security(checkCSRF=true)
    @ResourceMapping(value="/agentorderconfirmed")
    public AjaxResult<Boolean> agentOrderConfirmed(@RequestParam(name = "agentorderid") Long  agentOrderId) {
        AjaxResult<Boolean> ar = new AjaxResult<Boolean>();
        try {
            agentOrderAgentServiceAdapter.confirmAgentOrder(agentOrderId,getHavanaContext());
            ar.setSuccess(true);
            ar.setData(true);
        }
        catch(BusinessException e){
			logger.error("agentOrderConfirmed error agentorderid: " + agentOrderId, e);
            ar.setSuccess(false);
            ar.setData(false);
            ar.setExceptionDesc(e.getMessage());
        }
        catch(Exception e){
			logger.error("agentOrderConfirmed error agentorderid: " + agentOrderId, e);
            ar.setSuccess(false);
            ar.setData(false);
            ar.setExceptionDesc("系统异常,请刷新重试!");
        }
        return ar;
    }

    /**
     * 代购单确认已购买服务
     */
    @Security(checkCSRF=true)
    @ResourceMapping(value="/agentorderbought")
    public AjaxResult<Boolean> agentOrderBought(@RequestParam(name = "agentorderid") Long  agentOrderId) {
        AjaxResult<Boolean> ar = new AjaxResult<Boolean>();
        try {
            agentOrderAgentServiceAdapter.boughtAgentOrder(agentOrderId,getHavanaContext());
            ar.setSuccess(true);
            ar.setData(true);
        }
        catch(BusinessException e){
			logger.error("agentOrderBought error agentorderid: " + agentOrderId, e);
            ar.setSuccess(false);
            ar.setData(false);
            ar.setExceptionDesc(e.getMessage());
        }
        catch(Exception e){
			logger.error("agentOrderBought error agentorderid: " + agentOrderId, e);
            ar.setSuccess(false);
            ar.setData(false);
            ar.setExceptionDesc("系统异常,请刷新重试!");
        }
        return ar;
    }

    /**
     * 代购单取消
     */
    @Security(checkCSRF=true)
    @ResourceMapping(value="/agentordercancel")
    public AjaxResult<Boolean> agentOrderCancel(@RequestParam(name = "agentorderid") Long  agentOrderId) {
        AjaxResult<Boolean> ar = new AjaxResult<Boolean>();
        try {
            agentOrderAgentServiceAdapter.cancelAgentOrder(agentOrderId,getHavanaContext());
            ar.setSuccess(true);
            ar.setData(true);
        }
        catch(BusinessException e){
			logger.error("agentOrderCancel error agentorderid: " + agentOrderId, e);
            ar.setSuccess(false);
            ar.setData(false);
            ar.setExceptionDesc(e.getMessage());
        }
        catch(Exception e){
			logger.error("agentOrderCancel error agentorderid: " + agentOrderId, e);
            ar.setSuccess(false);
            ar.setData(false);
            ar.setExceptionDesc("系统异常,请刷新重试!");
        }
        return ar;
    }

    /**
     * 代购单获取数量服务
     */
    @ResourceMapping(value="/getCounts")
    public AjaxResult<AgentAgentOrderCountVo> getCounts() {
        AjaxResult<AgentAgentOrderCountVo> ar = new AjaxResult<AgentAgentOrderCountVo>();
        try {
            List<AgentOrderBuyerCountEnum> countEnums = new ArrayList<AgentOrderBuyerCountEnum>();
            countEnums.add(AgentOrderBuyerCountEnum.NEW_COUNT);
            countEnums.add(AgentOrderBuyerCountEnum.CONFIRMED_COUNT);
            countEnums.add(AgentOrderBuyerCountEnum.ORDERED_COUNT);
            AgentAgentOrderCountVo agentAgentOrderCountVo = agentOrderAgentServiceAdapter.getCountByCountEnum(countEnums, getHavanaContext());
            
            //待下单数量＝待确认数量+已确认数量
            agentAgentOrderCountVo.setNewCount(agentAgentOrderCountVo.getNewCount()+agentAgentOrderCountVo.getConfirmedCount());
            ar.setSuccess(true);
            ar.setData(agentAgentOrderCountVo);
        }
        catch(BusinessException e){
			logger.error("getCounts error", e);
            ar.setSuccess(false);
            ar.setExceptionDesc(e.getMessage());
        }
        catch(Exception e){
			logger.error("getCounts error", e);
            ar.setSuccess(false);
            ar.setExceptionDesc("系统异常,请刷新重试!");
        }
        return ar;
    }

    /**
     * 代购单确认服务
     */
    @ResourceMapping(value="/agentorderlist")
    public AgentOrderPagedVo agentOrderList(@RequestParam(name = "state") String  state,@RequestParam(name = "page") int  page,@RequestParam(name = "size") int  size) {
        AgentOrderAgentPagedQueryConditionDto agentOrderAgentPagedQueryConditionDto = new AgentOrderAgentPagedQueryConditionDto();
        PageConditionDto pageDto = new PageConditionDto();
        pageDto.setSize(size);
        pageDto.setStart((page-1)*size);
        agentOrderAgentPagedQueryConditionDto.setPage(pageDto);
        if(state!=null){
            Set<AgentOrderStateEnum> states = new HashSet<AgentOrderStateEnum>();
            AgentOrderStateEnum queryState = AgentOrderStateEnum.valueof(state);
            if (queryState!=null) {
            	
                /**
                 * 村淘代购单优化，NEW和CONFRIMED两种状态合成一个状态
                 */
            	if(queryState.equals(AgentOrderStateEnum.NEW) || queryState.equals(AgentOrderStateEnum.CONFIRMED)){
            		states.add(AgentOrderStateEnum.NEW);
            		states.add(AgentOrderStateEnum.CONFIRMED);
            	}
            	else{
            		states.add(queryState);
            	}
                
            }
            agentOrderAgentPagedQueryConditionDto.setStates(states);
        }
        AgentOrderPagedVo agentOrderPagedVo = new AgentOrderPagedVo();
        agentOrderPagedVo.setPage(page);
        agentOrderPagedVo.setSize(size);
        agentOrderPagedVo.setState(state);
        try {
            PagedResultModel<List<AgentOrderDetailDto>> returnAgentOrderList = agentOrderAgentServiceAdapter.queryPagedAgentOrderByCondition(agentOrderAgentPagedQueryConditionDto, getHavanaContext());
            agentOrderPagedVo.setSuccess(true);
            agentOrderPagedVo.setTotalPage((returnAgentOrderList.getTotalResultSize()-1)/size+1);
            agentOrderPagedVo.setResult(convert(returnAgentOrderList.getResult()));
            agentOrderPagedVo.setTotalSize(returnAgentOrderList.getTotalResultSize());
        }
        catch(BusinessException e){
			logger.error("agentOrderList error state: " + state, e);
            agentOrderPagedVo.setSuccess(false);
            agentOrderPagedVo.setExceptionDesc(e.getMessage());
        }
        catch(Exception e){
			logger.error("agentOrderList error state: " + state, e);
            agentOrderPagedVo.setSuccess(false);
            agentOrderPagedVo.setExceptionDesc("系统异常,请刷新重试!");
        }
        return agentOrderPagedVo;
    }


    private List<AegentOrderListDetailVo> convert(List<AgentOrderDetailDto> result) {
        if (CollectionUtil.isEmpty(result)) return null;
        List<AegentOrderListDetailVo> returnList = new ArrayList<AegentOrderListDetailVo>();
        List<Long> taobaoUserIds = new ArrayList<Long>();
        StringBuffer taobaoUserIdsString=new StringBuffer("");
        for (AgentOrderDetailDto agentOrderDetailDto:result){
            if (agentOrderDetailDto.getTaobaoUserId()!=null) {
                taobaoUserIds.add(agentOrderDetailDto.getTaobaoUserId());
                taobaoUserIdsString.append(agentOrderDetailDto.getTaobaoUserId()).append(",");
            }
        }
        Map<Long,String> nicksMap = getNicksByIds(taobaoUserIdsString);
        Map<Long, StationBuyerDetailDto> map = null;
        for (AgentOrderDetailDto agentOrderDetailDto:result){
            AegentOrderListDetailVo aegentOrderListDetailVo = new AegentOrderListDetailVo();
//            aegentOrderListDetailVo.setTaobaoUserId(agentOrderDetailDto.getTaobaoUserId().toString());
            aegentOrderListDetailVo.setAegentOrderedTime(sdf.format(agentOrderDetailDto.getAgentOrderedTime()));
            aegentOrderListDetailVo.setAegentSku(agentOrderDetailDto.getAegentSku());
            aegentOrderListDetailVo.setAgentItemId(agentOrderDetailDto.getAgentItemId());
            aegentOrderListDetailVo.setAgentName(agentOrderDetailDto.getAgentName());
            aegentOrderListDetailVo.setAgentNum(agentOrderDetailDto.getAgentNum());
            aegentOrderListDetailVo.setAgentOrderId(agentOrderDetailDto.getAgentOrderId());
            aegentOrderListDetailVo.setAgentPicUrl(WebUtil.convertToHttpsUrl(agentOrderDetailDto.getAgentPicUrl()));
            aegentOrderListDetailVo.setAgentPrice(agentOrderDetailDto.getAgentPrice());
            aegentOrderListDetailVo.setAgentSkuId(agentOrderDetailDto.getAgentSkuId());
			if (AgentOrderStateEnum.CANCEL.equals(agentOrderDetailDto.getState())) {
				if ("y".equals(agentOrderDetailDto.getCancelByBuyer())) {
					aegentOrderListDetailVo.setState("CANCEL_BY_BUYER");
					aegentOrderListDetailVo.setStateDetail("购买人已取消");
				} else {
					aegentOrderListDetailVo.setState("CANCEL_BY_STATION");	
					aegentOrderListDetailVo.setStateDetail("代购员已取消");
				}
			}
			else if (AgentOrderStateEnum.NEW.equals(agentOrderDetailDto.getState()) || AgentOrderStateEnum.CONFIRMED.equals(agentOrderDetailDto.getState()) ) {
                aegentOrderListDetailVo.setState(AgentOrderStateEnum.CONFIRMED.getCode());
                aegentOrderListDetailVo.setStateDetail("待下单");
			}	
			else {
				aegentOrderListDetailVo.setState(agentOrderDetailDto.getState().getCode());
				if (AgentOrderStateEnum.ORDERED.equals(agentOrderDetailDto.getState())) {
					aegentOrderListDetailVo.setUrl(agentOrderAgentServiceAdapter.getTaobaoPayUrl());
				}
			}
			//如果描述为空，则需要重新设置
			if (StringUtil.isBlank(aegentOrderListDetailVo.getStateDetail())) {
                aegentOrderListDetailVo.setStateDetail(agentOrderDetailDto.getState().getDesc());
			}
			aegentOrderListDetailVo.setRemarks(agentOrderDetailDto.getRemarks());
            if (agentOrderDetailDto.getTaobaoUserId() != null) {
				aegentOrderListDetailVo.setNickName(nicksMap.get(agentOrderDetailDto.getTaobaoUserId()));
			}
			if (StringUtils.isBlank(agentOrderDetailDto.getBuyerName())) {
				if (agentOrderDetailDto.getTaobaoUserId() != null) {
					if (map == null) {
						map = stationBuyerServiceAdapter.getByTaobaoUserIds(taobaoUserIds, getHavanaContext());
					}
					StationBuyerDetailDto stationBuyerDetailDto = map.get(agentOrderDetailDto.getTaobaoUserId());
					aegentOrderListDetailVo.setBuyerMobile(stationBuyerDetailDto == null ? null : stationBuyerDetailDto
							.getMobile());
					aegentOrderListDetailVo.setBuyerName(stationBuyerDetailDto == null ? null : stationBuyerDetailDto
							.getName());
				}
			} else {
				aegentOrderListDetailVo.setBuyerMobile(agentOrderDetailDto.getBuyerMobile());
				aegentOrderListDetailVo.setBuyerName(agentOrderDetailDto.getBuyerName());
			}
            returnList.add(aegentOrderListDetailVo);
        }
        return returnList;
    }
    private Map<Long,String> getNicksByIds(StringBuffer taobaoUserIdsString){
        Map<Long, String> returnMap = new HashMap<Long, String>();
        try {
            ResultDO<List<BaseUserDO>> nickMap = uicReadServiceClient.getBaseUserListByUserIds(taobaoUserIdsString.toString().substring(0, taobaoUserIdsString.length() - 1));
            if (nickMap != null && nickMap.isSuccess() && nickMap.getModule() != null) {
                List<BaseUserDO> returnList = nickMap.getModule();
                for (BaseUserDO baseUserDO : returnList) {
                    if (baseUserDO != null && baseUserDO.getId() != null && StringUtil.isNotBlank(baseUserDO.getNick())) {
                        returnMap.put(baseUserDO.getUserId(), baseUserDO.getNick());
                    }
                }
            }
        }
        catch(Exception e){
            logger.error("call uicReadServiceClient fail!taobaoUserIdsString is:"+taobaoUserIdsString);
        }
        return returnMap;

    }

    private HavanaContext getHavanaContext(){
        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        HavanaContext havanaContext = new HavanaContext();
        havanaContext.setLoginId(loginId);
        havanaContext.setTaobaoUserId(userId.toString());
        return havanaContext;
    }
    
	/**
	 * 更新代购单
	 */
    @Security(checkCSRF=true)
	@ResourceMapping(value = "/updateAgentOrder")
	public AjaxResult<Boolean> updateAgentOrder(@RequestParam(name = "agentorderid") Long agentOrderId,
			@RequestParam(name = "remarks") String remarks) {
		AjaxResult<Boolean> ar = new AjaxResult<Boolean>();
		try {
			AgentOrderUpdateDto agentOrderUpdateDto = new AgentOrderUpdateDto();
			agentOrderUpdateDto.setAgentOrderId(agentOrderId);
			agentOrderUpdateDto.setRemarks(StringUtils.trimToNull(remarks));
			if (agentOrderUpdateDto.getRemarks() != null) {
				agentOrderAgentServiceAdapter.updateAgentOrder(agentOrderUpdateDto, getHavanaContext());
				ar.setSuccess(true);
				ar.setData(true);
			} else {
				ar.setSuccess(false);
				ar.setData(false);
				ar.setExceptionDesc("备注不能为空");
			}
		} catch (BusinessException e) {
			logger.error("updateAgentOrder error agentorderid: " + agentOrderId, e);
			ar.setSuccess(false);
			ar.setData(false);
			ar.setExceptionDesc(e.getMessage());
		} catch (Exception e) {
			logger.error("updateAgentOrder error agentorderid: " + agentOrderId, e);
			ar.setSuccess(false);
			ar.setData(false);
			ar.setExceptionDesc("系统异常,请刷新重试!");
		}
		return ar;
	}
	
	/**
	 * 下单
	 */
	@Security(checkCSRF = true)
	@ResourceMapping(value = "/confirmOrder")
	public AjaxResult<CuntaoConfirmOrderResult> confirmOrder(@RequestParam(name = "agentorderid") String agentOrderId) {
		AjaxResult<CuntaoConfirmOrderResult> ar = new AjaxResult<CuntaoConfirmOrderResult>();
		try {
			if (StringUtils.isBlank(agentOrderId)) {
				ar.setSuccess(false);
				ar.setExceptionDesc("agentorderid不能为空");
			}

			StationHavanaContext stationHavanaContext = getStationHavanaContextByHavanaContext(getHavanaContext());
			CuntaoConfirmOrderDto cuntaoConfirmOrderDto = new CuntaoConfirmOrderDto();
			cuntaoConfirmOrderDto.setAgentOrderId(Long.parseLong(agentOrderId.trim()));
			ResultModel<CuntaoConfirmOrderResult> rm = cuntaoOrderService.confirmOrder(stationHavanaContext,
					cuntaoConfirmOrderDto);
			if (rm.isSuccess() && rm.getResult() != null) {
				ar.setData(rm.getResult());
				ar.setSuccess(true);
			} else {
				logger.error("confirmOrder error agentorderid: " + agentOrderId + ", rm: " + JSON.toJSONString(rm),
						rm.getException());
				ar.setSuccess(false);
				if (rm.getException() != null)
					ar.setExceptionDesc("系统异常：" + rm.getException().getMessage());
				else
					ar.setExceptionDesc("系统异常，请刷新重试！");
			}
		} catch (ServiceException e) {
			logger.error("confirmOrder error agentorderid: " + agentOrderId, e);
			ar.setSuccess(false);
			ar.setExceptionDesc("系统异常：" + e.getMessage());
		} catch (Exception e) {
			logger.error("confirmOrder error agentorderid: " + agentOrderId, e);
			ar.setSuccess(false);
			ar.setExceptionDesc("系统异常，请刷新重试！");
		}
		return ar;
	}

	private StationHavanaContext getStationHavanaContextByHavanaContext(HavanaContext havanaContext) {
		Long stationId = stationServiceAdapter.getStationIdByTaobaoUserId(havanaContext);
		StationHavanaContext stationHavanaContext = new StationHavanaContext();
		stationHavanaContext.setStationId(stationId);
		stationHavanaContext.setTaobaoUserId(havanaContext.getTaobaoUserId());
		stationHavanaContext.setLoginId(havanaContext.getLoginId());
		return stationHavanaContext;
	}
}
