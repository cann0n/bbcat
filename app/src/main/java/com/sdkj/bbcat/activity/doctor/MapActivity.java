package com.sdkj.bbcat.activity.doctor;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by ${Rhino} on 2015/8/18 11:32
 * adv显示器
 */
public class MapActivity extends SimpleActivity {

    @ViewInject(R.id.web_view)
    private WebView web_view;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebBackForwardList hisList = web_view.copyBackForwardList();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (hisList.getSize() == 1) {
                finish();
            } else if(web_view.canGoBack()){
                web_view.goBack();
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initBusiness() {
        
        web_view.getSettings().setJavaScriptEnabled(true);

        web_view.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissDialog();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                dismissDialog();
            }

        });
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                // 构建一个Builder来显示网页中的对话框
                Builder builder = new Builder(activity);
                builder.setTitle(R.string.app_name);
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // 点击确定按钮之后,继续执行网页中的操作
                                result.confirm();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

        });
        String linkUrl = (String) getVo("0");
        web_view.loadUrl(linkUrl);
        showDialog();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_adv;
    }
}
