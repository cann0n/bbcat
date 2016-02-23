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
import com.sdkj.bbcat.adapter.FeedNotesAdapter;
import com.sdkj.bbcat.bean.FeedInoVo;
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
public class FeedNotesActivity extends SimpleActivity {
    @ViewInject(R.id.feed_list)
    private CustomRecyclerView feed_list;
    private FeedNotesAdapter adapter;
    private int pageNum = 1;

    public void initBusiness() {
        EventBus.getDefault().register(this);
        new TitleBar(activity).setBg("#FF6469").setTitle("喂养记录").back().showShare(R.drawable.em_add, new View.OnClickListener() {
            public void onClick(View v) {
                skip(NewFoodRecordActivity.class);
            }
        });
        adapter = new FeedNotesAdapter(activity, new ArrayList<FeedInoVo>());
        feed_list.addFooter(adapter);
        feed_list.setAdapter(adapter);

        feed_list.setRefreshHeaderMode(feed_list.MODE_CLASSICDEFAULT_HEADER);
        feed_list.setLayoutManager(new LinearLayoutManager(activity));
        feed_list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            public void loadMore(int i, int i1) {
                if (feed_list.canLoadMore()) {
                    query();
                }
            }
        });
        feed_list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                query();
            }
        });
        showDialog();
        query();
    }

    private void query() {
        final PostParams params = new PostParams();

        HttpUtils.postJSONObject(activity, Const.FEED_RECORD, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                feed_list.setRefreshing(false);
                RespVo<FeedInoVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<FeedInoVo> feedInoVoList = respVo.getListData(jsonObject, FeedInoVo.class);
                    if (pageNum == 1) {
                        adapter.removeAll();
                        feed_list.setCanLoadMore();
                    }
                    if (Utils.isEmpty(feedInoVoList)) {
                        feed_list.setNoMoreData();
                    } else {
                        if (feedInoVoList.size() < 10) {
                            feed_list.setNoMoreData();
                        } else {
                            feed_list.setCanLoadMore();
                        }
                        adapter.addItems(feedInoVoList);
                    }
                    pageNum++;
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
                feed_list.setRefreshing(false);
                toast("查询失败");
            }
        });
    }

    public static class FreshEvent {

    }

    public void onEventMainThread(FreshEvent event) {
        pageNum =1;
        query();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_feednote;
    }
}