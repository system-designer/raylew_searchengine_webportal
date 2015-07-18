package com.searchengine.webportal.service.impl;

import java.util.Date;

/**
 * Created by liold on 2014/11/15.
 */
public class WebApiResult<T> {
    private int code;        //请求结果编码
    private String msg;        //请求结果文字说明
    private Date timestamp;    //服务器时间戳
    T data;                    //请求结果数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
