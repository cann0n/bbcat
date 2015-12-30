package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.huaxi100.networkapp.utils.Utils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.HospitalDetailActivity;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 */
public class HospitalAdapter extends UltimatCommonAdapter<NewsVo, HospitalAdapter.ViewHolder> {

    public HospitalAdapter(BaseActivity activity, List<NewsVo> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_dedical_online);
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final NewsVo newsVo = getItem(position);

            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(holder.iv_thumb);
            holder.tv_hospital_name.setText(newsVo.getTitle());
            if (!Utils.isEmpty(newsVo.getLevel())) {
                holder. ratingBar.setRating(Float.parseFloat(newsVo.getLevel()));
            } else {
                holder. ratingBar.setRating(3);
            }
            holder. tv_address.setText(newsVo.getAddress());
            holder. ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.skip(HospitalDetailActivity.class,newsVo.getId());
                }
            });
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {

        ImageView iv_thumb;
        TextView tv_hospital_name;
        RatingBar ratingBar;
        TextView tv_address;
        
        LinearLayout ll_item;
        
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
