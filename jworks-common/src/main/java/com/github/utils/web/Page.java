package com.github.utils.web;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by jinwei.li on 2017/5/5 0005.
 */
public class Page<T> implements Serializable {
    //结果条数
    private Integer total;
    //响应返回码
    private Integer code;
    //响应返回信息
    private String resultMsg;
    private T data;

    public Page() {

    }

    public Page(T data) {
        this.data = data;
    }

    public Page(Integer code, String resultMsg, T data) {
        this.code = code;
        this.resultMsg = resultMsg;
        this.data = data;
    }

    public Page total(Integer total) {
        this.total = total;
        return this;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", resultMsg);
        json.put("data", data);
        json.put("total", total);
        return json.toJSONString();
    }
}
