package com.sdkj.bbcat.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.widget.CustomRecyclerView;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.adapter.ArticleAdapter;
import com.sdkj.bbcat.adapter.TagBaseAdapter;
import com.sdkj.bbcat.bean.CircleTagVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.bean.SearchTagVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TagCloudLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class SearchActivity extends SimpleActivity {
    @ViewInject(R.id.et_search)
    private EditText mEt;
    @ViewInject(R.id.iv_search)
    private ImageView mSearch;
    @ViewInject(R.id.tagcontainer)
    private TagCloudLayout mTagLayout;
    private TagBaseAdapter mTagAdapter;
    private List<SearchTagVo> mTagListData;
    @ViewInject(R.id.ultimate_recycler_view)
    private CustomRecyclerView listView;
    private ArticleAdapter adapter;
    private int pageNum = 1;

    @OnClick(R.id.iv_back)
    void back(View view) {
        finish();
    }

    public int setLayoutResID() {
        return R.layout.activity_search;
    }

    public void initBusiness() {
        mTagListData = new ArrayList<>();
        mTagAdapter = new TagBaseAdapter(this, mTagListData);
        mTagLayout.setAdapter(mTagAdapter);

        /**请求热门词汇*/
        final PostParams params = new PostParams();
        params.put("category_id", "4");

        HttpUtils.postJSONObject(activity, Const.GetHotChar, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo<SearchTagVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<SearchTagVo> hotCharList = respVo.getListData(jsonObject, SearchTagVo.class);
                    mTagListData.addAll(hotCharList);
                    if (mTagListData.size() == 0) {
                        SearchTagVo vo = new SearchTagVo();
                        vo.setKeyword("暂无热门搜索标签");
                        mTagListData.add(vo);
                    }
                    mTagAdapter.notifyDataSetChanged();
                } else activity.toast(respVo.getMessage());
            }

            public void doFailed() {
                dismissDialog();
                activity.toast("链接服务器失败");
            }
        });

        adapter = new ArticleAdapter(activity, new ArrayList<CircleTagVo>());
        listView.addFooter(adapter);
        listView.setAdapter(adapter);
        listView.setNoMoreData();
        listView.setRefreshHeaderMode(listView.MODE_CLASSICDEFAULT_HEADER);
        listView.setLayoutManager(new LinearLayoutManager(activity));
        listView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            public void loadMore(int i, int i1) {
                if (listView.canLoadMore()) {
                    btnDoing();
                }
            }
        });

        listView.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            public void OnCustomRefresh(PtrFrameLayout frame) {
               /* pageNum = 1;*/
                btnDoing();
            }
        });

        btnDoing();

        mSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnDoing();
            }
        });

        mTagLayout.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            public void itemClick(int position) {
                if (!mTagListData.get(position).equals("暂无热门搜索标签")) {
                    mEt.setText(mTagListData.get(position).getKeyword());
                    btnDoing();
                }
            }
        });
    }

    private void btnDoing() {
        showDialog();
        PostParams params = new PostParams();
        params.put("category_id", "4");
        params.put("keyword", mEt.getText().toString().trim());

        HttpUtils.postJSONObject(activity, Const.SearchContent, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo<CircleTagVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<CircleTagVo> allData = respVo.getListData(jsonObject, CircleTagVo.class);
                    adapter.removeAll();
                    adapter.addItems(allData);
                    listView.setNoMoreData();

                  /*  List<BodyFeaBean> data = allData.get();
                    if (pageNum == 1)
                    {
                        adapter.removeAll();
                        list.setCanLoadMore();
                    }

                    if (Utils.isEmpty(data))
                        list.setNoMoreData();
                    else
                    {
                        if (data.size() < 10)
                            list.setNoMoreData();
                        else
                            list.setCanLoadMore();
                        adapter.addItems(data);
                    }
                    pageNum++;*/
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            public void doFailed() {
                toast("查询失败");
                dismissDialog();
                listView.setRefreshing(false);
            }
        });
    }
}
