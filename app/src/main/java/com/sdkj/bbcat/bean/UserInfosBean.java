package com.sdkj.bbcat.bean;

import java.io.Serializable;

/**
 * Created by Mr.Yuan on 2015/12/27 0027.
 */
public class UserInfosBean implements Serializable
{
    private String nickname;
    private String sex;
    private String birthday;
    private String baby_status;
    private String avatar;

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getBaby_status()
    {
        return baby_status;
    }

    public void setBaby_status(String baby_status)
    {
        this.baby_status = baby_status;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }
}