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
    
    private Standard standard;

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

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
    
    public  static class Standard{
        private Time quilt;
        private Time faver;
        private Sleep sleep;

        public Time getQuilt() {
            return quilt;
        }

        public void setQuilt(Time quilt) {
            this.quilt = quilt;
        }

        public Time getFaver() {
            return faver;
        }

        public void setFaver(Time faver) {
            this.faver = faver;
        }

        public Sleep getSleep() {
            return sleep;
        }

        public void setSleep(Sleep sleep) {
            this.sleep = sleep;
        }
    }
    
    public  static class Time{
        private int interval;
        
        private int temp;

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }
    }
    
    public  static class Sleep{
        private String month;
        private double sleep_min;
        private double sleep_max;
        private String min_notice;
        private String max_notice;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public double getSleep_min() {
            return sleep_min;
        }

        public void setSleep_min(double sleep_min) {
            this.sleep_min = sleep_min;
        }

        public double getSleep_max() {
            return sleep_max;
        }

        public void setSleep_max(double sleep_max) {
            this.sleep_max = sleep_max;
        }

        public String getMin_notice() {
            return min_notice;
        }

        public void setMin_notice(String min_notice) {
            this.min_notice = min_notice;
        }

        public String getMax_notice() {
            return max_notice;
        }

        public void setMax_notice(String max_notice) {
            this.max_notice = max_notice;
        }
    }
}