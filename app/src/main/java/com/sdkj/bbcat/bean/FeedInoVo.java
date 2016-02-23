package com.sdkj.bbcat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/6 0006.
 * 喂养和预防接种都用这个
 */


public class FeedInoVo implements Serializable {
    private String day ;
    private String week ;
   
    private List<FeedInfo> list;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<FeedInfo> getList() {
        return list;
    }

    public void setList(List<FeedInfo> list) {
        this.list = list;
    }

    public  static class FeedInfo implements Serializable{
        private int id;
        private String name;
        private String num;
        private String day;
        private String desc;
        private int type;
        private String time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
