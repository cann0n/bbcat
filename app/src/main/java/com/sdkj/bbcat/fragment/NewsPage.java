package com.sdkj.bbcat.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.sdkj.bbcat.activity.news.NewsListActivity;
import com.sdkj.bbcat.adapter.NewsAdapter;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
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
        tv_1 = (TextView) header.findViewById(R.id.tv_1);
        tv_2 = (TextView) header.findViewById(R.id.tv_2);
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
                    tv_1.setText(subs.get(0).getTitle());
                    tv_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.skip(NewsListActivity.class, subs.get(0).getId(), subs.get(0).getTitle());
                        }
                    });
                    
                    tv_2.setText(subs.get(1).getTitle());
                    tv_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.skip(NewsListActivity.class, subs.get(1).getId(), subs.get(1).getTitle());
                        }
                    });
                    
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
