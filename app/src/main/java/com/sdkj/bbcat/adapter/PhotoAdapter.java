package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.Utils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 */
public class PhotoAdapter extends UltimatCommonAdapter<String, PhotoAdapter.ViewHolder> {
    
    private int  width;

    public PhotoAdapter(BaseActivity activity, List<String> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_photo);
        width= (AppUtils.getWidth(activity)-60)/3;
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final String vo = getItem(position);
            
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(width,width);
            holder.iv_image.setLayoutParams(lp);
            holder.iv_image.setImageBitmap(Utils.getLoacalBitmap(vo));
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {

        ImageView iv_image;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
