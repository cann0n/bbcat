package com.sdkj.bbcat.activity.bracelet;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.*;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

/**
 * Created by ${Rhino} on 2016/2/23 18:00
 * 设置提醒时间
 */
public class SetNotifyActivity extends SimpleActivity implements NumberPicker.Formatter {

    @ViewInject(R.id.rl_set)
    RelativeLayout rl_set;

    @ViewInject(R.id.tv_time)
    TextView tv_time;

    @ViewInject(R.id.tv_finish)
    TextView tv_finish;

    @ViewInject(R.id.minuteicker)
    private NumberPicker minuteicker;

    @ViewInject(R.id.ll_set_time)
    LinearLayout ll_set_time;

    @ViewInject(R.id.hourpicker)
    private NumberPicker hourPicker;
    @ViewInject(R.id.valuepicker)
    private NumberPicker valuepicker;

    @ViewInject(R.id.tb_notify)
    ToggleButton tb_notify;

    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setBg("#FF6469").setTitle("疫苗提醒").showRight("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_time.getText().toString().length() == 0) {
                    toast("请先设置时间");
                    return;
                }
                setTime();
            }
        });
        init();
    }

    @OnClick(R.id.tv_time)
    void showTime(View view) {
        ll_set_time.setVisibility(View.VISIBLE);
        tv_finish.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_finish)
    void ok(View view) {
        tv_time.setText(((EditText) valuepicker.getChildAt(0)).getText().toString() + ((EditText) hourPicker.getChildAt(0)).getText().toString() + ":" + ((EditText) minuteicker.getChildAt(0)).getText().toString());
        tv_finish.setVisibility(View.GONE);
    }

    private void init() {
        hourPicker.setFormatter(this);
        hourPicker.setMaxValue(24);
        hourPicker.setMinValue(0);
        hourPicker.setValue(9);
        hourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        int count = hourPicker.getChildCount();

        minuteicker.setFormatter(this);
        minuteicker.setMaxValue(60);
        minuteicker.setMinValue(0);
        minuteicker.setValue(49);
        minuteicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        String[] city = {"疫苗前两天", "疫苗前一天", "疫苗当天"};
        valuepicker.setDisplayedValues(city);
        valuepicker.setMinValue(0);
        valuepicker.setMaxValue(city.length - 1);
        valuepicker.setValue(1);
        valuepicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    }

    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    public void setTime() {
        showDialog();
        PostParams params = new PostParams();
        if (tv_time.getText().toString().contains("两天")) {
            params.put("notice_day", "2");
        } else if (tv_time.getText().toString().contains("一天")) {
            params.put("notice_day", "1");
        } else {
            params.put("notice_day", "0");
        }
        HttpUtils.postJSONObject(activity, Const.SET_NOTIFY, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo resp = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (resp.isSuccess()) {
                    toast("设置成功");
                    SpUtil sp = new SpUtil(activity, Const.SP_NAME);
                    sp.setValue(Const.NOTIFY, tb_notify.isChecked());
                    sp.setValue(Const.NOTIFY_TIME,((EditText) hourPicker.getChildAt(0)).getText().toString() + ":" + ((EditText) minuteicker.getChildAt(0)).getText().toString());
                    finish();
                } else {
                    toast(resp.getMessage());
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
                toast("设置失败,请重试");
            }
        });
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_set_notify;
    }
}
