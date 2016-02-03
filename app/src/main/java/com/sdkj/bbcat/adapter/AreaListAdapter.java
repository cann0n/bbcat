package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.TextView;


import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.ExpandListViewCommonAdapter;
import com.huaxi100.networkapp.adapter.ListViewCommonAdapter;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.AreaVo;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/10/23 11:34
 */
public class AreaListAdapter extends ExpandListViewCommonAdapter<AreaVo,AreaVo> {
    private BaseActivity activity;
    public AreaListAdapter(BaseActivity activity, List parentData) {
        super(activity, ParentViewHolder.class, ChildViewHolder.class, R.id.class, parentData, R.layout.item_chapter_parent, R.layout.item_chapter_child);
        this.activity=activity;
    }

    @Override
    public void doParentExtra(View convertView, AreaVo item, int groupPosition, boolean isExpanded) {
        ParentViewHolder holder= (ParentViewHolder) parentHolder;
        holder.tv_title.setText(item.getName());
        if(isExpanded){
            holder.tv_open.setText("收起");

            holder.tv_open.setCompoundDrawablesWithIntrinsicBounds(null, null, activity.getDrawableRes(R.drawable.icon_address_up), null);
        }else{
            holder.tv_open.setText("展开");
            holder.tv_open.setCompoundDrawablesWithIntrinsicBounds(null, null, activity.getDrawableRes(R.drawable.icon_address_down), null);
        }
    }

    @Override
    public void doChildExtra(View convertView, AreaVo item, int groupPosition, int childPostion, boolean isLastChild) {
        ChildViewHolder holder= (ChildViewHolder) childHolder;
        holder.tv_name.setText(item.getName());
    }

    public static  class ParentViewHolder{
        TextView tv_title;
        TextView tv_open;
    }

    public static class ChildViewHolder{
        TextView tv_name;
    }
}
