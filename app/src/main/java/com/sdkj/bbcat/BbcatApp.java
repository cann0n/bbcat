package com.sdkj.bbcat;

import com.huaxi100.networkapp.application.BaseApplication;
import com.sdkj.bbcat.bean.LoginBean;

/**
 * Created by ${Rhino} on 2015/12/16 18:23
 */
public class BbcatApp extends BaseApplication
{
    private LoginBean mUser;

    @Override
    public void onCreate()
    {
        super.onCreate();
       /* EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);*/
    }

    public LoginBean getmUser()
    {
        return mUser;
    }

    public void setmUser(LoginBean mUser)
    {
        this.mUser = mUser;
    }
}
