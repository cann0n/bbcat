package com.sdkj.bbcat.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

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
public class ComDetailActivity extends SimpleActivity {
    @ViewInject(R.id.cd_list)
    private CustomRecyclerView cd_list;
    private ComDetailAdapter adapter;
    private int pageNum = 1;
    private CircleVo.ItemCircle newsVo;
    private boolean IsMody = false;

    public void initBusiness() {
        TitleBar titleBar = new TitleBar(activity) {
            public void backDoing() {
                if (IsMody) {
                    Intent intent = new Intent();
                    intent.putExtra("ismody", true);
                    setResult(0, intent);
                }
                finish();
            }
        }.setTitle("文章评论").back().showRight("评论", new View.OnClickListener() {
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setView(new EditText(activity));
                alertDialog.show();

                View view = LayoutInflater.from(activity).inflate(R.layout.inflater_comment, null);
                alertDialog.setContentView(view);
                final EditText et = (EditText) view.findViewById(R.id.comment_et);
                final TextView btn = (TextView) view.findViewById(R.id.comment_btn);

                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (!Utils.isEmpty(et.getText().toString().trim())) {

                            final PostParams params = new PostParams();
                            try {
                                params.put("id", newsVo.getNews_info().getId());
                                params.put("content", et.getText().toString().trim());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            HttpUtils.postJSONObject(activity, Const.CommitComment, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
                                public void getResp(JSONObject jsonObject) {
                                    ((SimpleActivity) activity).dismissDialog();
                                    RespVo<CommentVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                                    if (respVo.isSuccess()) {
                                        query();
                                        CommentVo commentVo = respVo.getData(jsonObject, CommentVo.class);
                                        activity.toast("评论成功");
                                        IsMody = true;
                                        alertDialog.dismiss();
                                    } else {
                                        activity.toast(respVo.getMessage());
                                    }
                                }

                                public void doFailed() {

                                    ((SimpleActivity) activity).dismissDialog();
                                    activity.toast("评论失败");
                                    alertDialog.dismiss();
                                }
                            });
                        } else {
                            activity.toast("请输入评论内容后再提交");
                        }
                    }
                });

                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                Window window = alertDialog.getWindow();
                window.setWindowAnimations(R.style.PhotoCameraDialogAnim);

                WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                params.width = displayMetrics.widthPixels;
                params.gravity = Gravity.BOTTOM;
                alertDialog.getWindow().setAttributes(params);
            }
        });
        newsVo = (CircleVo.ItemCircle) getIntent().getSerializableExtra("newvo");

        adapter = new ComDetailAdapter(activity, new ArrayList<CommentVo>());
        cd_list.addFooter(adapter);
        cd_list.setAdapter(adapter);

        cd_list.setRefreshHeaderMode(cd_list.MODE_CLASSICDEFAULT_HEADER);
        cd_list.setLayoutManager(new LinearLayoutManager(activity));
        cd_list.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            public void loadMore(int i, int i1) {
                if (cd_list.canLoadMore()) {
                    query();
                }
            }
        });
        cd_list.setOnCustomRefreshListener(new CustomRecyclerView.OnCustomRefreshListener() {
            public void OnCustomRefresh(PtrFrameLayout frame) {
                pageNum = 1;
                query();
            }
        });
        showDialog();
        query();
    }

    private void query() {
        PostParams params = new PostParams();
        params.put("id", newsVo.getNews_info().getId());

        HttpUtils.postJSONObject(activity, Const.GetComment, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo<CdComentVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
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
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                toast("获取文章评论失败");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (IsMody) {
            Intent intent = new Intent();
            intent.putExtra("ismody", true);
            setResult(0, intent);
        }
        super.onBackPressed();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_comdetail;
    }
}