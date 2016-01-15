package com.sdkj.bbcat.activity.loginandregister;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;

import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by Mr.Yuan on 2016/1/14 0014.
 */
public class StateActivity extends SimpleActivity
{
    @ViewInject(R.id.state_huaiyun)
    private RadioButton mHuaibtn;
    @ViewInject(R.id.state_beiyun)
    private RadioButton mBeibtn;
    @ViewInject(R.id.state_yiyun)
    private RadioButton mYibtn;

    public int setLayoutResID()
    {
        return R.layout.activity_state;
    }

    public void initBusiness()
    {
        new TitleBar(activity).setTitle("修改宝妈状态").back().showRight("完成", new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (! mHuaibtn.isChecked() && ! mBeibtn.isChecked() && !mYibtn.isChecked())
                    toast("请选择‘宝妈状态’后再点击完成按钮");
                else
                {
                    Intent intent = new Intent();
                    if(mHuaibtn.isChecked())
                        intent.putExtra("state","1");
                    else if(mBeibtn.isChecked())
                        intent.putExtra("state","2");
                    else
                        intent.putExtra("state", "3");
                    setResult(0,intent);
                    finish();
                }
            }
        });

        if(getIntent().getStringExtra("state").equals("1"))
            mHuaibtn.setChecked(true);
        else if(getIntent().getStringExtra("state").equals("2"))
            mBeibtn.setChecked(true);
        else if(getIntent().getStringExtra("state").equals("3"))
            mYibtn.setChecked(true);
    }
}
