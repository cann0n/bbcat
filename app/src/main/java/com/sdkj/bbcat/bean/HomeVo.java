package com.sdkj.bbcat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/26 0026.
 */
public class HomeVo {
    
    private List<Banner> slider;
    
    private List<Category> news;

    public List<Banner> getSlider() {
        return slider;
    }

    public void setSlider(List<Banner> slider) {
        this.slider = slider;
    }

    public List<Category> getNews() {
        return news;
    }

    public void setNews(List<Category> news) {
        this.news = news;
    }

    public static class Banner implements Serializable{
        private String id;
        private String thumb;
        private String link;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
    
    public  static  class Category implements Serializable{
        private  String category_id;
        
        private String category_title;
       
        private List<NewsVo> category_list;

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCategory_title() {
            return category_title;
        }

        public void setCategory_title(String category_title) {
            this.category_title = category_title;
        }

        public List<NewsVo> getCategory_list() {
            return category_list;
        }

        public void setCategory_list(List<NewsVo> category_list) {
            this.category_list = category_list;
        }
    }
    
}
