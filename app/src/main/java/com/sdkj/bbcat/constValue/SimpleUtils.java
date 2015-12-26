package com.sdkj.bbcat.constValue;

import com.huaxi100.networkapp.network.PostParams;

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
            return Const.DOMAIN + url;
        }
    }
    
    
}
