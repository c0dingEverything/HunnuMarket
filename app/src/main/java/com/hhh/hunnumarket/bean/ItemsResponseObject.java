package com.hhh.hunnumarket.bean;

import java.util.List;
import java.util.Map;

public class ItemsResponseObject<T> {

    public final static int STATUS_OK = 1;
    public final static int STATUS_ERROR = -1;
    public final static int STATUS_EXCEPTION = -2;
    public final static int STATUS_FUCKER = -3;
    public final static int STATUS_TOKEN_EXPIRED = -4;

    private int status = STATUS_FUCKER;
    private String msg = "爸爸爱你,求你别瞎搞!";
    private List<T> items;
    private Map<String, String[]> pics;

    @Override
    public String toString() {
        return "ItemsResponseObject{" +
                "items=" + items +
                ", pics=" + pics +
                '}';
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Map<String, String[]> getPics() {
        return pics;
    }

    public void setPics(Map<String, String[]> pics) {
        this.pics = pics;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
