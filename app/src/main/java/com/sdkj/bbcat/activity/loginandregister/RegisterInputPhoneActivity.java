package com.sdkj.bbcat.activity.loginandregister;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by Mr.Yuan on 2015/12/23 0023.
 */
public class RegisterInputPhoneActivity extends BaseActivity
{
    @ViewInject(R.id.registerinputphone_et)
    private EditText phoneEt;
    @ViewInject(R.id.registerinputphone_btn)
    private Button loginBtn;
    @ViewInject(R.id.registerinputphone_cb)
    private CheckBox agreeCb;

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_registerinputphone;
    }

    @Override
    public void initBusiness()
    {
        new TitleBar(activity).setTitle("注册").back();
        phoneEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(Utils.isPhoneNum(s.toString().trim()))
                {
                    loginBtn.setEnabled(true);
                    loginBtn.setBackgroundResource(R.drawable.btn_orange);
                }
                else
                {
                    loginBtn.setEnabled(false);
                    loginBtn.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(agreeCb.isChecked())
                {
                    /**获取验证码的请求*/
                    skip(RegisterInputVerifyCodeActivity.class);
                }
                else
                {
                    toast("请勾选同意条款后再提交验证码");
                }
            }
        });
    }
}
