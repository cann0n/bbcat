package com.sdkj.bbcat.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.adapter.SimplePageAdapter;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片浏览
 *
 */
public class PicScannerActivity extends SimpleActivity {
    @ViewInject(R.id.vp_pager)
    private ViewPager vp_pager;

    @ViewInject(R.id.tv_current)
    private TextView tv_current;

    private SimplePageAdapter adapter;
    
    private String index;
    
    @Override
    public void initBusiness() {
        final ArrayList<CircleVo.ItemCircle.Cover> urls = (ArrayList<CircleVo.ItemCircle.Cover>) getVo("0");
        index= (String) getVo("1");
        tv_current.setText(1 + "/" + urls.size());
        List<View> views = new ArrayList<View>(urls.size());
        for (CircleVo.ItemCircle.Cover pic : urls) {
            ImageView iv = new ImageView(activity);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AppUtils.getWidth(activity), LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setLayoutParams(lp);
            if(Utils.isEmpty(pic.getPath())){
                Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(pic.getImg())).into(iv);
            }else{
                Glide.with(activity.getApplicationContext()).load(pic.getPath()).into(iv);
            }
            views.add(iv);
        }
        adapter = new SimplePageAdapter(views);
        vp_pager.setAdapter(adapter);
        if(!Utils.isEmpty(index)){
            vp_pager.setCurrentItem(Integer.parseInt(index));
        }else{
            vp_pager.setCurrentItem(0);
        }
        vp_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_current.setText((position + 1) + "/" + urls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.iv_back)
    void back(View view) {
        finish();
    }
    @Override
    public int setLayoutResID() {
        return R.layout.activity_pic_scanner;
    }
}
