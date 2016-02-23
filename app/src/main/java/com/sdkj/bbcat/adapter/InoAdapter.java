package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.FeedInoVo;

import java.util.List;

public class InoAdapter extends UltimatCommonAdapter<FeedInoVo, FeedNotesAdapter.ViewHolder>
{
    public InoAdapter(BaseActivity activity, List<FeedInoVo> data)
    {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.inflater_ino);
    }

    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position)
    {
        if (h instanceof ViewHolder)
        {
            final ViewHolder holder = (ViewHolder) h;
            final FeedInoVo feedInoVo = getItem(position);
//            holder.ino_left.setText("【"+feedInoVo.getName()+"】");
//            holder.ino_bettwen.setText(feedInoVo.getNum());
            String[] dayStr = feedInoVo.getDay().split("-");
            holder.ino_right.setText(dayStr[0]+"年"+dayStr[1]+"月"+dayStr[2]+"日");
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder
    {
        TextView  ino_left;
        TextView  ino_bettwen;
        TextView  ino_right;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}