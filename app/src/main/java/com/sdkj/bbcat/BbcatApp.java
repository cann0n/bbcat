package com.sdkj.bbcat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
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

import java.util.Iterator;
import java.util.List;

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
        MultiDex.install(this);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null ||!processAppName.equalsIgnoreCase("com.sdkj.bbcat")) {
            //"com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);
        applicationContext = this;
        instance=this;
        DemoHelper.getInstance().init(applicationContext);
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);
        SimpleUtils.loginHx(this);
        
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
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
