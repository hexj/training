package com.taobao.cun.admin.web.home.module.rpc;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.NoneResultDecrator;
import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.tmall.lsp.locationservice.common.domain.division.VillageVO;
import com.tmall.lsp.locationservice.common.service.DivisionManagerService;

@WebResource("village")
public class VillageRpc {

	@Autowired
	private DivisionManagerService divisionManagerService;

	@NoneResultDecrator
    @ResourceMapping(value="/list")
	public AjaxResult<Collection<VillageVO>> list(@RequestParam(name = "townCode") String townCode) {
		AjaxResult<Collection<VillageVO>> result = new AjaxResult<Collection<VillageVO>>();
		try {
			Collection<VillageVO> villages = divisionManagerService.getChildrenByTownId(Long.valueOf(townCode));
			if (null == villages || villages.size() > 0) {
				result.setSuccess(true);
				result.setData(villages);
			} else {
				result.setData(Collections.<VillageVO> emptyList());
				result.setSuccess(true);
			}
			return result;
		} catch (Exception e) {
			result.setSuccess(false);
			result.setExceptionDesc(e.getMessage());
			return result;
		}
	}
}
