package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.CommentVo;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/15 0015.
 */
public class ComDetailAdapter extends UltimatCommonAdapter<CommentVo, BodyFeaAdapter.ViewHolder>
{
    public ComDetailAdapter(BaseActivity activity, List<CommentVo> data)
    {super(activity, ViewHolder.class, R.id.class, data, R.layout.item_comment);}

    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position)
    {
        if (h instanceof ViewHolder)
        {
            final ViewHolder holder = (ViewHolder) h;
            final CommentVo commentVo = getItem(position);
            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(commentVo.getAvatar())).into(holder.iv_item_avatar);
            holder.tv_item_name.setText(commentVo.getNickname());
            holder.tv_comment_time.setText(commentVo.getCreate_time());
            holder.tv_item_desc.setText(commentVo.getContent());
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder
    {
        ImageView iv_item_avatar;
        TextView tv_item_name;
        TextView tv_comment_time;
        TextView tv_item_desc;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}