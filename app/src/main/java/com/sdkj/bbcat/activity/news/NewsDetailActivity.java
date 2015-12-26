package com.sdkj.bbcat.activity.news;

import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by Administrator on 2015/12/26 0026.
 * 咨询详情
 */
public class NewsDetailActivity extends SimpleActivity{
    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("详情");
    }

    @Override
    public int setLayoutResID() {
        return 0;
    }
}
