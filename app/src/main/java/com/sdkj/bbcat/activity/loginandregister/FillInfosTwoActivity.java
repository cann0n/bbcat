package com.sdkj.bbcat.activity.loginandregister;

import android.view.View;
import android.widget.LinearLayout;

import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by Mr.Yuan on 2015/12/27 0027.
 */
public class FillInfosTwoActivity extends SimpleActivity implements View.OnClickListener
{
    @ViewInject(R.id.infostwo_nv)
    private LinearLayout nv_ll;
    @ViewInject(R.id.infostwo_nan)
    private LinearLayout nan_ll;

    private int state = 0;
    private int sex  = 0;

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_fillinfostwo;
    }

    @Override
    public void initBusiness()
    {
        state = (int)getVo("0");
        TitleBar titleBar = new TitleBar(activity).setTitle("宝宝性别").back();
        nv_ll.setOnClickListener(this);
        nan_ll.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        if(v == nv_ll)//暂定1男2女
            sex = 1;
        else if(v == nan_ll)
            sex = 2;
        skip(FillInfosEndActivity.class,state,sex);
        finish();
    }
}
