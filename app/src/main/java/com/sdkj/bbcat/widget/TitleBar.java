package com.sdkj.bbcat.widget;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.sdkj.bbcat.R;


/**
 * Created by ${Rhino} on 2015/8/4 10:17
 * 标题栏
 */
public class TitleBar {

    private BaseActivity activity;

    private ImageView iv_back;
    private ImageView iv_right;

    private TextView tv_title;
    
    private TextView tv_right;

    public TitleBar(BaseActivity activity) {
        this.activity = activity;
        iv_back = (ImageView) activity.findViewById(R.id.iv_back);
        iv_right = (ImageView) activity.findViewById(R.id.iv_right);
        tv_title = (TextView) activity.findViewById(R.id.tv_title);
        tv_right = (TextView) activity.findViewById(R.id.tv_right);
    }
    public TitleBar setTitle(String title) {
        tv_title.setText(title);
        return this;
    }
    public  TitleBar showRight(String text,View.OnClickListener listener){
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(text);
        tv_right.setOnClickListener(listener);
        return this;
    }
    public  TitleBar showShare(View.OnClickListener listener){
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(listener);
        return this;
    }

    public TitleBar back() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        return this;
    }

    public TitleBar hideBack() {
        iv_back.setVisibility(View.GONE);
        return this;
    }

    public TitleBar hideRight() {
        tv_right.setVisibility(View.GONE);
        return this;
    }
}
