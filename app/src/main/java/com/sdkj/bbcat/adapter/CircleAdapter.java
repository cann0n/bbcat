package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 * 我的圈列表
 */
public class CircleAdapter extends UltimatCommonAdapter<CircleVo.ItemCircle, CircleAdapter.ViewHolder> {
    
    public CircleAdapter(BaseActivity activity, List<CircleVo.ItemCircle> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_circle);
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final CircleVo.ItemCircle newsVo = getItem(position);

            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getUser_info().getAvatar())).into(holder.iv_avatar);
            
            holder.tv_name.setText(newsVo.getUser_info().getNickname());
            holder.tv_desc.setText(newsVo.getUser_info().getBirthday());
            holder.tv_guanzhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.toast("关注");
                }
            });
            if(Utils.isEmpty(newsVo.getNews_info().getCover())){
                holder.iv_thumb.setVisibility(View.GONE);
            }else{
                holder.iv_thumb.setVisibility(View.VISIBLE);
                Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getNews_info().getCover())).into(holder.iv_thumb);
            }
            if(Utils.isEmpty(newsVo.getNews_info().getAddress())){
                holder.tv_address.setVisibility(View.GONE);
            }else{
                holder.tv_address.setVisibility(View.VISIBLE);
                holder.tv_address.setText(newsVo.getNews_info().getAddress());
            }
            holder.tv_liulan.setText(newsVo.getNews_info().getView()+"");
            holder.tv_time.setText(newsVo.getNews_info().getCreate_time()+"");
            holder.tv_title.setText(newsVo.getNews_info().getTitle());
            holder.tv_comment.setText(newsVo.getNews_info().getComment());
            holder.tv_zan.setText(newsVo.getNews_info().getCollection());
            
            holder. ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.skip(DetailActivity.class,newsVo);
                }
            });
            holder. ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.toast("分享");
                }
            });
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {

        ImageView iv_avatar;
        ImageView iv_thumb;
        TextView tv_name;
        TextView tv_desc;
        
        TextView tv_guanzhu;
        TextView tv_address;
        TextView tv_liulan;
        TextView tv_time;
        TextView tv_title_label;
        TextView tv_title;
        TextView tv_comment;
        TextView tv_zan;

        LinearLayout ll_item;
        LinearLayout ll_share;
        
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
