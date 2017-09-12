package com.taobao.cun.admin.web.home.module.rpc.my;

import java.util.List;

import com.taobao.cun.admin.common.ajax.AjaxResult;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.NoneResultDecrator;
import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.citrus.extension.rpc.databind.JsonParam;
import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.adapter.cuntao.center.CuntaoReceivablesServiceAdapter;
import com.taobao.cun.admin.adapter.cuntao.center.TaobaoTradeOrderQueryServiceAdapter;
import com.taobao.cun.admin.dto.order.OrderDTO;
import com.taobao.cun.admin.dto.order.OrderItemDTO;
import com.taobao.cun.admin.dto.pay.PayDTO;
import com.taobao.cun.admin.dto.pay.ReceiptDTO;
import com.taobao.cun.admin.enums.Pay.PayMethod;
import com.taobao.cun.admin.enums.Pay.PayMethodSubType;
import com.taobao.cun.admin.enums.Pay.PaySource;
import com.taobao.cun.admin.util.ListUtils;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.admin.web.vo.PayParam;

@WebResource("my")
public class PayRPC extends BaseController{

	@Autowired
	private TaobaoTradeOrderQueryServiceAdapter taobaoTradeOrderQueryServiceAdapter;
	
	@Autowired
	private CuntaoReceivablesServiceAdapter cuntaoReceivablesServiceAdapter;
	
	@Autowired
	private URIBrokerService uriBrokerService;
	
	@ResourceMapping("/pay")
	@NoneResultDecrator
	@Security(checkCSRF = true)
	public AjaxResult<ReceiptDTO> pay( @JsonParam("payParam") PayParam param) {
		if (!checkParam(param))
			return AjaxResult.unSuccess("Illeagal param!");
		
		OrderDTO order = taobaoTradeOrderQueryServiceAdapter.queryOrderByOrderNo(param.getOrderNo(), getUserId());
		if (order == null)
			return AjaxResult.unSuccess("找不到该订单！请稍后重试！");
		
		PayDTO pay = buildPayDTO(param, order);
		
		if (!checkMailNo(param.getMailNo(), order))
			pay.setMailNo("");
		
		return AjaxResult.toAjaxResult(cuntaoReceivablesServiceAdapter.payOrder(buildHavanaContext(), pay));
	}

	private boolean checkParam(PayParam param) {
		if (param == null)
			return false;
		
		if (StringUtil.isBlank(param.getOrderNo()))
			return false;
			
		if (NumberUtils.toLong(param.getOrderNo(), 0L) == 0L)
			return false;
		
		if (StringUtil.isBlank(param.getPayMethod())
				|| (!PayMethod.ALIPAY.name().equalsIgnoreCase(param.getPayMethod()) 
						&& !PayMethod.CASH.name().equalsIgnoreCase(param.getPayMethod())))
			return false;
		
		if(PayMethod.ALIPAY.name().equalsIgnoreCase(param.getPayMethod()) && StringUtil.isBlank(param.getDynamicPayId()))
			return false;
		
		return true;
	}

	private PayDTO buildPayDTO(PayParam param, OrderDTO order) {
		
		PayDTO pay = new PayDTO();
		
		pay.setMailNo(param.getMailNo());
		
		pay.setOrderNo(Long.parseLong(order.getOrderNo()));
		
		if (PayMethod.ALIPAY.name().equalsIgnoreCase(param.getPayMethod())){
			pay.setMethod(PayMethod.ALIPAY);
			pay.setDynamicPayId(param.getDynamicPayId());
			pay.setPayMethodSubType(PayMethodSubType.BAR_CODE);
		} else 
			pay.setMethod(PayMethod.CASH);
			
		pay.setReceiverContact(order.getReceiverContact());
		pay.setReceiverName(order.getReceiverName());

		if (PaySource.CAI_NIAO.name().equalsIgnoreCase(param.getPaySource()))
			pay.setSource(PaySource.CAI_NIAO);
		else
			pay.setSource(PaySource.CUNTAO_ADMIN);
		
		pay.setRemarks("");
		
		return pay;
	}
	
	private boolean checkMailNo(String mailNo, OrderDTO order) {
		if (mailNo == null)
			return false;
		
		List<OrderDTO> orders = taobaoTradeOrderQueryServiceAdapter.queryOrderByMailNo(mailNo, getUserId());
		
		if (ListUtils.isBlank(orders))
			return false;
		
		for (OrderDTO orderTemp : orders){
			if (orderTemp.getOrderNo().equals(order.getOrderNo()))
				return true;
		}
			
		return false;
	}

	@ResourceMapping("/findReceipt")
	@NoneResultDecrator
	public AjaxResult<ReceiptDTO> findReceiptBy(@RequestParam(name = "orderNo") String orderNo){
		ReceiptDTO receiptDTO = cuntaoReceivablesServiceAdapter.getReceiptDTO(buildHavanaContext(), NumberUtils.toLong(orderNo, 0L));
		
		if (receiptDTO != null){
			fixOrderItemPicUrl(receiptDTO);
			fixRecieptOrderProductPicUrl(receiptDTO);
		}
		else
			return AjaxResult.unSuccess("Cannot find any receipt for this orderNO : " + orderNo);
		
		return AjaxResult.success(receiptDTO);
	}

	private void fixOrderItemPicUrl(ReceiptDTO receiptDTO) {
		if (receiptDTO == null || receiptDTO.getOrder() == null
				|| ListUtils.isBlank(receiptDTO.getOrder().getOrderItems()))
			return;

		for (OrderItemDTO orderItem : receiptDTO.getOrder().getOrderItems()) {
			if (orderItem == null
					|| StringUtil.isBlank(orderItem.getProductPicUrl())
					|| orderItem.getProductPicUrl().startsWith("http://"))
				continue;
			
			orderItem.setProductPicUrl(uriBrokerService.getURIBroker(
					"uploadImageServer").fullURI()
					+ "/" + orderItem.getProductPicUrl());
		}
	}
	
	private void fixRecieptOrderProductPicUrl(ReceiptDTO receiptDTO) {
		if (receiptDTO == null || receiptDTO.getOrder() == null
				|| ListUtils.isBlank(receiptDTO.getOrder().getOrderItems()))
			return;
		
		List<OrderItemDTO> orderitems = receiptDTO.getOrder().getOrderItems();
		OrderItemDTO orderItem = ListUtils.first(orderitems);
		if (orderItem != null)
			receiptDTO.setOrderProductPicUrl(orderItem.getProductPicUrl());
	}
}
