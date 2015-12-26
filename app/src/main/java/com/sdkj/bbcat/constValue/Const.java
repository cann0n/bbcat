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

    //网络请求信息配置
    public static final String Schema = "http://120.55.185.75:8080/bubumao";

    public static final String GetVerifyCode = Schema + "/app/sms/sendVerifyCode";

    public static final String PostVerifyCode = Schema + "/app/user/registerFirstStep";

    public static final String PostVerifyCodeEnd = Schema + "/app/user/registerSecondStep";

    public static final String FindScretePostPhoneAndVerifyCode = Schema + "/app/user/findPwdFirstStep";

    public static final String FindScreteEnd = Schema + "/app/user/findPwdSecondStep";

    public static final String Login = Schema + "/app/user/login";
}
