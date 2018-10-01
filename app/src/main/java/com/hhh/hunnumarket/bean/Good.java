package com.hhh.hunnumarket.bean;

import android.os.Parcelable;

import java.io.Serializable;

public class Good implements Serializable {
    public final static int STATE_NOT_VERIFIED = 0;
    public final static int STATE_VERIFIED = 1;
    public final static int STATE_ON_SALE = 2;
    public final static int STATE_SOLD = 3;
    public final static int STATE_DELETED = 4;
    public final static int TYPE_SELL = 0;
    public final static int TYPE_DEMAND = 1;

    private Integer gid;
    private Integer type;
    private String keyword;
    private Integer uid;
    private Integer visited;
    private String details;
    private Float price;
    private Integer tendency;
    private String user_contact;
    private Integer state;
    private Integer reports;
    private String post_time;
    private String user_location;
    private Integer cid;
    private Integer buyer_id;
    private Integer deleted_by;

    @Override
    public String toString() {
        return "Good{" +
                "gid=" + gid +
                ", keyword='" + keyword + '\'' +
                ", uid=" + uid +
                ", price=" + price +
                '}';
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getVisited() {
        return visited;
    }

    public void setVisited(Integer visited) {
        this.visited = visited;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getTendency() {
        return tendency;
    }

    public void setTendency(Integer tendency) {
        this.tendency = tendency;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getReports() {
        return reports;
    }

    public void setReports(Integer reports) {
        this.reports = reports;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(Integer buyer_id) {
        this.buyer_id = buyer_id;
    }

    public Integer getDeleted_by() {
        return deleted_by;
    }

    public void setDeleted_by(Integer deleted_by) {
        this.deleted_by = deleted_by;
    }
}
