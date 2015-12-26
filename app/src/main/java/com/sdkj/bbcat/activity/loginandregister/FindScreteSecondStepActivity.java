package com.sdkj.bbcat.activity.loginandregister;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
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
 * Created by Mr.Yuan on 2015/12/25 0025.
 */
public class FindScreteSecondStepActivity extends BaseActivity implements View.OnClickListener
{
    @ViewInject(R.id.findscretesecond_inputphone)
    private EditText phoneEt;
    @ViewInject(R.id.findscretesecond_getverifycode)
    private TextView getVerifyCodeTv;
    @ViewInject(R.id.findscretesecond_inputverifycode)
    private EditText verifyCodeEt;
    @ViewInject(R.id.findscretesecond_btn)
    private Button   verifyCodeBtn;

    private GetVerifyCodeBean currentDatas;
    private int     daojishi = 0;
    private Handler handler  = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (daojishi == 0)
            {
                getVerifyCodeTv.setBackgroundResource(R.drawable.btn_orange);
                getVerifyCodeTv.setText("发送验证码");
                phoneEt.setEnabled(true);
                phoneEt.setText(phoneEt.getText().toString());
                verifyCodeEt.setText("");
                verifyCodeEt.setEnabled(false);
            } else
            {
                getVerifyCodeTv.setBackgroundResource(R.drawable.btn_gray);
                getVerifyCodeTv.setText(daojishi + "秒");
                getVerifyCodeTv.setEnabled(false);
                daojishi--;
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_findscretesecondstep;
    }

    @Override
    public void initBusiness()
    {
        new TitleBar(activity).setTitle("找回密码").back();
        phoneEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (Utils.isPhoneNum(s.toString().trim()))
                {
                    getVerifyCodeTv.setEnabled(true);
                    getVerifyCodeTv.setBackgroundResource(R.drawable.btn_orange);
                } else
                {
                    getVerifyCodeTv.setEnabled(false);
                    getVerifyCodeTv.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        verifyCodeEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (! Utils.isEmpty(s.toString().trim()))
                {
                    verifyCodeBtn.setEnabled(true);
                    verifyCodeBtn.setBackgroundResource(R.drawable.btn_orange);
                } else
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
        getVerifyCodeTv.setOnClickListener(this);
        verifyCodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == getVerifyCodeTv)
        {
            phoneEt.setEnabled(false);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("from", "findpwd");
            map.put("type", "sms");
            map.put("phone", phoneEt.getText().toString().trim());

            HttpUtils.getJSONObject(FindScreteSecondStepActivity.this, getCompleteUrl(Const.GetVerifyCode, map), new RespJSONObjectListener(FindScreteSecondStepActivity.this)
            {
                public void getResp(JSONObject jsonObject)
                {
                    GetVerifyCodeBean bean = GsonTools.getVo(jsonObject.toString(), GetVerifyCodeBean.class);
                    currentDatas = bean;
                    if (currentDatas.getReturnCode().equals("SUCCESS"))
                    {
                        daojishi = 60;
                        verifyCodeEt.setEnabled(true);
                        handler.sendEmptyMessage(0);
                    }
                    else
                       toast(currentDatas.getMessage());
                }
                public void doFailed()
                {
                    toast("链接服务器失败");
                }
            });
        }

        else if (v == verifyCodeBtn)
        {
            phoneEt.setEnabled(false);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("sessionId", currentDatas.getSessionId());
            map.put("username",phoneEt.getText().toString().trim());
            map.put("verifyCode", verifyCodeEt.getText().toString().trim());

            HttpUtils.getJSONObject(FindScreteSecondStepActivity.this, getCompleteUrl(Const.FindScretePostPhoneAndVerifyCode, map), new RespJSONObjectListener(FindScreteSecondStepActivity.this)
            {
                public void getResp(JSONObject jsonObject)
                {
                    baseBean bean = GsonTools.getVo(jsonObject.toString(), baseBean.class);
                    if (bean.getReturnCode().equals("SUCCESS"))
                    {
                        skip(FindScreteThirdStepActivity.class, phoneEt.getText().toString().trim());
                        daojishi = 0;
                        currentDatas = null;
                        getVerifyCodeTv.setEnabled(false);
                        getVerifyCodeTv.setText("发送验证码");
                        getVerifyCodeTv.setBackgroundResource(R.drawable.btn_gray);
                        phoneEt.setText("");
                        verifyCodeBtn.setEnabled(false);
                        verifyCodeBtn.setBackgroundResource(R.drawable.btn_gray);
                        verifyCodeEt.setText("");
                        verifyCodeEt.setEnabled(false);
                    }
                    else
                        toast(bean.getMessage());
                }
                public void doFailed()
                {
                    toast("链接服务器失败");
                }
            });
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
