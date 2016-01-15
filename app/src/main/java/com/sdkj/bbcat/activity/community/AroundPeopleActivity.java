package com.sdkj.bbcat.activity.community;

import android.support.v7.widget.LinearLayoutManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.sdkj.bbcat.adapter.AroundAdapter;
import com.sdkj.bbcat.bean.AroundPeopleVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by ${Rhino} on 2016/1/13 21:13
 */
public class AroundPeopleActivity extends SimpleActivity {

    @ViewInject(R.id.people_list)
    private CustomRecyclerView people_list;

    private int pageNum = 20;

    private AroundAdapter adapter;

    private double lat = 0;

    private double lng = 0;

    @Override
    public void initBusiness() {
        new TitleBar(this).back().setTitle("附近的人");
        adapter = new AroundAdapter(activity, new ArrayList<AroundPeopleVo>());
        people_list.addFooter(adapter);
        people_list.setAdapter(adapter);

        people_list.setRefreshHeaderMode(people_list.MODE_CLASSICDEFAULT_HEADER);
        people_list.setLayoutManager(new LinearLayoutManager(activity));
        people_list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (people_list.canLoadMore()) {
                    query();
                }
            }
        });
        people_list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            @Override
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 20;
                if (lat == 0) {
                    startBaiduLocation();
                } else {
                    query();
                }
            }
        });
        startBaiduLocation();
        showDialog();
    }

    private void query() {
        final PostParams params = new PostParams();
        params.put("km", pageNum + "");
        params.put("lng", lng + "");
        params.put("lat", lat + "");
        HttpUtils.postJSONObject(activity, Const.AROUND_PEOPLE, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                people_list.setRefreshing(false);
                RespVo<AroundPeopleVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {

                    List<AroundPeopleVo> data = respVo.getListData(jsonObject, AroundPeopleVo.class);
                    if (pageNum == 20) {
                        adapter.removeAll();
                        people_list.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data)) {
                        people_list.setNoMoreData();
                    } else {
                        if (data.size() < 10) {
                            people_list.setNoMoreData();
                        } else {
                            people_list.setCanLoadMore();
                        }
                        adapter.addItems(data);
                    }
                    pageNum = pageNum + 5;
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
                people_list.setRefreshing(false);
                toast("查询失败");
            }
        });
    }

    private void startBaiduLocation() {
        LocationClient mLocationClient = new LocationClient(activity.getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener(mLocationClient));    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private class MyLocationListener implements BDLocationListener {

        LocationClient mLocationClient;

        public MyLocationListener(LocationClient mLocationClient) {
            this.mLocationClient = mLocationClient;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (!Utils.isEmpty(location.getLatitude() + "") && !Utils.isEmpty(location.getLongitude() + "")) {
                mLocationClient.stop();
                lat = location.getLatitude();
                lng = location.getLongitude();
                query();
            } else {
                toast("定位失败");
                finish();
            }
        }
    }


    @Override
    public int setLayoutResID() {
        return R.layout.activity_around;
    }
}
