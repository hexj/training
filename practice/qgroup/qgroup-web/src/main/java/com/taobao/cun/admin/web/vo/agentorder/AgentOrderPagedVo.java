package com.taobao.cun.admin.web.vo.agentorder;

import com.taobao.cun.common.exception.ServiceException;

import java.util.List;

/**
 * Created by wangbowangb on 15-1-6.
 */
public class AgentOrderPagedVo {
    protected boolean success;
    protected String exceptionDesc;
    protected List<AegentOrderListDetailVo> result;
    private Long totalPage;
    private Integer page;
    private Integer size;
    private String state;
    private Long totalSize;

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getExceptionDesc() {
        return exceptionDesc;
    }

    public void setExceptionDesc(String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }

    public List<AegentOrderListDetailVo> getResult() {
        return result;
    }

    public void setResult(List<AegentOrderListDetailVo> result) {
        this.result = result;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }
}
