package com.sdkj.bbcat.activity.bracelet;

import android.view.View;

import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by ${Rhino} on 2016/2/22 16:04
 */
public class NewFoodRecordActivity extends SimpleActivity{

    @Override
    public void initBusiness() {
        new TitleBar(activity).setTitle("新建记录").back().setBg("#FF6469");
    }
    
    @OnClick(R.id.tv_1)
    void clickTv1(View view){
        skip(AddFeedNotesActivity.class,1,"奶瓶母乳");
    }
    @OnClick(R.id.tv_2)
    void clickTv2(View view){
        skip(AddFeedNotesActivity.class,2,"奶粉");
    }
    @OnClick(R.id.tv_3)
    void clickTv3(View view){
        skip(AddFeedNotesActivity.class,3,"哺乳");
    }
    @OnClick(R.id.tv_4)
    void clickTv4(View view){
        skip(AddFeedNotesActivity.class,4,"辅食");
    }
    
    @Override
    public int setLayoutResID() {
        return R.layout.activity_new_record;
    }
}
