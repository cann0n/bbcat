package com.sdkj.bbcat;

import android.content.Context;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.huaxi100.networkapp.application.BaseApplication;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.hx.DemoHelper;

/**
 * Created by ${Rhino} on 2015/12/16 18:23
 */
public class BbcatApp extends BaseApplication {
    private LoginBean mUser;

    public static Context applicationContext;
    private static BbcatApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);
        applicationContext = this;
        instance=this;
        DemoHelper.getInstance().init(applicationContext);
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);
        SimpleUtils.loginHx(this);
        
    }

    public LoginBean getmUser() {
        return mUser;
    }

    public void setmUser(LoginBean mUser) {
        this.mUser = mUser;
    }

    public static BbcatApp getInstance() {
        return instance;
    }

}
