package com.sdkj.bbcat.activity.doctor;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.easeui.domain.EaseUser;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.widget.CustomRecyclerView;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.PublishActivity;
import com.sdkj.bbcat.activity.community.ChatActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.adapter.CircleAdapter;
import com.sdkj.bbcat.adapter.OnlineFaqAdapter;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.Constant;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 在线咨询
 */
public class OnlineQAActivity extends SimpleActivity {

    @ViewInject(R.id.note_list)
    private CustomRecyclerView list_view;

    private OnlineFaqAdapter adapter;

    private int pageNum = 1;

    public void initBusiness() {
        new TitleBar(activity).setTitle("在线咨询").back().showShare(R.drawable.icon_onlie_msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        adapter = new OnlineFaqAdapter(activity, new ArrayList<CircleVo.ItemCircle>());

        list_view.addFooter(adapter);
        list_view.setAdapter(adapter);

        list_view.setRefreshHeaderMode(list_view.MODE_CLASSICDEFAULT_HEADER);
        list_view.setLayoutManager(new LinearLayoutManager(activity));
        list_view.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (list_view.canLoadMore()&&pageNum!=1) {
                    queryData();
                }
            }
        });
        list_view.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            @Override
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                queryData();
            }
        });

        View header = activity.makeView(R.layout.view_qa_header);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AppUtils.getWidth(activity) + 10, LinearLayout.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(lp);
        lp.rightMargin = -5;
        UltimateRecyclerView.CustomRelativeWrapper wrapper = new UltimateRecyclerView.CustomRelativeWrapper(activity);
        wrapper.addView(header);
        adapter.setCustomHeaderView(wrapper);
        showDialog();
        queryData();
    }

    private void queryData() {
        PostParams params = new PostParams();
        params.put("page", pageNum + "");

        HttpUtils.postJSONObject(activity, Const.ONLINE_FAQ, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
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

    public int setLayoutResID() {
        return R.layout.activity_babynotes;
    }
}