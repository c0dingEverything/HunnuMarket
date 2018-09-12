package com.hhh.hunnumarket.bean;

public class UserToken {
    private Integer tid;
    private Integer uid;
    private String access_token;
    private String expiry;

    @Override
    public String toString() {
        return "UserToken{" +
                "uid=" + uid +
                ", access_token='" + access_token + '\'' +
                ", expiry='" + expiry + '\'' +
                '}';
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
