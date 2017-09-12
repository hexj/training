package com.taobao.cun.admin.web.home.module.screen.my;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.taobao.cun.admin.dto.ApplyDto;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;

/**
 * ����˳����룬Э��ȷ��ҳ��
 * @author yangwei.pengyw
 *
 */
public class PartnerQuitApply {
    @Autowired
    private StationApplyService stationApplyService;
    @Autowired
    private HttpSession session;
    
    private static final String CUN_QUIT_PROTOCOL = "partner-quit-agreements-v1"; //����˳���Э��
    private static final String CUN_QUIT_TYPE = "partner-quit";//�ϻ��������˳�
    
    /**
     * �鿴��������
     * @param context
     */
    public void execute(Context context) {
        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        ApplyDto applyDto = stationApplyService.getStationApplyDetailSafed(userId+"",loginId);
        
        //�����ֶζ������ύ���뵥�����ú�̨RPC��ʱ��ʹ�ã���ʵ�����ҳ�棬���µĲ������Ѿ���ȷ������
        context.put("stationApplyId", applyDto.getStationApplyId()); //���뵥id
        context.put("cunQuitProtocol", CUN_QUIT_PROTOCOL); //ȷ�ϵ�Э��
        context.put("cunQuitType", CUN_QUIT_TYPE); //�ύ�ϻ����˳�����Դ
    }
    
}
