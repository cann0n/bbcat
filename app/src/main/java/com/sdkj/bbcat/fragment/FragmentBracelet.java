package com.sdkj.bbcat.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.BluetoothBle.CommandUtil;
import com.sdkj.bbcat.BluetoothBle.LightBLEService;
import com.sdkj.bbcat.BluetoothBle.RFLampDevice;
import com.sdkj.bbcat.BluetoothBle.SearchBluetoothActivity;
import com.sdkj.bbcat.BluetoothBle.SearchBluetoothResultActivity;
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

import de.greenrobot.event.EventBus;

/**
 * Created by Mr.Yuan on 2015/12/31 0031.
 */
public class FragmentBracelet extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.bra_bottomll)
    private LinearLayout bra_bottomll;

    @ViewInject(R.id.bra_connectionstate)
    private TextView mScanBraceletState;

    @ViewInject(R.id.bra_connectionstatebtn)
    private TextView mScanBraceletBtn;

    @ViewInject(R.id.bra_burncalories)
    private TextView bra_burncalories;

    private BroastRecevice recevicer;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            new Thread() {
                public void run() {
                    if (Tools.device == null) return;
                    Tools.device.reConnected();
                }
            }.start();
        }
    };

    @OnClick(R.id.bra_burncalories)
    void getKll(View view) {
        if (Tools.device != null && Tools.device.isConnected()) {
            //获取卡里路和温度
            Tools.device.sendUpdate(CommandUtil.getCaryRd());
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
            if("扫描手环".equals(mScanBraceletBtn.getText().toString())){
                activity.skip(SearchBluetoothActivity.class);
            } else  if("断开连接".equals(mScanBraceletBtn.getText().toString())){
                if(Tools.device!=null&&Tools.device.isConnected()){
                    Tools.device.disconnectedDevice();
                }
            }else if("重新连接".equals(mScanBraceletBtn.getText().toString())){
                if(Tools.device!=null&&!Tools.device.isConnected()){
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }
        }
    }

    public void onEventMainThread(BlueStatusChange status) {
        if (!Utils.isEmpty(status.getStatus())) {
            mScanBraceletBtn.setText(status.getStatus());
            mScanBraceletState.setText(status.getStatus());
        }
        if (!Utils.isEmpty(status.passWord)) {
            Tools.device = new RFLampDevice(activity, status.device);
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
        activity.unregisterReceiver(recevicer);
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
                        SearchBluetoothResultActivity.ConnectSuccess evnent = new SearchBluetoothResultActivity.ConnectSuccess();
                        evnent.passRight = true;
                        EventBus.getDefault().post(evnent);
                    } else if (result == 2) {
                        activity.toast("温度数据" + Tools.byte2Hex(value));
                        System.out.println("温度数据" + Tools.byte2Hex(value));
                    }
                }
            } else if (action.equals(LightBLEService.ACTION_GATT_CONNECTED)) {
                mScanBraceletState.setText("已连接");
                mScanBraceletBtn.setText("断开连接");
                SpUtil sp = new SpUtil(activity, Const.SP_NAME);
                if (!Utils.isEmpty(sp.getStringValue(Const.BLUE_PASS))) {
                    Tools.device.sendUpdate(CommandUtil.inputPass(sp.getStringValue(Const.BLUE_PASS)));
                }
            } else if (action.equals(LightBLEService.ACTION_GATT_DISCONNECTED)) {
                mScanBraceletState.setText("已经断开连接");
                mScanBraceletBtn.setText("重新连接");
                isConnected = false;
            } else if (action.equals("onDescriptorWrite")) {
                isConnected = true;
                SpUtil sp = new SpUtil(activity, Const.SP_NAME);
                if (!Utils.isEmpty(sp.getStringValue(Const.BLUE_PASS))) {
                    Tools.device.sendUpdate(CommandUtil.inputPass(sp.getStringValue(Const.BLUE_PASS)));
                }
                mScanBraceletState.setText("开启通知");
            } else if (action.equals(LightBLEService.ACTION_GATT_SERVICES_DISCOVERED)) {
                System.out.println("正在搜索服务");
            } else if (action.equals("正在连接")) {
                mScanBraceletState.setText("正在连接");

            }
        }

    }
}