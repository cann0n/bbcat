package com.sdkj.bbcat.activity.loginandregister;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.BbcatApp;
import com.sdkj.bbcat.MainActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.bean.VerifyBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.hx.PreferenceManager;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import de.greenrobot.event.EventBus;

/**
 * Created by Mr.Yuan on 2015/12/18 0018.
 */
public class LoginActivity extends SimpleActivity implements View.OnClickListener, PlatformActionListener {
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

    private VerifyBean datas;
    private boolean etState[] = {false, false};

    @Override
    public int setLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    public void initBusiness() {
        EventBus.getDefault().register(this);
        TitleBar titleBar = new TitleBar(activity).setTitle("登录").back();
        titleBar.showRight("注册", new View.OnClickListener() {
            public void onClick(View v) {
                skip(RegisterStep1Activity.class);
            }
        });

        mAccountEt.addTextChangedListener(new TextWatcher() {
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
                public void getResp(JSONObject jsonObject) {
                    dismissDialog();
                    RespVo<LoginBean> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);

                    if (respVo.isSuccess()) {
                        LoginBean bean = respVo.getData(jsonObject, LoginBean.class);
                        ((BbcatApp) getApplication()).setmUser(bean);
                        SpUtil sp_login = new SpUtil(activity, Const.SP_NAME);
                        sp_login.setValue("sex", bean.getUserInfo().getSex());
                        sp_login.setValue("birthday", bean.getUserInfo().getBirthday());
                        sp_login.setValue("baby_status", bean.getUserInfo().getBaby_status());
                        sp_login.setValue(Const.TOKEN, bean.getToken());
                        sp_login.setValue(Const.UID, bean.getUid());
                        sp_login.setValue(Const.NICKNAME, bean.getUserInfo().getNickname());
                        sp_login.setValue(Const.AVATAR, bean.getUserInfo().getAvatar());
                        PreferenceManager.getInstance().setCurrentUserAvatar(SimpleUtils.getImageUrl(bean.getUserInfo().getAvatar()));
                        PreferenceManager.getInstance().setCurrentUserNick(bean.getUserInfo().getNickname());
                        sp_login.setValue(Const.PHONE, mAccountEt.getText().toString().trim());
                        sp_login.setValue(Const.NOTIFY_MSG,respVo.getNotice_message());
                        SimpleUtils.loginHx(activity.getApplicationContext());
                        Intent intent = new Intent();
                        intent.putExtra("alreadymody", true);
                        setResult(0, intent);
                        toast("登陆成功");
                        finish();
                    } else {
                        toast(respVo.getMessage());
                    }
                }

                @Override
                public void doFailed() {
                    dismissDialog();
                    toast("链接服务器失败");
                }
            });
        } else if (v == mFindPasswordsTv) {
            skip(FindScreteFirstStepActivity.class);
        }

    }

    @OnClick(R.id.login_weixin)
    void wxLogin(View view) {
        ShareSDK.initSDK(activity);
        authorize(new Wechat(this));
    }

    @OnClick(R.id.login_qq)
    void qqLogin(View view) {
        doLogin(QQ.NAME);
    }

    @OnClick(R.id.login_weibo)
    void sinaLogin(View view) {
        doLogin(SinaWeibo.NAME);
    }

    private void authorize(Platform plat) {
        if (plat.isValid()) {
            plat.removeAccount(true);
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    private void doLogin(String name) {
        ShareSDK.initSDK(activity);
        Platform plat = ShareSDK.getPlatform(name);
        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }
        plat.setPlatformActionListener(this);
        plat.authorize();
    }

    @Override
    public void onComplete(Platform plat, int action, HashMap<String, Object> arg2) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = plat;
        handler.sendMessage(msg);
    }

    @Override
    public void onError(Platform plat, int action, Throwable throwable) {
        Message msg = new Message();
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = plat;
        handler.sendMessage(msg);
    }


    @Override
    public void onCancel(Platform plat, int action) {
        Message msg = new Message();
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = plat;
        handler.sendMessage(msg);

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Platform plat = (Platform) msg.obj;

            switch (msg.arg1) {
                case 1: {
                    // 成功
                    if (plat.isAuthValid()) {
                        String userId = plat.getDb().getUserId();
                        login(userId, plat.getDb().getUserName(), plat.getDb().getUserIcon());
                    }else{
                        toast(plat.getName()+"平台未授权");
                    }
                }
                break;
                case 2: {
                    // 失败
                    toast(plat.getName() + "失败");
                }
                case 3: {
                    // 取消
                    toast(plat.getName() + "取消");
                }
            }

        }
    };

    private void login(final String uuid, String nickname, String avatar) {
        showDialog();
        PostParams params = new PostParams();
        params.put("avatar", avatar);
        params.put("nickname", nickname);
        params.put("uuid", uuid);
        HttpUtils.postJSONObject(activity, Const.THIRD_LOGIN, params, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo<LoginBean> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    LoginBean bean = respVo.getData(jsonObject, LoginBean.class);
                    ((BbcatApp) getApplication()).setmUser(bean);
                    SpUtil sp_login = new SpUtil(activity, Const.SP_NAME);
                    sp_login.setValue("sex", bean.getUserInfo().getSex());
                    sp_login.setValue("birthday", bean.getUserInfo().getBirthday());
                    sp_login.setValue("baby_status", bean.getUserInfo().getBaby_status());
                    sp_login.setValue(Const.TOKEN, bean.getToken());
                    sp_login.setValue(Const.UID, bean.getUid());
                    sp_login.setValue(Const.NICKNAME, bean.getUserInfo().getNickname());
                    sp_login.setValue(Const.AVATAR, bean.getUserInfo().getAvatar());
                    PreferenceManager.getInstance().setCurrentUserAvatar(SimpleUtils.getImageUrl(bean.getUserInfo().getAvatar()));
                    PreferenceManager.getInstance().setCurrentUserNick(bean.getUserInfo().getNickname());
                    sp_login.setValue(Const.PHONE, System.currentTimeMillis()/100+"");
                    SimpleUtils.loginHx(activity.getApplicationContext());
                    Intent intent = new Intent();
                    intent.putExtra("alreadymody", true);
                    setResult(0, intent);
                    toast("登陆成功");
                    finish();
                } else {
                    toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
            }
        });
    }

    public void onEventMainThread(RegisterStep1Activity.FinishEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (!Utils.isEmpty(getVo("0").toString())) {
            skip(MainActivity.class);
        }
        
    }
}