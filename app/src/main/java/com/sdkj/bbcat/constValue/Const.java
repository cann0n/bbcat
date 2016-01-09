package com.sdkj.bbcat.constValue;

/**
 * Created by Mr.Yuan on 2015/12/18 0018.
 */
public class Const
{
    //SharePreference信息配置
    public static final String SP_NAME = "sp_bbcat";

    public static final String FIR_INSTALL = "firstinstall";

    public static final String AL_LOGIN = "alreadylogin";
    
    public static  final String APK_VERSION="0.1";
   
   
    public static  final String CLIENT="android";
    public static  final String UID="bbcat_uid";
    public static  final String TOKEN="bbcat_token";
    public static  final String PHONE="bbcat_phone";
    

    //网络请求信息配置
    public static final String DOMAIN = "http://120.26.212.241/api/";

    /**
     * 图片基地址
     */
    public static final String IMAGE_DOMAIN = "http://120.26.212.241/";

    /**
     * 文档地址
     */
    public static final String WIKI = "http://120.26.212.241:81/doku.php?id=wiki:oss";

    /**
     * 获取首页信息
     */
    public static final String HOME_PAGE = DOMAIN + "index/index";

    /**
     * 详情页
     */
    public static final String NEWS_DETAIL=DOMAIN+"index/detail";
    
    /**
     * 分类列表页
     */
    public static final String CATEGORY_LIST=DOMAIN+"index/lists";

    /**
     * 医院列表
     */
    public static final String HOSPITAL_LIST=DOMAIN+"/Hospital/index";

    /**
     * 医院详情
     */
    public static final String HOSPITAL_detail=DOMAIN+"/Hospital/detail";

    /**
     * 医院医生列表
     */
    public static final String EXPERT_LIST=DOMAIN+"/Hospital/expert";

    /**
     * 我的圈首页
     */
    public static final String MY_CIRCLE=DOMAIN+"/circle/index";

    /**
     * 医院最新活动
     */
    public static final String HOSPITAL_ACTIVITY=DOMAIN+"/Hospital/activity";
   
    /**
     * 圈圈详情
     */
    public static final String CIRCLE_DETAIL=DOMAIN+"/circle/detail";

    /**
     * 发布动态
     */
    public static final String PUBLIC_CIRCLE=DOMAIN+"/circle/push";

    /**
     * 发布动态获取tag
     */
    public static final String GET_TAGS=DOMAIN+"/circle/getTags";

    /**
     * 点赞
     */
    public static final String DO_LIKE=DOMAIN+"/circle/doCollection";

    /**
     * 上传图片
     */
    public static final String UPLOAD_IMAGE=DOMAIN+"/index/uploadPicture";

    /**
     * 搜索用户
     */
    public static final String FIND_FRIENDS=DOMAIN+"/user/search";

    /**
     * 关注
     */
    public static final String DO_FOLLOW=DOMAIN+"/circle/follow";
    
    public static final String Schema = "http://120.26.212.241/";

    public static final String GetVerifyCode = Schema + "api/sms/GetVerifyCode";

    public static final String Register = Schema + "api/user/reg";

    public static final String FindScrete = Schema + "api/user/findPassword";

    public static final String Login = Schema + "api/user/login";
}
