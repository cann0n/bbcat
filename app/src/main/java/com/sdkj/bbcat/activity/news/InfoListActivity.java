package com.sdkj.bbcat.activity.news;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.widget.CustomRecyclerView;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.SearchActivity;
import com.sdkj.bbcat.adapter.NewsAdapter;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.fragment.NewsPage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 咨询列表
 */

public class InfoListActivity extends SimpleActivity {

    private int pageNum = 1;

    @ViewInject(R.id.hospital_list)
    private CustomRecyclerView hospital_list;

    private NewsAdapter adapter;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.ll_tabs)
    LinearLayout ll_tabs;
    @ViewInject(R.id.hs_container)
    HorizontalScrollView hs_container;
    String id;
    String defaultId;

    @Override
    public void initBusiness() {

        id = (String) getVo("0");
        defaultId = (String) getVo("0");
        tv_title.setText((String) getVo("1"));

        adapter = new NewsAdapter(activity, new ArrayList<NewsVo>());
        hospital_list.addFooter(adapter);
        hospital_list.setAdapter(adapter);

        hospital_list.setRefreshHeaderMode(hospital_list.MODE_CLASSICDEFAULT_HEADER);
        hospital_list.setLayoutManager(new LinearLayoutManager(activity));
        hospital_list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (hospital_list.canLoadMore()) {
                    query(id);
                }
            }
        });
        hospital_list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            @Override
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                query(id);
            }
        });
        showDialog();
        query(id);
    }

    private void query(String id) {
        final PostParams params = new PostParams();
        params.put("category_id", id);
        params.put("page", pageNum + "");
        HttpUtils.postJSONObject(activity, Const.CATEGORY_LIST, params, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                hospital_list.setRefreshing(false);
                RespVo<NewsVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    if (ll_tabs.getChildCount() == 0) {
                        List<NewsPage.Sub> subs = GsonTools.getList(jsonObject.optJSONObject("data").optJSONObject("now_category").optJSONArray("sub"), NewsPage.Sub.class);
                        if (Utils.isEmpty(subs)) {
                            hs_container.setVisibility(View.GONE);
                        } else {
                            hs_container.setVisibility(View.VISIBLE);
                            for (int i = 0; i < subs.size()+1; i++) {
                                final TextView text = (TextView) makeView(R.layout.item_tab);
                                if (i != 0) {
                                    final NewsPage.Sub sub = subs.get(i-1);
                                    text.setText(sub.getTitle());
                                    text.setTextColor(Color.parseColor("#E6E9E0"));
                                    text.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            pageNum = 1;
                                            query(sub.getId()+"");
                                        }
                                    });
                                } else {
                                    text.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            pageNum = 1;
                                            query(defaultId);
                                        }
                                    });
                                }
                                ll_tabs.addView(text);
                            }
                        }
                    }
                    List<NewsVo> data = GsonTools.getList(jsonObject.optJSONObject("data").optJSONArray("list"), NewsVo.class);
                    if (pageNum == 1) {
                        adapter.removeAll();
                        hospital_list.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data)) {
                        hospital_list.setNoMoreData();
                    } else {
                        if (data.size() < 10) {
                            hospital_list.setNoMoreData();
                        } else {
                            hospital_list.setCanLoadMore();
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
                dismissDialog();
                hospital_list.setRefreshing(false);
                toast("查询失败");
            }
        });
    }

    @OnClick(R.id.iv_search)
    void search(View view) {
        skip(SearchActivity.class);
    }

    @OnClick(R.id.iv_catdoctor)
    void catDoctor(View view) {
        finish();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_info_list;
    }
}
