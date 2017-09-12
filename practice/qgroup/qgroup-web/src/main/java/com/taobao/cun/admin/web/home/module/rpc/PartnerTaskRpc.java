package com.taobao.cun.admin.web.home.module.rpc;

import javax.annotation.Resource;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.common.exception.BusinessException;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.motion.service.PartnerMotionService;

public class PartnerTaskRpc extends BaseController {
	
	private static Logger logger=LoggerFactory.getLogger(PartnerTaskRpc.class);
	
	@Resource
    private PartnerMotionService partnerMotionService;

    /**
     * �Ѿ�֪���������񲢽����˲鿴
     */
    @Security(checkCSRF=true)
    @ResourceMapping(value="/hasKnowNewMotionInstances")
    public AjaxResult<Boolean> hasKnowNewMotionInstances(@RequestParam(name = "userId") Long  userId) {
        AjaxResult<Boolean> ar = new AjaxResult<Boolean>();
        try {
        	ResultModel<String> result=partnerMotionService.hasKnowNewMotionInstances(userId.toString());
        	if(result!=null&&result.isSuccess()){
        		ar.setSuccess(true);
        		ar.setData(true);
        	}
        	else{
        		ar.setSuccess(false);
        	}
        }
        catch(Exception e){
			logger.error("hasKnowNewMotionInstances error userId: " + userId+",error="+e.getMessage());
            ar.setSuccess(false);
            ar.setData(false);
            ar.setExceptionDesc("ϵͳ�쳣,��ˢ������!");
        }
        return ar;
    }

}
