package com.sdkj.bbcat;

import android.app.ProgressDialog;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.utils.SpUtil;
import com.sdkj.bbcat.constValue.Const;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ${Rhino} on 2015/12/10 15:32
 */
public abstract class SimpleActivity extends BaseActivity {

    /**
     * 加载对话框
     */
    private ProgressDialog loadingDialog;

    public void showDialog() {
        if (activity == null) {
            return;
        }
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(activity, R.style.LodingDialog);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();
        loadingDialog.setContentView(R.layout.view_pb);
    }

    public void showDialog(String msg) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(activity);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        if (msg.length() < 1) {
            showDialog();
        } else {
            loadingDialog.setMessage(msg);
            loadingDialog.show();
        }
    }

    public void dismissDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public boolean isLogin() {
        SpUtil sp = new SpUtil(this, Const.AL_LOGIN);
        return sp.getBoolValueFalse("isLogin");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
