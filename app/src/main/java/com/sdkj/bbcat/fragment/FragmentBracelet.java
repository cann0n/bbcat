package com.sdkj.bbcat.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.BluetoothBle.BleIn_BService;
import com.sdkj.bbcat.BluetoothBle.BleOut_AUuidData;
import com.sdkj.bbcat.BluetoothBle.BleOut_CResponseClass;
import com.sdkj.bbcat.MainActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.adapter.DeviceListAdapter;
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

    private BleIn_BService.BleBinder mBinder = null;
    private ServiceConnection mConnect = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (BleIn_BService.BleBinder) service;
        }

        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    protected int setLayoutResID() {
        return R.layout.bracelet_fragment;
    }

    protected void setListener() {
        EventBus.getDefault().register(this);
//        Intent sIntent= new Intent(activity,BleIn_BService.class);
//        activity.bindService(sIntent, mConnect, Context.BIND_AUTO_CREATE);
        queryData();
        mScanBraceletBtn.setOnClickListener(this);
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
            activity.toast("后台开发中~");
//            if (mScanBraceletBtn.getText().toString().trim().equals("扫描手环"))
//                mBinder.startSearchDevices(activity, "咘咘猫请求打开蓝牙设备", null);
//            else if (mScanBraceletBtn.getText().toString().trim().equals("停止扫描"))
//                mBinder.stopSearchDevices();
//            else if (mScanBraceletBtn.getText().toString().trim().equals("断开连接"))
//                mBinder.disConDevice();
//            else if (mScanBraceletBtn.getText().toString().trim().equals("停止重连")) {
//                mBinder.stopReConDevice();
//                mScanBraceletState.setText("未连接");
//                mScanBraceletBtn.setText("扫描手环");
//            }
        }
    }

    public void onEventMainThread(BleOut_CResponseClass.BeginSearchBleBtClazz bsb) {
        mScanBraceletState.setText("未连接");
        mScanBraceletBtn.setText("停止扫描");
    }

    public void onEventMainThread(BleOut_CResponseClass.FinishSearchBleBtClazz fsb) {
        mScanBraceletState.setText("未连接");
        mScanBraceletBtn.setText("扫描手环");
    }

    private Boolean isConnectingDevice;

    public void onEventMainThread(final BleOut_CResponseClass.CompleteSearchBleBtClazz csb) {
        mScanBraceletState.setText("未连接");
        if (csb.getDeviceList().size() == 0) {
            mScanBraceletBtn.setText("扫描手环");
            Toast.makeText(activity, "没有扫描到任何相关设备,请重新扫描！", Toast.LENGTH_LONG).show();
        } else {
            isConnectingDevice = false;
            mScanBraceletBtn.setText("扫描完成");
            Toast.makeText(activity, "请选择一款扫描出的远端设备来进行连接！", Toast.LENGTH_LONG).show();
            final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();

            View view = LayoutInflater.from(activity).inflate(R.layout.inflater_devicelist, null);
            alertDialog.setContentView(view);
            ListView listView = (ListView) view.findViewById(R.id.devicelist_content);
            DeviceListAdapter adapter = DeviceListAdapter.getInstance(activity);
            adapter.setDeviceList(csb.getDeviceList());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mBinder.conDevice(csb.getDeviceList().get(position).getmDeviceAddress(), BleOut_AUuidData.SERVICE_UUID);
                    isConnectingDevice = true;
                    alertDialog.dismiss();
                }
            });

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            Window window = alertDialog.getWindow();
            window.setWindowAnimations(R.style.BlueToothOpenDialogAnim);
            WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
            params.width = displayMetrics.widthPixels / 5 * 3;
            params.height = displayMetrics.heightPixels / 5 * 3;
            params.gravity = Gravity.CENTER;
            alertDialog.getWindow().setAttributes(params);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    if (!isConnectingDevice) mScanBraceletBtn.setText("扫描手环");
                }
            });
        }
    }

    public void onEventMainThread(BleOut_CResponseClass.ConnectingBleBtClazz cbb) {
        mScanBraceletState.setText("未连接");
        mScanBraceletBtn.setText("正在连接");
    }

    public void onEventMainThread(BleOut_CResponseClass.ConnectedBleBtClazz cbb) {
        mScanBraceletState.setText("已连接");
        mScanBraceletBtn.setText("断开连接");
    }

    public void onEventMainThread(BleOut_CResponseClass.ConnectedFailBleBtClazz cfb) {
        mScanBraceletState.setText("未连接");
        mScanBraceletBtn.setText("扫描手环");
    }

    public void onEventMainThread(BleOut_CResponseClass.DisConnectingBleBtClazz dcb) {
        mScanBraceletState.setText("已连接");
        mScanBraceletBtn.setText("正在断开");
    }

    public void onEventMainThread(BleOut_CResponseClass.DisConnectedBleBtClazz dcb) {
        mScanBraceletState.setText("未连接");
        mScanBraceletBtn.setText("扫描手环");
    }

    public void onEventMainThread(BleOut_CResponseClass.DisConnectedFailBleBtClazz dcf) {
        mScanBraceletState.setText("已连接");
        mScanBraceletBtn.setText("断开连接");
    }

    public void onEventMainThread(BleOut_CResponseClass.ReConnectBleBtClazz rcb) {
        mScanBraceletState.setText("未连接");
        mScanBraceletBtn.setText("停止重连");
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}