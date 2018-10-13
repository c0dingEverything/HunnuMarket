package com.hhh.hunnumarket.bean;

public class MsgAdmin {
    private Integer mid;
    private Integer aid;
    private String body;
    private String title;
    private Integer priority;
    private String post_datetime;

    @Override
    public String toString() {
        return "MsgAdmin{" +
                "mid=" + mid +
                ", aid=" + aid +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                ", post_datetime=" + post_datetime +
                '}';
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPost_datetime() {
        return post_datetime;
    }

    public void setPost_datetime(String post_datetime) {
        this.post_datetime = post_datetime;
    }
}
