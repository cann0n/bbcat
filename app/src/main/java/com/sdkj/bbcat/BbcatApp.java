package com.sdkj.bbcat;

import android.content.Context;

import com.huaxi100.networkapp.application.BaseApplication;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.hx.DemoHelper;

/**
 * Created by ${Rhino} on 2015/12/16 18:23
 */
public class BbcatApp extends BaseApplication
{
    private LoginBean mUser;

    public static Context applicationContext;
    private static BbcatApp instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
       /* EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);*/
        applicationContext = this;
        DemoHelper.getInstance().init(applicationContext);
    }

    public LoginBean getmUser()
    {
        return mUser;
    }

    public void setmUser(LoginBean mUser)
    {
        this.mUser = mUser;
    }

    public static BbcatApp getInstance() {
        return instance;
    }
}
