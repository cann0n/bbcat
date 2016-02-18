package com.sdkj.bbcat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${Rhino} on 2016/2/18 10:43
 */
public class VaccineVo implements Serializable{
    private String month;
    
    private List<VaccineChildVo> list;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<VaccineChildVo> getList() {
        return list;
    }

    public void setList(List<VaccineChildVo> list) {
        this.list = list;
    }

    public  static  class VaccineChildVo implements Serializable{
        private String id;
        private String month;
        private String title;
        private String desc ;
        private String times;
        private String must;
        private String user_vac_ed;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getMust() {
            return must;
        }

        public void setMust(String must) {
            this.must = must;
        }

        public String getUser_vac_ed() {
            return user_vac_ed;
        }

        public void setUser_vac_ed(String user_vac_ed) {
            this.user_vac_ed = user_vac_ed;
        }
    }
}
