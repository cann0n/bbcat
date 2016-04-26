package com.sdkj.bbcat.BluetoothBle;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.fragment.FragmentBracelet;
import com.sdkj.bbcat.widget.TitleBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.greenrobot.event.EventBus;

public class SearchBluetoothResultActivity extends SimpleActivity {

    @ViewInject(R.id.ll_container)
    LinearLayout ll_container;

    private HashMap<String, BluetoothDevice> listDevice;
    private HashMap<String, Integer> signals;
    private String lastName = "";

    @Override
    public void initBusiness() {
        EventBus.getDefault().register(this);
        new TitleBar(activity).back().setTitle("扫描结果");
        Intent intent = getIntent();
        listDevice = (HashMap<String, BluetoothDevice>) intent.getSerializableExtra("0");
        signals = (HashMap<String, Integer>) intent.getSerializableExtra("1");
        Iterator it = listDevice.keySet().iterator();
        while (it.hasNext()) {
            final BluetoothDevice device = listDevice.get(it.next());
            View view = makeView(R.layout.item_blue_result);
            ImageView image = (ImageView) view.findViewById(R.id.iv_signal);
            TextView tv_device = (TextView) view.findViewById(R.id.tv_device);
            TextView tv_select = (TextView) view.findViewById(R.id.tv_select);
            tv_device.setText(device.getName());
            int sin = signals.get(device.getAddress()) + 100;
            if (sin < 20) {
                image.setImageResource(R.drawable.icon_signal_4);
            } else if (sin > 20 && sin < 50) {
                image.setImageResource(R.drawable.icon_signal_3);
            } else {
                image.setImageResource(R.drawable.icon_signal_2);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    MD_
                    if (!Utils.isEmpty(device.getName())&&!device.getName().startsWith("Baby")) {
                        toast("请选择手环设备进行链接");
                        return;
                    }
                    final EditText et = (EditText) makeView(R.layout.view_blue_pass);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("请输入密码").
                            setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (et.getText().toString().length() < 6) {
                                toast("请输入6位密码");
                                return;
                            }
                            FragmentBracelet.BlueStatusChange change = new FragmentBracelet.BlueStatusChange();
                            lastName = device.getName();
                            change.passWord = et.getText().toString();
                            change.device = device;
                            EventBus.getDefault().post(change);
                            SpUtil sp = new SpUtil(activity, Const.SP_NAME);
                            sp.setValue(Const.BLUE_PASS, et.getText().toString());
                        }
                    }).setNegativeButton("取消", null).show();
                }
            });
            ll_container.addView(view);
        }
    }

    public void onEventMainThread(ConnectSuccess status) {
        if (status.passRight) {
            toast("已链接手环");
            finish();
        } else {
            toast("密码错误,请重试");
        }
    }

    public static class ConnectSuccess {
        public boolean passRight;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_search_blue_resutlt;
    }
}
