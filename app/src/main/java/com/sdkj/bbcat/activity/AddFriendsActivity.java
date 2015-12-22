package com.sdkj.bbcat.activity;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.widget.TitleBar;

public class AddFriendsActivity extends BaseActivity{
    @Override
    public void initBusiness() {
        new TitleBar(activity).setTitle("找好友").back();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_addfriends;
    }
}
