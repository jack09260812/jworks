package com.github.utils.web;

/**
 * Created by jinwei.li on 2017/5/5 0005.
 */
public class RequestVo {
    //开始序号
    private Integer offset;
    //结束序号序号
    private Integer end;
    //每页数量
    private Integer limit;
    //页数
    private Integer pageNo;

    public RequestVo() {
        this.offset = 0;
        //默认为10
        this.limit = 10;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        this.offset = (pageNo - 1) * limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
        this.pageNo = offset / limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getEnd() {
        return offset + limit;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }
}
