package com.sdkj.bbcat.activity.loginandregister;

import android.view.View;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.widget.TitleBar;

public class FindScreteFirstStepActivity extends BaseActivity implements View.OnClickListener
{
    @ViewInject(R.id.findscrete_first)
    private TextView verifyFirstTv;

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_findscretefirststep;
    }

    @Override
    public void initBusiness()
    {
        new TitleBar(activity).setTitle("找回密码").back();
        verifyFirstTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v == verifyFirstTv)
        {
            skip(FindScreteSecondStepActivity.class);
        }
    }
}
