package com.sdkj.bbcat.activity.bracelet;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.ComDetailActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.CommentVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.AutoScrollViewPager;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.wechat.favorite.WechatFavorite;

public class BabyFoodsActivity extends SimpleActivity {

    @ViewInject(R.id.web_view)
    private WebView web_view;

    @Override
    public void initBusiness() {
        new TitleBar(activity).setTitle("宝宝辅食").back();
        web_view.getSettings().setJavaScriptEnabled(true);

        web_view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_view.setScrollContainer(true);
        web_view.setWebChromeClient(new WebChromeClient());
        web_view.getSettings().setJavaScriptEnabled(true);
        if (AppUtils.checkNet(activity)) {
            web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            web_view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        web_view.setWebViewClient(new WebViewClient() {
        });

        web_view.loadUrl(Const.BABY_FOODS);
        
    }

   

    @Override
    public int setLayoutResID() {
        return R.layout.activity_baby_foods;
    }
}
