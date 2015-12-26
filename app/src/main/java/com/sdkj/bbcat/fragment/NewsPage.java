package com.sdkj.bbcat.fragment;

import android.view.View;
import android.widget.ImageView;

import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 */
public class NewsPage extends BaseFragment implements View.OnClickListener
{
    @ViewInject(R.id.iv_right)
    private ImageView loginImg;

    @Override
    protected int setLayoutResID()
    {
        return R.layout.fragment_news;
    }

    @Override
    protected void setListener()
    {
        loginImg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        if(v == loginImg)
        {
            activity.skip(LoginActivity.class);
        }
    }
}
