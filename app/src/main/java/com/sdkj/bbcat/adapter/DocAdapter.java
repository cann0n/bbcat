package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.HospitalDetailActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.CircleTagVo;

import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/16 0016.
 */
public class DocAdapter extends UltimatCommonAdapter<CircleTagVo, DocAdapter.ViewHolder> {
    /**
     * 0：新闻1：医院
     */
    private int type;
    public DocAdapter(BaseActivity activity, List<CircleTagVo> data,int type) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_search_words);
        this.type=type;
    }

    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final CircleTagVo vo = getItem(position);
            holder.itemsearch_content.setText(vo.getTitle());
            holder.itemsearch_all.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(type==0){
                        activity.skip(NewsDetailActivity.class,vo.getId());
                    }else if(type==1){
                        activity.skip(HospitalDetailActivity.class,vo.getId());
                    }
                }
            });
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {
        RelativeLayout itemsearch_all;
        TextView itemsearch_content;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}