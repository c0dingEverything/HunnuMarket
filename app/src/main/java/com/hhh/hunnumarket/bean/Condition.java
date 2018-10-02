package com.hhh.hunnumarket.bean;

public class Condition {
    public final static String ORDER_UP = "asc";
    public final static String ORDER_DOWN = "desc";
    public final static int SEX_MAN = 0;
    public final static int SEX_WOMEN = 1;
    public final static int SEX_ALL = 2;

    private String[] keyword;
    private String order = ORDER_DOWN;
    private String orderBy;
    private int page;
    private int size = 10;
    private int type = Good.TYPE_SELL;
    private int cid;
    private int sex_tendency = SEX_ALL;
    private int uid = -1;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getSex_tendency() {
        return sex_tendency;
    }

    public void setSex_tendency(int sex_tendency) {
        this.sex_tendency = sex_tendency;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String[] getKeyword() {
        return keyword;
    }

    public void setKeyword(String[] keyword) {
        this.keyword = keyword;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
