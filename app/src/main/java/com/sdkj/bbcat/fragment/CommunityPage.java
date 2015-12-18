package com.sdkj.bbcat.fragment;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huaxi100.networkapp.adapter.FragPagerAdapter;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.fragment.FragmentVo;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.PublishActivity;

import java.util.ArrayList;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 */
public class CommunityPage extends BaseFragment {
    
    @ViewInject(R.id.vp_pager)
    private ViewPager vp_pager;
    
    @ViewInject(R.id.tv_guys)
    private TextView tv_guys;
    
    
    @ViewInject(R.id.tv_club)
    private TextView tv_club;
    
    @ViewInject(R.id.v_line1)
    private View v_line1;

    @ViewInject(R.id.v_line2)
    private View v_line2;
    
    @ViewInject(R.id.iv_edit)
    private ImageView iv_edit;


    private View popupClub;

    private PopupWindow popupWindowClub;
    
    
    @Override
    protected void setListener() {
        ArrayList<FragmentVo> pageVo = new ArrayList<FragmentVo>();
        pageVo.add(new FragmentVo(new FragmentMycircle(), "我的圈"));
        pageVo.add(new FragmentVo(new FragmentMyFriend(), "猫友"));
        FragPagerAdapter adapter = new FragPagerAdapter(activity.getSupportFragmentManager(), pageVo);
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

    @OnClick(R.id.tv_guys)
    void left(View view) {
        vp_pager.setCurrentItem(0);
    }

    @OnClick(R.id.tv_club)
    void right(View view) {
        vp_pager.setCurrentItem(1);
    }

    @OnClick(R.id.iv_edit)
    void select(View view){
        
            showClubWindow();
        
    }

    private void changeBtn(int position) {
        if (position == 0) {
            tv_guys.setTextColor(Color.parseColor("#ffffff"));
            tv_club.setTextColor(Color.parseColor("#ffffff"));

            v_line2.setVisibility(View.INVISIBLE);
            v_line1.setVisibility(View.VISIBLE);
        } else {
            tv_guys.setTextColor(Color.parseColor("#ffffff"));
            tv_club.setTextColor(Color.parseColor("#ffffff"));

            v_line2.setVisibility(View.VISIBLE);
            v_line1.setVisibility(View.INVISIBLE);
        }
    }

    private void showClubWindow() {
        if (popupClub == null) {
            int w= AppUtils.getWidth(activity)*2/5;
            popupClub = activity.makeView(R.layout.club_popup_dialog);
            popupWindowClub = new PopupWindow(popupClub, w, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindowClub.setFocusable(true);
            popupWindowClub.setOutsideTouchable(true);
            popupWindowClub.setOutsideTouchable(true);
            popupWindowClub.setBackgroundDrawable(new BitmapDrawable());
            popupWindowClub.setAnimationStyle(R.style.AnimationPreview);

            LinearLayout window = (LinearLayout) popupClub.findViewById(R.id.ll_window);
            final TextView tv_default = (TextView) popupClub.findViewById(R.id.tv_default);
            final TextView tv_rock_bar = (TextView) popupClub.findViewById(R.id.tv_rock_bar);
            final TextView tv_disco = (TextView) popupClub.findViewById(R.id.tv_disco);

            tv_default.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowClub.dismiss();
                }
            });
            tv_rock_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowClub.dismiss();
                }
            });
            tv_disco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowClub.dismiss();
                }
            });

            window.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindowClub.dismiss();
                }
            });
        }
        if (popupWindowClub.isShowing()) {
            popupWindowClub.dismiss();
        } else {
            popupWindowClub.showAsDropDown(iv_edit, -220, 20);
        }
    }
    
    @OnClick(R.id.iv_left)
    void publish(View ivew){
            activity.skip(PublishActivity.class);    
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_community;
    }
}
