package com.sdkj.bbcat.activity.loginandregister;

import android.view.View;

import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by Mr.Yuan on 2015/12/27 0027.
 */
public class FillInfosEndActivity extends SimpleActivity implements View.OnClickListener
{
    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_fillinfosend;
    }

    @Override
    public void initBusiness()
    {
        TitleBar titleBar = new TitleBar(activity).setTitle("宝宝生日").back();
    }

    @Override
    public void onClick(View v)
    {

    }
}
