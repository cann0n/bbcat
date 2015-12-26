package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.huaxi100.networkapp.utils.Utils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.HospitalDetailActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 */
public class NewsAdapter extends UltimatCommonAdapter<NewsVo, NewsAdapter.ViewHolder> {
    
    public NewsAdapter(BaseActivity activity, List<NewsVo> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_recommend);
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final NewsVo newsVo = getItem(position);

            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(holder.iv_image);
            holder.tv_title.setText(newsVo.getTitle());
            holder. tv_come_form.setText(newsVo.getCategory_name());
            holder.tv_count.setText(newsVo.getView());
            holder. ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.skip(NewsDetailActivity.class, newsVo);
                }
            });
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {

        ImageView iv_image;
        TextView tv_title;
        TextView tv_come_form;
        TextView tv_count;

        RelativeLayout ll_item;
        
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
