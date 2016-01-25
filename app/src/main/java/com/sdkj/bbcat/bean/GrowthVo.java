package com.sdkj.bbcat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/6 0006.
 */
public class GrowthVo implements  Serializable
{
    private List<String>    now_category;
    private BobyState     baby_status;
    //private List<BobyState>     baby_status;
    private List<NewsVo>    news;
    private List<FeedInoVo> feed_log;
    private List<FeedInoVo>    vac_log;

    public List<String> getNow_category()
    {
        return now_category;
    }

    public void setNow_category(List<String> now_category)
    {
        this.now_category = now_category;
    }

   public BobyState getBaby_status()
   {
       return baby_status;
   }

    public void setBaby_status(BobyState baby_status)
    {
        this.baby_status = baby_status;
    }

   /* public List<BobyState> getBaby_status()
    {
        return baby_status;
    }

    public void setBaby_status(List<BobyState> baby_status)
    {
        this.baby_status = baby_status;
    }*/

    public List<NewsVo> getNews()
    {
        return news;
    }

    public void setNews(List<NewsVo> news)
    {
        this.news = news;
    }

    public List<FeedInoVo> getFeed_log()
    {
        return feed_log;
    }

    public void setFeed_log(List<FeedInoVo> feed_log)
    {
        this.feed_log = feed_log;
    }

    public List<FeedInoVo> getVac_log()
    {
        return vac_log;
    }

    public void setVac_log(List<FeedInoVo> vac_log)
    {
        this.vac_log = vac_log;
    }

    public static class BobyState implements Serializable
    {
        private String id;
        private String uid;
        private String day;
        private String height;
        private String weight;
        private String head;
        private String create_time;
        private String month;
        private float min_height;
        private float max_height;
        private float min_weight;
        private float max_weight;
        private float min_head;
        private float max_head;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getUid()
        {
            return uid;
        }

        public void setUid(String uid)
        {
            this.uid = uid;
        }

        public String getDay()
        {
            return day;
        }

        public void setDay(String day)
        {
            this.day = day;
        }

        public String getHeight()
        {
            return height;
        }

        public void setHeight(String height)
        {
            this.height = height;
        }

        public String getWeight()
        {
            return weight;
        }

        public void setWeight(String weight)
        {
            this.weight = weight;
        }

        public String getHead()
        {
            return head;
        }

        public void setHead(String head)
        {
            this.head = head;
        }

        public String getCreate_time()
        {
            return create_time;
        }

        public void setCreate_time(String create_time)
        {
            this.create_time = create_time;
        }

        public String getMonth()
        {
            return month;
        }

        public void setMonth(String month)
        {
            this.month = month;
        }

        public float getMin_height()
        {
            return min_height;
        }

        public void setMin_height(float min_height)
        {
            this.min_height = min_height;
        }

        public float getMax_height()
        {
            return max_height;
        }

        public void setMax_height(float max_height)
        {
            this.max_height = max_height;
        }

        public float getMin_weight()
        {
            return min_weight;
        }

        public void setMin_weight(float min_weight)
        {
            this.min_weight = min_weight;
        }

        public float getMax_weight()
        {
            return max_weight;
        }

        public void setMax_weight(float max_weight)
        {
            this.max_weight = max_weight;
        }

        public float getMin_head()
        {
            return min_head;
        }

        public void setMin_head(float min_head)
        {
            this.min_head = min_head;
        }

        public float getMax_head()
        {
            return max_head;
        }

        public void setMax_head(float max_head)
        {
            this.max_head = max_head;
        }
    }
}