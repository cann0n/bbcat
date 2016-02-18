package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.CheckBox;
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
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.VaccineVo;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 */
public class VaccineAdapter extends UltimatCommonAdapter<VaccineVo, VaccineAdapter.ViewHolder> {

    public VaccineAdapter(BaseActivity activity, List<VaccineVo> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_vaccine);
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final VaccineVo vo = getItem(position);
            holder.tv_mouth.setText(vo.getMonth());
            holder.ll_child_container.removeAllViews();
            if(!Utils.isEmpty(vo.getList())){
                for(VaccineVo.VaccineChildVo child:vo.getList()){
                    View childView=activity.makeView(R.layout.item_vaccine_child);
                    CheckBox cb_select= (CheckBox) childView.findViewById(R.id.cb_select);
                    if("1".equals(child.getUser_vac_ed())){
                        cb_select.setChecked(true);
                    }else{
                        cb_select.setChecked(false);
                    }
                    TextView tv_title= (TextView) childView.findViewById(R.id.tv_title);
                    tv_title.setText(child.getTitle());
                    
                    TextView tv_desc= (TextView) childView.findViewById(R.id.tv_desc);
                    tv_desc.setText(child.getDesc());
                    TextView tv_status= (TextView) childView.findViewById(R.id.tv_status);
                    if("1".equals(child.getMust())){
                        tv_status.setVisibility(View.VISIBLE);
                        tv_status.setText("必打");
                    }else{
                        tv_status.setVisibility(View.INVISIBLE);
                    }
                    
                    TextView tv_times= (TextView) childView.findViewById(R.id.tv_times);
                    tv_times.setText(child.getTimes());
                    
                    holder.ll_child_container.addView(childView);
                }
            }
           
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView tv_mouth;
        LinearLayout ll_child_container;

        LinearLayout ll_item;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
