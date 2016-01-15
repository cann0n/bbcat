package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.CircleTagVo;

import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/16 0016.
 */
public class ArticleAdapter extends UltimatCommonAdapter<CircleTagVo, ArticleAdapter.ViewHolder>
{
    public ArticleAdapter(BaseActivity activity, List<CircleTagVo> data)
    {super(activity, ViewHolder.class, R.id.class, data, R.layout.item_search_words);}

    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position)
    {
        if (h instanceof ViewHolder)
        {
            final ViewHolder holder = (ViewHolder) h;
            final CircleTagVo vo = getItem(position);
            holder.itemsearch_content.setText(vo.getTitle());
            holder.itemsearch_all.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    activity.toast(vo.getTitle());
                }
            });
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder
    {
        RelativeLayout itemsearch_all;
        TextView itemsearch_content;
        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
