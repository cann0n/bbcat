package com.sdkj.bbcat.bean;

import java.io.Serializable;

/**
 * Created by Mr.Yuan on 2016/1/6 0006.
 * 喂养和预防接种都用这个
 */


public class FeedInoVo implements Serializable
{
    private String id="";
    private String uid="";
    private String name="";
    private String num="";
    private String day="";
    private String type="";

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNum()
    {
        return num;
    }

    public void setNum(String num)
    {
        this.num = num;
    }

    public String getDay()
    {
        return day;
    }

    public void setDay(String day)
    {
        this.day = day;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }
}
