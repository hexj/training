package com.taobao.cun.admin.web.home.module.rpc;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.common.ajax.AjaxPagedResult;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.service.CuntaoTpaService;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.common.PageConditionDto;
import com.taobao.cun.common.resultmodel.PagedResultModel;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.SystemTypeEnum;
import com.taobao.cun.dto.UserTypeEnum;
import com.taobao.cun.dto.coupon.CouponDto;
import com.taobao.cun.dto.coupon.enums.CouponStatusEnum;
import com.taobao.cun.service.coupon.CouponService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 代金券rpc
 * @author quanzhu.wangqz
 *
 */
@WebResource("coupon")
public class CouponRpc extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(CouponRpc.class);
	
	@Resource
	CouponService couponService;
	@Resource
	private CuntaoTpaService cuntaoTpaService;

	@ResourceMapping("/getMyCoupon")
	public AjaxPagedResult<List<CouponDto>> getMyCouponList(@RequestParam(name = "pageNum") int pageNum, 
			@RequestParam(name = "pageSize") int pageSize) {

		PageConditionDto page = new PageConditionDto();
		if (pageNum <= 0)
			pageNum = 1;
		if (pageSize <= 0)
			pageSize = 10;
		page.setSize(pageSize);
		page.setStart((pageNum - 1) * pageSize);
		
		AjaxPagedResult<List<CouponDto>> result = new AjaxPagedResult<List<CouponDto>>();
			try {
				//淘帮手无权访问
			if(true == cuntaoTpaService.isCuntaoTpa(getUserId()))
				throw new RuntimeException();
			CouponDto couponDto = new CouponDto();
			couponDto.setUserId(getUserId());
			couponDto.setStatus(CouponStatusEnum.HAS_USED.getCode());
			couponDto.setPage(page);
			PagedResultModel<List<CouponDto>> dtoList = couponService.getCouponList(couponDto);
			if (!dtoList.isSuccess()) {
				logger.error("CouponRpc.getMyCouponList error! userId:" + getUserId(),dtoList.getException());
				return AjaxPagedResult.unSuccess("查询我使用的代金券信息异常！");
			}

			List<CouponDto> list = dtoList.getResult();
			result.setData(list);
			Long totalPage = (dtoList.getTotalResultSize() - 1) / pageSize + 1;
			result.setTotalPage(totalPage);
			result.setPage(pageNum);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			logger.error("CouponRpc.getMyCouponList error! userId:" + getUserId(), e);
			result.setSuccess(false);
			result.setExceptionDesc(e.getMessage());
		}
		return result;
	}
	
	@Security(checkCSRF=true)
	@ResourceMapping("/modifyCoupon")
	public AjaxResult<Boolean> modifyCoupon(@RequestParam(name = "couponId") String couponId, 
			@RequestParam(name = "orderId") Long orderId,@RequestParam(name = "spreadId") Long spreadId) {
		if (StringUtils.isEmpty(couponId) || orderId <=0 || spreadId <= 0) {
			return AjaxResult.unSuccess("参数异常！");
		}
		try {
			//淘帮手无权访问
			if(true == cuntaoTpaService.isCuntaoTpa(getUserId()))
				throw new RuntimeException();
			CouponDto couponDto = new CouponDto();
			couponDto.setUserId(getUserId());
			couponDto.setStatus(CouponStatusEnum.HAS_USED.getCode());
			couponDto.setCouponUseTime(new Date());
			couponDto.setCouponId(couponId);
			couponDto.setOrderId(orderId);
			couponDto.setSpreadId(spreadId);
			
			ContextDto contextDto = assemberContext(getUserId());
			ResultModel<Long>  resDto = couponService.updateCouponRecord(contextDto, couponDto);
			if (!resDto.isSuccess()) {
				logger.error("CouponRpc.modifyCoupon error! couponId:" + couponId+" orderId:"+orderId+" spreadId:"+spreadId,resDto.getException());
				return AjaxResult.unSuccess(resDto.getException().getMessage());
			}

			return AjaxResult.success(Boolean.TRUE);
		} catch (Exception e) {
			logger.error("CouponRpc.modifyCoupon error! couponId:" + couponId+" orderId:"+orderId+" spreadId:"+spreadId,e);
			return AjaxResult.unSuccess("更新代金券异常！");
		}
	}
	
	private static ContextDto assemberContext(Long loginId) {
		ContextDto context = new ContextDto();
		context.setLoginId(String.valueOf(loginId));
		context.setAppId(String.valueOf(loginId));
		context.setUserType(UserTypeEnum.HAVANA);
		context.setSystemType(SystemTypeEnum.CUNTAO_ADMIN);
		return context;
	}
}
