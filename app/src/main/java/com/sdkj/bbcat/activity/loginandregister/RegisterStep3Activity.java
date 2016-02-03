package com.sdkj.bbcat.activity.loginandregister;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
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
import com.sdkj.bbcat.BbcatApp;
import com.sdkj.bbcat.MainActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.bean.RegisterBean;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.hx.PreferenceManager;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Mr.Yuan on 2015/12/24 0024.
 */
public class RegisterStep3Activity extends SimpleActivity {
    @ViewInject(R.id.registerinputscrete_etone)
    private EditText screteEtOne;
    @ViewInject(R.id.registerinputscrete_ettwo)
    private EditText screteEtTwo;

    @ViewInject(R.id.et_invite)
    private EditText et_invite;

    @ViewInject(R.id.registerinputscrete_btn)
    private Button screteBtn;
    private String phoneNum;

    public int setLayoutResID() {
        return R.layout.activity_registerinputscrete;
    }

    public void initBusiness() {
        new TitleBar(activity).setTitle("注册").back();
        phoneNum = (String) getVo("0");

        screteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Utils.isEmpty(screteEtOne.getText().toString().trim()) && !Utils.isEmpty(screteEtTwo.getText().toString().trim())) {
                    if (screteEtOne.getText().toString().trim().equals(screteEtTwo.getText().toString().trim())) {
                        if (screteEtOne.getText().toString().trim().length() >= 6 && screteEtOne.getText().toString().trim().length() <= 11) {
                            PostParams params = new PostParams();
                            params.put("mobile", (String) getVo("0"));
                            params.put("vid", (String) getVo("1"));
                            params.put("password", screteEtOne.getText().toString().trim());
                            params.put("verifyCode", (String) getVo("2"));
                            params.put("inviter_mobile", et_invite.getText().toString());
                            showDialog();
                            HttpUtils.postJSONObject(RegisterStep3Activity.this, Const.Register, params, new RespJSONObjectListener(RegisterStep3Activity.this) {
                                @Override
                                public void getResp(JSONObject jsonObject) {
                                    dismissDialog();
//                                    RespVo<LoginBean> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
//                                    if (respVo.isSuccess()) {
//                                        SpUtil sp=new SpUtil(activity,Const.SP_NAME);
//                                        sp.setValue(Const.PHONE,(String) getVo("0"));
//                                        showCompleteDialog();
//                                    } else {
//                                        toast(respVo.getMessage());
//                                    }

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
                                        sp_login.setValue(Const.PHONE, (String) getVo("0"));
                                        SimpleUtils.loginHx(activity.getApplicationContext());
//                                        Intent intent = new Intent();
//                                        intent.putExtra("alreadymody", true);
//                                        setResult(0, intent);
//                                        toast("注册成功");
//                                        finish();
                                        showCompleteDialog();
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
                        } else {
                            toast("请输入6-11位字母数字符号组合");
                        }
                    } else {
                        toast("两次输入的密码不一致");
                    }
                } else {
                    toast("不允许为空哟");
                }
            }
        });
    }

    public void showCompleteDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        View view = makeView(R.layout.inflater_registerdialog);
        alertDialog.setContentView(view);
        TextView skipTv = (TextView) view.findViewById(R.id.registerdialog_skip);
        skipTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                EventBus.getDefault().post(new RegisterStep1Activity.FinishEvent());
                skip(MainActivity.class);
                finish();
            }
        });

        TextView goOnTv = (TextView) view.findViewById(R.id.registerdialog_goon);
        goOnTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(RegisterStep3Activity.this, FillInfosFirstActivity.class);
                startActivity(intent);
                EventBus.getDefault().post(new RegisterStep1Activity.FinishEvent());
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
