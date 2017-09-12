package com.taobao.cun.admin.web.home.module.rpc.my;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.common.exception.BusinessException;
import com.taobao.cun.common.resultmodel.ResultModel;


/***
 * 代购员申请退出村点的RPC调用
 * @author yangwei.pengyw
 *
 */
@WebResource("quitapply")
public class PartnerQuitApplyRPC {
	@Autowired
    private HttpSession session;
    @Resource
    private StationApplyService stationApplyService;

    //村淘退出的时候，不能将ct测试账号002退出，必须保证合伙人身份
    private static final  Long CUNTAO_FORBIDDEN_ACCOUNT = 2476363990L;

    @Security(checkCSRF=true)
    @ResourceMapping(value = "/doConfirmQuitApply")
    public  AjaxResult<Boolean>  doConfirmQuitApply(@RequestParam(name = "stationApplyId") Long  stationApplyId,
    		@RequestParam(name = "cunQuitProtocol") String  cunQuitProtocol,
    		@RequestParam(name = "cunQuitType") String  cunQuitType) {
    	
    	AjaxResult<Boolean>  result = new AjaxResult<Boolean>();       
        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        
        if(CUNTAO_FORBIDDEN_ACCOUNT.equals(userId)){
        	//村淘退出的时候，不能将ct测试账号002退出，必须保证合伙人身份
        	//因为目前村淘的价格依赖这个账号，退出之后将造成线上问题
        	result.setSuccess(false);
        	result.setExceptionDesc("账号ct测试账号002不能退出！");
        	return result;
		}
	 
		ResultModel<Boolean> quitResult  = stationApplyService.quit(userId+"", loginId, stationApplyId, cunQuitProtocol, cunQuitType);
		if (quitResult.isSuccess()){
			result.setSuccess(true);
			result.setData(true);
		}else{
			result.setSuccess(false);
			if (quitResult.getException()!=null && (quitResult.getException() instanceof BusinessException)){
				result.setExceptionDesc(quitResult.getException().getMessage());
			}
			else{
				result.setExceptionDesc("系统错误");
			}
		}
        return result;

    }
	
}
