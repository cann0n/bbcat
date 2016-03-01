package com.sdkj.bbcat.activity;

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
import com.sdkj.bbcat.activity.bracelet.DiseaseRecordActivity;
import com.sdkj.bbcat.adapter.CircleAdapter;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by ${Rhino} on 2016/3/1 10:46
 */
public class MyDynamicActivity extends SimpleActivity {
    @ViewInject(R.id.list_circle)
    private CustomRecyclerView list_circle;

    private int pageNum = 1;

    private CircleAdapter adapter;

    @Override
    public void initBusiness() {
        EventBus.getDefault().register(this);
        new TitleBar(activity).back().setTitle("我的动态");

        adapter = new CircleAdapter(activity, new ArrayList<CircleVo.ItemCircle>());
        list_circle.addFooter(adapter);
        list_circle.setAdapter(adapter);

        list_circle.setRefreshHeaderMode(list_circle.MODE_CLASSICDEFAULT_HEADER);
        list_circle.setLayoutManager(new LinearLayoutManager(activity));
        list_circle.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (list_circle.canLoadMore()) {
                    query(false);
                }
            }
        });
        list_circle.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            @Override
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                query(false);
            }
        });
        showDialog();
        query(true);
    }

    private void query(final boolean isFirst) {
        final PostParams params = new PostParams();
        params.put("page", pageNum + "");
        HttpUtils.postJSONObject(activity, Const.MY_DYNAMIC, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                list_circle.setRefreshing(false);
                RespVo<CircleVo.ItemCircle> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<CircleVo.ItemCircle> data = respVo.getListData(jsonObject, CircleVo.ItemCircle.class);
                    if (pageNum == 1) {
                        adapter.removeAll();
                        list_circle.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data)) {
                        list_circle.setNoMoreData();
                    } else {
                        if (data.size() < 10) {
                            list_circle.setNoMoreData();
                        } else {
                            list_circle.setCanLoadMore();
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
                list_circle.setRefreshing(false);
            }
        });
    }

    public void onEventMainThread(DiseaseRecordActivity.RefreshEvent event) {
        pageNum = 1;
        query(false);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_my_dynamic;
    }
}
