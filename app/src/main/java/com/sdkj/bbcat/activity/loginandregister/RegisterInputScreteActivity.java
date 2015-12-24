package com.sdkj.bbcat.activity.loginandregister;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.widget.TitleBar;

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

    public int setLayoutResID()
    {
        return R.layout.activity_registerinputscrete;
    }

    public void initBusiness()
    {
        new TitleBar(activity).setTitle("注册").back();
        screteBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
              if(!Utils.isEmpty(screteEtOne.getText().toString().trim()) && !Utils.isEmpty(screteEtTwo.getText().toString().trim()))
              {
                  if(screteEtOne.getText().toString().trim().equals(screteEtTwo.getText().toString().trim()))
                  {
                      toast("注册成功");
                      Intent intent = new Intent(RegisterInputScreteActivity.this,LoginActivity.class);
                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      startActivity(intent);
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
}
