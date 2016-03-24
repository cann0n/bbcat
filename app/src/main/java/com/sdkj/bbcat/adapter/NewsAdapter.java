package com.sdkj.bbcat.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.HospitalDetailActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 */
public class NewsAdapter extends UltimatCommonAdapter<NewsVo, NewsAdapter.ViewHolder> {

    private int type;

    public NewsAdapter(BaseActivity activity, List<NewsVo> data, int type) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_recommend);
        this.type = type;
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, final int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final NewsVo newsVo = getItem(position);

            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(holder.iv_image);

            if (Utils.isEmpty(newsVo.getColor())) {
                holder.tv_title.setText(newsVo.getTitle());
            } else {
                String text = "<font color=\"+newsVo.getColor+\">" + "[" + newsVo.getCategory_name() + "]" + "</font>";
                holder.tv_title.setText(Html.fromHtml(text + newsVo.getTitle()));
            }
            holder.tv_come_form.setText(newsVo.getCategory_name());
            holder.tv_count.setText(newsVo.getView());
            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.skip(NewsDetailActivity.class, newsVo.getId());
                }
            });
            holder.rl_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (type == 0) {

                        new AlertDialog.Builder(activity).setTitle("系统提示")//设置对话框标题  
                                .setMessage("请确认要删除此收藏")//设置显示的内容  
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮  
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        delete(newsVo.getId(), position);
                                    }

                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮  

                            @Override

                            public void onClick(DialogInterface dialog, int which) {//响应事件  
                            }

                        }).show();//在按键响应事件中显示此对话框  
                    }
                    return true;
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

    private void delete(String id, final int position) {
        ((SimpleActivity) activity).showDialog();
        PostParams param = new PostParams();
        param.put("id", id);
        HttpUtils.postJSONObject(activity, Const.DO_LIKE, SimpleUtils.buildUrl(activity, param), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                ((SimpleActivity) activity).dismissDialog();
                RespVo<String> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    remove(position);
                } else if (respVo.isNeedLogin()) {
                    activity.skip(LoginActivity.class);
                }
            }

            @Override
            public void doFailed() {
                ((SimpleActivity) activity).dismissDialog();
            }
        });
    }
}
