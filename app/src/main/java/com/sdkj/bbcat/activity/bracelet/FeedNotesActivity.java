package com.sdkj.bbcat.activity.bracelet;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.widget.CustomRecyclerView;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.adapter.FeedNotesAdapter;
import com.sdkj.bbcat.bean.FeedInoVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Mr.Yuan on 2016/1/7 0007.
 */
public class FeedNotesActivity extends SimpleActivity
{
    @ViewInject(R.id.feednote_list)
    private CustomRecyclerView feednote_list;
    private FeedNotesAdapter adapter;
    private int pageNum = 1;

    public void initBusiness()
    {
        TitleBar titleBar = new TitleBar(activity).setTitle("喂养记录").back().showRight("添加", new View.OnClickListener()
        {
            public void onClick(View v)
            {
                skip(AddFeedNotesActivity.class);
            }
        });
        adapter = new FeedNotesAdapter(activity, new ArrayList<FeedInoVo>());
        feednote_list.addFooter(adapter);
        feednote_list.setAdapter(adapter);

        feednote_list.setRefreshHeaderMode(feednote_list.MODE_CLASSICDEFAULT_HEADER);
        feednote_list.setLayoutManager(new LinearLayoutManager(activity));
        feednote_list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener()
        {
            public void loadMore(int i, int i1)
            {
                if (feednote_list.canLoadMore())
                {
                    query();
                }
            }
        });
        feednote_list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener()
        {
            public void OnCustomRefresh(PtrFrameLayout frame)
            {
                pageNum = 1;
                query();
            }
        });
        showDialog();
        query();
    }

    private void query()
    {
        final PostParams params = new PostParams();
        params.put("type","1");

        HttpUtils.postJSONObject(activity,Const.GetFeedAndIno, SimpleUtils.buildUrl(activity,params), new RespJSONObjectListener(activity)
        {
            public void getResp(JSONObject jsonObject)
            {
                dismissDialog();
                feednote_list.setRefreshing(false);
                RespVo<FeedInoVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess())
                {
                    List<FeedInoVo>  feedInoVoList = respVo.getListData(jsonObject, FeedInoVo.class);
                    adapter.removeAll();
                    adapter.addItems(feedInoVoList);
                    feednote_list.setNoMoreData();
                 /*   if (pageNum == 1)
                    {
                        adapter.removeAll();
                        feednote_list.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data))
                    {
                        feednote_list.setNoMoreData();
                    }

                    else
                    {
                        if (data.size() < 10)
                        {
                            feednote_list.setNoMoreData();
                        } else
                        {
                            feednote_list.setCanLoadMore();
                        }
                        adapter.addItems(data);
                    }
                    pageNum++;*/
                } else
                {
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed()
            {
                dismissDialog();
                feednote_list.setRefreshing(false);
                toast("查询失败");
            }
        });
    }

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_feednote;
    }
}