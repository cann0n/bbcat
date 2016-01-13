package com.sdkj.bbcat.activity.loginandregister;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.BbcatApp;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.CircleImageView;

/**
 * Created by Mr.Yuan on 2016/1/13 0013.
 */
public class PersonalCenter extends SimpleActivity implements View.OnClickListener
{
    @ViewInject(R.id.pc_head)
    private CircleImageView mHead;
    @ViewInject(R.id.pc_unregisall)
    private LinearLayout    mUnLoginLL;
    @ViewInject(R.id.pc_login)
    private TextView        mLogin;
    @ViewInject(R.id.pc_regis)
    private TextView        mRegis;
    @ViewInject(R.id.pc_name)
    private TextView        mName;
    @ViewInject(R.id.pc_pinglun)
    private TextView        mConment;
    @ViewInject(R.id.pc_dianzan)
    private TextView        mZan;
    @ViewInject(R.id.pc_fenxiang)
    private TextView        mShare;
    @ViewInject(R.id.pc_jifenall)
    private RelativeLayout  mJiFenRL;
    @ViewInject(R.id.pc_jifen)
    private TextView        mJiFen;
    @ViewInject(R.id.pc_myfriendall)
    private RelativeLayout  mFriendRL;
    @ViewInject(R.id.pc_friendmsgall)
    private RelativeLayout  mFriendMsgRL;
    @ViewInject(R.id.pc_friendmsg)
    private TextView        mFriendMsgNum;
    @ViewInject(R.id.pc_zujiall)
    private RelativeLayout  mZuJiRL;
    @ViewInject(R.id.pc_ringall)
    private RelativeLayout  mRingRL;
    @ViewInject(R.id.pc_personinfosall)
    private RelativeLayout  mPersonRL;
    @ViewInject(R.id.pc_qitaall)
    private RelativeLayout  mQitaRL;
    private LoginBean       mLoginBean;

    public int setLayoutResID()
    {
        return R.layout.activity_personcenter;
    }

    public void initBusiness()
    {
        if (isLogin())
        {
            mLoginBean = ((BbcatApp) getApplication()).getmUser();
            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl((mLoginBean.getUserInfo().getAvatar()))).into(mHead);
            mUnLoginLL.setVisibility(View.GONE);
            mName.setVisibility(View.VISIBLE);
            mName.setText(mLoginBean.getUserInfo().getNickname());
        }
        else
        {
            mUnLoginLL.setVisibility(View.VISIBLE);
            mName.setVisibility(View.GONE);
        }

        mConment.setText("0");
        mZan.setText("0");
        mShare.setText("0");
        mJiFen.setText("0");
        mFriendMsgNum.setText("0");
        mLogin.setOnClickListener(this);
        mRegis.setOnClickListener(this);
        mJiFenRL.setOnClickListener(this);
        mFriendRL.setOnClickListener(this);
        mFriendMsgRL.setOnClickListener(this);
        mZuJiRL.setOnClickListener(this);
        mRingRL.setOnClickListener(this);
        mPersonRL.setOnClickListener(this);
        mQitaRL.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        if(v == mLogin)
        {
            skip(LoginActivity.class);
        }

        else if(v == mRegis)
        {
            skip(FindScreteFirstStepActivity.class);
        }

        /**积分*/
        else if(v == mJiFenRL)
        {
            toast("积分");
        }

        /**我的好友*/
        else if(v == mFriendRL)
        {
            toast("我的好友");
        }

        /**好友动态*/
        else if(v == mFriendMsgRL)
        {
            toast("好友动态");
        }

        /**成长足迹*/
        else if(v == mZuJiRL)
        {
            toast("成长足迹");
        }

        /**我的圈圈*/
        else if(v == mRingRL)
        {
            toast("我的圈圈");
        }

        /**个人信息*/
        else if(v == mPersonRL)
        {
           skip(PersonInfosActivity.class);
        }

        /**其他*/
        else if(v == mQitaRL)
        {
            toast("其它");
        }
    }
}