package com.hhh.hunnumarket.bean;


public class ResponseObject <T>{

    public final static int STATUS_OK = 1;
    public final static int STATUS_ERROR = -1;
    public final static int STATUS_EXCEPTION = -2;
    public final static int STATUS_TOKEN_EXPIRED = -4;

    private int status;
    private String msg;
    private T data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
