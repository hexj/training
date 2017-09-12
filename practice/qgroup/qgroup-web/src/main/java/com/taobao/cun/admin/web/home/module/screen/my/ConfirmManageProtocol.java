package com.taobao.cun.admin.web.home.module.screen.my;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.switches.CuntaoAdminConfig;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.station.StationApplyDetailDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by jiandong.zjd on 2015/6/27.
 */
public class ConfirmManageProtocol {

    public static final Logger logger = LoggerFactory.getLogger(ConfirmManageProtocol.class);

    @Resource
    private StationApplyService stationApplyService;

    @Autowired
    private HttpSession session;


    /**
     * �������Э��ҳ��
     * @param rundata
     * @param context
     */
    public void execute(TurbineRunData rundata,Context context) {
        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        StationApplyDetailDto saDetailDto = null;
        if (userId != null && StringUtils.isNotBlank(loginId)) {
            saDetailDto = stationApplyService.getStationDetail(userId + "", loginId);
        }
        if (saDetailDto == null) {
            throw new IllegalArgumentException("param error !");
        }
        //TODO Double check �Ѿ�ǩ������Э���ֱ�ӽ�ȥ������ҳ
        if(StringUtil.isNotBlank(saDetailDto.getManageProtocolVersion())&&
                Integer.valueOf(saDetailDto.getManageProtocolVersion())>= CuntaoAdminConfig.MANAGE_PROTOCOL_VERSION){
            rundata.setRedirectLocation("/my/index.htm");
            return ;
        }
        //TODO �����
        context.put("manageProtocolVersion",CuntaoAdminConfig.MANAGE_PROTOCOL_VERSION);
        context.put("applyStationId",saDetailDto.getStationApplyId());
    }
}
