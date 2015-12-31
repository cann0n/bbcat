package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.ListViewCommonAdapter;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.huaxi100.networkapp.utils.Utils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.HospitalDetailVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 */
public class DoctorAdapter extends ListViewCommonAdapter<HospitalDetailVo.Expert> {

    public DoctorAdapter(BaseActivity activity, List<HospitalDetailVo.Expert> data) {
        super(data, activity, R.layout.item_doctor, ViewHolder.class, R.id.class);
    }

    @Override
    public void doExtra(View view, HospitalDetailVo.Expert newsVo, int position) {
        final ViewHolder h = (ViewHolder) holder;
        Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getAvatar())).into(h.iv_image);
        h.tv_title.setText(newsVo.getExport_name());
        h.tv_desc.setText(newsVo.getExport_signature());
        h.tv_job.setText(newsVo.getExport_position());
        h.tv_time.setText(newsVo.getExport_time());
    }

    public static class ViewHolder {
        ImageView iv_image;
        TextView tv_title;
        TextView tv_job;
        TextView tv_desc;
        TextView tv_time;
    }
}
