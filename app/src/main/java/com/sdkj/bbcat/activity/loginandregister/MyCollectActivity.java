package com.sdkj.bbcat.activity.loginandregister;

import android.support.v7.widget.LinearLayoutManager;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.widget.CustomRecyclerView;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.adapter.FollowAdapter;
import com.sdkj.bbcat.adapter.NewsAdapter;
import com.sdkj.bbcat.bean.FollowVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by ${Rhino} on 2016/1/19 11:58
 */
public class MyCollectActivity extends SimpleActivity{
    @ViewInject(R.id.follow_list)
    private CustomRecyclerView follow_list;

    private NewsAdapter adapter;

    private int pageNum = 1;
    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("我的收藏");
        adapter = new NewsAdapter(activity, new ArrayList<NewsVo>());

        follow_list.addFooter(adapter);
        follow_list.setAdapter(adapter);

        follow_list.setRefreshHeaderMode(follow_list.MODE_CLASSICDEFAULT_HEADER);
        follow_list.setLayoutManager(new LinearLayoutManager(activity));
        follow_list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (follow_list.canLoadMore()) {
                    queryData("");
                }
            }
        });
        follow_list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            @Override
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                queryData("");
            }
        });
        showDialog();
        queryData("");
    }

    private void queryData(String id) {
        PostParams params = new PostParams();
        params.put("page", pageNum + "");
        HttpUtils.postJSONObject(activity, Const.MY_COLLECT, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                follow_list.setRefreshing(false);
                RespVo<NewsVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<NewsVo> data = respVo.getListData(jsonObject, NewsVo.class);
                    if (pageNum == 1) {
                        adapter.removeAll();
                        follow_list.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data)) {
                        follow_list.setNoMoreData();
                    } else {
                        if (data.size() < 10) {
                            follow_list.setNoMoreData();
                        } else {
                            follow_list.setCanLoadMore();
                        }
                        adapter.addItems(data);
                    }
                    pageNum++;
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            public void doFailed() {
                follow_list.setRefreshing(false);
                toast("查询失败");
                dismissDialog();
            }
        });
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_follow;
    }
}
