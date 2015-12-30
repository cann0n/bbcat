package com.sdkj.bbcat.bean;

/**
 * Created by Administrator on 2015/12/31 0031.
 *动态详情
 */
public class CircleDetailVo {
    private CircleVo.ItemCircle.UserInfo user_info;
    private CircleVo.ItemCircle.NewsInfo news_info;

    public CircleVo.ItemCircle.NewsInfo getNews_info() {
        return news_info;
    }

    public void setNews_info(CircleVo.ItemCircle.NewsInfo news_info) {
        this.news_info = news_info;
    }

    public CircleVo.ItemCircle.UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(CircleVo.ItemCircle.UserInfo user_info) {
        this.user_info = user_info;
    }
}
