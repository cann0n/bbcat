package com.sdkj.bbcat.activity.bracelet;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.widget.CustomRecyclerView;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.PublishActivity;
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
 * Created by Mr.Yuan on 2016/1/7 0007.
 */
public class BabyNotesActivity extends SimpleActivity {

    @ViewInject(R.id.note_list)
    private CustomRecyclerView list_view;

    private CircleAdapter adapter;

    private int pageNum = 1;

    private int currentType = 1;

    public int setLayoutResID() {
        return R.layout.activity_babynotes;
    }

    public void initBusiness() {
        EventBus.getDefault().register(this);
        
        new TitleBar(activity).setTitle("宝宝日记").back().showRight("发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(PublishActivity.class,"0");
            }
        });

        adapter = new CircleAdapter(activity, new ArrayList<CircleVo.ItemCircle>());

        list_view.addFooter(adapter);
        list_view.setAdapter(adapter);

        list_view.setRefreshHeaderMode(list_view.MODE_CLASSICDEFAULT_HEADER);
        list_view.setLayoutManager(new LinearLayoutManager(activity));
        list_view.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (list_view.canLoadMore()) {
                    queryData("");
                }
            }
        });
        list_view.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            @Override
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                queryData("");
            }
        });

//        View header = activity.makeView(R.layout.view_note_header);
//        TextView tv_normal = (TextView) header.findViewById(R.id.tv_normal);
//        TextView tv_first = (TextView) header.findViewById(R.id.tv_first);
//        tv_normal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pageNum = 1;
//                currentType = 1;
//                showDialog();
//                queryData("");
//            }
//        });
//        tv_first.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pageNum = 1;
//                currentType = 2;
//                
//                showDialog();
//                queryData("");
//            }
//        });
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AppUtils.getWidth(activity) + 10, LinearLayout.LayoutParams.WRAP_CONTENT);
//        header.setLayoutParams(lp);
//        lp.rightMargin = -5;
//        UltimateRecyclerView.CustomRelativeWrapper wrapper = new UltimateRecyclerView.CustomRelativeWrapper(activity);
//        wrapper.addView(header);
//        adapter.setCustomHeaderView(wrapper);
        showDialog();
        queryData("");
    }

    private void queryData(String id) {
        PostParams params = new PostParams();
        params.put("type", currentType + "");
        params.put("page", pageNum + "");

        HttpUtils.postJSONObject(activity, Const.NOTE_LIST, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                list_view.setRefreshing(false);
                RespVo<CircleVo.ItemCircle> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<CircleVo.ItemCircle> data = respVo.getListData(jsonObject, CircleVo.ItemCircle.class);
                    if (pageNum == 1) {
                        adapter.removeAll();
                        list_view.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data)) {
                        list_view.setNoMoreData();
                    } else {
                        if (data.size() < 10) {
                            list_view.setNoMoreData();
                        } else {
                            list_view.setCanLoadMore();
                        }
                        adapter.addItems(data);
                    }
                    pageNum++;
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            public void doFailed() {
                list_view.setRefreshing(false);
                toast("查询失败");
                dismissDialog();
            }
        });
    }

    public void onEventMainThread(DiseaseRecordActivity.RefreshEvent event) {
        pageNum=1;
        queryData("");
    }
    
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}