package com.sdkj.bbcat.activity.loginandregister;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by Mr.Yuan on 2016/1/14 0014.
 */
public class NickNameActivity extends SimpleActivity
{
    @ViewInject(R.id.nickname_et)
    private EditText mEt;

    public int setLayoutResID()
    {
        return R.layout.activity_nickname;
    }

    public void initBusiness()
    {
        new TitleBar(activity).setTitle("修改昵称").back().showRight("完成", new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (mEt.getText().toString().trim().length() != 0)
                {
                    Intent intent = new Intent();
                    intent.putExtra("nickname", mEt.getText().toString().trim());
                    setResult(0, intent);
                    finish();
                }

                else
                {
                    toast("请填写‘昵称’后再点击完成按钮");
                }
            }
        });
        mEt.setText(getIntent().getStringExtra("nickname"));
        mEt.setSelection(mEt.getText().toString().length());
    }
}