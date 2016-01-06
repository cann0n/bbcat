package com.sdkj.bbcat.fragment;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.huaxi100.networkapp.adapter.FragPagerAdapter;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.fragment.FragmentVo;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;

import java.util.ArrayList;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 */
public class FragmentMyFriend extends BaseFragment {

    @ViewInject(R.id.vp_pager)
    private ViewPager vp_pager;

    @ViewInject(R.id.tv_huihua)
    private TextView tv_huihua;

    @ViewInject(R.id.tv_qunzu)
    private TextView tv_qunzu;

    @ViewInject(R.id.tv_liaotianshi)
    private TextView tv_liaotianshi;

    @ViewInject(R.id.tv_heimingdan)
    private TextView tv_heimingdan;

    @Override
    protected void setListener() {
        ArrayList<FragmentVo> pageVo = new ArrayList<FragmentVo>();
        pageVo.add(new FragmentVo(new HuiHuaFragment(), "会话"));
        pageVo.add(new FragmentVo(new GroupFragment(), "群组"));
        pageVo.add(new FragmentVo(new FragmentChatRoom(), "聊天室"));
        pageVo.add(new FragmentVo(new ContactFragment(), "通讯录"));
        FragPagerAdapter adapter = new FragPagerAdapter(getChildFragmentManager(), pageVo);
        vp_pager.setAdapter(adapter);
        vp_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeBtn(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        changeBtn(0);
    }

    private void changeBtn(int position) {
        if (position == 0) {
            tv_huihua.setTextColor(Color.parseColor("#ea5413"));
            tv_qunzu.setTextColor(Color.parseColor("#333333"));
            tv_liaotianshi.setTextColor(Color.parseColor("#333333"));
            tv_heimingdan.setTextColor(Color.parseColor("#333333"));

            tv_huihua.setBackgroundDrawable(activity.getDrawableRes(R.drawable.bg_bottom_line_orange));
            tv_qunzu.setBackgroundColor(Color.parseColor("#00000000"));
            tv_liaotianshi.setBackgroundColor(Color.parseColor("#00000000"));
            tv_heimingdan.setBackgroundColor(Color.parseColor("#00000000"));
        } else if (position == 1) {
            tv_qunzu.setTextColor(Color.parseColor("#ea5413"));
            tv_huihua.setTextColor(Color.parseColor("#333333"));
            tv_liaotianshi.setTextColor(Color.parseColor("#333333"));
            tv_heimingdan.setTextColor(Color.parseColor("#333333"));

            tv_qunzu.setBackgroundDrawable(activity.getDrawableRes(R.drawable.bg_bottom_line_orange));
            tv_huihua.setBackgroundColor(Color.parseColor("#00000000"));
            tv_liaotianshi.setBackgroundColor(Color.parseColor("#00000000"));
            tv_heimingdan.setBackgroundColor(Color.parseColor("#00000000"));
        } else if (position == 2) {
            tv_liaotianshi.setTextColor(Color.parseColor("#ea5413"));
            tv_qunzu.setTextColor(Color.parseColor("#333333"));
            tv_huihua.setTextColor(Color.parseColor("#333333"));
            tv_heimingdan.setTextColor(Color.parseColor("#333333"));

            tv_liaotianshi.setBackgroundDrawable(activity.getDrawableRes(R.drawable.bg_bottom_line_orange));
            tv_qunzu.setBackgroundColor(Color.parseColor("#00000000"));
            tv_huihua.setBackgroundColor(Color.parseColor("#00000000"));
            tv_heimingdan.setBackgroundColor(Color.parseColor("#00000000"));
        } else {
            tv_heimingdan.setTextColor(Color.parseColor("#ea5413"));
            tv_qunzu.setTextColor(Color.parseColor("#333333"));
            tv_liaotianshi.setTextColor(Color.parseColor("#333333"));
            tv_huihua.setTextColor(Color.parseColor("#333333"));

            tv_heimingdan.setBackgroundDrawable(activity.getDrawableRes(R.drawable.bg_bottom_line_orange));
            tv_qunzu.setBackgroundColor(Color.parseColor("#00000000"));
            tv_liaotianshi.setBackgroundColor(Color.parseColor("#00000000"));
            tv_huihua.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

    @OnClick(R.id.tv_huihua)
    void huihua(View view) {
        vp_pager.setCurrentItem(0);
    }

    @OnClick(R.id.tv_qunzu)
    void qunzu(View view) {
        vp_pager.setCurrentItem(1);
    }

    @OnClick(R.id.tv_liaotianshi)
    void liaotianshi(View view) {
        vp_pager.setCurrentItem(2);
    }

    @OnClick(R.id.tv_heimingdan)
    void heimingdan(View view) {
        vp_pager.setCurrentItem(3);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_friend;
    }
}
