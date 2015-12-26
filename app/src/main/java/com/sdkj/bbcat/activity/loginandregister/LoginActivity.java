package com.sdkj.bbcat.activity.loginandregister;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.GetVerifyCodeBean;
import com.sdkj.bbcat.bean.baseBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr.Yuan on 2015/12/18 0018.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener
{
    @ViewInject(R.id.login_aacount)
    private EditText     mAccountEt;
    @ViewInject(R.id.login_password)
    private EditText     mPasswordEt;
    @ViewInject(R.id.login_verification)
    private EditText     mVerificationEt;
    @ViewInject(R.id.login_verificationbtn)
    private Button       mVerificationBtn;
    @ViewInject(R.id.login_btn)
    private Button       mLoginBtn;
    @ViewInject(R.id.login_forgetpassword)
    private TextView     mFindPasswordsTv;
    @ViewInject(R.id.login_weixin)
    private LinearLayout mWeiXinBtn;
    @ViewInject(R.id.login_qq)
    private LinearLayout mQQBtn;
    @ViewInject(R.id.login_weibo)
    private LinearLayout mWeiBoBtn;

    private GetVerifyCodeBean datas;
    private int               daojishi;
    private boolean etState[] = {false, false, false};
    private Handler handler   = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (daojishi == 0)
            {
                mVerificationBtn.setBackgroundResource(R.drawable.yanzhengkuangorange);
                mVerificationBtn.setTextColor(getResources().getColor(R.color.font_red));
                mVerificationBtn.setText("发送验证码");
                mVerificationEt.setText("");
                mVerificationEt.setEnabled(false);
                mAccountEt.setEnabled(true);
                mAccountEt.setText(mAccountEt.getText().toString());
            } else
            {
                mVerificationBtn.setBackgroundResource(R.drawable.btn_gray);
                mVerificationBtn.setTextColor(getResources().getColor(R.color.color_white));
                mVerificationBtn.setText(daojishi + "秒后重发");
                mVerificationBtn.setEnabled(false);
                daojishi--;
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };


    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_login;
    }

    @Override
    public void initBusiness()
    {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.toString().trim().length() != 0)
                    etState[0] = true;
                else
                    etState[0] = false;

                if (etState[0] == true && etState[1] == true && etState[2] == true)
                {
                    mLoginBtn.setEnabled(true);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_orange);
                } else
                {
                    mLoginBtn.setEnabled(false);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_gray);
                }

                if (Utils.isPhoneNum(s.toString().trim()))
                {
                    mVerificationBtn.setEnabled(true);
                    mVerificationBtn.setTextColor(getResources().getColor(R.color.font_red));
                    mVerificationBtn.setBackgroundResource(R.drawable.yanzhengkuangorange);

                } else
                {
                    mVerificationBtn.setEnabled(false);
                    mVerificationBtn.setTextColor(getResources().getColor(R.color.color_white));
                    mVerificationBtn.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        mAccountEt.setText("");

        mPasswordEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.toString().trim().length() != 0)
                    etState[1] = true;
                else
                    etState[1] = false;

                if (etState[0] == true && etState[1] == true && etState[2] == true)
                {
                    mLoginBtn.setEnabled(true);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_orange);
                } else
                {
                    mLoginBtn.setEnabled(false);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mVerificationEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.toString().trim().length() != 0)
                    etState[2] = true;
                else
                    etState[2] = false;

                if (etState[0] == true && etState[1] == true && etState[2] == true)
                {
                    mLoginBtn.setEnabled(true);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_orange);
                } else
                {
                    mLoginBtn.setEnabled(false);
                    mLoginBtn.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mVerificationBtn.setOnClickListener(this);
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
            PostParams params = new PostParams();
            params.put("username", mAccountEt.getText().toString().trim());
            params.put("password", mPasswordEt.getText().toString().trim());
            params.put("verifyCode", mVerificationEt.getText().toString().trim());
            if(datas != null)
            {
                params.put("sessionId", datas.getSessionId());
                HttpUtils.getJSONObject(LoginActivity.this, Const.Login + "?" + params.bindUrl(), new RespJSONObjectListener(LoginActivity.this)
                {
                    @Override
                    public void getResp(JSONObject jsonObject)
                    {
                        baseBean bean = GsonTools.getVo(jsonObject.toString(), baseBean.class);
                        if (bean.getReturnCode().equals("SUCCESS"))
                        {
                            //登陆成功，在此做缓存数据的处理。
                            //并返回到刚刚的界面。。。。。。。
                            toast("登陆成功");
                            finish();
                        }
                        else
                        {
                            toast(bean.getMessage());
                        }
                    }

                    public void doFailed()
                    {
                        toast("链接服务器失败");
                    }
                });
            }
            else
                toast("请使用正确的验证码进行登录");
        }

        else if (v == mFindPasswordsTv)
        {
            skip(FindScreteFirstStepActivity.class);
        }

        else if (v == mVerificationBtn)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("from", "login");
            map.put("type", "sms");
            map.put("phone", mAccountEt.getText().toString().trim());

            HttpUtils.getJSONObject(LoginActivity.this, getCompleteUrl(Const.GetVerifyCode, map), new RespJSONObjectListener(LoginActivity.this)
            {
                public void getResp(JSONObject jsonObject)
                {
                    GetVerifyCodeBean bean = GsonTools.getVo(jsonObject.toString(), GetVerifyCodeBean.class);
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
        }

        else if (v == mWeiXinBtn)
        {
            toast("点击微信登陆");
        }

        else if (v == mQQBtn)
        {
            toast("点击QQ登陆");
        }

        else if (v == mWeiBoBtn)
        {
            toast("点击微博登陆");
        }
    }

    private final static String getCompleteUrl(String url, HashMap<String, String> params)
    {
        if (null != params && params.size() != 0)
        {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(url + "?");
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
            return stringBuffer.substring(0, stringBuffer.length() - 1);
        } else
            return url;
    }
}