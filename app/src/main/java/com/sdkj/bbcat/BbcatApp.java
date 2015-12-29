package com.sdkj.bbcat;

import com.easemob.chat.EMChat;
import com.huaxi100.networkapp.application.BaseApplication;

/**
 * Created by ${Rhino} on 2015/12/16 18:23
 */
public class BbcatApp extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        EMChat.getInstance().init(this);

        EMChat.getInstance().setDebugMode(true);
    }
}
