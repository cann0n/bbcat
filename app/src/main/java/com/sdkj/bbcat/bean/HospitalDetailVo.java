package com.sdkj.bbcat.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/30 0030.
 */
public class HospitalDetailVo {
    
    private Detail hospital_detail;
    
    private List<Expert> hospital_expert;
    
    private int hospital_activity;
    private int chat_open;

    public int getChat_open() {
        return chat_open;
    }

    public void setChat_open(int chat_open) {
        this.chat_open = chat_open;
    }

    public int getHospital_activity() {
        return hospital_activity;
    }

    public void setHospital_activity(int hospital_activity) {
        this.hospital_activity = hospital_activity;
    }

    public Detail getHospital_detail() {
        return hospital_detail;
    }

    public void setHospital_detail(Detail hospital_detail) {
        this.hospital_detail = hospital_detail;
    }

    public List<Expert> getHospital_expert() {
        return hospital_expert;
    }

    public void setHospital_expert(List<Expert> hospital_expert) {
        this.hospital_expert = hospital_expert;
    }

    public static class Detail{
        private String id;
        private String title;
        private String cover;
        private String address;
        private int level;
        private String contact_phone;
        private String detail;
        private String category_name;
        private String hxchat_id;
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getHxchat_id() {
            return hxchat_id;
        }

        public void setHxchat_id(String hxchat_id) {
            this.hxchat_id = hxchat_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
    }
    
    public  static  class Expert{
        private String uid;
        private String export_name;
        private String avatar;
        private String export_depart;
        private String export_position;
        private String export_desc;
        private String export_time;
        private String export_signature;

        public String getExport_signature() {
            return export_signature;
        }

        public void setExport_signature(String export_signature) {
            this.export_signature = export_signature;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getExport_name() {
            return export_name;
        }

        public void setExport_name(String export_name) {
            this.export_name = export_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getExport_depart() {
            return export_depart;
        }

        public void setExport_depart(String export_depart) {
            this.export_depart = export_depart;
        }

        public String getExport_position() {
            return export_position;
        }

        public void setExport_position(String export_position) {
            this.export_position = export_position;
        }

        public String getExport_desc() {
            return export_desc;
        }

        public void setExport_desc(String export_desc) {
            this.export_desc = export_desc;
        }

        public String getExport_time() {
            return export_time;
        }

        public void setExport_time(String export_time) {
            this.export_time = export_time;
        }
    }
}
