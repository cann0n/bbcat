package com.sdkj.bbcat.activity;

import android.view.View;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;

/**
 * 医疗在线
 */

public class MedicalOnlineActivity extends BaseActivity{
    @Override
    public void initBusiness() {

    }

    @OnClick(R.id.iv_search)
    void search(View view){
        skip(SearchActivity.class);
    }
    
    @Override
    public int setLayoutResID() {
        return R.layout.activity_medicalonline;
    }
}
