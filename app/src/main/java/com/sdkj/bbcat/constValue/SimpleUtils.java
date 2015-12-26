package com.sdkj.bbcat.constValue;

import android.content.Context;

import com.huaxi100.networkapp.network.PostParams;

import java.io.InputStream;

/**
 * Created by Administrator on 2015/12/26 0026.
 */
public class SimpleUtils {
    
    public  static String buildUrl(String url){
        if(url.endsWith("?")){
            url=url+"version="+Const.APK_VERSION+"&client="+Const.CLIENT;
        }else{
            url=url+"&version="+Const.APK_VERSION+"&client="+Const.CLIENT;
        }
        return  url;
    }
    public  static PostParams buildUrl(PostParams params){
        params.put("version",Const.APK_VERSION);
        params.put("client",Const.CLIENT);
        return  params;
    }
    public static String getImageUrl(String url) {
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
    
}
