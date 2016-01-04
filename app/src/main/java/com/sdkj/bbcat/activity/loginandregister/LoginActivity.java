package com.sdkj.bbcat.activity.loginandregister;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.BbcatApp;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.bean.VerifyBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr.Yuan on 2015/12/18 0018.
 */
public class LoginActivity extends SimpleActivity implements View.OnClickListener {
    @ViewInject(R.id.login_aacount)
    private EditText mAccountEt;
    @ViewInject(R.id.login_password)
    private EditText mPasswordEt;
    @ViewInject(R.id.login_verification)
    private EditText mVerificationEt;
    /* @ViewInject(R.id.login_verificationbtn)
     private Button       mVerificationBtn;*/
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

    private VerifyBean datas;
    private boolean etState[] = {false, false};

    @Override
    public int setLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    public void initBusiness() {
        TitleBar titleBar = new TitleBar(activity).setTitle("登陆").back();
        titleBar.showRight("注册", new View.OnClickListener()
        {
            public void onClick(View v)
            {
                skip(RegisterInputPhoneActivity.class);
            }
        });

        mAccountEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) etState[0] = true;
                else etState[0] = false;

                if (etState[0] == true && etState[1] == true) {
                    mLoginBtn.setEnabled(true);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_orange);
                } else {
                    mLoginBtn.setEnabled(false);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mAccountEt.setText("");

        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) etState[1] = true;
                else etState[1] = false;

                if (etState[0] == true && etState[1] == true) {
                    mLoginBtn.setEnabled(true);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_orange);
                } else {
                    mLoginBtn.setEnabled(false);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLoginBtn.setOnClickListener(this);
        mFindPasswordsTv.setOnClickListener(this);
        mWeiXinBtn.setOnClickListener(this);
        mQQBtn.setOnClickListener(this);
        mWeiBoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginBtn) {
            showDialog();
            PostParams params = new PostParams();
            params.put("mobile", mAccountEt.getText().toString().trim());
            params.put("password", mPasswordEt.getText().toString().trim());
            HttpUtils.postJSONObject(LoginActivity.this, Const.Login, params, new RespJSONObjectListener(LoginActivity.this) {
                @Override
                public void getResp(JSONObject jsonObject)
                {
                    dismissDialog();
                    RespVo<LoginBean> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);

                    if (respVo.isSuccess())
                    {
                        LoginBean bean = respVo.getData(jsonObject, LoginBean.class);
                        ((BbcatApp)getApplication()).setmUser(bean);
                        SpUtil sp_login =  new SpUtil(activity,Const.AL_LOGIN);
                        sp_login.setValue("isLogin", true);
                        sp_login.setValue("sex", bean.getUserInfo().getSex());
                        sp_login.setValue("birthday", bean.getUserInfo().getBirthday());
                        sp_login.setValue("nickname", bean.getUserInfo().getNickname());
                        sp_login.setValue("token", bean.getToken());
                        toast("登陆成功");
                        finish();
                    }

                    else
                    {
                        toast(respVo.getMessage());
                    }
                }

                @Override
                public void doFailed()
                {
                    dismissDialog();
                    toast("链接服务器失败");
                }
            });
        }

        else if (v == mFindPasswordsTv)
        {
            skip(FindScreteFirstStepActivity.class);
        }

     /*   else if (v == mVerificationBtn)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("from", "login");
            map.put("type", "sms");
            map.put("phone", mAccountEt.getText().toString().trim());

            HttpUtils.getJSONObject(LoginActivity.this, getCompleteUrl(Const.GetVerifyCode, map), new RespJSONObjectListener(LoginActivity.this)
            {
                public void getResp(JSONObject jsonObject)
                {
                    VerifyBean bean = GsonTools.getVo(jsonObject.toString(), VerifyBean.class);
                    datas = bean;
                    if (datas.getReturnCode().equals("SUCCESS"))
                    {
                        daojishi = 60;
                        mVerificationEt.setEnabled(true);
                        mAccountEt.setEnabled(false);
                        handler.sendEmptyMessage(0);
                    } else
                        toast(datas.getMessage());
                }

                public void doFailed()
                {
                    toast("链接服务器失败");
                }
            });
        }*/

        else if (v == mWeiXinBtn) {
            toast("点击微信登陆");
        } else if (v == mQQBtn) {
            toast("点击QQ登陆");
        } else if (v == mWeiBoBtn) {
            toast("点击微博登陆");
        }
    }

    private final static String getCompleteUrl(String url, HashMap<String, String> params) {
        if (null != params && params.size() != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(url + "?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
            return stringBuffer.substring(0, stringBuffer.length() - 1);
        } else return url;
    }
}