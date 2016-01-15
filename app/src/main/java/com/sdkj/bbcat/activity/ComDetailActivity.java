package com.sdkj.bbcat.activity;

import android.support.v7.widget.LinearLayoutManager;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.widget.CustomRecyclerView;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.adapter.ComDetailAdapter;
import com.sdkj.bbcat.bean.CdComentVo;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.CommentVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Mr.Yuan on 2016/1/15 0015.
 */
public class ComDetailActivity extends SimpleActivity
{
    @ViewInject(R.id.cd_list)
    private CustomRecyclerView cd_list;
    private ComDetailAdapter   adapter;
    private int pageNum = 1;
    private CircleVo.ItemCircle newsVo;

    public void initBusiness()
    {
        TitleBar titleBar = new TitleBar(activity).setTitle("文章评论").back();
        newsVo = (CircleVo.ItemCircle) getVo("0");

        adapter = new ComDetailAdapter(activity, new ArrayList<CommentVo>());
        cd_list.addFooter(adapter);
        cd_list.setAdapter(adapter);

        cd_list.setRefreshHeaderMode(cd_list.MODE_CLASSICDEFAULT_HEADER);
        cd_list.setLayoutManager(new LinearLayoutManager(activity));
        cd_list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener()
        {
            public void loadMore(int i, int i1)
            {
                if (cd_list.canLoadMore())
                {
                    query();
                }
            }
        });
        cd_list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener()
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
        PostParams params = new PostParams();
        params.put("id", newsVo.getNews_info().getId());

        HttpUtils.postJSONObject(activity, Const.GetComment, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity)
        {
            public void getResp(JSONObject jsonObject)
            {
                dismissDialog();
                RespVo<CdComentVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess())
                {
                    CdComentVo comentVo = respVo.getData(jsonObject, CdComentVo.class);
                    adapter.removeAll();
                    adapter.addItems(comentVo.getList());
                    cd_list.setNoMoreData();
                    /*if (pageNum == 1)
                    {
                        adapter.removeAll();
                        cd_list.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data))
                    {
                        feednote_list.setNoMoreData();
                    }

                    else
                    {
                        if (data.size() < 100)
                        {
                            feednote_list.setNoMoreData();
                        } else
                        {
                            feednote_list.setCanLoadMore();
                        }
                        adapter.addItems(data);
                    }
                    pageNum++;*/
                }

                else
                {
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed()
            {
                toast("获取文章评论失败");
            }
        });
    }

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_comdetail;
    }
}