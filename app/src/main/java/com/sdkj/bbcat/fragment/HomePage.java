package com.sdkj.bbcat.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.HospitalDetailActivity;
import com.sdkj.bbcat.activity.MedicalOnlineActivity;
import com.sdkj.bbcat.activity.SearchActivity;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 */
public class HomePage extends BaseFragment {

    @ViewInject(R.id.ll_recommend)
    private LinearLayout ll_recommend;
    
    @ViewInject(R.id.ll_diary)
    private LinearLayout ll_diary;
    
    @ViewInject(R.id.ll_doctor)
    private LinearLayout ll_doctor;
    
    @ViewInject(R.id.ll_knowledge)
    private LinearLayout ll_knowledge;
    
    @ViewInject(R.id.ll_circle)
    private LinearLayout ll_circle;
    
    @ViewInject(R.id.ll_guess)
    private LinearLayout ll_guess;
    

    @Override
    protected void setListener() {
        for (int i=0;i<4;i++){
            View view=activity.makeView(R.layout.item_recommend);
            ll_recommend.addView(view);
        }
        for (int i=0;i<4;i++){
            View view=activity.makeView(R.layout.item_diary);
            ll_diary.addView(view);
        }
        for (int i=0;i<4;i++){
            View view=activity.makeView(R.layout.item_dedical_online);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.skip(HospitalDetailActivity.class);
                }
            });
            ll_doctor.addView(view);
        }
        for (int i=0;i<4;i++){
            View view=activity.makeView(R.layout.item_recommend);
            ll_knowledge.addView(view);
        }
        for (int i=0;i<5;i++){
            View view=activity.makeView(R.layout.item_recommend);
            ll_circle.addView(view);
        }
        for (int i=0;i<3;i++){
            View view=activity.makeView(R.layout.item_recommend);
            ll_guess.addView(view);
        }
        
    }
    
    @OnClick(R.id.tv_doctor)
    void doctor(View view){
        activity.skip(MedicalOnlineActivity.class);
    }
    
    @OnClick(R.id.iv_right)
    void search(View view){
        activity.skip(SearchActivity.class);
    }
    
    
    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_page;
    }
}
