package com.sdkj.bbcat.activity.news;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.NewsDetailVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/26 0026.
 * 咨询详情
 */
public class NewsDetailActivity extends SimpleActivity {


    @ViewInject(R.id.web_view)
    private WebView web_view;

    @ViewInject(R.id.tv_count)
    private TextView tv_count;

    @ViewInject(R.id.tv_come_form)
    private TextView tv_come_form;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.tv_collect)
    private TextView tv_collect;

    public static String tempContent = null;

    private NewsVo vo;

    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("详情");
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.setScrollContainer(true);
        web_view.setWebChromeClient(new WebChromeClient());
        tempContent = SimpleUtils.readTemplate(activity, "template.html");

        if (AppUtils.checkNet(activity)) {
            web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            web_view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        web_view.setWebViewClient(new WebViewClient() {
        });

        vo = (NewsVo) getVo("0");

        tv_count.setText(vo.getView());
        tv_come_form.setText(vo.getCategory_name());
        tv_title.setText(vo.getTitle());
        PostParams params = new PostParams();
        params.put("news_id", vo.getId());
        showDialog();
        HttpUtils.postJSONObject(activity, Const.NEWS_DETAIL, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo<NewsDetailVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    NewsDetailVo vo = respVo.getData(obj, NewsDetailVo.class);
                    web_view.loadDataWithBaseURL(null, String.format(tempContent, vo.getDetail()), "text/html", "utf-8", null);
                    if ("0".equals(vo.getIs_collected())) {
                        tv_collect.setText("收藏");
                    } else {
                        tv_collect.setText("已收藏");
                    }
                } else {
                    toast(respVo.getMessage());
                }

            }

            @Override
            public void doFailed() {
                dismissDialog();
            }
        });
    }

    @OnClick(R.id.ll_share)
    void share(View view) {
        toast("分享");
    }

    @OnClick(R.id.ll_collect)
    void collect(View view) {
        if (SimpleUtils.isLogin(activity)) {
            doLike(vo.getId());
        } else {
            activity.skip(LoginActivity.class);
        }
    }

    private void doLike(String id) {
        showDialog();
        PostParams param = new PostParams();
        param.put("id", id);
        HttpUtils.postJSONObject(activity, Const.DO_FOLLOW, SimpleUtils.buildUrl(activity, param), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo<String> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    if ("收藏".equals(tv_collect.getText().toString())) {
                        tv_collect.setText("已收藏");
                    } else {
                        tv_collect.setText("收藏");
                    }

                } else if (respVo.isNeedLogin()) {
                    activity.skip(LoginActivity.class);
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
            }
        });
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_news_detail;
    }
}
