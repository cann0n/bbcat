package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.FeedInoVo;

import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/8 0008.
 */
public class FeedNotesAdapter extends UltimatCommonAdapter<FeedInoVo, FeedNotesAdapter.ViewHolder>
{
    public FeedNotesAdapter(BaseActivity activity, List<FeedInoVo> data)
    {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.inflater_feednote);
    }

    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position)
    {
        if (h instanceof ViewHolder)
        {
            final ViewHolder holder = (ViewHolder) h;
            final FeedInoVo feedInoVo = getItem(position);
            holder.feednote_left.setText(feedInoVo.getName());
            holder.feednote_bettwen.setText(feedInoVo.getNum());
            String[] dayStr = feedInoVo.getDay().split("-");
            holder.feednote_right.setText(dayStr[0]+"年"+dayStr[1]+"月"+dayStr[2]+"日");
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder
    {
        TextView  feednote_left;
        TextView  feednote_bettwen;
        TextView  feednote_right;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
