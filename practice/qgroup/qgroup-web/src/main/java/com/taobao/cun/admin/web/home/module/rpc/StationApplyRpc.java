package com.taobao.cun.admin.web.home.module.rpc;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.taobao.cun.admin.alipay.corporate.AlipayBailService;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;

/**
 * Created by Fe on 14/11/12.
 */
@WebResource("apply")
public class StationApplyRpc {

    public static final Logger logger = LoggerFactory.getLogger(StationApplyRpc.class);

    @Resource
    private StationApplyService stationApplyService;
    @Resource
    private AlipayBailService alipayBailService;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Security(checkCSRF=true)
    @ResourceMapping(value = "/doConfirmApply")
    public Map<String,Object> doConfirmApply() {

        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);

        boolean flag = stationApplyService.confirmStationAppply(userId + "", loginId);

        Map<String,Object> resultMap = new HashMap<String, Object>();

        resultMap.put("succ",flag);
        if (flag) {
            resultMap.put("url","/apply/confirmSuccess.htm");
        }
        return resultMap;
    }


    /**
     * �û�������֤�𶳽�ӿ�
     * 1.��ѯ�û��Ƿ�ǩԼ
     * 2.���û��ǩԼ����locationurl�����û��ض�����Ȩҳ��
     * 3.����Ѿ�ǩԼ,����֧���� ��֤�𶳽�ӿ�,������ɣ������û�����ʾҳ��
     * @return
     */
//    @ResourceMapping(value = "/doAlipayBail")
//    public Map<String,Object> doAlipayBail() {
//        String loginId = WebUtil.getLoginId(session);
//        Long userId = WebUtil.getUserId(session);
//
//        AlipayResponseDto alipayResponseDto = alipayBailService.alipayBail(userId+"",loginId);
//
//
//        Map<String,Object> resultMap = new HashMap<String, Object>();
//        resultMap.put("success",true);
//
//        resultMap.put("needOpenNewWindow",false);
//        if (alipayResponseDto.isSuccess()) {
//            resultMap.put("locationUrl",alipayResponseDto.getData());
//            if (!alipayResponseDto.getData().equals("/apply/applySuccess.htm")) {
//                resultMap.put("needOpenNewWindow",true);
//            }
//        } else {
//            resultMap.put("locationUrl", "/apply/applyFail.htm?code=" + alipayResponseDto.getErrorCode());
//        }
//        return resultMap;
//    }

}
