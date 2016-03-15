package com.sdkj.bbcat.widget;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.utils.AppUtils;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoScrollViewPager {
    //初始化图片轮播所需控件
    private LinearLayout lpointGroup;// 小圆点
    private ViewPager lADViewPgaer;// 顶部自动横滑广告viewpager
    private List<ImageView> ladImageView;
    private int lcurrentItem;// 当前顶部 广告 指针
    private Activity activity;
    private MyPagerAdapter adapter;
    //外部回调接口
    public OnViewPagerClick onViewPagerClick;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lADViewPgaer.setCurrentItem(lcurrentItem);
        }

    };

    public AutoScrollViewPager(Activity activity) {
        this.activity = activity;
    }

    /**
     * 加载图片轮播
     *
     *
     */
    public void loadAutoScrollViewPager(View view, List<?> data) {

        int width = AppUtils.getWidth(activity);
        int height = 9 * width / 16;
        lpointGroup = (LinearLayout) view.findViewById(R.id.index_viewgroup);
        lpointGroup.removeAllViews();
        lADViewPgaer = (ViewPager) view.findViewById(R.id.index_viewpager);
        lADViewPgaer.setOffscreenPageLimit(data.size());
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) lADViewPgaer.getLayoutParams();
        lp.width=width;
        lp.height=height;
        lADViewPgaer.setLayoutParams(lp);
        lADViewPgaer.removeAllViews();
        ladImageView = new ArrayList<ImageView>();
        for (int i = 0, len = data.size(); i < len; i++) {
            CircleVo.ItemCircle.Cover ads= (CircleVo.ItemCircle.Cover) data.get(i);
            ImageView imageView = new ImageView(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.width = width;
            params.height = height;

            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(ads.getImg())).centerCrop().into(imageView);
            imageView.setLayoutParams(params);
            ladImageView.add(imageView);
            // 添加指示小圆点
            ImageView point = new ImageView(activity);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if(len!=1){
                params2.rightMargin = 15;
            }
            params2.height = 24;
            params2.width = 24;
            point.setLayoutParams(params2);
            // 如果显示的是第一页，则将第一个指示点点亮，其他的指示点为默认状态
            if (i == 0) {
                point.setImageResource(R.drawable.icon_point_select);
            } else {
                point.setImageResource(R.drawable.icon_point_unselect);
            }
            lpointGroup.addView(point);
        }
        adapter = new MyPagerAdapter(ladImageView);
        lADViewPgaer
                .setAdapter(adapter);
        lADViewPgaer
                .setOnPageChangeListener(new MyOnPageChangeListener());
        lcurrentItem = 0;
    }

    //图片轮播的动作逻辑
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    break;
                case 2:// 界面切换中
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            // 改变指示点的状态
            for (int i = 0, len = ladImageView.size(); i < len; i++) {
                ImageView imgPoint = (ImageView) lpointGroup.getChildAt(i);
                if (i == arg0) {// 当前小圆点
                    imgPoint.setImageResource(R.drawable.icon_point_select);
                    lcurrentItem = arg0;
                } else {// 设置没有在当前页面的指示点
                    imgPoint.setImageResource(R.drawable.icon_point_unselect);
                }
            }
        }

    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (lADViewPgaer) {
                if (lcurrentItem < ladImageView.size() - 1) {
                    lcurrentItem++;
                } else {
                    lcurrentItem = 0;
                }
//                currentItem = (currentItem+1)%imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    //开始自动轮播
    public void startScrollViewPager() {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleWithFixedDelay(new SlideShowTask(), 0, 3, TimeUnit.SECONDS);
            return;
        } else {
            return;
        }
    }


    /**
     * 填充ViewPager的页面适配器
     * Created by Levi on 2015/9/21.
     */
    public class MyPagerAdapter extends PagerAdapter {
        private List<ImageView> limgList;

        public MyPagerAdapter(List<ImageView> limgList) {
            this.limgList = limgList;
        }

        @Override
        public int getCount() {
            return limgList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public Object instantiateItem(ViewGroup container, final int position) {
            limgList.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(onViewPagerClick!=null){
                    onViewPagerClick.onViewPagerClick(v, position);
                }
                }
            });
            container.addView(limgList.get(position));
            return limgList.get(position);
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }
    }

    //接口注册函数
    public void setOnViewPagerClick(OnViewPagerClick onViewPagerClick) {
        this.onViewPagerClick = onViewPagerClick;
    }

    //外部回调接口
    public interface OnViewPagerClick {
        void onViewPagerClick(View view, int position);
    }
}
