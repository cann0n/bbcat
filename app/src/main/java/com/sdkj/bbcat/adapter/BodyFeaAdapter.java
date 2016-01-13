package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.BodyFeaBean;

import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/13 0013.
 */
public class BodyFeaAdapter extends UltimatCommonAdapter<BodyFeaBean, BodyFeaAdapter.ViewHolder>
{
    public BodyFeaAdapter(BaseActivity activity, List<BodyFeaBean> data)
    {super(activity, ViewHolder.class, R.id.class, data, R.layout.item_bodyfea);}

    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position)
    {
        if (h instanceof ViewHolder)
        {
            final ViewHolder holder = (ViewHolder) h;
            final BodyFeaBean bodyFeaBean = getItem(position);
            holder.bodyfea_left.setText(bodyFeaBean.getHeight());
            holder.bodyfea_bettwen.setText(bodyFeaBean.getWeight());
            holder.bodyfea_right.setText(bodyFeaBean.getHead());
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder
    {
        TextView bodyfea_left;
        TextView  bodyfea_bettwen;
        TextView  bodyfea_right;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}