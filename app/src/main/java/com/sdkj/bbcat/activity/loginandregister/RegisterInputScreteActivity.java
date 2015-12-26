package com.sdkj.bbcat.activity.loginandregister;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.baseBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

/**
 * Created by Mr.Yuan on 2015/12/24 0024.
 */
public class RegisterInputScreteActivity extends BaseActivity
{
    @ViewInject(R.id.registerinputscrete_etone)
    private EditText screteEtOne;
    @ViewInject(R.id.registerinputscrete_ettwo)
    private EditText screteEtTwo;
    @ViewInject(R.id.registerinputscrete_btn)
    private Button   screteBtn;
    private String phoneNum;

    public int setLayoutResID()
    {
        return R.layout.activity_registerinputscrete;
    }

    public void initBusiness()
    {
        new TitleBar(activity)
        {
            protected void backDoing()
            {
                Intent intent = new Intent(RegisterInputScreteActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }.setTitle("注册").back();
        phoneNum = (String)getVo("0");

        screteBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
              if(!Utils.isEmpty(screteEtOne.getText().toString().trim()) && !Utils.isEmpty(screteEtTwo.getText().toString().trim()))
              {
                  if(screteEtOne.getText().toString().trim().equals(screteEtTwo.getText().toString().trim()))
                  {
                      PostParams params = new PostParams();
                      params.put("confirmPwd", screteEtTwo.getText().toString().trim());
                      params.put("newPwd", screteEtOne.getText().toString().trim());
                      params.put("userName", phoneNum);

                      HttpUtils.getJSONObject(RegisterInputScreteActivity.this, Const.PostVerifyCodeEnd +"?"+ params.bindUrl(),new RespJSONObjectListener(RegisterInputScreteActivity.this)
                      {
                          @Override
                          public void getResp(JSONObject jsonObject)
                          {
                              baseBean bean = GsonTools.getVo(jsonObject.toString(), baseBean.class);
                              if (bean.getReturnCode().equals("SUCCESS"))
                              {
                                  showCompleteDialog();
                              } else
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
                      toast("两次输入的密码不一致");
                  }
              }
              else
              {
                  toast("不允许为空哟");
              }
            }
        });
    }

    public void showCompleteDialog()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        View view = LayoutInflater.from(this).inflate(R.layout.inflater_registerdialog,null);
        alertDialog.setContentView(view);
        ImageView imageView = (ImageView)view.findViewById(R.id.registerdialog_img);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
                Intent intent = new Intent(RegisterInputScreteActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(RegisterInputScreteActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
