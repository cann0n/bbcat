package com.sdkj.bbcat.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sdkj.bbcat.activity.DetailActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.CommentVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.CircleImageView;

import org.json.JSONObject;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.wechat.favorite.WechatFavorite;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 * 我的圈列表
 */
public class CircleAdapter extends UltimatCommonAdapter<CircleVo.ItemCircle, CircleAdapter.ViewHolder>
{
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
            if (Utils.isEmpty(newsVo.getNews_info().getMulti_cover())) {
                holder.iv_thumb.setVisibility(View.GONE);
            } else {
                holder.iv_thumb.setVisibility(View.VISIBLE);
                Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getNews_info().getMulti_cover().get(0).getImg())).into(holder.iv_thumb);
            }
            if (Utils.isEmpty(newsVo.getNews_info().getAddress())) {
                holder.tv_address.setVisibility(View.GONE);
            } else {
                holder.tv_address.setVisibility(View.VISIBLE);
                holder.tv_address.setText(newsVo.getNews_info().getAddress());
            }
            holder.tv_liulan.setText(newsVo.getNews_info().getView() + "");
            holder.tv_time.setText(newsVo.getNews_info().getCreate_time() + "");
            if (Utils.isEmpty(newsVo.getNews_info().getColor())) {
                holder.tv_title.setText(newsVo.getNews_info().getTitle());
            } else {
                String text = "<html><font color=\""+newsVo.getNews_info().getColor()+"\">" + "[" + newsVo.getNews_info().getCategory_name() + "]" + "</font></html>";
                holder.tv_title.setText(Html.fromHtml(text + newsVo.getNews_info().getTitle()));
            }
            
            
            holder.tv_comment.setText(newsVo.getNews_info().getComment());
            holder.tv_zan.setText(newsVo.getNews_info().getCollection());

            holder.ll_item.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                   activity.skip(DetailActivity.class,newsVo);
                }
            });
            holder.ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utils.isEmpty(newsVo.getNews_info().getMulti_cover())){
                        showShare(activity, null, false, newsVo.getNews_info().getTitle(), newsVo.getNews_info().getId(),"");
                    }else {
                        showShare(activity, null, false, newsVo.getNews_info().getTitle(), newsVo.getNews_info().getId(), SimpleUtils.getImageUrl(newsVo.getNews_info().getMulti_cover().get(0).getImg()));
                    }
                }
            });

            holder.ll_comment.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.setView(new EditText(activity));
                    alertDialog.show();

                    View view = LayoutInflater.from(activity).inflate(R.layout.inflater_comment,null);
                    alertDialog.setContentView(view);
                    final EditText et = (EditText)view.findViewById(R.id.comment_et);
                    final TextView btn = (TextView)view.findViewById(R.id.comment_btn);

                    btn.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            if(!Utils.isEmpty(et.getText().toString().trim()))
                            {

                                final PostParams params = new PostParams();
                                try
                                {
                                    params.put("id", newsVo.getNews_info().getId());
                                    params.put("content",et.getText().toString().trim());
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }

                                HttpUtils.postJSONObject(activity, Const.CommitComment, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity)
                                {
                                    public void getResp(JSONObject jsonObject)
                                    {
                                        ((SimpleActivity) activity).dismissDialog();
                                        RespVo<CommentVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                                        if (respVo.isSuccess())
                                        {
                                            CommentVo commentVo = respVo.getData(jsonObject, CommentVo.class);
                                            holder.tv_comment.setText((Integer.valueOf(newsVo.getNews_info().getComment())+1)+"");
                                            activity.toast("评论成功");
                                            alertDialog.dismiss();
                                        }
                                        else
                                        {
                                            activity.toast(respVo.getMessage());
                                        }
                                    }

                                    public void doFailed()
                                    {

                                        ((SimpleActivity) activity).dismissDialog();
                                        activity.toast("评论失败");
                                        alertDialog.dismiss();
                                    }
                                });
                            }

                            else
                            {
                                activity.toast("请输入评论内容后再提交");
                            }
                        }
                    });

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    Window window = alertDialog.getWindow();
                    window.setWindowAnimations(R.style.PhotoCameraDialogAnim);

                    WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                    params.width = displayMetrics.widthPixels;
                    params.gravity = Gravity.BOTTOM;
                    alertDialog.getWindow().setAttributes(params);
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

        CircleImageView iv_avatar;
        ImageView iv_thumb;
        TextView tv_name;
        TextView tv_desc;

        TextView tv_guanzhu;
        TextView tv_address;
        TextView tv_liulan;
        TextView tv_time;
        TextView tv_title;
        TextView tv_comment;
        TextView tv_zan;
        TextView tv_zan_add;

        ImageView iv_zan;


        LinearLayout ll_item;
        LinearLayout ll_share;
        LinearLayout ll_zan;
        LinearLayout ll_comment;

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
                        new Handler().postDelayed(new Runnable()
                        {
                            public void run()
                            {
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
        SpUtil sp = new SpUtil(activity, Const.SP_NAME);
        if(sp.getStringValue(Const.UID).equals(newsVo.getUser_info().getUid())){
            activity.toast("不能对自己进行操作");
            return;
        }
        PostParams param = new PostParams();
        param.put("to_uid", newsVo.getUser_info().getUid());
        param.put("uid", sp.getStringValue(Const.UID));
        HttpUtils.postJSONObject(activity, Const.DO_FOLLOW, SimpleUtils.buildUrl(activity, param), new RespJSONObjectListener(activity)
        {
            @Override
            public void getResp(JSONObject jsonObject)
            {
                RespVo<String> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess())
                {
                    if ("0".equals(newsVo.getUser_info().getIs_following()))
                    {
                        newsVo.getUser_info().setIs_following("1");
                        follow.setText("已关注");
                    } else
                    {
                        newsVo.getUser_info().setIs_following("1");
                        follow.setText("关注");
                    }


                } else if (respVo.isNeedLogin())
                {
                    activity.skip(LoginActivity.class);
                }
            }

            @Override
            public void doFailed()
            {

            }
        });
    }

    public void showShare(Context context, String platformToShare, boolean showContentEdit,String title,String id,String cover) {
        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }
        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        oks.addHiddenPlatform(WechatFavorite.NAME);
        oks.setTitle(title);//分享标题
        oks.setTitleUrl(Const.SHARE + id);//分享地址
        oks.setText(title);//分享文本
        if(!Utils.isEmpty(cover)){
            oks.setImageUrl(SimpleUtils.getImageUrl(cover));//分享图片
        }
        oks.setUrl(Const.SHARE+id); //微信不绕过审核分享链接
        oks.setComment("分享"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
        oks.setSite("咘咘猫");  //QZone分享完之后返回应用时提示框上显示的名称
        oks.setSiteUrl(Const.SHARE + id);//QZone分享参数
        oks.setVenueName("咘咘猫");
        oks.setVenueDescription(title);
        oks.setShareFromQQAuthSupport(false);
        oks.show(context);
    }
}
