package com.sdkj.bbcat.BluetoothBle;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by ${Rhino} on 2016/4/20 15:26
 */
public class BlueSettingsActivity extends SimpleActivity {

    @ViewInject(R.id.tb_1)
    private CheckBox tb_1;

    @ViewInject(R.id.tb_2)
    private CheckBox tb_2;

    @ViewInject(R.id.tb_3)
    private CheckBox tb_3;

    @ViewInject(R.id.tb_4)
    private CheckBox tb_4;
    SpUtil sp;

    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("手环设置");
        sp = new SpUtil(activity, Const.SP_NAME);
        tb_1.setChecked(sp.getBoolValue(Const.NOTIFY_1));
        tb_2.setChecked(sp.getBoolValue(Const.NOTIFY_2));
        tb_3.setChecked(sp.getBoolValue(Const.NOTIFY_3));
        tb_4.setChecked(sp.getBoolValue(Const.NOTIFY_4));
    }
        
    @OnClick(R.id.rl_1)
    void click1(View view){
        if (tb_1.isChecked()) {
            tb_1.setChecked(false);
            sp.setValue(Const.NOTIFY_1, false);
        } else {
            tb_1.setChecked(true);
            sp.setValue(Const.NOTIFY_1, true);
        } 
    }
    @OnClick(R.id.rl_2)
    void click2(View view){
        if (tb_2.isChecked()) {
            tb_2.setChecked(false);
            sp.setValue(Const.NOTIFY_2, false);
        } else {
            tb_2.setChecked(true);
            sp.setValue(Const.NOTIFY_2, true);
        }
    }
    @OnClick(R.id.rl_3)
    void click3(View view){
        if (tb_3.isChecked()) {
            tb_3.setChecked(false);
            sp.setValue(Const.NOTIFY_3, false);
        } else {
            tb_3.setChecked(true);
            sp.setValue(Const.NOTIFY_3, true);
        }
    }
    @OnClick(R.id.rl_4)
    void click4(View view){
        if (tb_4.isChecked()) {
            tb_4.setChecked(false);
            sp.setValue(Const.NOTIFY_4, false);
        } else {
            tb_4.setChecked(true);
            sp.setValue(Const.NOTIFY_4, true);
        } 
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_blue_settings;
    }
}
