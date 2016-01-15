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
public class SexActivity extends SimpleActivity
{
    @ViewInject(R.id.sex_man)
    private RadioButton mM_Rbtn;
    @ViewInject(R.id.sex_woman)
    private RadioButton mW_Rbtn;

    public int setLayoutResID()
    {
        return R.layout.activity_sex;
    }

    public void initBusiness()
    {
        new TitleBar(activity).setTitle("修改性别").back().showRight("完成", new View.OnClickListener()
        {
            public void onClick(View v)
            {
              if(!mM_Rbtn.isChecked() && !mW_Rbtn.isChecked())
                  toast("请选择‘性别’后再点击完成按钮");
              else
              {
                  Intent intent = new Intent();
                  if(mM_Rbtn.isChecked())
                      intent.putExtra("sex","1");
                  else
                      intent.putExtra("sex","2");
                  setResult(0,intent);
                  finish();
              }
            }
        });

        if(getIntent().getStringExtra("sex").equals("1"))
            mM_Rbtn.setChecked(true);
        else if(getIntent().getStringExtra("sex").equals("2"))
            mW_Rbtn.setChecked(true);
    }
}
