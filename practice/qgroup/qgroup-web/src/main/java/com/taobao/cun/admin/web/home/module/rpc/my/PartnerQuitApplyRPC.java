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
 * ����Ա�����˳�����RPC����
 * @author yangwei.pengyw
 *
 */
@WebResource("quitapply")
public class PartnerQuitApplyRPC {
	@Autowired
    private HttpSession session;
    @Resource
    private StationApplyService stationApplyService;

    //�����˳���ʱ�򣬲��ܽ�ct�����˺�002�˳������뱣֤�ϻ������
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
        	//�����˳���ʱ�򣬲��ܽ�ct�����˺�002�˳������뱣֤�ϻ������
        	//��ΪĿǰ���Եļ۸���������˺ţ��˳�֮�������������
        	result.setSuccess(false);
        	result.setExceptionDesc("�˺�ct�����˺�002�����˳���");
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
				result.setExceptionDesc("ϵͳ����");
			}
		}
        return result;

    }
	
}
