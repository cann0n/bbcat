package com.sdkj.bbcat.BluetoothBle;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SearchBluetoothActivity extends SimpleActivity {


    private static int TIME_OUT_SCAN = 10000;

    private ConcurrentHashMap<String, BluetoothDevice> listDevice = new ConcurrentHashMap<String, BluetoothDevice>();
    private ConcurrentHashMap<String, Integer> signals = new ConcurrentHashMap<>();

    @ViewInject(R.id.infoOperating)
    private ImageView infoOperating;

    private Handler mHandler;

    private BluetoothAdapter mBluetoothAdapter;

    private boolean mScanning;

    @Override
    public void initBusiness() {
        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            toast("抱歉你的手机不支持蓝牙设备");
            finish();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            toast("抱歉你的手机不支持蓝牙设备");
            finish();
            return;
        }
        scanLeDevice(true);
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.progress_small);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        infoOperating.startAnimation(operatingAnim);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                    Intent intent = new Intent();
                    intent.setClass(activity, SearchBluetoothResultActivity.class);
                    intent.putExtra("0", listDevice);
                    intent.putExtra("1", signals);
                    startActivity(intent);
                    finish();
                }
            }, TIME_OUT_SCAN);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            mScanning = true;
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            listDevice.put(device.getAddress(), device);
            signals.put(device.getAddress(), rssi);
        }
    };

    @Override
    public int setLayoutResID() {
        return R.layout.activity_search_bluetooth;
    }

    @OnClick(R.id.iv_back)
    void back(View view) {
        infoOperating.clearAnimation();
        finish();
    }

}
