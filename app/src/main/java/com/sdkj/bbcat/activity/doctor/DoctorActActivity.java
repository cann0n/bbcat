package com.sdkj.bbcat.activity.doctor;

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
import com.sdkj.bbcat.bean.NewsDetailVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/26 0026.
 * 医院最新活动
 */
public class DoctorActActivity extends SimpleActivity {

    @ViewInject(R.id.web_view)
    private WebView web_view;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    
    public static String tempContent = null;
    
    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("最新活动");
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

        PostParams params = new PostParams();
        params.put("hospital_id", (String)getVo("0"));
        showDialog();
        HttpUtils.postJSONObject(activity, Const.HOSPITAL_ACTIVITY, SimpleUtils.buildUrl(activity,params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo<ActDetail> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    ActDetail vo = respVo.getData(obj, ActDetail.class);
                    web_view.loadDataWithBaseURL(null, String.format(tempContent, vo.getContent()), "text/html", "utf-8", null);
                    tv_title.setText(vo.getTitle());
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
        toast("收藏");
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_hospital_act;
    }
    
    public static class ActDetail{
        
        private String title;
        
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
