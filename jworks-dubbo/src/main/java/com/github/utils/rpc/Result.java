package com.github.utils.rpc;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by jinwei.li on 2017/5/5 0005.
 */
public class Result<T> implements Serializable {
    /**
     * 页码，从1开始
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 总数
     */
    private long total;

    //响应返回码
    private Integer code;

    //结果
    private T data;

    public Result() {

    }

    public Result(T data) {
        this.data = data;
    }

    public Result(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(int pageNum, int pages, long total, T data) {
        this.pageNum = pageNum;
        this.pages = pages;
        this.total = total;
        this.data = data;
    }

    public Result(int pageNum, int pages, long total, Integer code, T data) {
        this.pageNum = pageNum;
        this.pages = pages;
        this.total = total;
        this.code = code;
        this.data = data;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
