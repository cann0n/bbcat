package com.sdkj.bbcat.activity.loginandregister;

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
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.bean.VerifyBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr.Yuan on 2015/12/25 0025.
 */
public class FindScreteSecondStepActivity extends SimpleActivity implements View.OnClickListener {
    @ViewInject(R.id.findscretesecond_inputphone)
    private EditText phoneEt;
    @ViewInject(R.id.findscretesecond_getverifycode)
    private TextView getVerifyCodeTv;
    @ViewInject(R.id.findscretesecond_inputverifycode)
    private EditText verifyCodeEt;
    @ViewInject(R.id.findscretesecond_btn)
    private Button verifyCodeBtn;

    private String verifyVid = "";
    private int daojishi = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (daojishi == 0) {
                getVerifyCodeTv.setBackgroundResource(R.drawable.btn_orange);
                getVerifyCodeTv.setText("发送验证码");
                phoneEt.setEnabled(true);
                phoneEt.setText(phoneEt.getText().toString());
                verifyCodeEt.setText("");
                verifyCodeEt.setEnabled(false);
            } else {
                getVerifyCodeTv.setBackgroundResource(R.drawable.btn_gray);
                getVerifyCodeTv.setText(daojishi + "秒");
                getVerifyCodeTv.setEnabled(false);
                daojishi--;
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    public int setLayoutResID() {
        return R.layout.activity_findscretesecondstep;
    }

    @Override
    public void initBusiness() {
        new TitleBar(activity).setTitle("找回密码").back();
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utils.isPhoneNum(s.toString().trim())) {
                    getVerifyCodeTv.setEnabled(true);
                    getVerifyCodeTv.setBackgroundResource(R.drawable.btn_orange);
                } else {
                    getVerifyCodeTv.setEnabled(false);
                    getVerifyCodeTv.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verifyCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Utils.isEmpty(s.toString().trim())) {
                    verifyCodeBtn.setEnabled(true);
                    verifyCodeBtn.setBackgroundResource(R.drawable.btn_orange);
                } else {
                    verifyCodeBtn.setEnabled(false);
                    verifyCodeBtn.setBackgroundResource(R.drawable.btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getVerifyCodeTv.setOnClickListener(this);
        verifyCodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == getVerifyCodeTv) {
            showDialog();
            phoneEt.setEnabled(false);
            PostParams params = new PostParams();
            params.put("from", "req");
            params.put("mobile", phoneEt.getText().toString().trim());

            HttpUtils.postJSONObject(FindScreteSecondStepActivity.this, Const.GetVerifyCode, params, new RespJSONObjectListener(FindScreteSecondStepActivity.this) {
                @Override
                public void getResp(JSONObject jsonObject) {
                    dismissDialog();
                    RespVo<VerifyBean> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                    if (respVo.isSuccess()) {
                        daojishi = 60;
                        verifyCodeEt.setEnabled(true);
                        handler.sendEmptyMessage(0);
                        verifyVid = respVo.getData(jsonObject, VerifyBean.class).getVid();
                    } else {
                        dismissDialog();
                        toast(respVo.getMessage());
                    }
                }

                @Override
                public void doFailed() {
                    dismissDialog();
                    toast("链接服务器失败");
                }
            });
        } else if (v == verifyCodeBtn) {
//            if (verifyCodeEt.getText().toString().trim().equals("123456")) {
                if(Utils.isEmpty(verifyCodeEt.getText().toString())){
                    toast("请输入验证码");
                    return;
                }    
                if(Utils.isEmpty(verifyVid)){
                    toast("请先接收验证码");
                    return;
                }
                skip(FindScreteThirdStepActivity.class, phoneEt.getText().toString().trim(), verifyVid,verifyCodeEt.getText().toString());
                daojishi = 0;
                getVerifyCodeTv.setEnabled(false);
                getVerifyCodeTv.setText("发送验证码");
                getVerifyCodeTv.setBackgroundResource(R.drawable.btn_gray);
                phoneEt.setText("");
                verifyCodeBtn.setEnabled(false);
                verifyCodeBtn.setBackgroundResource(R.drawable.btn_gray);
                verifyCodeEt.setText("");
                verifyCodeEt.setEnabled(false);
//            } else {
//                toast("验证码不正确");
//            }

           /* phoneEt.setEnabled(false);
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
        }*/
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
