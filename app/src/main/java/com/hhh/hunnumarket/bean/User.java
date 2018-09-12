package com.hhh.hunnumarket.bean;

import java.io.Serializable;

public class User implements Serializable {
    private Integer uid;
    private String sid;
    private String password;
    private String nickname;
    private String register_time;
    private String imei;
    private String last_online;
    private String qq;
    private String phone_number;
    private String register_ip;
    private String user_location;
    private Integer state;
    private Integer banned_by;
    private String last_ip;
    private Integer login_count;
    private String image_url;

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", sid='" + sid + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", register_time='" + register_time + '\'' +
                ", imei='" + imei + '\'' +
                ", last_online='" + last_online + '\'' +
                ", qq='" + qq + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", register_ip='" + register_ip + '\'' +
                ", user_location='" + user_location + '\'' +
                ", state=" + state +
                ", banned_by=" + banned_by +
                ", last_ip='" + last_ip + '\'' +
                ", login_count=" + login_count +
                ", image_url='" + image_url + '\'' +
                '}';
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLast_online() {
        return last_online;
    }

    public void setLast_online(String last_online) {
        this.last_online = last_online;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getRegister_ip() {
        return register_ip;
    }

    public void setRegister_ip(String register_ip) {
        this.register_ip = register_ip;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getBanned_by() {
        return banned_by;
    }

    public void setBanned_by(Integer banned_by) {
        this.banned_by = banned_by;
    }

    public String getLast_ip() {
        return last_ip;
    }

    public void setLast_ip(String last_ip) {
        this.last_ip = last_ip;
    }

    public Integer getLogin_count() {
        return login_count;
    }

    public void setLogin_count(Integer login_count) {
        this.login_count = login_count;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
