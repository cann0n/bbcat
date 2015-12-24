package com.sdkj.bbcat.activity.loginandregister;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
import com.sdkj.bbcat.bean.baseBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

/**
 * Created by Mr.Yuan on 2015/12/24 0024.
 */
public class RegisterInputVerifyCodeActivity extends BaseActivity
{
    @ViewInject(R.id.registerinputverifycode_et)
    private EditText verifyCodeEt;
    @ViewInject(R.id.registerinputverifycode_btn)
    private Button   verifyCodeBtn;
    private GetVerifyCodeBean data;
    private String phoneNum;

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_registerinputverifycode;
    }

    @Override
    public void initBusiness()
    {
        new TitleBar(activity).setTitle("注册").back();
        data = (GetVerifyCodeBean)getVo("0");
        phoneNum = (String)getVo("1");
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
                PostParams params = new PostParams();
                params.put("username",phoneNum);
                params.put("verifyCode", verifyCodeEt.getText().toString());
                params.put("sessionId", data.getSessionId());
                params.bindUrl();
                String url=Const.PostVerifyCode+"?"+params.bindUrl();

//
                HttpUtils.getJSONObject(activity, url, new RespJSONObjectListener(activity) {
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
                });
//
//                Test req = new Test(activity, params, Const.PostVerifyCode, new RespJSONObjectListener(activity)
//                {
//                    @Override
//                    public void getResp(JSONObject jsonObject)
//                    {
//                        baseBean bean = GsonTools.getVo(jsonObject.toString(), baseBean.class);
//                        if (bean.getReturnCode().equals("SUCCESS"))
//                        {
//                            skip(RegisterInputScreteActivity.class,phoneNum);
//                        }
//                        else
//                        {
//                            toast("服务器返回内容错误");
//                        }
//                    }
//
//                    @Override
//                    public void doFailed()
//                    {
//                        toast("链接服务器失败");
//                    }
//                });
//                RequestQueue queue = SingleRequestQueue.getRequestQueue(activity);
//                queue.add(req);
//                HttpUtils.postJSONObject(activity,Const.PostVerifyCode,params, new RespJSONObjectListener(activity)
//                {
//                    @Override
//                    public void getResp(JSONObject jsonObject)
//                    {
//                        baseBean bean = GsonTools.getVo(jsonObject.toString(), baseBean.class);
//                        if (bean.getReturnCode().equals("SUCCESS"))
//                        {
//                            skip(RegisterInputScreteActivity.class,phoneNum);
//                        }
//                        else
//                        {
//                            toast("服务器返回内容错误");
//                        }
//                    }
//
//                    @Override
//                    public void doFailed()
//                    {
//                        toast("链接服务器失败");
//                    }
//                });
            }
        });
    }
    
}