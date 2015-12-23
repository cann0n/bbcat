package com.sdkj.bbcat.adapter;

import android.view.View;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.ListViewCommonAdapter;
import com.sdkj.bbcat.R;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/23 18:45
 */
public class TalkAdapter extends ListViewCommonAdapter<String> {
    public TalkAdapter(List<String> data, BaseActivity activity) {
        super(data, activity, R.layout.item_qunzu, ViewHolder.class, R.id.class);
    }

    @Override
    public void doExtra(View view, String o, int i) {
        
    }
    
    public static class ViewHolder{
        
    }
}
