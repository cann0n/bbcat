package com.sdkj.bbcat.activity;

import android.view.View;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.widget.TitleBar;

public class HospitalDetailActivity extends BaseActivity{
    @Override
    public void initBusiness() {
        new TitleBar(activity).setTitle("医院详情").back();
    }


    @OnClick(R.id.iv_doctor_more)
    void doctor_more(View view){
        skip(DoctorListActivity.class);
    }

    @OnClick(R.id.rl_bida)
    void bida(View view){
        skip(AskActivity.class);
    }
    @Override
    public int setLayoutResID() {
        return R.layout.activity_hospitaldetail;
    }
}
