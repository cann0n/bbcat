package com.sdkj.bbcat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/13 0013.
 */
public class AllBodyFeaBean implements Serializable
{
    private List<BodyFeaBean> logs;
    private List<NewsVo> news;

    public List<BodyFeaBean> getLogs()
    {
        return logs;
    }

    public void setLogs(List<BodyFeaBean> logs)
    {
        this.logs = logs;
    }

    public List<NewsVo> getNews()
    {
        return news;
    }

    public void setNews(List<NewsVo> news)
    {
        this.news = news;
    }
}
