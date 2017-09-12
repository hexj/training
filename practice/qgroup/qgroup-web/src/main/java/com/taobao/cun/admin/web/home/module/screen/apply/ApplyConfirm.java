package com.taobao.cun.admin.web.home.module.screen.apply;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.alipay.AlipayConfig;
import com.taobao.cun.admin.dto.ApplyDto;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.station.enums.StationApplyProtocolConfirmingStepEnum;
import com.taobao.cun.dto.station.enums.StationApplyStateEnum;

/**
 * Created by Fe on 14/11/8.
 */
public class ApplyConfirm {

    public static final Logger logger = LoggerFactory.getLogger(ApplyConfirm.class);

    @Resource
    private StationApplyService stationApplyService;
    
    @Resource
    private AlipayConfig alipayConfig;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;

    /**
     * �����ͬҳ��
     * @param context
     */
    public void execute(TurbineRunData rundata,Context context) {

        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        ApplyDto applyDto = stationApplyService.getStationApplyDetail( userId + "", loginId);

        if (applyDto == null) {
            throw new IllegalArgumentException("param error !");
        }

        //TODO �Ѿ�ȷ�ϵĺ�ͬ  ֱ�ӽ��� ����ȷ��ҳ��
        if (applyDto.getProtocolConfirmingStep() != null && applyDto.getProtocolConfirmingStep().equals(StationApplyProtocolConfirmingStepEnum.CONFIRMED)) {
            rundata.setRedirectLocation("/apply/confirmSuccess.htm");
            return;
        }        

        //TODO �Ѿ������еô����� �����ٴ�ȷ��Э��
        if (applyDto.getState() != null && ! (applyDto.getState().equals(StationApplyStateEnum.SUMITTED) || applyDto.getState().equals(StationApplyStateEnum.CONFIRMED)) ) {
            throw new RuntimeException(" state error, not allow applyConirm!");
        }
      

        context.put("name",applyDto.getName());
        context.put("loginId",applyDto.getLoginId());
        context.put("alipayAccount",applyDto.getAlipayAccount());
        context.put("mobile",applyDto.getMobile());
        context.put("applierName",applyDto.getApplierName());
        context.put("idenNum",applyDto.getIdenNum());
        context.put("provinceDetail",applyDto.getProvinceDetail());
		context.put("cityDetail",applyDto.getCityDetail());
		context.put("countyDetail",applyDto.getCountyDetail());
		context.put("townDetail",applyDto.getTownDetail());
        context.put("address",applyDto.getAddress());
        context.put("idCardImageUrl",applyDto.getIdCardImageUrl());
        context.put("stationApplyContent",applyDto.getStationApplyContent());
        context.put("applyStationId",applyDto.getApplyStationId());
        context.put("version",applyDto.getVersion());
        if(StringUtils.isBlank(applyDto.getProtocolVersion())){
        	//Ϊ�գ���ʾ����ȷ�ϵ�
        	context.put("protocolVersion", alipayConfig.getProtocolVersion()); //��ʾ��Э��汾
        }else {
        	//���·���������ߵ������Ǳ�ʾ���������ǰ���ú��ˣ���ô��ʹ����ǰ���õİ汾
        	context.put("protocolVersion", applyDto.getProtocolVersion()); //��ʾ��Э��汾
        }
    }
}
