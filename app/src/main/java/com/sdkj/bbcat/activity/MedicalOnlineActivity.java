package com.sdkj.bbcat.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.widget.CustomRecyclerView;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.community.ChatActivity;
import com.sdkj.bbcat.activity.doctor.OnlineQAActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.adapter.HospitalAdapter;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.Constant;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 医疗在线
 */

public class MedicalOnlineActivity extends SimpleActivity {

    private int pageNum = 1;

    @ViewInject(R.id.hospital_list)
    private CustomRecyclerView hospital_list;

    private HospitalAdapter adapter;

    @ViewInject(R.id.ll_location)
    private LinearLayout ll_location;

    @ViewInject(R.id.ll_container)
    private LinearLayout ll_container;

    @ViewInject(R.id.tv_local)
    private TextView tv_local;

    @ViewInject(R.id.tv_city_name)
    private TextView tv_city_name;

    @ViewInject(R.id.ll_sort)
    private LinearLayout ll_sort;

    @ViewInject(R.id.tv_sort)
    private TextView tv_sort;


    @ViewInject(R.id.ll_type)
    private LinearLayout ll_type;

    @ViewInject(R.id.tv_type)
    private TextView tv_type;

    @ViewInject(R.id.iv_call)
    private ImageView iv_call;

    private View popupViewSort;

    private PopupWindow popupWindowSort;

    private View popupViewLocal;

    private PopupWindow popupWindowLocal;

    private View popupViewStatus;

    private PopupWindow popupWindowStatus;
    
    private double lat=0;
    private double lng=0;
    private String order="level desc";
    
    private String classify="";
    private String km="0.5";

    @Override
    public void initBusiness() {
        
        final String id = (String) getVo("0");
        adapter = new HospitalAdapter(activity, new ArrayList<NewsVo>());
        hospital_list.addFooter(adapter);
        hospital_list.setAdapter(adapter);

        hospital_list.setRefreshHeaderMode(hospital_list.MODE_CLASSICDEFAULT_HEADER);
        hospital_list.setLayoutManager(new LinearLayoutManager(activity));
        hospital_list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (hospital_list.canLoadMore()) {
                    query(false);
                }
            }
        });
        hospital_list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            @Override
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                query(false);
            }
        });
        showDialog();
        startBaiduLocation();
    }

    private void query(boolean isFirst) {
        final PostParams params = new PostParams();
        params.put("lat", lat+"");
        params.put("lng", lng+"");
        params.put("order", order);
        params.put("classify", classify);
        params.put("km", km);
        params.put("page", pageNum + "");
        if(isFirst){
            showDialog();
        }
        HttpUtils.postJSONObject(activity, Const.HOSPITAL_LIST, params, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                iv_call.setVisibility(View.VISIBLE);
                hospital_list.setRefreshing(false);
                RespVo<NewsVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<NewsVo> data = respVo.getListData(jsonObject, NewsVo.class);
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
                toast("查询失败");
                hospital_list.setRefreshing(false);
            }
        });
    }

    @OnClick(R.id.iv_search)
    void search(View view) {
        skip(DocSearchActivity.class);
    }

    @OnClick(R.id.iv_catdoctor)
    void catdoctor(View view) {
       finish();
    }

    @OnClick(R.id.ll_location)
    void selectLocation(View view){
        showLocationWindow();
    }
    @OnClick(R.id.ll_sort)
    void selectSort(View view){
        showDayWindow();
    }

    @OnClick(R.id.ll_type)
    void selectType(View view){
        showStatusWindow();
    }

    private void showDayWindow() {
        if (popupViewSort == null) {
            popupViewSort = activity.makeView(R.layout.window_select_day);
            popupWindowSort = new PopupWindow(popupViewSort, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            popupWindowSort.setFocusable(true);
            popupWindowSort.setOutsideTouchable(true);
            popupWindowSort.setBackgroundDrawable(new BitmapDrawable());

            LinearLayout window = (LinearLayout) popupViewSort.findViewById(R.id.ll_window);
            LinearLayout content = (LinearLayout) popupViewSort.findViewById(R.id.ll_content);
            TextView tv_all = (TextView) popupViewSort.findViewById(R.id.tv_all);
            TextView tv_day1 = (TextView) popupViewSort.findViewById(R.id.tv_day1);
           
            tv_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowSort.dismiss();
                    tv_sort.setText("信誉升");
                    tv_sort.setTag("");
                    pageNum = 1;
                    order="level asc";
                    query(true);
                }
            });

            tv_day1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowSort.dismiss();
                    tv_sort.setText("信誉降");
                    tv_sort.setTag("1");
                    pageNum = 1;
                    order="level desc";
                    query(true);
                }
            });
          

            window.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindowSort.dismiss();
                }
            });
            content.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindowSort.dismiss();
                }
            });
        }
        if (popupWindowSort.isShowing()) {
            popupWindowSort.dismiss();
        } else {
            popupWindowSort.showAsDropDown(ll_container);
        }
    }

    private void showLocationWindow() {
        if (popupViewLocal == null) {

            popupViewLocal = activity.makeView(R.layout.window_select_local);
            popupWindowLocal = new PopupWindow(popupViewLocal, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            popupWindowLocal.setFocusable(true);
            popupWindowLocal.setOutsideTouchable(true);
            popupWindowLocal.setOutsideTouchable(true);
            popupWindowLocal.setBackgroundDrawable(new BitmapDrawable());

            LinearLayout window = (LinearLayout) popupViewLocal.findViewById(R.id.ll_window);
            LinearLayout content = (LinearLayout) popupViewLocal.findViewById(R.id.ll_content);
            final TextView tv_all = (TextView) popupViewLocal.findViewById(R.id.tv_all);
            final TextView tv_grade1 = (TextView) popupViewLocal.findViewById(R.id.tv_grade1);
            final TextView tv_grade2 = (TextView) popupViewLocal.findViewById(R.id.tv_grade2);

            tv_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_local.setText("500M内");
                    tv_local.setTag("");
                    km="0.5";
                    popupWindowLocal.dismiss();
                    pageNum = 1;
                    query(true);
                }
            });
            tv_grade1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_local.setText("10KM内");
                    tv_local.setTag("1");
                    popupWindowLocal.dismiss();
                    pageNum = 1;
                    km="10";
                    query(true);
                }
            });
            tv_grade2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_local.setText(">10KM");
                    tv_local.setTag("2");
                    km="100";
                    popupWindowLocal.dismiss();
                    pageNum = 1;
                    query(true);
                }
            });

            window.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindowLocal.dismiss();
                }
            });
            content.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindowLocal.dismiss();
                }
            });
        }
        if (popupWindowLocal.isShowing()) {
            popupWindowLocal.dismiss();
        } else {
            popupWindowLocal.showAsDropDown(ll_container);
        }
    }


    private void showStatusWindow() {
        if (popupViewStatus == null) {

            popupViewStatus = activity.makeView(R.layout.window_course_status);
            popupWindowStatus = new PopupWindow(popupViewStatus, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            popupWindowStatus.setFocusable(true);
            popupWindowStatus.setOutsideTouchable(true);
            popupWindowStatus.setOutsideTouchable(true);
            popupWindowStatus.setBackgroundDrawable(new BitmapDrawable());

            LinearLayout window = (LinearLayout) popupViewStatus.findViewById(R.id.ll_window);
            LinearLayout content = (LinearLayout) popupViewStatus.findViewById(R.id.ll_content);
            final TextView tv_all = (TextView) popupViewStatus.findViewById(R.id.tv_all);
            final TextView tv_wait = (TextView) popupViewStatus.findViewById(R.id.tv_wait);
            final TextView tv_ing = (TextView) popupViewStatus.findViewById(R.id.tv_ing);
            final TextView tv_finish = (TextView) popupViewStatus.findViewById(R.id.tv_finish);
            final TextView tv_cancel = (TextView) popupViewStatus.findViewById(R.id.tv_cancel);
            final TextView tv_last = (TextView) popupViewStatus.findViewById(R.id.tv_last);

            tv_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_type.setText("一级");
                    tv_type.setTag("1");
                    classify="1";
                    popupWindowStatus.dismiss();
                    pageNum = 1;
                    query(true);
                }
            });
            tv_wait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_type.setText("二级");
                    tv_type.setTag("2");
                    classify="2";
                    popupWindowStatus.dismiss();
                    pageNum = 1;
                    query(true);
                }
            });
            tv_ing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_type.setText("三级");
                    tv_type.setTag("3");
                    classify="3";
                    popupWindowStatus.dismiss();
                    pageNum = 1;
                    query(true);
                }
            });
            tv_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_type.setText("公立");
                    tv_type.setTag("4");
                    classify="4";
                    popupWindowStatus.dismiss();
                    pageNum = 1;
                    query(true);
                }
            });
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_type.setText("私立");
                    tv_type.setTag("5");
                    classify="5";
                    popupWindowStatus.dismiss();
                    pageNum = 1;
                    query(true);
                }
            });
            tv_last.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_type.setText("妇幼保健院");
                    tv_type.setTag("6");
                    classify="6";
                    popupWindowStatus.dismiss();
                    pageNum = 1;
                    query(true);
                }
            });


            window.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindowStatus.dismiss();
                }
            });
            content.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindowStatus.dismiss();
                }
            });
        }
        if (popupWindowStatus.isShowing()) {
            popupWindowStatus.dismiss();
        } else {
            popupWindowStatus.showAsDropDown(ll_container);
        }
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
                tv_city_name.setText(location.getCity());
                lat=location.getLatitude();
                lng=location.getLongitude();
                query(false);
            }
        }
    }
    
    @OnClick(R.id.iv_call)
    void makeCall(View view){
//        skip(OnlineQAActivity.class);
        if(SimpleUtils.isLogin(activity)){
            String username = "admin";
            // demo中直接进入聊天页面，实际一般是进入用户详情页
            Intent intent = new Intent(activity, ChatActivity.class);
            SpUtil sp = new SpUtil(activity, Const.SP_NAME);

            intent.putExtra(Constant.EXTRA_USER_ID, username);
            intent.putExtra(Constant.EXTRA_USER_AVATAR, sp.getStringValue(Const.AVATAR));
            intent.putExtra(Constant.EXTRA_USER_NICKNAME, sp.getStringValue(Const.NICKNAME));
            startActivity(intent);
        }else{
            skip(LoginActivity.class);
        }
    }
    
    @Override
    public int setLayoutResID() {
        return R.layout.activity_medicalonline;
    }
}
