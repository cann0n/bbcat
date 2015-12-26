package com.sdkj.bbcat.constValue;

/**
 * Created by Mr.Yuan on 2015/12/18 0018.
 */
public class Const
{
    //SharePreference信息配置
    public static final String SPNAME = "sp_bbcat";

    public static final String SPNAME_FIRSTINSTALL = "firstinstall";

    public static final String SPNAME_ALREADYLOGIN = "alreadylogin";
    
    public static  final String APK_VERSION="0.1";
   
    public static  final String CLIENT="android";
    
    

    //网络请求信息配置
    public static final String DOMAIN = "http://120.55.185.75:8080/bubumao/";

    public static final String GetVerifyCode = DOMAIN + "app/sms/sendVerifyCode";

    public static final String PostVerifyCode = DOMAIN + "app/user/registerFirstStep";

    public static final String PostVerifyCodeEnd = DOMAIN + "app/user/registerSecondStep";

    /**
     * 获取首页信息
     */
    public static final String HOME_PAGE = DOMAIN + "api/index/index";

    /**
     * 详情页
     */
    public static final String NEWS_DETAIL=DOMAIN+"api/index/detail";
    
    
}
