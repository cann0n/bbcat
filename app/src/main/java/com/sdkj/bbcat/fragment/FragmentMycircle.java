package com.sdkj.bbcat.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.fragment.BaseFragment;
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
import com.sdkj.bbcat.activity.MoreTagsActivity;
import com.sdkj.bbcat.activity.news.NewsListActivity;
import com.sdkj.bbcat.adapter.CircleAdapter;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 * 我的圈
 */
public class FragmentMycircle extends BaseFragment {

    private LinearLayout ll_label;

    @ViewInject(R.id.list_circle)
    private CustomRecyclerView list_circle;

    private int pageNum = 1;
    
    private CircleAdapter adapter;

    @Override
    protected void setListener() {

        adapter=new CircleAdapter(activity,new ArrayList<CircleVo.ItemCircle>());
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

        View header = activity.makeView(R.layout.view_circle_header);
        ll_label= (LinearLayout) header.findViewById(R.id.ll_label);
        RelativeLayout rl_more_tag= (RelativeLayout) header.findViewById(R.id.rl_more_tag);
        rl_more_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.skip(MoreTagsActivity.class);
            }
        });
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AppUtils.getWidth(activity) + 10, LinearLayout.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(lp);
        lp.rightMargin = -5;
        UltimateRecyclerView.CustomRelativeWrapper wrapper = new UltimateRecyclerView.CustomRelativeWrapper(activity);
        wrapper.addView(header);
        adapter.setCustomHeaderView(wrapper);

        query(true);

    }

    private void query(final boolean isFirst) {

        final PostParams params = new PostParams();
        params.put("page", pageNum + "");
        HttpUtils.postJSONObject(activity, Const.MY_CIRCLE, SimpleUtils.buildUrl(activity,params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                list_circle.setRefreshing(false);
                RespVo<CircleVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    CircleVo data=respVo.getData(jsonObject,CircleVo.class);
                    if(isFirst&&!Utils.isEmpty(data.getHot_tags())){
                        for(final CircleVo.HotTagsVo tag:data.getHot_tags()){
                            View view=activity.makeView(R.layout.item_qunzu);
                            ImageView iv_avatar= (ImageView) view.findViewById(R.id.iv_avatar);
                            TextView tv_name= (TextView) view.findViewById(R.id.tv_name);
                            TextView tv_desc= (TextView) view.findViewById(R.id.tv_desc);
                            TextView tv_num= (TextView) view.findViewById(R.id.tv_num);
                            TextView tv_jiaru= (TextView) view.findViewById(R.id.tv_jiaru);
                            tv_num.setVisibility(View.GONE);
                            tv_jiaru.setVisibility(View.GONE);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(tag.getCover())).into(iv_avatar);
                            tv_name.setText(tag.getTitle());
                            tv_desc.setText(tag.getJoined_person()+"人参与");
                            
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    activity.skip(NewsListActivity.class,tag.getId(),tag.getTitle());
                                }
                            });
                            ll_label.addView(view);
                        }
                    }
                    if (pageNum == 1) {
                        adapter.removeAll();
                        list_circle.setCanLoadMore();
                    }
                    if (Utils.isEmpty(data.getNews())) {
                        list_circle.setNoMoreData();
                    } else {
                        if (data.getNews().size() < 10) {
                            list_circle.setNoMoreData();
                        } else {
                            list_circle.setCanLoadMore();
                        }
                        adapter.addItems(data.getNews());
                    }
                    pageNum++;
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                list_circle.setRefreshing(false);
            }
        });
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_circle;
    }
}
