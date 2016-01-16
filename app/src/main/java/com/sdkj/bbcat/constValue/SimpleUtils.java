package com.sdkj.bbcat.constValue;

import android.content.Context;

import com.bumptech.glide.util.Util;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;

import java.io.InputStream;

/**
 * Created by Administrator on 2015/12/26 0026.
 */
public class SimpleUtils {
    
    public  static String buildUrl(BaseActivity activity,String url){
        if(url.endsWith("?")){
            url=url+"version="+Const.APK_VERSION+"&client="+Const.CLIENT;
        }else{
            url=url+"&version="+Const.APK_VERSION+"&client="+Const.CLIENT;
        }
        if(isLogin(activity)){
            SpUtil sp=new SpUtil(activity,Const.SP_NAME);
            url=url+"&token="+sp.getStringValue(Const.TOKEN)+"&uid="+sp.getStringValue(Const.UID);
        }
        return  url;
    }
    public  static PostParams buildUrl(BaseActivity activity,PostParams params){
        params.put("version",Const.APK_VERSION);
        params.put("client",Const.CLIENT);
        if(isLogin(activity)){
            SpUtil sp=new SpUtil(activity,Const.SP_NAME);
            params.put("token",sp.getStringValue(Const.TOKEN));
            params.put("uid",sp.getStringValue(Const.UID));
        }
        return  params;
    }
    
    public  static boolean isLogin(BaseActivity activity){
        SpUtil sp=new SpUtil(activity,Const.SP_NAME);
        if(Utils.isEmpty(sp.getStringValue(Const.TOKEN))){
            return  false;
        }
        return  true;
    }
    
    public static String getImageUrl(String url) {
        if(Utils.isEmpty(url)){
            return "";
        }else 
        if (url.startsWith("http") || url.startsWith("https")) {
            return url;
        } else {
            return Const.IMAGE_DOMAIN + url;
        }
    }

    /**
     * 获取模板文件内容
     *
     * @param context
     * @param tempFile 模板文件名
     * @return 模板文件内容
     */
    public static String readTemplate(Context context, String tempFile) {
        InputStream is = null;
        try {
            is = context.getAssets().open(tempFile);
            byte[] list = new byte[is.available()];
            byte b = 0;
            int i = 0;
            while ((b = (byte) is.read()) != -1) {
                list[i++] = b;
            }
            return new String(list, "UTF8");
        } catch (Exception e) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
                is = null;
            }
        }
    }
    
    public static void loginOut(BaseActivity activity){
        final SpUtil sp=new SpUtil(activity, Const.SP_NAME);
        sp.remove(Const.PHONE);
        sp.remove(Const.AVATAR);
        sp.remove(Const.UID);
        sp.remove(Const.NICKNAME);
        sp.remove(Const.TOKEN);
        loginOutHx();
        activity.skip(LoginActivity.class, "fromReconnect");
        activity.finish();
    }
    
    public static void loginOutHx(){
        EMChatManager.getInstance().logout(new EMCallBack() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {

            }
        });
    }
    
    public static void loginHx(final Context context){
        final SpUtil sp=new SpUtil(context, Const.SP_NAME);
        if(!Utils.isEmpty(sp.getStringValue(Const.PHONE))){
            EMChatManager.getInstance().login(sp.getStringValue(Const.PHONE),sp.getStringValue(Const.PHONE),new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    System.out.println("sp = 登录环信成功" );
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    System.out.println("code = " + code);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                // 调用sdk注册方法
                                EMChatManager.getInstance().createAccountOnServer(sp.getStringValue(Const.PHONE), sp.getStringValue(Const.PHONE));
                            } catch (final EaseMobException e) {
                                //注册失败
                            }
                        }}).start();
                }
            });
        }

    }
}
