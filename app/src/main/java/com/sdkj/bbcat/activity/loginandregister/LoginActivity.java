package com.sdkj.bbcat.activity.loginandregister;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by Mr.Yuan on 2015/12/18 0018.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener
{
    @ViewInject(R.id.login_aacount)
    private EditText mAccountEt;
    @ViewInject(R.id.login_password)
    private EditText mPasswordEt;
   /* @ViewInject(R.id.login_verification)
    private EditText mVerificationEt;
    @ViewInject(R.id.login_verificationbtn)
    private Button mVerificationBtn;*/
    @ViewInject(R.id.login_btn)
    private Button mLoginBtn;
    @ViewInject(R.id.login_forgetpassword)
    private TextView mFindPasswordsTv;
    @ViewInject(R.id.login_weixin)
    private LinearLayout mWeiXinBtn;
    @ViewInject(R.id.login_qq)
    private LinearLayout mQQBtn;
    @ViewInject(R.id.login_weibo)
    private LinearLayout mWeiBoBtn;

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_login;
    }

    @Override
    public void initBusiness()
    {
        TitleBar titleBar = new TitleBar(activity).setTitle("登陆").back();
        titleBar.showRight("注册",new View.OnClickListener()
        {
            public void onClick(View v)
            {
               skip(RegisterInputPhoneActivity.class);
            }
        });

        mLoginBtn.setOnClickListener(this);
        mFindPasswordsTv.setOnClickListener(this);
        mWeiXinBtn.setOnClickListener(this);
        mQQBtn.setOnClickListener(this);
        mWeiBoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == mLoginBtn)
        {
            /*skip(MainActivity.class);*/
        }
        else if(v == mFindPasswordsTv)
        {
            skip(FindScreteFirstStepActivity.class);
        }
        else if(v == mWeiXinBtn)
        {
            toast("点击微信登陆");
        }
        else if(v == mQQBtn)
        {
            toast("点击QQ登陆");
        }
        else if(v == mWeiBoBtn)
        {
            toast("点击微博登陆");
        }
    }
}


