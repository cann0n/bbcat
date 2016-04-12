package com.sdkj.bbcat.BluetoothBle;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothConnectActivity extends SimpleActivity {

    @Override
    public void initBusiness() {
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_bluetooth_connect;
    }

    @OnClick(R.id.tv_tips)
    void sendMsg(View view) {
    }

}
