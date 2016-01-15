package com.sdkj.bbcat.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.sdkj.bbcat.widget.AutoScrollViewPager;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.List;

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
        new TitleBar(activity).setTitle("详情").back();
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

    @Override
    protected void onResume() {
        super.onResume();


    }

    @OnClick(R.id.ll_zan_bottom)
    void zan(View view) {
        doLike(newsVo, iv_zan_bottom, tv_zan_bottom, tv_zan_add_bottom);
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
}
