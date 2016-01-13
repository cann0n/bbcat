package com.sdkj.bbcat.widget;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by ${Rhino} on 2015/12/15 12:02
 */
public class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {
    
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int i, int i1) {
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
                .error(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true)
                        //.centerCrop()
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
