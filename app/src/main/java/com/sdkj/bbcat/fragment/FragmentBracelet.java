package com.sdkj.bbcat.fragment;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.easemob.chat.EMGroupManager;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.BluetoothBle.Bledevice;
import com.sdkj.bbcat.BluetoothBle.CommandUtil;
import com.sdkj.bbcat.BluetoothBle.LightBLEService;
import com.sdkj.bbcat.BluetoothBle.RFLampDevice;
import com.sdkj.bbcat.BluetoothBle.SearchBluetoothActivity;
import com.sdkj.bbcat.BluetoothBle.SearchBluetoothResultActivity;
import com.sdkj.bbcat.BluetoothBle.StringHexUtils;
import com.sdkj.bbcat.BluetoothBle.Tools;
import com.sdkj.bbcat.MainActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.BraceletBotVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by Mr.Yuan on 2015/12/31 0031.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class FragmentBracelet extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.bra_bottomll)
    private LinearLayout bra_bottomll;

    @ViewInject(R.id.bra_connectionstate)
    private TextView mScanBraceletState;

    @ViewInject(R.id.bra_temperature)
    private TextView bra_temperature;

    @ViewInject(R.id.bra_connectionstatebtn)
    private TextView mScanBraceletBtn;

    @ViewInject(R.id.bra_burncalories)
    private TextView bra_burncalories;

    @ViewInject(R.id.bra_datasjilu)
    private ImageView bra_datasjilu;

    private BroastRecevice recevicer;

    UploadLocalReceiver receiver;

    private static BluetoothDevice selectDevice;

    private static String tempPass = "";

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            new Thread() {
                public void run() {
//                    Tools.device = new RFLampDevice(activity, selectDevice);
//                    Tools.device.reConnected();
                            if(Tools.device!=null){
                                Tools.device.reConnected();
                            }
                }
            }.start();
        }
    };

    @OnClick(R.id.bra_burncalories)
    void getKll(View view) {
        if (Tools.device != null && Tools.device.isConnected()) {
            //获取卡里路和温度
            sendCommands();
        }
    }

    protected int setLayoutResID() {
        return R.layout.bracelet_fragment;
    }

    protected void setListener() {
        EventBus.getDefault().register(this);
        queryData();
        mScanBraceletBtn.setOnClickListener(this);

        recevicer = new BroastRecevice();

        IntentFilter filter = new IntentFilter();
        filter.addAction(LightBLEService.ACTION_DATA_AVAILABLE);
        filter.addAction(LightBLEService.ACTION_GATT_CONNECTED);
        filter.addAction(LightBLEService.ACTION_GATT_DISCONNECTED);
        filter.addAction(LightBLEService.ACTION_GATT_SERVICES_DISCOVERED);
        filter.addAction("onDescriptorWrite");
        filter.addAction("正在连接");
        activity.registerReceiver(recevicer, filter);


        receiver = new UploadLocalReceiver();

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(Const.ACTION_UPLOAD_CAR_LOCAL);

        activity.registerReceiver(receiver, filter2);

    }

    private void queryData() {
        HttpUtils.postJSONObject(activity, Const.GetBraBotDates, SimpleUtils.buildUrl(activity, new PostParams()), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject obj) {
                ((MainActivity) activity).dismissDialog();
                RespVo<BraceletBotVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    BraceletBotVo value = respVo.getData(obj, BraceletBotVo.class);
                    if (!Utils.isEmpty(value.getNewest())) {
                        for (final NewsVo newsVo : value.getNewest()) {
                            View view = activity.makeView(R.layout.item_recommend);
                            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                            TextView tv_come_form = (TextView) view.findViewById(R.id.tv_come_form);
                            TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(iv_image);
                            tv_title.setText(newsVo.getTitle());
                            tv_come_form.setText(newsVo.getCategory_name());
                            tv_count.setText(newsVo.getView() + "");
                            view.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    activity.skip(NewsDetailActivity.class, newsVo.getId());
                                }
                            });
                            view.setBackgroundResource(R.color.color_white);
                            bra_bottomll.addView(view);
                        }
                    }
                }
            }

            public void doFailed() {
                ((MainActivity) activity).dismissDialog();
            }
        });
    }

    public void onClick(View v) {
        if (v == mScanBraceletBtn) {
            if ("扫描手环".equals(mScanBraceletBtn.getText().toString())) {
                activity.skip(SearchBluetoothActivity.class);
            } else if ("断开连接".equals(mScanBraceletBtn.getText().toString())) {
                if (Tools.device != null && Tools.device.isConnected()) {
                    Tools.device.disconnectedDevice();
                }
                mScanBraceletBtn.setText("重新连接");
            } else if ("重新连接".equals(mScanBraceletBtn.getText().toString())) {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
                mScanBraceletBtn.setText("正在重连");
            }
        }
    }

    public void onEventMainThread(BlueStatusChange status) {
        if (!Utils.isEmpty(status.getStatus())) {
            mScanBraceletBtn.setText(status.getStatus());
            mScanBraceletState.setText(status.getStatus());
        }
        if (!Utils.isEmpty(status.passWord)) {
            tempPass = status.passWord;
            selectDevice = status.device;
            if(Tools.device==null){
                Tools.device = new RFLampDevice(activity, status.device);
            }
            Tools.device.sendUpdate(CommandUtil.inputPass(tempPass));
        }
    }

    public static class BlueStatusChange {
        String status;
        public String passWord;
        public BluetoothDevice device;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

        if (isConnected) {
            Tools.device.disconnectedDevice();
        }
        if (receiver != null) {
            activity.unregisterReceiver(receiver);
        }
        if (recevicer != null) {
            activity.unregisterReceiver(recevicer);
        }
    }

    boolean isConnected;
    private int sCount;
    private int fCount;
    private int recevCount;

    class BroastRecevice extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();

            byte[] value = intent.getByteArrayExtra(LightBLEService.EXTRA_DATA);
            if (action.equals(LightBLEService.ACTION_DATA_AVAILABLE)) {
                if (value != null) {
                    int result = CommandUtil.getResultMsg(Tools.byte2Hex(value));
                    if (result == 0) {
                        SearchBluetoothResultActivity.ConnectSuccess evnent = new SearchBluetoothResultActivity.ConnectSuccess();
                        evnent.passRight = false;
                        EventBus.getDefault().post(evnent);
                    } else if (result == 1) {
                        SpUtil sp = new SpUtil(activity, Const.SP_NAME);
                        sp.setValue(Const.BLUE_PASS, tempPass);
                        SearchBluetoothResultActivity.ConnectSuccess evnent = new SearchBluetoothResultActivity.ConnectSuccess();
                        evnent.passRight = true;
                        EventBus.getDefault().post(evnent);
                        //链接成功发送指令集
                        sendCommands();
                    } else if (result == 2) {
                        String tempData = StringHexUtils.Bytes2HexString(value);
                        System.out.println("tempData = " + tempData);
                    } else if (result == 3) {
                        String tempData = StringHexUtils.Bytes2HexString(value);

                        double t = Double.parseDouble(new BigInteger(tempData.substring(16, 20), 16).toString()) / 10;
                        double j = Double.parseDouble(new BigInteger(tempData.substring(21, 25), 16).toString()) / 10;
                        bra_temperature.setText(t + "");
                        bra_burncalories.setText(j + "");

//                        if (sin < 20) {
//                            image.setImageResource(R.drawable.icon_signal_4);
//                        } else if (sin > 20 && sin < 50) {
//                            image.setImageResource(R.drawable.icon_signal_3);
//                        } else {
//                            image.setImageResource(R.drawable.icon_signal_2);
//                        }
                    }
                }
            } else if (action.equals(LightBLEService.ACTION_GATT_CONNECTED)) {
                mScanBraceletState.setText("已连接");
                mScanBraceletBtn.setText("断开连接");
                SpUtil sp = new SpUtil(activity, Const.SP_NAME);
                if (!Utils.isEmpty(sp.getStringValue(Const.BLUE_PASS))) {
                    Tools.device.sendUpdate(CommandUtil.inputPass(tempPass));
                }
            } else if (action.equals(LightBLEService.ACTION_GATT_DISCONNECTED)) {
                mScanBraceletState.setText("已经断开连接");
                mScanBraceletBtn.setText("重新连接");
                isConnected = false;
            } else if (action.equals("onDescriptorWrite")) {
                isConnected = true;
//                if (!Utils.isEmpty(tempPass)) {
//                    Tools.device.sendUpdate(CommandUtil.inputPass(tempPass));
//                }
                mScanBraceletState.setText("开启通知");
            } else if (action.equals(LightBLEService.ACTION_GATT_SERVICES_DISCOVERED)) {
                System.out.println("正在搜索服务");
            } else if (action.equals("正在连接")) {
                mScanBraceletState.setText("正在连接");

            }
        }
    }

    private void sendCommands() {

        //设置自动温度和活动量检测使能
        Tools.device.sendUpdate(CommandUtil.setCaryRd());
        //获取温度和活动量
        Tools.device.sendUpdate(CommandUtil.getTemperature());
        Tools.device.sendUpdate(CommandUtil.getCaryRd());
        
    }

    class UploadLocalReceiver extends BroadcastReceiver {

        private void init(final Context context) {
            if (Tools.device != null) {
                sendCommands();
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Const.ACTION_UPLOAD_CAR_LOCAL.equals(action)) {
                if (SimpleUtils.isLogin(context)) {
//                    init(context);
//                    scanLeDevice();
                }
            }

        }
    }

    Map<String, Integer> signals = new HashMap<>();

    private void scanLeDevice() {
        if (Tools.device == null) {
            return;
        }
        BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                if (signals.size() > 0) {
                    int sin = signals.get(Tools.device.deviceMac) + 100;
                    if (sin < 20) {
                        bra_datasjilu.setImageResource(R.drawable.icon_signal_4);
                    } else if (sin > 20 && sin < 50) {
                        bra_datasjilu.setImageResource(R.drawable.icon_signal_3);
                    } else {
                        bra_datasjilu.setImageResource(R.drawable.icon_signal_2);
                    }
                }
            }
        }, 3);
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            signals.put(device.getAddress(), rssi);
        }
    };

}