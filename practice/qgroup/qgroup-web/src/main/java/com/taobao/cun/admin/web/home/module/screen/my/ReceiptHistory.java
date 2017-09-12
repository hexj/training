package com.taobao.cun.admin.web.home.module.screen.my;

import java.util.List;

import com.taobao.cun.admin.dto.ApplyDto;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.admin.adapter.cuntao.center.CuntaoReceivablesServiceAdapter;
import com.taobao.cun.admin.dto.order.OrderDTO;
import com.taobao.cun.admin.dto.order.OrderItemDTO;
import com.taobao.cun.admin.dto.pay.ReceiptDTO;
import com.taobao.cun.admin.util.ListUtils;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.common.resultmodel.PagedResultModel;

public class ReceiptHistory extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ReceiptHistory.class);

	@Autowired
	private CuntaoReceivablesServiceAdapter cuntaoReceivablesServiceAdapter;

	@Autowired
	private URIBrokerService uriBrokerService;
	@Autowired
	private StationApplyService stationApplyService;

	public void execute(
			@Param(name = "pageNum", defaultValue = "1") Integer pageNum,
			@Param(name = "pageSize", defaultValue = "20") Integer pageSize,
			Context context, Navigator navigator) {
		try {

			String loginId = WebUtil.getLoginId(session);
			Long userId = WebUtil.getUserId(session);
			ApplyDto applyDto = stationApplyService.getStationApplyDetailSafed(userId+"",loginId);
			if (applyDto == null) {
				throw new IllegalArgumentException("applyService.getStationApply result is null");
			}

			if (pageSize == null || pageSize <= 0)
				pageSize = 20;

			if (pageNum == null || pageNum < 1)
				pageNum = 1;

			PagedResultModel<List<ReceiptDTO>> receiptDTOs = queryReceiptLists((pageNum - 1) * pageSize, pageSize);

			/* 设置receipt 代表商品图片地址 */
			setReceiptStandOrderItemPicUrl(receiptDTOs);
			
			addOrdersToContext(receiptDTOs, pageSize, pageNum, context);

			addOtherThingsToContext(context);

		} catch (Exception e) {
			logger.error("prepare for receiptHistory vm error!", e);
			navigator.forwardTo("//err.taobao.com/error2.html");
		}

	}
	
	private PagedResultModel<List<ReceiptDTO>> queryReceiptLists(Integer pageStart, Integer pageSize) {
		return cuntaoReceivablesServiceAdapter.getReceiptList(buildHavanaContext(), pageStart, pageSize);
	}

	private void setReceiptStandOrderItemPicUrl(PagedResultModel<List<ReceiptDTO>> receiptDTOs) {
		if (receiptDTOs == null || !receiptDTOs.isSuccess()|| ListUtils.isBlank(receiptDTOs.getResult()))
			return;
		
		for (ReceiptDTO receipt : receiptDTOs.getResult()){
			fixOrderItemPicUrl(receipt.getOrder());
			setReceiptStandOrderItemPicUrl(receipt.getOrder(), receipt);
		}
	}
	
	private void fixOrderItemPicUrl(OrderDTO order) {
		if (order == null || ListUtils.isBlank(order.getOrderItems()))
			return;

		for (OrderItemDTO orderItem : order.getOrderItems()) {
			if (orderItem == null || StringUtil.isBlank(orderItem.getProductPicUrl())
					|| orderItem.getProductPicUrl().startsWith("http://"))
				continue;

			orderItem.setProductPicUrl(uriBrokerService.getURIBroker("uploadImageServer").fullURI() + "/"
					+ orderItem.getProductPicUrl());

		}
	}
	
	private void setReceiptStandOrderItemPicUrl(OrderDTO order, ReceiptDTO receipt) {
		if (order == null || ListUtils.isBlank(order.getOrderItems()))
			return;
		
		List<OrderItemDTO> orderitems = order.getOrderItems();
		OrderItemDTO orderItem = ListUtils.first(orderitems);
		if (orderItem != null)
			receipt.setOrderProductPicUrl(orderItem.getProductPicUrl());
		
	}

	private void addOrdersToContext(PagedResultModel<List<ReceiptDTO>> receiptDTOs, Integer pageSize, Integer pageNum,
			Context context) {

		if (receiptDTOs.isSuccess() && ListUtils.isNotBlank(receiptDTOs.getResult())) {
			context.put("receiptDTOs", receiptDTOs.getResult());
			context.put("totalResultSize", receiptDTOs.getTotalResultSize());
		} else {
			context.put("totalResultSize", 0);
			return;
		}

		context.put("pageSize", pageSize);
		context.put("pageNum", pageNum);
		context.put("totalPage", calculateTotalPage(pageSize, receiptDTOs.getTotalResultSize()));
	}

	private Long calculateTotalPage(Integer pageSize, Long resultSize) {
		if (resultSize == null || pageSize == null)
			return 1L;

		return resultSize % pageSize > 0 ? resultSize / pageSize + 1 : resultSize / pageSize;
	}

	private void addOtherThingsToContext(Context context) {

	}
}
