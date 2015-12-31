package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.ImageView;
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
public class DiaryAdapter extends UltimatCommonAdapter<NewsVo, DiaryAdapter.ViewHolder> {
    
    public DiaryAdapter(BaseActivity activity, List<NewsVo> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_diary);
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final NewsVo newsVo = getItem(position);

            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(holder.iv_image);
            holder.tv_title.setText(newsVo.getTitle());
            if(Utils.isEmpty(newsVo.getCreate_time())){
                holder.tv_come_form.setText(Utils.formatTime(System.currentTimeMillis()+"", "yyyy-MM-dd"));
            }else {
                holder.tv_come_form.setText(Utils.formatTime(newsVo.getCreate_time() + "000", "yyyy-MM-dd"));
            }
            holder.tv_count.setText(newsVo.getView());
            holder. rl_item.setOnClickListener(new View.OnClickListener() {
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

        RelativeLayout rl_item;
        
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
