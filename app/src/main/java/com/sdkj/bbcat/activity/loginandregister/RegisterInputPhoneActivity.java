package com.sdkj.bbcat.activity.loginandregister;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.GetVerifyCodeBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private GetVerifyCodeBean data;

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
                    HashMap<String,String> map = new HashMap<String, String>();zz
                    map.put("from","register");
                    map.put("type", "sms");
                    map.put("phone", phoneEt.getText().toString().trim());

                    
                    HttpUtils.getJSONObject(RegisterInputPhoneActivity.this, getCompleteUrl(Const.GetVerifyCode, map), new RespJSONObjectListener(RegisterInputPhoneActivity.this)
                    {
                        @Override
                        public void getResp(JSONObject jsonObject)
                        {
                            GetVerifyCodeBean bean = GsonTools.getVo(jsonObject.toString(), GetVerifyCodeBean.class);
                            data = bean;
                            if (data.getReturnCode().equals("SUCCESS"))
                            {
                                skip(RegisterInputVerifyCodeActivity.class,data,phoneEt.getText().toString().trim());
                            }
                            else
                            {
                                toast("服务器返回内容错误");
                            }
                        }

                        @Override
                        public void doFailed()
                        {
                            toast("链接服务器失败");
                        }
                    });
                }
                else
                {
                    toast("请勾选同意条款后再提交验证码");
                }
            }
        });
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
