package com.sdkj.bbcat.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.huaxi100.networkapp.utils.Utils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.DetailActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.CircleImageView;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 * 在线咨询
 */
public class OnlineFaqAdapter extends UltimatCommonAdapter<CircleVo.ItemCircle, OnlineFaqAdapter.ViewHolder> {
    public OnlineFaqAdapter(BaseActivity activity, List<CircleVo.ItemCircle> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_qa);
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final CircleVo.ItemCircle newsVo = getItem(position);
            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getUser_info().getAvatar())).into(holder.iv_avatar);

            holder.tv_name.setText(newsVo.getUser_info().getNickname());
            holder.tv_desc.setText(newsVo.getNews_info().getTitle());

            holder.tv_comment.setText(newsVo.getNews_info().getCreate_time());
            holder.tv_zan.setText(newsVo.getNews_info().getView());
            holder.tv_share.setText(newsVo.getNews_info().getComment());
            holder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.skip(NewsDetailActivity.class, newsVo.getNews_info().getId());
                }
            });
            if(Utils.isEmpty(newsVo.getNews_info().getPosition())){
                holder.ll_item.setBackgroundColor(Color.WHITE);
            }else{
                if("4".equals(newsVo.getNews_info().getPosition())){
                    holder.ll_item.setBackgroundResource(R.drawable.icon_top);
                }else if("2".equals(newsVo.getNews_info().getPosition())){
                    holder.ll_item.setBackgroundResource(R.drawable.icon_real);
                }else{
                    holder.ll_item.setBackgroundColor(Color.WHITE);
                }
            }
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {

        CircleImageView iv_avatar;
        TextView tv_name;
        TextView tv_desc;

        TextView tv_comment;
        TextView tv_zan;
        TextView tv_share;


        LinearLayout ll_item;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
