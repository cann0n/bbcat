package com.sdkj.bbcat.activity.bracelet;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

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
import com.sdkj.bbcat.adapter.VaccineAdapter;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.bean.VaccineVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by ${Rhino} on 2016/2/18 10:20
 * 预防接种
 */
public class VaccineActivity extends SimpleActivity {

    @ViewInject(R.id.vaccine_list)
    private CustomRecyclerView vaccine_list;

    private VaccineAdapter adapter;

    private int pageNum = 1;

    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("预防接种").showRight("提醒", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(SetNotifyActivity.class);
            }
        }).setBg("#FF6469");
        adapter = new VaccineAdapter(activity, new ArrayList<VaccineVo>());

        vaccine_list.addFooter(adapter);
        vaccine_list.setAdapter(adapter);

        vaccine_list.setRefreshHeaderMode(vaccine_list.MODE_CLASSICDEFAULT_HEADER);
        vaccine_list.setLayoutManager(new LinearLayoutManager(activity));
        vaccine_list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (vaccine_list.canLoadMore()) {
                    queryData();
                }
            }
        });
        vaccine_list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            @Override
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                queryData();
            }
        });
        showDialog();
        queryData();
    }

    private void queryData() {
        PostParams params = new PostParams();
        params.put("page", pageNum + "");

        HttpUtils.postJSONObject(activity, Const.VACCINE_LIST, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                vaccine_list.setRefreshing(false);
                RespVo<VaccineVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<VaccineVo> data = respVo.getListData(jsonObject, VaccineVo.class);
                    if (pageNum == 1) {
                        adapter.removeAll();
                        vaccine_list.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data)) {
                        vaccine_list.setNoMoreData();
                    } else {
                        if (data.size() < 10) {
                            vaccine_list.setNoMoreData();
                        } else {
                            vaccine_list.setCanLoadMore();
                        }
                        adapter.addItems(data);
                    }
                    pageNum++;
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            public void doFailed() {
                vaccine_list.setRefreshing(false);
                toast("查询失败");
                dismissDialog();
            }
        });
    }


    @Override
    public int setLayoutResID() {
        return R.layout.activity_vaccine;
    }
}
