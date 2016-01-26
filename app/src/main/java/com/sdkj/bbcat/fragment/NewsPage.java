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

    @ViewInject(R.id.list_view)
    private CustomRecyclerView list_view;
    private NewsAdapter adapter;
    private int pageNum = 1;
    private static final String ID = "3";

    TextView tv_1;
    TextView tv_2;
    TextView tv_3;
    TextView tv_4;
    TextView tv_5;
    TextView tv_6;
    TextView tv_7;
    TextView tv_8;
    LinearLayout ll_recommend;
    LinearLayout ll_top2;
    LinearLayout ll_top1;

    @Override
    protected void setListener() {
        new TitleBar(activity, rootView).setTitle("育儿知识").hideBack();
        adapter = new NewsAdapter(activity, new ArrayList<NewsVo>());

        list_view.addFooter(adapter);
        list_view.setAdapter(adapter);

        list_view.setRefreshHeaderMode(list_view.MODE_CLASSICDEFAULT_HEADER);
        list_view.setLayoutManager(new LinearLayoutManager(activity));
        list_view.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (list_view.canLoadMore()) {
                    query(ID);
                }
            }
        });
        list_view.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            @Override
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                query(ID);
            }
        });

        View header = activity.makeView(R.layout.view_news_header);
        TextView tv_last_more = (TextView) header.findViewById(R.id.tv_last_more);
        ll_recommend = (LinearLayout) header.findViewById(R.id.ll_recommend);
        ll_top1 = (LinearLayout) header.findViewById(R.id.ll_top1);
        ll_top2 = (LinearLayout) header.findViewById(R.id.ll_top2);
        tv_1 = (TextView) header.findViewById(R.id.tv_1);
        tv_2 = (TextView) header.findViewById(R.id.tv_2);
        tv_3 = (TextView) header.findViewById(R.id.tv_3);
        tv_4 = (TextView) header.findViewById(R.id.tv_4);
        tv_5 = (TextView) header.findViewById(R.id.tv_5);
        tv_6 = (TextView) header.findViewById(R.id.tv_6);
        tv_7 = (TextView) header.findViewById(R.id.tv_7);
        tv_8 = (TextView) header.findViewById(R.id.tv_8);
        tv_last_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skip(NewsListActivity.class, "3", "育儿知识");
            }
        });


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AppUtils.getWidth(activity) + 10, LinearLayout.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(lp);
        lp.rightMargin = -5;
        UltimateRecyclerView.CustomRelativeWrapper wrapper = new UltimateRecyclerView.CustomRelativeWrapper(activity);
        wrapper.addView(header);
        adapter.setCustomHeaderView(wrapper);
        query(ID);
    }

    private void query(String id) {
        final PostParams params = new PostParams();
        params.put("category_id", id);
        params.put("page", pageNum + "");
        HttpUtils.postJSONObject(activity, Const.CATEGORY_LIST, params, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                list_view.setRefreshing(false);
                RespVo<NewsVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    final List<Sub> subs = GsonTools.getList(jsonObject.optJSONObject("data").optJSONObject("now_category").optJSONArray("sub"), Sub.class);
                    try {
                        buildHeader(subs);
                    } catch (Exception e) {
                    }
                    List<NewsVo> data = GsonTools.getList(jsonObject.optJSONObject("data").optJSONArray("list"), NewsVo.class);

                    if (pageNum == 1) {
                        adapter.removeAll();
                        list_view.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data)) {
                        list_view.setNoMoreData();
                    } else {
                        if (data.size() < 10) {
                            list_view.setNoMoreData();
                        } else {
                            list_view.setCanLoadMore();
                        }

                        if (pageNum == 1 && data.size() > 3) {
                            int count = 0;
                            ll_recommend.removeAllViews();
                            for (final NewsVo newsVo : data) {
                                if (count < 3) {
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
                                    count++;
                                }
                            }
                            data.remove(0);
                            data.remove(0);
                            data.remove(0);
                        }

                        adapter.addItems(data);
                    }
                    pageNum++;
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                list_view.setRefreshing(false);
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
