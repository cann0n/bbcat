package com.sdkj.bbcat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class CircleVo {

    private List<HotTagsVo>  hot_tags;
    
    private List<ItemCircle> news;

    public List<HotTagsVo> getHot_tags() {
        return hot_tags;
    }

    public void setHot_tags(List<HotTagsVo> hot_tags) {
        this.hot_tags = hot_tags;
    }

    public List<ItemCircle> getNews() {
        return news;
    }

    public void setNews(List<ItemCircle> news) {
        this.news = news;
    }

    public static class HotTagsVo implements Serializable{
        private String id;
        private String title;
        private String cover;
        private String joined_person;

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

        public String getJoined_person() {
            return joined_person;
        }

        public void setJoined_person(String joined_person) {
            this.joined_person = joined_person;
        }
    }
    
    public  static class ItemCircle implements Serializable{
        
        private NewsInfo news_info;
        private UserInfo user_info;

        public NewsInfo getNews_info() {
            return news_info;
        }

        public void setNews_info(NewsInfo news_info) {
            this.news_info = news_info;
        }

        public UserInfo getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfo user_info) {
            this.user_info = user_info;
        }

        public  static  class UserInfo implements Serializable{ 
            private String avatar;
            private String nickname;
            private String birthday;
            /**
             * 0:未关注，1：已关注
             */
            private String is_following;
            
            private String uid;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getIs_following() {
                return is_following;
            }

            public void setIs_following(String is_following) {
                this.is_following = is_following;
            }
        }
        public  static  class NewsInfo implements Serializable{
            private  String id;
            private  String address;
            private  String create_time;
            private  String view;
            private  String title;
            private  String cover;
            private  String comment;
            private  String collection;
            private  String uid;
            
            private String is_collected;

            public String getIs_collected() {
                return is_collected;
            }

            public void setIs_collected(String is_collected) {
                this.is_collected = is_collected;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getView() {
                return view;
            }

            public void setView(String view) {
                this.view = view;
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

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getCollection() {
                return collection;
            }

            public void setCollection(String collection) {
                this.collection = collection;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
        
    }
}
