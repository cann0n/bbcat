package com.sdkj.bbcat.activity.loginandregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huaxi100.networkapp.utils.SpUtil;
import com.sdkj.bbcat.MainActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.adapter.FirstInstallViewPagerAdapter;
import com.sdkj.bbcat.constValue.Const;

import java.util.ArrayList;
import java.util.List;

public class FirstInstallActivity extends Activity {
    private List<View> mContentArray;
    private ViewPager mFirstInstallViewPager;
    private FirstInstallViewPagerAdapter mFirstInstallViewPagerAdapter;

    public static final int[] mDrawableArray = {R.drawable.firstinstallimg_one, R.drawable.firstinstallimg_two, R.drawable.firstinstallimg_three};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstinstall);
        SpUtil sp = new SpUtil(this, Const.SP_NAME);
        sp.setValue(Const.NOTIFY, false);
        mContentArray = new ArrayList<View>();
        mFirstInstallViewPager = (ViewPager) findViewById(R.id.firstinstallactivity_viewpager);
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (int index = 0; index < mDrawableArray.length; index++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(llparams);
            imageView.setBackgroundResource(mDrawableArray[index]);
            mContentArray.add(imageView);
            if (index == mDrawableArray.length - 1) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(FirstInstallActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
        mFirstInstallViewPagerAdapter = new FirstInstallViewPagerAdapter(this, mContentArray);
        mFirstInstallViewPager.setAdapter(mFirstInstallViewPagerAdapter);
    }
}
