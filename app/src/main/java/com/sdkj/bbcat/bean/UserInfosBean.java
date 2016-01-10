package com.sdkj.bbcat.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Mr.Yuan on 2015/12/27 0027.
 */
public class UserInfosBean implements Serializable {
    private String nickname;
    private String sex;
    private String birthday;
    @Expose
    private String avatar = "http://img0.imgtn.bdimg.com/it/u=3710103736,733712610&fm=21&gp=0.jpg";

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}