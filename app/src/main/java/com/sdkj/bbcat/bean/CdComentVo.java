package com.sdkj.bbcat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/15 0015.
 */
public class CdComentVo implements Serializable
{
    private String total_count;
    private List<CommentVo> list;

    public String getTotal_count()
    {
        return total_count;
    }

    public void setTotal_count(String total_count)
    {
        this.total_count = total_count;
    }

    public List<CommentVo> getList()
    {
        return list;
    }

    public void setList(List<CommentVo> list)
    {
        this.list = list;
    }
}