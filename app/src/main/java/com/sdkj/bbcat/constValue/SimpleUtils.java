package com.sdkj.bbcat.constValue;

import android.app.AlertDialog;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/26 0026.
 */
public class SimpleUtils {

    public static String buildUrl(BaseActivity activity, String url) {
        if (url.endsWith("?")) {
            url = url + "version=" + Const.APK_VERSION + "&client=" + Const.CLIENT;
        } else {
            url = url + "&version=" + Const.APK_VERSION + "&client=" + Const.CLIENT;
        }
        if (isLogin(activity)) {
            SpUtil sp = new SpUtil(activity, Const.SP_NAME);
            url = url + "&token=" + sp.getStringValue(Const.TOKEN) + "&uid=" + sp.getStringValue(Const.UID);
        }
        return url;
    }

    /**
     * 将时间字符串转化为时间戳
     * PHP/1000
     */
    public static long getTimeStamp(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }
    public static long getTimeMillion(String time) {
//        Timestamp millionSeconds = Timestamp.valueOf(time + " 00:00:00");//毫秒
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        long lTime = System.currentTimeMillis();
        Date dt2 = null;
        try {
            dt2 = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        lTime = dt2.getTime() / 1000;

        return lTime;
    }
    public static PostParams buildUrl(BaseActivity activity, PostParams params) {
        params.put("version", Const.APK_VERSION);
        params.put("client", Const.CLIENT);
        if (isLogin(activity)) {
            SpUtil sp = new SpUtil(activity, Const.SP_NAME);
            params.put("token", sp.getStringValue(Const.TOKEN));
            params.put("uid", sp.getStringValue(Const.UID));
        }
        return params;
    }

    public static boolean isLogin(BaseActivity activity) {
        SpUtil sp = new SpUtil(activity, Const.SP_NAME);
        if (Utils.isEmpty(sp.getStringValue(Const.TOKEN))) {
            return false;
        }
        return true;
    }

    public static String getImageUrl(String url) {
        if (Utils.isEmpty(url)) {
            return "";
        } else if (url.startsWith("http") || url.startsWith("https")) {
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

    public static void loginOut(BaseActivity activity) {
        final SpUtil sp = new SpUtil(activity, Const.SP_NAME);
        sp.remove(Const.PHONE);
        sp.remove(Const.AVATAR);
        sp.remove(Const.UID);
        sp.remove(Const.NICKNAME);
        sp.remove(Const.TOKEN);
        loginOutHx();
        activity.skip(LoginActivity.class, "fromReconnect");
        activity.finish();
    }

    public static void loginOutHx() {
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

    public static void loginHx(final Context context) {
        final SpUtil sp = new SpUtil(context, Const.SP_NAME);
        if (!Utils.isEmpty(sp.getStringValue(Const.PHONE))) {
            EMChatManager.getInstance().login(sp.getStringValue(Const.PHONE), sp.getStringValue(Const.PHONE), new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    System.out.println("sp = 登录环信成功");
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
                        }
                    }).start();
                }
            });
        }

    }

    public static void showNotify(Context context) {
        SpUtil sp = new SpUtil(context, Const.SP_NAME);
        if (sp.getBoolValue(Const.NOTIFY)) {
            if (sp.getStringValue(Const.NOTIFY_TIME).equals(Utils.formatHour(System.currentTimeMillis() + ""))) {
                new AlertDialog.Builder(context).setMessage(sp.getStringValue(Const.NOTIFY_MSG)).setPositiveButton("确定", null).show();
            }
        }
    }


    public static Map bd_decrypt(double bd_lat, double bd_lon) {
         double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        Map map = new HashMap();
        double mgLat, mgLon;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        mgLon = z * Math.cos(theta);
        mgLat = z * Math.sin(theta);
        DecimalFormat df   = new DecimalFormat("0.000000");
        map.put("lat", mgLat);
        map.put("long",mgLon );
//        DecimalFormat df   = new DecimalFormat("0.000000");
//        map.put("lat", df.format(mgLat));
//        map.put("long",df.format(mgLon) );
        return map;
    }
}
