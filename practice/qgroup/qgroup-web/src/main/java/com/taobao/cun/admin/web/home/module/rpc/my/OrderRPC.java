package com.taobao.cun.admin.web.home.module.rpc.my;

import java.util.List;

import com.taobao.cun.admin.common.ajax.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.NoneResultDecrator;
import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.adapter.cuntao.center.CuntaoReceivablesServiceAdapter;
import com.taobao.cun.admin.adapter.cuntao.center.TaobaoTradeOrderQueryServiceAdapter;
import com.taobao.cun.admin.dto.order.OrderDTO;
import com.taobao.cun.admin.dto.order.OrderItemDTO;
import com.taobao.cun.admin.dto.pay.ReceiptDTO;
import com.taobao.cun.admin.enums.Receipt.ReceiptStatus;
import com.taobao.cun.admin.util.ListUtils;
import com.taobao.cun.admin.web.home.module.BaseController;

/**
 * 依据一定的查询条件获取订单详情的rpc
 * @author zixing.liangzx
 */
@WebResource("my")
public class OrderRPC extends BaseController{
	
	@Autowired
	private TaobaoTradeOrderQueryServiceAdapter taobaoTradeOrderQueryServiceAdapter;
	
	@Autowired
	private CuntaoReceivablesServiceAdapter cuntaoReceivablesServiceAdapter;
	
	@Autowired
	private URIBrokerService uriBrokerService;
	
	/**
	 * 依据运单号或者订单号查询订单详情
	 * @param yunDanHao
	 * @return
	 */
	@ResourceMapping("/queryOrders")
	@NoneResultDecrator
	public AjaxResult<List<OrderDTO>> queryOrders(@RequestParam(name = "orderNoOrMailNo") String orderNoOrMailNo){
		if (!isLogin()){
			return AjaxResult.unSuccess("请登录后重试！");
		}
		
		if (!checkParam(orderNoOrMailNo)){
			return AjaxResult.unSuccess("非法的参数！请刷新页面后重试！");
		}
		
		List<OrderDTO> orders = queryOrdersBy(StringUtil.trim(orderNoOrMailNo), getUserId());
		
		fixOrderItemPicUrl(orders);
		
		mergeReceiptInfo(orders);
		
		if (ListUtils.isBlank(orders))
			return AjaxResult.unSuccess("Cannot find any order for this orderNoOrMailNo：" + orderNoOrMailNo);
		
		return AjaxResult.success(orders);
	}
	
	private boolean checkParam(String orderNoOrMailNo) {
		return StringUtil.isNotBlank(orderNoOrMailNo);
	}

	private List<OrderDTO> queryOrdersBy(String orderNoOrMailNo, Long userId) {
		return taobaoTradeOrderQueryServiceAdapter.queryOrder(orderNoOrMailNo, userId);
	}
	
	private void fixOrderItemPicUrl(List<OrderDTO> orders) {
		if (ListUtils.isBlank(orders))
			return;

		for (OrderDTO order : orders) {
			if (order == null || ListUtils.isBlank(order.getOrderItems()))
				continue;

			for (OrderItemDTO orderItem : order.getOrderItems()) {
				if (orderItem == null
						|| StringUtil.isBlank(orderItem.getProductPicUrl())
						|| orderItem.getProductPicUrl().startsWith("http://"))
					continue;
				
				orderItem.setProductPicUrl(uriBrokerService.getURIBroker(
						"uploadImageServer").fullURI()
						+ "/" + orderItem.getProductPicUrl());
			}
		}
	}
	
	private void mergeReceiptInfo(List<OrderDTO> orders) {
		if (ListUtils.isBlank(orders))
			return ;
		
		for (OrderDTO order : orders) {
			if (order == null)
				continue;
			
			ReceiptDTO receipt = getReceiptDTO(order);
			if (receipt != null)
				order.setPayStatus(receipt.getReceiptStatus());
			else
				order.setPayStatus(ReceiptStatus.NONE);
		}
	}

	private ReceiptDTO getReceiptDTO(OrderDTO order) {
		return cuntaoReceivablesServiceAdapter.getReceiptDTO(buildHavanaContext(), Long.parseLong(order.getOrderNo()));
	}

}
