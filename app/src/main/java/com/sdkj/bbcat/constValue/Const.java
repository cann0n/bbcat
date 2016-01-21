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


    public static  final String PHONE="bbcat_phone";
    public static  final String AVATAR="bbcat_avatar";
    public static  final String NICKNAME="bbcat_nickname";


    public static final String APK_VERSION = "0.1";


    public static final String CLIENT = "android";
    public static final String UID    = "bbcat_uid";
    public static final String TOKEN  = "bbcat_token";


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
     * 热门标签
     */
    public static final String HOT_TAGS=DOMAIN+"/circle/hotTagsAll";

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
     * 获取好友头像
     */
    public static final String GET_AVATARS=DOMAIN+"/user/getUsersInfo";

    /**
     * 关注
     */
    public static final String DO_FOLLOW=DOMAIN+"/circle/follow";

    public static final String Schema = "http://120.26.212.241/";

    public static final String GetVerifyCode = DOMAIN + "sms/GetVerifyCode";
    /**
     * 注册账号
     */
    public static final String Register = DOMAIN + "user/reg";

    /**
     * 找回密码
     */
    public static final String FindScrete = DOMAIN + "user/findPassword";

    /**
     * 登陆账号
     */
    public static final String Login = DOMAIN + "user/login";

    /**
     * 获取手环页面底部数据
     */
    public static final String GetBraBotDates = DOMAIN + "Band/index";

    /**
     * 获取成长足迹首页数据
     */
    public static final String GetGrowthDatas = DOMAIN + "Band/grows";

    /**
     * 获取成长足迹身体特侦数据
     */
    public static final String GetBodyFeatures = DOMAIN + "Band/babyBodyStatus";

    /**
     * 获取喂养和预防接种记录
     */
    public static final String GetFeedAndIno = DOMAIN + "Band/getFeedVacLogs";

    /**
     * 设置喂养和预防接种记录
     */
    public static final String SetFeedAndIno = DOMAIN + "Band/feedVacLog";

    /**
     * 记录宝宝成长数据记录
     */
    public static final String SetBabyFeatureInfos = DOMAIN + "Band/babyBodyLog";
    

    /**
     * 周围的人
     */
    public static final String AROUND_PEOPLE = DOMAIN + "user/nearby";
    
    /**
     * 保存用户信息
     */
    public static final String SaveUserInfos = DOMAIN + "user/saveInfo";

    /**
     * 上传用户头像
     */
    public static final String UpdateUserAvatar = DOMAIN + "user/userAvatar";

    /**
     * 发布评论
     */
    public static final String CommitComment = DOMAIN + "circle/doComment";

    /**
     * 获取文章评论
     */
    public static final String GetComment = DOMAIN + "circle/commentList";

    /**
     * 搜索栏热门词语
     */
    public static final String GetHotChar = DOMAIN + "index/getHotSearch";

    /**
     * 搜索栏热门词语
     */
    public static final String SearchContent = DOMAIN + "index/search";

    /**
     * 第三方登录
     */
    public static final String THIRD_LOGIN = DOMAIN + "user/thirdLogin";

    /**
     * 获取用户统计数据
     */
    public static final String USER_INFO = DOMAIN + "user/getUserStat";
    /**
     * 分享
     */
    public static final String SHARE = DOMAIN + "circle/share/id/";

    /**
     * 获取所有群组
     */
    public static final String GET_GROUPS = DOMAIN + "webim/getAllGroup";

    /**
     * 获取宝宝日记
     */
    public static final String NOTE_LIST = DOMAIN + "circle/lists";

    /**
     * 我的关注
     */
    public static final String MY_FOLLOWS = DOMAIN + "user/myFollows";

    /**
     * 我的收藏
     */
    public static final String MY_COLLECT = DOMAIN + "index/myCollections";

    /**
     * 宝宝辅食
     */
    public static final String BABY_FOODS = IMAGE_DOMAIN + "baby/food/index";

    /**
     * 在线咨询
     */
    public static final String ONLINE_FAQ = DOMAIN + "index/questions";
}
