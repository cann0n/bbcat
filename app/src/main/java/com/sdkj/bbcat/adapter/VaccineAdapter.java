package com.sdkj.bbcat.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.bean.VaccineVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

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
                for(final VaccineVo.VaccineChildVo child:vo.getList()){
                    View childView=activity.makeView(R.layout.item_vaccine_child);
                    RelativeLayout rl_child_item= (RelativeLayout) childView.findViewById(R.id.rl_child_item);
                    final CheckBox cb_select= (CheckBox) childView.findViewById(R.id.cb_select);
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
                    rl_child_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title="";
                            if("1".equals(child.getUser_vac_ed())){
                                title="设置为未打?";
                            }else{
                                title="设置为已打?";
                            }
                            new AlertDialog.Builder(activity)
                                    .setMessage(title)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            setVaccine(child,cb_select);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                    });
                    
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
    
    private void setVaccine(final VaccineVo.VaccineChildVo vo, final CheckBox cb){
        ((SimpleActivity)activity).showDialog();
        PostParams params=new PostParams();
        params.put("ids",vo.getId());
        HttpUtils.postJSONObject(activity, Const.SET_VACCINE, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                ((SimpleActivity) activity).dismissDialog();
                RespVo respVo= GsonTools.getVo(jsonObject.toString(),RespVo.class);
                if(respVo.isSuccess()){
                    if("1".equals(vo.getUser_vac_ed())){
                        cb.setChecked(false);
                        vo.setUser_vac_ed("0");
                    }else{
                        cb.setChecked(true);
                        vo.setUser_vac_ed("1");
                    }
                }else{
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                activity.toast("设置失败");
            }
        });
    }
}
