package com.sdkj.bbcat.activity.loginandregister;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.VerifyBean;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr.Yuan on 2015/12/23 0023.
 */
public class RegisterInputPhoneActivity extends SimpleActivity
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
                    showDialog();
                    PostParams params= new PostParams();
                    params.put("from","reg");
                    params.put("mobile", phoneEt.getText().toString().trim());

                    HttpUtils.postJSONObject(RegisterInputPhoneActivity.this, Const.GetVerifyCode, params, new RespJSONObjectListener(RegisterInputPhoneActivity.this)
                    {
                        @Override
                        public void getResp(JSONObject jsonObject)
                        {
                            dismissDialog();
                            RespVo<VerifyBean> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                            if (respVo.isSuccess())
                            {
                                skip(RegisterInputVerifyCodeActivity.class, phoneEt.getText().toString().trim(), respVo.getData(jsonObject, VerifyBean.class).getVid());
                            } else
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
