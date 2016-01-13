package com.sdkj.bbcat.activity.bracelet;

import android.support.v7.widget.LinearLayoutManager;

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
import com.sdkj.bbcat.adapter.BodyFeaAdapter;
import com.sdkj.bbcat.bean.AllBodyFeaBean;
import com.sdkj.bbcat.bean.BodyFeaBean;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Mr.Yuan on 2016/1/12 0012.
 */
public class AllBodyFeaActivity extends SimpleActivity
{
    private int pageNum = 1;
    private BodyFeaAdapter adapter;
    @ViewInject(R.id.allbofyfeatures_list)
    private CustomRecyclerView list;

    public int setLayoutResID()
    {
        return R.layout.activity_allbodyfeatures;
    }

    public void initBusiness()
    {
        new TitleBar(activity).setTitle("身体特征").back();
        adapter = new BodyFeaAdapter(activity, new ArrayList<BodyFeaBean>());
        list.addFooter(adapter);
        list.setAdapter(adapter);

        list.setRefreshHeaderMode(list.MODE_CLASSICDEFAULT_HEADER);
        list.setLayoutManager(new LinearLayoutManager(activity));
        list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener()
        {
            public void loadMore(int i, int i1)
            {
                if (list.canLoadMore())
                {
                    query();
                }
            }
        });

        list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener()
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
        params.put("page",pageNum+"");

        HttpUtils.postJSONObject(activity, Const.GetBodyFeatures, SimpleUtils.buildUrl(activity,params), new RespJSONObjectListener(activity)
        {
            public void getResp(JSONObject jsonObject)
            {
                dismissDialog();
                list.setRefreshing(false);
                RespVo<AllBodyFeaBean> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess())
                {
                    AllBodyFeaBean allData = respVo.getData(jsonObject, AllBodyFeaBean.class);
                    List<BodyFeaBean> data = allData.getLogs();
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
                    pageNum++;
                } else
                {
                    activity.toast(respVo.getMessage());
                }
            }

            public void doFailed()
            {
                dismissDialog();
                toast("查询失败");
                list.setRefreshing(false);
            }
        });
    }
}