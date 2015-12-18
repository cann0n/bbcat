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
public class LoginActivity extends BaseActivity
{
    @ViewInject(R.id.login_aacount)
    private EditText mAccountEt;
    @ViewInject(R.id.login_password)
    private EditText mPasswordEt;
    @ViewInject(R.id.login_verification)
    private EditText mVerificationEt;
    @ViewInject(R.id.login_verificationbtn)
    private Button mVerificationBtn;
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
                toast("点击注册按钮");
            }
        });
    }
}
