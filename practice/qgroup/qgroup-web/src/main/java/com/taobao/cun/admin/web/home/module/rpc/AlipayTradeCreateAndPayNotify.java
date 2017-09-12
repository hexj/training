package com.taobao.cun.admin.web.home.module.rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.taobao.cun.admin.util.DateUtil;
import com.taobao.cun.alipay.exception.AlipayApiException;
import com.taobao.cun.alipay.parse.AlipayResponseParser;
import com.taobao.cun.alipay.response.AsynAlipayAcquireTradeCreateAndPayResponse;
import com.taobao.cun.alipay.util.CommonEncoding;
import com.taobao.cun.dto.Context;
import com.taobao.cun.dto.HavanaContext;
import com.taobao.cun.dto.alipay.enums.TradeStatusEnum;
import com.taobao.cun.dto.cashier.CuntaoAlipayFacePayDto;
import com.taobao.cun.service.cashier.CuntaoReceivablesService;

/**
 * Created by chenhui.lich on 2015/1/12.
 */
@WebResource("alipay")
public class AlipayTradeCreateAndPayNotify {

	public static final Logger logger = LoggerFactory.getLogger(AlipayBail.class);

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	@Resource
	private AlipayResponseParser defaultAlipayResponseParser;
	@Resource
	private CuntaoReceivablesService cuntaoReceivablesService;

	/**
	 * 接收交易创建的异步消息
	 */
	@ResourceMapping(value = "/acquire/receive_createandpay_notify")
	public void receiveTradeCreateAndPayMessage() {
		Map<String, String> paramMap = createParameterMap();
		if (logger.isDebugEnabled()) {
			logger.debug(com.taobao.cun.alipay.util.FastJson.toJson(paramMap));
		}

		try {
			AsynAlipayAcquireTradeCreateAndPayResponse response = defaultAlipayResponseParser.parseAsynResponse(paramMap, AsynAlipayAcquireTradeCreateAndPayResponse.class, "UTF-8");
			CuntaoAlipayFacePayDto cuntaoAlipayFacePayDto = this.createCuntaoAlipayFacePayDto(response);
			String loginId = "asyn-alipay";
			Long userId = 888l;
			Context context = getContext(userId.toString(), loginId);

			cuntaoReceivablesService.updateReceivables(context, cuntaoAlipayFacePayDto);

		} catch (AlipayApiException e) {
			Log.error("receive asyn response from alipay error:" + e.getMessage(), e);
			this.printMessage("receive asyn response from alipay error:" + e.getMessage());
			return;
		}
		printSuccess();

	}

	private CuntaoAlipayFacePayDto createCuntaoAlipayFacePayDto(AsynAlipayAcquireTradeCreateAndPayResponse response) {
		CuntaoAlipayFacePayDto cuntaoAlipayFacePayDto = new CuntaoAlipayFacePayDto();

		if (StringUtils.isNotBlank(response.getOutTradeNo())) {
			/**
			 * 获取外部商户号
			 */
			String outTradeNo = response.getOutTradeNo();
			String[] param = outTradeNo.split("_");
			if (param.length < 2) {
				throw new java.lang.RuntimeException("outTradeNo format error!");
			}
			
			
			//Long tbUserId = Long.valueOf(param[0]);
			//做一个解码的操作
			Long tbUserId = CommonEncoding.XOREncode(Long.valueOf(param[0]));
			Long tbTradeParentId = Long.valueOf(param[1]);

			cuntaoAlipayFacePayDto.setTbTradeParentId(tbTradeParentId);
			cuntaoAlipayFacePayDto.setTbUserId(tbUserId);
			cuntaoAlipayFacePayDto.setOutTradeNo(outTradeNo);
		}

		// 交易付款时间
		if (StringUtils.isNotBlank(response.getGmtPayment())) {
			cuntaoAlipayFacePayDto.setAlipayGmtPayment(parseDate(response.getGmtPayment()));
		}
		// 村代购点卖家ID
		if (StringUtils.isNotBlank(response.getSellerId())) {
			cuntaoAlipayFacePayDto.setAlipayId(response.getSellerId());
		}
		// 村代购点卖家Email
		if (StringUtils.isNotBlank(response.getSellerEmail())) {
			cuntaoAlipayFacePayDto.setAlipayLoginId(response.getSellerEmail());
		}
		// 村民Id
		if (StringUtils.isNotBlank(response.getBuyerId())) {
			cuntaoAlipayFacePayDto.setBuyerAlipayId(response.getBuyerId());
		}
		// 村民登陆ID
		if (StringUtils.isNotBlank(response.getBuyerEmail())) {
			cuntaoAlipayFacePayDto.setBuyerAlipayLoginId(response.getBuyerEmail());
		}

		// 交易Subject
		if (StringUtils.isNotBlank(response.getSubject())) {
			cuntaoAlipayFacePayDto.setAlipaySubject(response.getSubject());
		}
		// 交易总金额
		if (StringUtils.isNotBlank(response.getTotalFee())) {
			cuntaoAlipayFacePayDto.setTotalFee(new BigDecimal(response.getTotalFee()));
		}

		// 交易总金额
		if (StringUtils.isNotBlank(response.getTotalFee())) {
			cuntaoAlipayFacePayDto.setTotalFee(new BigDecimal(response.getTotalFee()));
		}
		// 支付宝交易号
		if (StringUtils.isNotBlank(response.getTradeNo())) {
			cuntaoAlipayFacePayDto.setAlipayTradeNo(response.getTradeNo());
		}
		// 支付宝交易号
		if (StringUtils.isNotBlank(response.getTradeStatus())) {
			cuntaoAlipayFacePayDto.setAlipayTradeStatus(TradeStatusEnum.valueof(response.getTradeStatus()));
		}
		
		return cuntaoAlipayFacePayDto;
	}

	private Date parseDate(String date) {
		return DateUtil.parseDate(date, DateUtil.TIME_PATTON_DEFAULT);
	}

	private HavanaContext getContext(String userId, String loginId) {
		HavanaContext context = new HavanaContext();
		context.setTaobaoUserId(userId);
		context.setLoginId(loginId);
		return context;
	}

	private Map<String, String> createParameterMap() {
		Map<String, String> paramMap = new HashMap<String, String>();
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = request.getParameter(key);
			paramMap.put(key, value);
		}
		return paramMap;
	}

	private void printSuccess() {
		this.printMessage("success");
	}

	private void printMessage(String message) {
		PrintWriter output = null;
		try {
			output = response.getWriter();
			output.write(message);
		} catch (IOException e) {
			logger.error("printSuccess error! ");
		} finally {
			if (output != null) {
				output.flush();
				output.close();
			}
		}
	}

}
