package com.sdkj.bbcat.activity.loginandregister;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by Mr.Yuan on 2015/12/24 0024.
 */
public class RegisterInputVerifyCodeActivity extends SimpleActivity
{
    @ViewInject(R.id.registerinputverifycode_et)
    private EditText verifyCodeEt;
    @ViewInject(R.id.registerinputverifycode_btn)
    private Button   verifyCodeBtn;

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_registerinputverifycode;
    }

    @Override
    public void initBusiness()
    {
        new TitleBar(activity).setTitle("注册").back();
        verifyCodeEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!Utils.isEmpty(s.toString().trim()))
                {
                    verifyCodeBtn.setEnabled(true);
                    verifyCodeBtn.setBackgroundResource(R.drawable.btn_orange);
                }
                else
                {
                    verifyCodeBtn.setEnabled(false);
                    verifyCodeBtn.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        verifyCodeBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (verifyCodeEt.getText().toString().trim().equals("123456"))
                {
                    skip(RegisterInputScreteActivity.class, getVo("0"),getVo("1"));
                } else
                {
                    toast("验证码不正确");
                }

               /* PostParams params = new PostParams();
                params.put("username",phoneNum);
                params.put("verifyCode", verifyCodeEt.getText().toString());
                params.put("sessionId", data.getSessionId());
                params.bindUrl();
                String url=Const.PostVerifyCode+"?"+params.bindUrl();

                HttpUtils.getJSONObject(activity, url, new RespJSONObjectListener(activity)
                {
                    @Override
                    public void getResp(JSONObject jsonObject) {
                        baseBean bean = GsonTools.getVo(jsonObject.toString(), baseBean.class);
                        if (bean.getReturnCode().equals("SUCCESS")) {
                            skip(RegisterInputScreteActivity.class, phoneNum);
                        } else {
                            toast(bean.getMessage());
                        }
                    }

                    @Override
                    public void doFailed() {
                        toast("链接服务器失败");
                    }
                });*/
            }
        });
    }
}