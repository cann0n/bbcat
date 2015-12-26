package com.sdkj.bbcat.activity;

import android.view.View;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;

public class SearchActivity extends BaseActivity{
    @Override
    public void initBusiness() {

    }

    
    @OnClick(R.id.iv_back)
    void back(View view){
        finish();
    }
    @Override
    public int setLayoutResID() {
        return R.layout.activity_search;
    }
}
