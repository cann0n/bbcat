package com.sdkj.bbcat.activity.loginandregister;

import android.view.View;
import android.widget.LinearLayout;

import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by Mr.Yuan on 2015/12/27 0027.
 */
public class FillInfosFirstActivity extends SimpleActivity implements View.OnClickListener
{
    @ViewInject(R.id.fillinfos_huaiyun)
    private LinearLayout huanyun_ll;
    @ViewInject(R.id.fillinfos_beiyun)
    private LinearLayout beiyun_ll;
    @ViewInject(R.id.fillinfos_chusheng)
    private LinearLayout chusheng_ll;

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_fillinfosfirst;
    }

    @Override
    public void initBusiness()
    {
        TitleBar titleBar = new TitleBar(activity).setTitle("完善资料").back();
        huanyun_ll.setOnClickListener(this);
        beiyun_ll.setOnClickListener(this);
        chusheng_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v == huanyun_ll)
        {

        }
        else if(v == beiyun_ll)
        {

        }
        else if(v == chusheng_ll)
        {
            skip(FillInfosTwoActivity.class);
        }
    }
}
