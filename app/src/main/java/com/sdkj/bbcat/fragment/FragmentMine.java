package com.sdkj.bbcat.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.BbcatApp;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.MyDynamicActivity;
import com.sdkj.bbcat.activity.loginandregister.BlacklistActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.activity.loginandregister.MyCollectActivity;
import com.sdkj.bbcat.activity.loginandregister.MyFollowActivity;
import com.sdkj.bbcat.activity.loginandregister.PersonInfosActivity;
import com.sdkj.bbcat.activity.loginandregister.RegisterStep1Activity;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.bean.UserStatVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.CircleImageView;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by ${Rhino} on 2016/2/1 17:10
 */
public class FragmentMine extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.pc_head)
    private CircleImageView mHead;
    @ViewInject(R.id.pc_unregisall)
    private LinearLayout mUnLoginLL;
    @ViewInject(R.id.pc_login)
    private TextView mLogin;
    @ViewInject(R.id.pc_regis)
    private TextView mRegis;
    @ViewInject(R.id.pc_name)
    private TextView mName;
    @ViewInject(R.id.pc_pinglun)
    private TextView mConment;
    @ViewInject(R.id.pc_dianzan)
    private TextView mZan;
    @ViewInject(R.id.pc_fenxiang)
    private TextView mShare;
    @ViewInject(R.id.pc_jifenall)
    private RelativeLayout mJiFenRL;
    @ViewInject(R.id.pc_jifen)
    private TextView mJiFen;
    @ViewInject(R.id.pc_myfriendall)
    private RelativeLayout mFriendRL;
    @ViewInject(R.id.pc_friendmsgall)
    private RelativeLayout mFriendMsgRL;
    @ViewInject(R.id.pc_friendmsg)
    private TextView mFriendMsgNum;
    @ViewInject(R.id.pc_zujiall)
    private RelativeLayout mZuJiRL;
    @ViewInject(R.id.pc_ringall)
    private RelativeLayout mRingRL;
    @ViewInject(R.id.pc_personinfosall)
    private RelativeLayout mPersonRL;
    @ViewInject(R.id.pc_qitaall)
    private RelativeLayout mQitaRL;
    
    @ViewInject(R.id.tv_version)
    private TextView tv_version;

    public static final int MLOGIN = 0X0001;
    public static final int MMDOY = 0X0002;

    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);
        initData();
        mLogin.setOnClickListener(this);
        mRegis.setOnClickListener(this);
        mJiFenRL.setOnClickListener(this);
        mFriendRL.setOnClickListener(this);
        mFriendMsgRL.setOnClickListener(this);
        mZuJiRL.setOnClickListener(this);
        mRingRL.setOnClickListener(this);
        mPersonRL.setOnClickListener(this);
        mQitaRL.setOnClickListener(this);
        tv_version.setText("当前版本:"+Const.APK_VERSION);
    }

    private void initData() {
        SpUtil sp = new SpUtil(activity, Const.SP_NAME);
        if (SimpleUtils.isLogin(activity)) {
            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(sp.getStringValue(Const.AVATAR))).into(mHead);
            mUnLoginLL.setVisibility(View.GONE);
            mName.setVisibility(View.VISIBLE);
            mName.setText(sp.getStringValue(Const.NICKNAME));
            getUserInfo();
        } else {
            mUnLoginLL.setVisibility(View.VISIBLE);
            mName.setVisibility(View.GONE);
            mConment.setText("0");
            mZan.setText("0");
            mShare.setText("0");
            mJiFen.setText("0");
            mFriendMsgNum.setText("0");
        }
    }

    private void getUserInfo() {
        HttpUtils.postJSONObject(activity, Const.USER_INFO, SimpleUtils.buildUrl(activity, new PostParams()), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                RespVo<UserStatVo> resp = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (resp.isSuccess()) {
                    UserStatVo userStat = resp.getData(jsonObject, UserStatVo.class);
                    mConment.setText(userStat.getComments());
                    mZan.setText(userStat.getCollections());
                    mShare.setText(userStat.getShares());
                    mJiFen.setText(userStat.getScore());
                }
            }

            @Override
            public void doFailed() {

            }
        });
    }

    public void onClick(View v) {
        if (v == mLogin) {
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivityForResult(intent, MLOGIN);
        } else if (v == mRegis) {
            activity.skip(RegisterStep1Activity.class);
        }

        /**积分*/
        else if (v == mJiFenRL) {
//            toast("积分");
        }

        /**我的好友*/
        else if (v == mFriendRL) {
            activity.skip(MyCollectActivity.class);
        }

        /**好友动态*/
        else if (v == mFriendMsgRL) {

            activity.skip(MyFollowActivity.class);
        }

        /**成长足迹*/
        else if (v == mZuJiRL) {
            activity.skip(BlacklistActivity.class);
        }

        /**我的圈圈*/
        else if (v == mRingRL) {
        }

        /**个人信息*/
        else if (v == mPersonRL) {
            if (SimpleUtils.isLogin(activity)) {
                Intent intent = new Intent(activity, PersonInfosActivity.class);
                startActivityForResult(intent, MMDOY);
            } else {
            }
        }

        /**其他*/
        else if (v == mQitaRL) {
            SimpleUtils.loginOut(activity);
        }
    }
    public void onEventMainThread(RefreshEvent event) {
        SpUtil sp=new SpUtil(activity,Const.SP_NAME);
        Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(sp.getStringValue(Const.AVATAR))).into(mHead);
//        mUnLoginLL.setVisibility(View.GONE);
//        mName.setVisibility(View.VISIBLE);
        mName.setText(sp.getStringValue(Const.NICKNAME));
//        getUserInfo();
    }
    
    
    public  static class RefreshEvent{
        
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.rl_my_dynamic)
    void showDynamic(View view) {
        activity.skip(MyDynamicActivity.class);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_mine;
    }
}
