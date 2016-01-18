package com.sdkj.bbcat.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2015/12/31 0031.
 * 发布动态时，的圈圈
 */
public class CircleTagVo {
    private String id;
    private String title;

    /**
     * 1:普通日记，2：宝宝第一次
     */
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
