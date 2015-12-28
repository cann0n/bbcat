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
}