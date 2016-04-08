package com.sdkj.bbcat.BluetoothBle;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SearchBluetoothResultActivity extends SimpleActivity {

    @ViewInject(R.id.ll_container)
    LinearLayout ll_container;

    private HashMap<String, BluetoothDevice> listDevice;
    private HashMap<String, Integer> signals;

    @Override
    public void initBusiness() {
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
                    final EditText et = new EditText(activity);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("请输入密码").setIcon(android.R.drawable.ic_dialog_info).
                            setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toast(et.getText().toString());
                            Intent intent = new Intent();
                            intent.putExtra("0", device);
                            intent.putExtra("1", et.getText().toString());
                            intent.setClass(activity, BluetoothConnectActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("取消", null).show();
                }
            });
            ll_container.addView(view);
        }
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_search_blue_resutlt;
    }
}
