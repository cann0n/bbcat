package com.sdkj.bbcat.bean;

import java.io.Serializable;

/**
 * Created by Mr.Yuan on 2015/12/27 0027.
 */
public class LoginBean implements Serializable
{
    private UserInfosBean userInfo;
    private String token;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserInfosBean getUserInfo()
    {
        return userInfo;
    }

    public void setUserInfo(UserInfosBean userInfo)
    {
        this.userInfo = userInfo;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}