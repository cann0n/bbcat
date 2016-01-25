package com.sdkj.bbcat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.CommentVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.hx.DemoHelper;
import com.sdkj.bbcat.widget.AutoScrollViewPager;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.wechat.favorite.WechatFavorite;

public class DetailActivity extends SimpleActivity {

    @ViewInject(R.id.iv_avatar)
    ImageView iv_avatar;

    @ViewInject(R.id.tv_name)
    TextView tv_name;

    @ViewInject(R.id.tv_desc)
    TextView tv_desc;

    @ViewInject(R.id.tv_guanzhu)
    TextView tv_guanzhu;

    @ViewInject(R.id.tv_address)
    TextView tv_address;

    @ViewInject(R.id.tv_liulan)
    TextView tv_liulan;

    @ViewInject(R.id.tv_zan_bottom)
    TextView tv_zan_bottom;

    @ViewInject(R.id.tv_comment_num)
    TextView tv_comment_num;
    @ViewInject(R.id.tv_comment_bottom)
    TextView tv_comment_bottom;

    @ViewInject(R.id.tv_zan_num)
    TextView tv_zan_num;

    @ViewInject(R.id.tv_time)
    TextView tv_time;

    @ViewInject(R.id.tv_title)
    TextView tv_title;

    @ViewInject(R.id.tv_zan_add_bottom)
    TextView tv_zan_add_bottom;

    @ViewInject(R.id.iv_zan_bottom)
    ImageView iv_zan_bottom;

    @ViewInject(R.id.tv_comment)
    TextView tv_comment;

    @ViewInject(R.id.tv_zan)
    TextView tv_zan;

    @ViewInject(R.id.ll_comment_bar)
    LinearLayout ll_comment_bar;

    @ViewInject(R.id.ll_comment_container)
    LinearLayout ll_comment_container;

    @ViewInject(R.id.ll_comment_bottom)
    LinearLayout ll_comment_bottom;

    private CircleVo.ItemCircle newsVo;

    AutoScrollViewPager banner;

    @ViewInject(R.id.ll_banner)
    LinearLayout ll_banner;

    @Override
    public void initBusiness() {
        new TitleBar(activity).setTitle("详情").back().showRight("加TA", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newsVo!=null&&newsVo.getUser_info()!=null){
                    addContact(newsVo.getUser_info().getMobile());
                }
            }
        });
        ll_comment_bar.setVisibility(View.GONE);
        banner = new AutoScrollViewPager(activity);

        newsVo = (CircleVo.ItemCircle) getVo("0");
        if ("1".equals(newsVo.getNews_info().getIs_collected())) {
            iv_zan_bottom.setImageResource(R.drawable.icon_zan1);
        } else {
            iv_zan_bottom.setImageResource(R.drawable.icon_zan);
        }
        tv_zan_bottom.setText(newsVo.getNews_info().getCollection());


        Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getUser_info().getAvatar())).into(iv_avatar);

        tv_name.setText(newsVo.getUser_info().getNickname());
        tv_desc.setText(newsVo.getUser_info().getBirthday());
        tv_guanzhu.setText("0".equals(newsVo.getUser_info().getIs_following()) ? "关注" : "已关注");
        tv_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SimpleUtils.isLogin(activity)) {
                    doLike(newsVo, tv_guanzhu);
                } else {
                    skip(LoginActivity.class);
                }
            }
        });
        banner.loadAutoScrollViewPager(ll_banner, newsVo.getNews_info().getMulti_cover());
        if (Utils.isEmpty(newsVo.getNews_info().getMulti_cover())) {
            ll_banner.setVisibility(View.GONE);
        } else {
            ll_banner.setVisibility(View.VISIBLE);
        }

        if (Utils.isEmpty(newsVo.getNews_info().getAddress())) {
            tv_address.setVisibility(View.GONE);
        } else {
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText(newsVo.getNews_info().getAddress());
        }

        tv_liulan.setText(newsVo.getNews_info().getView() + "");
        tv_time.setText(newsVo.getNews_info().getCreate_time() + "");
        tv_title.setText(newsVo.getNews_info().getTitle());
        tv_comment.setText(newsVo.getNews_info().getComment());
        tv_comment_bottom.setText(newsVo.getNews_info().getComment());
        tv_zan.setText(newsVo.getNews_info().getCollection());
        tv_zan_num.setText("赞" + newsVo.getNews_info().getCollection());
        showDialog();
        PostParams params = new PostParams();
        params.put("id", newsVo.getNews_info().getId());

        HttpUtils.postJSONObject(activity, Const.CIRCLE_DETAIL, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<CommentVo> data = GsonTools.getList(obj.optJSONObject("data").optJSONObject("comment_list").optJSONArray("list"), CommentVo.class);
                    tv_comment_num.setText("评论" + obj.optJSONObject("data").optJSONObject("comment_list").optString("total_count"));
                    tv_comment_bottom.setText(obj.optJSONObject("data").optJSONObject("comment_list").optString("total_count"));
                    if (Utils.isEmpty(data)) {
                        return;
                    }
                    for (CommentVo comment : data) {
                        View view = makeView(R.layout.item_comment);
                        ImageView iv_item_avatar = (ImageView) view.findViewById(R.id.iv_item_avatar);
                        TextView tv_item_name = (TextView) view.findViewById(R.id.tv_item_name);
                        TextView tv_item_desc = (TextView) view.findViewById(R.id.tv_item_desc);
                        TextView tv_comment_time = (TextView) view.findViewById(R.id.tv_comment_time);
                        Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(comment.getAvatar())).into(iv_item_avatar);
                        tv_item_name.setText(comment.getNickname());
                        tv_item_desc.setText(comment.getContent());
                        tv_comment_time.setText(comment.getCreate_time());
                        ll_comment_container.addView(view);
                    }

                }

            }

            @Override
            public void doFailed() {
                dismissDialog();
            }
        });

        ll_comment_bottom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ComDetailActivity.class);
                intent.putExtra("newvo", newsVo);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void doLike(final CircleVo.ItemCircle newsVo, final TextView follow) {
        SpUtil sp = new SpUtil(activity, Const.SP_NAME);
        if (sp.getStringValue(Const.UID).equals(newsVo.getUser_info().getUid())) {
            activity.toast("不能对自己进行操作");
            return;
        }
        PostParams param = new PostParams();
        param.put("to_uid", newsVo.getUser_info().getUid());
        param.put("uid", sp.getStringValue(Const.UID));
        HttpUtils.postJSONObject(activity, Const.DO_FOLLOW, SimpleUtils.buildUrl(activity, param), new RespJSONObjectListener(activity) {
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
                    tv_zan_num.setText("赞" + like_num.getText().toString());

                } else if (respVo.isNeedLogin()) {
                    activity.skip(LoginActivity.class);
                }
            }

            @Override
            public void doFailed() {

            }
        });
    }


    @OnClick(R.id.ll_zan_bottom)
    void zan(View view) {
        doLike(newsVo, iv_zan_bottom, tv_zan_bottom, tv_zan_add_bottom);
    }
    
    @OnClick(R.id.ll_share_bottom)
    void share(View view){
        if(Utils.isEmpty(newsVo.getNews_info().getMulti_cover())){
            showShare(activity, null, false, newsVo.getNews_info().getTitle(), newsVo.getNews_info().getId(),"");
        }else {
            showShare(activity, null, false, newsVo.getNews_info().getTitle(), newsVo.getNews_info().getId(), SimpleUtils.getImageUrl(newsVo.getNews_info().getMulti_cover().get(0).getImg()));
        }
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 0: {
                    if (data.getBooleanExtra("ismody", false)) {
                        ll_comment_container.removeAllViews();
                        initBusiness();
                    }
                }
            }
        }
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

    public void addContact(final String mobile){
        if(EMChatManager.getInstance().getCurrentUser().equals(mobile)){
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if(DemoHelper.getInstance().getContactList().containsKey(mobile)){
            //提示已在好友列表中(在黑名单列表里)，无需添加
            if(EMContactManager.getInstance().getBlackListUsernames().contains(mobile)){
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                try {
                    //demo写死了个reason，实际应该让用户手动填入
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMContactManager.getInstance().addContact(mobile, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
