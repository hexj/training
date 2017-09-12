package com.taobao.cun.admin.web.home.module.rpc;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.NoneResultDecrator;
import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.common.ajax.AjaxPagedResult;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.crius.common.exception.ServiceException;
import com.taobao.cun.crius.common.resultmodel.PagedResultModel;
import com.taobao.cun.crius.exam.dto.UserDispatchDto;
import com.taobao.cun.crius.exam.query.ExamUserDispatchQueryCondition;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;

@WebResource("exam")
public class ExamRpc extends BaseController {
	@Autowired
	private HttpSession session;
	@Autowired
	private ExamUserDispatchService examDispatchSerivce;

	private static final Logger logger = LoggerFactory.getLogger(ExamRpc.class);
	private static final int DEFAULT_PAGE_SIZE = 10;

	@Security(checkCSRF = true)
	@ResourceMapping("/list")
	@NoneResultDecrator
	public AjaxPagedResult<List<UserDispatchDto>> getExamList(@RequestParam(name = "pageSize") Integer pageSize,
			@RequestParam(name = "pageNum") Integer pageNum) {
		try {
			Long userId = WebUtil.getUserId(session);
			ExamUserDispatchQueryCondition condition = new ExamUserDispatchQueryCondition();
			if (null == pageSize || 0 == pageSize) {
				pageSize = DEFAULT_PAGE_SIZE;
			}
			if (null == pageNum || pageNum < 1) {
				pageNum = 1;
			}
			condition.setPageSize(pageSize);
			condition.setStart((pageNum - 1) * pageSize);
			condition.setUserId(userId);
			PagedResultModel<List<UserDispatchDto>> prm = examDispatchSerivce.queryExamUserDispatchList(condition);
			if (null != prm && prm.isSuccess()) {
				return buildAjaxPagedResult(prm, pageSize, pageNum);
			} else {
				logger.error("queryExamUserDispatchList exception: " + JSON.toJSONString(prm));
				throw new ServiceException("queryExamUserDispatchList exception");
			}
		} catch (Exception e) {
			return AjaxPagedResult.unSuccess("queryExamUserDispatchList failed");
		}
	}

	public static <C> AjaxPagedResult<C> buildAjaxPagedResult(PagedResultModel<C> resultModel, Integer pageSize,
			Integer pageNum) {
		if (resultModel == null)
			return AjaxPagedResult.unSuccess("null resultModel!");

		if (resultModel.isSuccess()) {
			AjaxPagedResult<C> ajaxPagedResult = AjaxPagedResult.success(resultModel.getResult());
			ajaxPagedResult.setTotalSize(resultModel.getTotalResultSize());
			ajaxPagedResult.setPage(pageNum);

			long totalpage = resultModel.getTotalResultSize() / pageSize;
			if (resultModel.getTotalResultSize() % pageSize != 0) {
				totalpage++;
			}
			ajaxPagedResult.setTotalPage(totalpage);
			ajaxPagedResult.setSize(pageSize);

			return ajaxPagedResult;
		} else
			return AjaxPagedResult.unSuccess(resultModel.getException() == null ? "no exception message"
					: resultModel.getException().getMessage());
	}
}
