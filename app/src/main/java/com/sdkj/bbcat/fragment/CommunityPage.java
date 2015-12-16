package com.sdkj.bbcat.fragment;

import android.widget.TextView;

import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 */
public class CommunityPage extends BaseFragment {
    @ViewInject(R.id.tv_text)
    private TextView tv_text;

    @Override
    protected void setListener() {
        tv_text.setText("页面四");
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_page;
    }
}
