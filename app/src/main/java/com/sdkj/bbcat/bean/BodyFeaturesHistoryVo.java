package com.sdkj.bbcat.bean;

import java.util.List;

/**
 * Created by ${Rhino} on 2016/1/26 10:36
 */
public class BodyFeaturesHistoryVo {
    private List<NewsVo> news;
    private List<GrowthVo.BobyState> logs;

    public List<NewsVo> getNews() {
        return news;
    }

    public void setNews(List<NewsVo> news) {
        this.news = news;
    }

    public List<GrowthVo.BobyState> getLogs() {
        return logs;
    }

    public void setLogs(List<GrowthVo.BobyState> logs) {
        this.logs = logs;
    }
}
