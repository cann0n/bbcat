package com.sdkj.bbcat.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.widget.CustomRecyclerView;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.news.InfoListActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.activity.news.NewsListActivity;
import com.sdkj.bbcat.adapter.NewsAdapter;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 */
public class NewsPage extends BaseFragment {

    TextView tv_1;
    TextView tv_2;
    TextView tv_3;
    TextView tv_4;
    TextView tv_5;
    TextView tv_6;
    TextView tv_7;
    TextView tv_8;
    LinearLayout ll_recommend;
    LinearLayout ll_normal;
    LinearLayout ll_his;
    LinearLayout ll_top2;
    LinearLayout ll_top1;

    @Override
    protected void setListener() {
        new TitleBar(activity, rootView).setTitle("育儿知识").hideBack();

        TextView tv_last_more = (TextView) rootView.findViewById(R.id.tv_last_more);
        ll_recommend = (LinearLayout) rootView.findViewById(R.id.ll_recommend);
        ll_normal = (LinearLayout) rootView.findViewById(R.id.ll_normal);
        ll_his = (LinearLayout) rootView.findViewById(R.id.ll_his);
        ll_top1 = (LinearLayout) rootView.findViewById(R.id.ll_top1);
        ll_top2 = (LinearLayout) rootView.findViewById(R.id.ll_top2);
        tv_1 = (TextView) rootView.findViewById(R.id.tv_1);
        tv_2 = (TextView) rootView.findViewById(R.id.tv_2);
        tv_3 = (TextView) rootView.findViewById(R.id.tv_3);
        tv_4 = (TextView) rootView.findViewById(R.id.tv_4);
        tv_5 = (TextView) rootView.findViewById(R.id.tv_5);
        tv_6 = (TextView) rootView.findViewById(R.id.tv_6);
        tv_7 = (TextView) rootView.findViewById(R.id.tv_7);
        tv_8 = (TextView) rootView.findViewById(R.id.tv_8);
        tv_last_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(NewsListActivity.class, "3", "育儿知识");
            }
        });
        query();
    }

    private void query() {
        final PostParams params = new PostParams();
        HttpUtils.postJSONObject(activity, Const.NEWS_LIST, SimpleUtils.buildUrl(activity,params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                RespVo<NewsVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    final List<Sub> subs = GsonTools.getList(jsonObject.optJSONObject("data").optJSONObject("now_category").optJSONArray("sub"), Sub.class);
                    try {
                        buildHeader(subs);
                    } catch (Exception e) {
                    }
                    List<NewsVo> newest = GsonTools.getList(jsonObject.optJSONObject("data").optJSONArray("newest"), NewsVo.class);
                    List<NewsVo> normal = GsonTools.getList(jsonObject.optJSONObject("data").optJSONArray("normal"), NewsVo.class);
                    List<NewsVo> history = GsonTools.getList(jsonObject.optJSONObject("data").optJSONArray("history"), NewsVo.class);


                    ll_recommend.removeAllViews();
                    if (Utils.isEmpty(newest)) {
                        ll_recommend.setVisibility(View.GONE);
                    } else {
                        ll_recommend.setVisibility(View.VISIBLE);
                        for (final NewsVo newsVo : newest) {
                            View view = activity.makeView(R.layout.item_recommend);
                            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                            TextView tv_come_form = (TextView) view.findViewById(R.id.tv_come_form);
                            TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(iv_image);
                            tv_title.setText(newsVo.getTitle());
                            tv_come_form.setText(newsVo.getCategory_name());
                            tv_count.setText(newsVo.getView() + "");
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    activity.skip(NewsDetailActivity.class, newsVo.getId());
                                }
                            });
                            ll_recommend.addView(view);
                        }
                    }
                    if (Utils.isEmpty(normal)) {
                        ll_normal.setVisibility(View.GONE);
                    } else {
                        ll_normal.setVisibility(View.VISIBLE);
                        ll_normal.removeAllViews();
                        for (final NewsVo newsVo : normal) {
                            View view = activity.makeView(R.layout.item_recommend);
                            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                            TextView tv_come_form = (TextView) view.findViewById(R.id.tv_come_form);
                            TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(iv_image);
                            tv_title.setText(newsVo.getTitle());
                            tv_come_form.setText(newsVo.getCategory_name());
                            tv_count.setText(newsVo.getView() + "");
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    activity.skip(NewsDetailActivity.class, newsVo.getId());
                                }
                            });
                            ll_normal.addView(view);
                        }
                    }
                    ll_his.removeAllViews();
                    if (Utils.isEmpty(history)) {
                        ll_his.setVisibility(View.GONE);
                    } else {
                        ll_his.setVisibility(View.VISIBLE);
                        for (final NewsVo newsVo : history) {
                            View view = activity.makeView(R.layout.item_recommend);
                            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                            TextView tv_come_form = (TextView) view.findViewById(R.id.tv_come_form);
                            TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(iv_image);
                            tv_title.setText(newsVo.getTitle());
                            tv_come_form.setText(newsVo.getCategory_name());
                            tv_count.setText(newsVo.getView() + "");
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    activity.skip(NewsDetailActivity.class, newsVo.getId());
                                }
                            });
                            ll_his.addView(view);
                        }
                    }

                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
            }
        });
    }

    private void buildHeader(final List<Sub> subs) throws Exception {
        if (Utils.isEmpty(subs)) {
            return;
        }
        tv_1.setText(subs.get(0).getTitle());
        tv_1.setVisibility(View.VISIBLE);
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(InfoListActivity.class, subs.get(0).getId(), subs.get(0).getTitle());
            }
        });

        tv_2.setText(subs.get(1).getTitle());
        tv_2.setVisibility(View.VISIBLE);
        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(InfoListActivity.class, subs.get(1).getId(), subs.get(1).getTitle());
            }
        });

        tv_3.setText(subs.get(2).getTitle());
        tv_3.setVisibility(View.VISIBLE);
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(InfoListActivity.class, subs.get(2).getId(), subs.get(2).getTitle());
            }
        });

        tv_4.setText(subs.get(3).getTitle());
        tv_4.setVisibility(View.VISIBLE);
        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(InfoListActivity.class, subs.get(3).getId(), subs.get(3).getTitle());
            }
        });

        tv_5.setText(subs.get(4).getTitle());
        tv_5.setVisibility(View.VISIBLE);
        tv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(InfoListActivity.class, subs.get(4).getId(), subs.get(4).getTitle());
            }
        });

        tv_6.setText(subs.get(5).getTitle());
        tv_6.setVisibility(View.VISIBLE);
        tv_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(InfoListActivity.class, subs.get(5).getId(), subs.get(5).getTitle());
            }
        });


        tv_7.setText(subs.get(6).getTitle());
        tv_7.setVisibility(View.VISIBLE);
        tv_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(InfoListActivity.class, subs.get(6).getId(), subs.get(6).getTitle());
            }
        });


        tv_8.setText(subs.get(7).getTitle());
        tv_8.setVisibility(View.VISIBLE);
        tv_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(InfoListActivity.class, subs.get(7).getId(), subs.get(7).getTitle());
            }
        });

    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_news;
    }

    public static class Sub implements Serializable {
        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
