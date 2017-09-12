package com.taobao.cun.admin.web.home.module.screen.apply;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.dto.ApplyDto;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;

/**
 * Created by wangbowangb on 15-4-10.
 */
public class ApplyAgreements {
    public static final Logger logger = LoggerFactory.getLogger(ApplyConfirm.class);
    @Autowired
    private HttpSession session;
    @Resource
    private StationApplyService stationApplyService;
    /**
     * ����Э��ҳ��
     * @param context
     */
    public void execute(TurbineRunData rundata,Context context) {

        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        ApplyDto applyDto = stationApplyService.getStationApplyDetail( userId + "", loginId);

        if (applyDto == null || applyDto.getStationApplyId()==null) {
            throw new IllegalArgumentException("��û���ύ�����뵥!");
        }
        //�鿴�汾��ʱ����ʾ���µİ汾
        context.put("protocolVersion", applyDto.getProtocolVersion()); //��ʾ��Э��汾
    }
}
