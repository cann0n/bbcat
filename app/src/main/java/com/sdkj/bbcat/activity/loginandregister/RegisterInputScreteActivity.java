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
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.BbcatApp;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.RegisterBean;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

/**
 * Created by Mr.Yuan on 2015/12/24 0024.
 */
public class RegisterInputScreteActivity extends SimpleActivity {
    @ViewInject(R.id.registerinputscrete_etone)
    private EditText screteEtOne;
    @ViewInject(R.id.registerinputscrete_ettwo)
    private EditText screteEtTwo;
    @ViewInject(R.id.registerinputscrete_btn)
    private EditText et_invite;
    @ViewInject(R.id.et_invite)
    private Button screteBtn;
    private String phoneNum;

    public int setLayoutResID() {
        return R.layout.activity_registerinputscrete;
    }

    public void initBusiness() {
        new TitleBar(activity) {
            protected void backDoing() {
                Intent intent = new Intent(RegisterInputScreteActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }.setTitle("注册").back();
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
                            HttpUtils.postJSONObject(RegisterInputScreteActivity.this, Const.Register, params, new RespJSONObjectListener(RegisterInputScreteActivity.this) {
                                @Override
                                public void getResp(JSONObject jsonObject) {
                                    dismissDialog();
                                    RespVo<RegisterBean> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                                    if (respVo.isSuccess()) {
                                        showCompleteDialog();
//                                      new Thread(new Runnable() {
//                                          public void run() {
//                                              try {
//                                                  // 调用sdk注册方法
//                                                  EMChatManager.getInstance().createAccountOnServer((String)getVo("0"), (String)getVo("0"));
//                                              } catch (final EaseMobException e) {
//                                                  //注册失败
//                                              }
//                                          }}).start();
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

        View view = LayoutInflater.from(this).inflate(R.layout.inflater_registerdialog, null);
        alertDialog.setContentView(view);
        TextView skipTv = (TextView) view.findViewById(R.id.registerdialog_skip);
        skipTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(RegisterInputScreteActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        TextView goOnTv = (TextView) view.findViewById(R.id.registerdialog_goon);
        goOnTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(RegisterInputScreteActivity.this, FillInfosFirstActivity.class);
                startActivity(intent);
                ((BbcatApp) getApplication()).finishAct("RegisterInputPhoneActivity");
                ((BbcatApp) getApplication()).finishAct("RegisterInputVerifyCodeActivity");
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(RegisterInputScreteActivity.this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        finish();
    }
}
