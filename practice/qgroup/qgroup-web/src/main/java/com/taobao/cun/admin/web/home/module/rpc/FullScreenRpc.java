package com.taobao.cun.admin.web.home.module.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.fullscreen.CuntaoFullScreenItemService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.HavanaContext;
import com.taobao.cun.dto.partneritem.FullScreenItemDto;
import com.taobao.cun.dto.partneritem.FullScreenItemListDto;
import com.taobao.cun.dto.partneritem.FullScreenItemSaveDto;
import com.taobao.cun.service.ic.CuntaoInvestmentExtraService;


/**
 * 大屏-合伙人推荐后台页面RPC处理类
 *
 * @author haoqi.zh
 * @since 2015/04/14
 */
@WebResource("fullscreen")
public class FullScreenRpc {
    private static final Logger logger = LoggerFactory.getLogger(FullScreenRpc.class);

    @Resource
    private CuntaoFullScreenItemService cuntaoFullScreenItemService;
    @Resource
    private CuntaoInvestmentExtraService cuntaoInvestmentExtraService;

    @Autowired
    private HttpSession session;
    @Autowired
    protected URIBrokerService urlBrokerService;

    @Security(checkCSRF=true)
    @ResourceMapping(value = "/addItem")
    public AjaxResult<FullScreenItemSaveDto> saveItem(@RequestParam(name = "itemId") String itemId) {
        AjaxResult<FullScreenItemSaveDto> result = new AjaxResult<FullScreenItemSaveDto>();
        Long itemIdLong;
        try {
			itemIdLong=Long.valueOf(itemId);
		} catch (NumberFormatException e) {
			FullScreenItemSaveDto fullScreenItemSaveDto=new FullScreenItemSaveDto();
			fullScreenItemSaveDto.setResultStatus(2);
			result.setData(fullScreenItemSaveDto);
            result.setSuccess(true);
			return result;
		}
        HavanaContext havanaContext = getHavanaContext();
        if (havanaContext.getTaobaoUserId() == null) {
            result.setSuccess(false);
            result.setExceptionDesc("请先登陆");
        } else {
            if (itemIdLong == null || itemIdLong < 1) {
                result.setSuccess(false);
                result.setExceptionDesc("商品ID不能为空");
            } else {
                try {
                    FullScreenItemSaveDto fullScreenItemSaveDto = cuntaoFullScreenItemService.saveItem(itemIdLong, havanaContext);

                    result.setData(fullScreenItemSaveDto);
                    result.setSuccess(true);
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setExceptionDesc(e.getMessage());

                    logger.error(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    @Security(checkCSRF=true)
    @ResourceMapping(value = "/deleteItem")
    public AjaxResult<Boolean> deleteItem(@RequestParam(name = "id") Long id) {
        AjaxResult<Boolean> result = new AjaxResult<Boolean>();
        HavanaContext havanaContext = getHavanaContext();
        if (havanaContext.getTaobaoUserId() == null) {
            result.setSuccess(false);
            result.setExceptionDesc("请先登陆");
        } else {
            if (id == null || id < 1) {
                result.setSuccess(false);
                result.setExceptionDesc("商品ID不能为空");
            } else {
                try {
                    Boolean deleteResult = cuntaoFullScreenItemService.deleteItem(id, Long.valueOf(havanaContext.getTaobaoUserId()));
                    result.setData(deleteResult);
                    result.setSuccess(true);
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setExceptionDesc(e.getMessage());

                    logger.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    @Security(checkCSRF=true)
    @ResourceMapping(value = "/getMyItems")
    public AjaxResult<Map> getMyItems() {
        AjaxResult<Map> result = new AjaxResult<Map>();
        HavanaContext havanaContext = getHavanaContext();
        if (havanaContext.getTaobaoUserId() == null) {
            result.setSuccess(false);
            result.setExceptionDesc("请先登陆");
        } else {
            try {
                FullScreenItemListDto screenItemList = cuntaoFullScreenItemService.getItemsByUserId(Long.valueOf(havanaContext.getTaobaoUserId()));
                result.setData(toItemListResultMap(screenItemList));
                result.setSuccess(true);
            } catch (Exception e) {
                result.setSuccess(false);
                result.setExceptionDesc(e.getMessage());

                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    private Map<String, Object> toItemListResultMap(FullScreenItemListDto fullScreenItemListDto) {
        Map<String, Object> rpcResultMap = new HashMap<String, Object>();
        rpcResultMap.put("remain_num", fullScreenItemListDto.getRemainNum());

        if (null != fullScreenItemListDto.getFullScreenItems() && fullScreenItemListDto.getFullScreenItems().size() > 0) {
            List<Map> itemList = new ArrayList<Map>();
            for (FullScreenItemDto item : fullScreenItemListDto.getFullScreenItems()) {
                Map<String, Object> singleItemResultMap = this.toSingleItemResultMap(item);
                itemList.add(singleItemResultMap);
            }

            rpcResultMap.put("full_screen_items", itemList);
        }

        return rpcResultMap;
    }

    private Map<String, Object> toSingleItemResultMap(FullScreenItemDto fullScreenItemDto) {
        Map<String, Object> rpcResultMap = new HashMap<String, Object>();
        rpcResultMap.put("item_numiid", fullScreenItemDto.getItemId());
        rpcResultMap.put("item_current_price", fullScreenItemDto.getItemCurrentPrice());
        rpcResultMap.put("item_pic", fullScreenItemDto.getItemPic());
        rpcResultMap.put("item_trade_num", fullScreenItemDto.getItemTradeNum());
        rpcResultMap.put("item_price", fullScreenItemDto.getItemPrice());
        rpcResultMap.put("item_title", fullScreenItemDto.getItemTitle());
        rpcResultMap.put("item_auction_status",fullScreenItemDto.getAuctionStatus());
        rpcResultMap.put("item_pic_list", fullScreenItemDto.getItemPicList());

        return rpcResultMap;
    }

    private HavanaContext getHavanaContext() {
        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        HavanaContext havanaContext = new HavanaContext();
        havanaContext.setLoginId(loginId);
        havanaContext.setTaobaoUserId(userId.toString());
        return havanaContext;
    }

//    private String getFullImgUrl(String imgUri) {
//        if (StringUtil.isEmpty(imgUri))
//            return StringUtil.EMPTY_STRING;
//        else
//            return urlBrokerService.getURIBroker("uploadImageServer").fullURI() + "/" + imgUri;
//    }

}
