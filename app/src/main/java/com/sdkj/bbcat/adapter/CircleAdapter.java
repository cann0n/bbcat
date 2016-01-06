package com.sdkj.bbcat.adapter;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
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
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.DetailActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

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
            if (Utils.isEmpty(newsVo.getNews_info().getCover())) {
                holder.iv_thumb.setVisibility(View.GONE);
            } else {
                holder.iv_thumb.setVisibility(View.VISIBLE);
                Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getNews_info().getCover())).into(holder.iv_thumb);
            }
            if (Utils.isEmpty(newsVo.getNews_info().getAddress())) {
                holder.tv_address.setVisibility(View.GONE);
            } else {
                holder.tv_address.setVisibility(View.VISIBLE);
                holder.tv_address.setText(newsVo.getNews_info().getAddress());
            }
            holder.tv_liulan.setText(newsVo.getNews_info().getView() + "");
            holder.tv_time.setText(newsVo.getNews_info().getCreate_time() + "");
            holder.tv_title.setText(newsVo.getNews_info().getTitle());
            holder.tv_comment.setText(newsVo.getNews_info().getComment());
            holder.tv_zan.setText(newsVo.getNews_info().getCollection());

            holder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.skip(DetailActivity.class, newsVo);
                }
            });
            holder.ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.toast("分享");
                }
            });

            holder.tv_zan_add.setVisibility(View.INVISIBLE);
            //已关注
            if ("1".equals(newsVo.getUser_info().getIs_following())) {
                holder.tv_guanzhu.setText("已关注");
            } else {
                holder.tv_guanzhu.setText("关注");
            }

            holder.tv_guanzhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SimpleUtils.isLogin(activity)) {
                        doLike(newsVo, holder.tv_guanzhu);
                    } else {
                        activity.skip(LoginActivity.class);
                    }
                }
            });
            if ("1".equals(newsVo.getNews_info().getIs_collected())) {
                holder.iv_zan.setImageResource(R.drawable.icon_zan1);
            } else {
                holder.iv_zan.setImageResource(R.drawable.icon_zan);
            }

            holder.ll_zan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (SimpleUtils.isLogin(activity)) {
                        doLike(newsVo, holder.iv_zan, holder.tv_zan, holder.tv_zan_add);
                    } else {
                        activity.skip(LoginActivity.class);
                    }
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
        TextView tv_zan_add;

        ImageView iv_zan;


        LinearLayout ll_item;
        LinearLayout ll_share;
        LinearLayout ll_zan;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void doLike(final CircleVo.ItemCircle newsVo, final ImageView iv_like, final TextView like_num, final TextView anim) {
        PostParams param = new PostParams();
        param.put("id", newsVo.getNews_info().getId());
        SpUtil sp = new SpUtil(activity, Const.SP_NAME);
        param.put("uid", sp.getStringValue(Const.UID));
        HttpUtils.postJSONObject(activity, Const.DO_LIKE, SimpleUtils.buildUrl(activity, param), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                RespVo<String> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    if ("0".equals(newsVo.getNews_info().getIs_collected())) {
                        newsVo.getNews_info().setIs_collected("1");
                        iv_like.setImageResource(R.drawable.icon_zan1);
                        like_num.setText((Integer.parseInt(like_num.getText().toString()) + 1) + "");
                        newsVo.getNews_info().setCollection(like_num.getText().toString());
                        anim.setVisibility(View.VISIBLE);
                        anim.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.applaud_animation));
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                anim.setVisibility(View.GONE);
                            }
                        }, 1000);
                    } else {
                        newsVo.getNews_info().setIs_collected("0");
                        iv_like.setImageResource(R.drawable.icon_zan);
                        like_num.setText((Integer.parseInt(like_num.getText().toString()) - 1) + "");
                        newsVo.getNews_info().setCollection(like_num.getText().toString());
                    }

                } else if (respVo.isNeedLogin()) {
                    activity.skip(LoginActivity.class);
                }
            }

            @Override
            public void doFailed() {

            }
        });
    }

    private void doLike(final CircleVo.ItemCircle newsVo, final TextView follow) {
        PostParams param = new PostParams();
        param.put("id", newsVo.getNews_info().getId());
        SpUtil sp = new SpUtil(activity, Const.SP_NAME);
        param.put("uid", sp.getStringValue(Const.UID));
        HttpUtils.postJSONObject(activity, Const.DO_LIKE, SimpleUtils.buildUrl(activity, param), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                RespVo<String> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    if ("0".equals(newsVo.getUser_info().getIs_following())) {
                        newsVo.getUser_info().setIs_following("1");
                        follow.setText("已关注");
                    } else {
                        newsVo.getUser_info().setIs_following("1");
                        follow.setText("关注");
                    }


                } else if (respVo.isNeedLogin()) {
                    activity.skip(LoginActivity.class);
                }
            }

            @Override
            public void doFailed() {

            }
        });
    }
}
