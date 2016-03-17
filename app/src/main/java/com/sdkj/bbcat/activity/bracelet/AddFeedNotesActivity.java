package com.sdkj.bbcat.activity.bracelet;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.FeedInoVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TimeDateSelectView.DatePicker;
import com.sdkj.bbcat.widget.TimeDateSelectView.TimePicker;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * Created by Mr.Yuan on 2016/1/12 0012.
 */
public class AddFeedNotesActivity extends SimpleActivity {
    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    @ViewInject(R.id.tv_type)
    private TextView tv_type;

    @ViewInject(R.id.start_time)
    private TextView start_time;

    @ViewInject(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    @ViewInject(R.id.tv_delete)
    private TextView tv_delete;

    @ViewInject(R.id.tv_num)
    private TextView tv_num;

    @ViewInject(R.id.et_desc)
    private EditText et_desc;

    @ViewInject(R.id.et_num)
    private EditText et_num;

    @ViewInject(R.id.time_start)
    private ImageView time_start;

    @ViewInject(R.id.time_end)
    private ImageView time_end;

    @ViewInject(R.id.rl_time)
    RelativeLayout rl_time;

    @ViewInject(R.id.rl_desc)
    RelativeLayout rl_desc;

    @ViewInject(R.id.rl_num)
    LinearLayout rl_num;

    @ViewInject(R.id.ct_long)
    Chronometer ct_long;

    private boolean isStart = false;
    private long recordingTime = 0;// 记录下来的总时间

    private int type = 0;
    String name = "";

    private View view;
    private Calendar calendar;
    private DatePicker dp_test;
    private TimePicker tp_test;
    private TextView tv_ok, tv_cancel;    //确定、取消button
    private PopupWindow pw;
    private String selectDate, selectTime;
    //选择时间与当前时间，用于判断用户选择的是否是以前的时间
    private int currentHour, currentMinute, currentDay, selectHour, selectMinute, selectDay;

    private long time = 0;

    FeedInoVo.FeedInfo vo;

    @OnClick(R.id.time_start)
    void startTime(View view) {
        if (!isStart) {
            isStart = true;
            ct_long.setBase(SystemClock.elapsedRealtime());
            ct_long.setVisibility(View.VISIBLE);
            //开始计时  
            ct_long.start();
            start_time.setText(Utils.formatTime(System.currentTimeMillis() + ""));
            tv_time.setText("");
        }
    }

    @OnClick(R.id.time_end)
    void endTime(View view) {
        if (isStart) {
            ct_long.stop();
            recordingTime = SystemClock.elapsedRealtime() - ct_long.getBase();
            isStart = false;
            int temp = (int) (recordingTime / 1000);
            if (temp > 60) {
                tv_time.setText(temp / 60 + "分钟" + temp % 60 + "秒");
            } else {
                tv_time.setText(temp + "秒");
            }
            ct_long.setVisibility(View.GONE);
            recordingTime = 0;
        }
    }

    @OnClick(R.id.start_time)
    void selectTime(View view) {
        showTimePopup();
    }

    private void showTimePopup() {
        calendar = Calendar.getInstance();
        view = makeView(R.layout.dialog_select_time);
        selectDate = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
//                + DatePicker.getDayOfWeekCN(calendar.get(Calendar.DAY_OF_WEEK));
        //选择时间与当前时间的初始化，用于判断用户选择的是否是以前的时间，如果是，弹出toss提示不能选择过去的时间
        selectDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectMinute = currentMinute = calendar.get(Calendar.MINUTE);
        selectHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        selectTime = currentHour + ":" + ((currentMinute < 10) ? ("0" + currentMinute) : currentMinute);
        dp_test = (DatePicker) view.findViewById(R.id.dp_test);
        tp_test = (TimePicker) view.findViewById(R.id.tp_test);
        tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        //设置滑动改变监听器
        dp_test.setOnChangeListener(dp_onchanghelistener);
        tp_test.setOnChangeListener(tp_onchanghelistener);
        pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//				//设置这2个使得点击pop以外区域可以去除pop
//				pw.setOutsideTouchable(true);
//				pw.setBackgroundDrawable(new BitmapDrawable());
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        pw.setBackgroundDrawable(colorDrawable);
        pw.setTouchable(true);
        pw.setOutsideTouchable(true);
        //出现在布局底端
        pw.showAtLocation(rl_bottom, 0, 0, Gravity.END);

        //点击确定
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                if(selectDay == currentDay ){	//在当前日期情况下可能出现选中过去时间的情况
//                    if(selectHour < currentHour){
//                        toast("不能选择过去的时间\n" +
//                                "   请重新选择");
//                        return;
//                    }else if( (selectHour == currentHour) && (selectMinute < currentMinute) ){
//                        toast("不能选择过去的时间\n" +
//                                "   请重新选择");
//                        return;
//                    }else {
//                        time= SimpleUtils.getTimeStamp(selectDate + " " + selectTime);
//                        tv_time.setText(selectDate +" "+ selectTime);
//                        pw.dismiss();
//                        return;
//                    }
//                }
                time = SimpleUtils.getTimeStamp(selectDate + " " + selectTime);
//                if (time<System.currentTimeMillis()/1000){
//                    toast("不能选择过去的时间\n" +
//                            "   请重新选择");
//                    time=0;
//                    return;
//                }
//                start_time.setText(selectDate +" "+ selectTime);
                start_time.setText(Utils.formatTime(time + "000"));

                pw.dismiss();
            }
        });

        //点击取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                pw.dismiss();
            }
        });
    }

    //listeners
    DatePicker.OnChangeListener dp_onchanghelistener = new DatePicker.OnChangeListener() {
        @Override
        public void onChange(int year, int month, int day) {
            selectDay = day;
            selectDate = year + "-" + ((month < 10) ? ("0" + month) : month) + "-" + ((day < 10) ? ("0" + day) : day);
//            + DatePicker.getDayOfWeekCN(day_of_week);
        }
    };
    TimePicker.OnChangeListener tp_onchanghelistener = new TimePicker.OnChangeListener() {
        @Override
        public void onChange(int hour, int minute) {
            selectTime = ((hour < 10) ? ("0" + hour) : hour) + ":" + ((minute < 10) ? ("0" + minute) : minute);
            selectHour = hour;
            selectMinute = minute;
        }
    };


    @OnClick(R.id.tv_delete)
    void delete(View view) {
        if (vo != null) {
            delete();
        } else {
            finish();
        }
    }

    private void delete() {
        showDialog();
        PostParams params = new PostParams();
        params.put("id", vo.getId() + "");
        HttpUtils.postJSONObject(activity, Const.DELETE_FEED_RECORD, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    EventBus.getDefault().post(new FeedNotesActivity.FreshEvent());
                    finish();
                } else {
                    toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
            }
        });
    }


    public int setLayoutResID() {
        return R.layout.activity_addfeednote;
    }

    public void initBusiness() {
        new TitleBar(activity).setTitle("新建记录").back().setBg("#FF6469").showRight("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        type = (int) getVo("0");
        name = (String) getVo("1");
        start_time.setText(Utils.formatTime(System.currentTimeMillis() + ""));
        if(getVo("2")!=null){
            vo= (FeedInoVo.FeedInfo) getVo("2");
            if(type==3){
                rl_time.setVisibility(View.VISIBLE);
                tv_time.setText(vo.getNum());
            }else {
                rl_time.setVisibility(View.GONE);
                et_num.setText(vo.getNum());
            }
            start_time.setText(vo.getDay());
            tv_type.setText(vo.getName());
            et_desc.setText(vo.getDesc());
        }
        tv_type.setText(name);
        if (type != 3) {
            ct_long.setVisibility(View.GONE);
            time_start.setVisibility(View.GONE);
            time_end.setVisibility(View.GONE);
        }
        if (type == 4) {
            tv_num.setText("食物");
            et_num.setInputType(InputType.TYPE_CLASS_TEXT);
            et_num.setHint("请输入食物");
        }
        if(type==3){
            rl_time.setVisibility(View.VISIBLE);
        }
    }

    public void save() {
        if (Utils.isEmpty(tv_time.getText().toString().trim())) {
            toast("请先计时");
            return;
        }
//        if (Utils.isEmpty(et_desc.getText().toString().trim())) {
//            toast("请输入喂养情况");
//            return;
//        }
        final PostParams params = new PostParams();
        if(vo!=null){
            params.put("id",vo.getId()+"");
        }
        params.put("type", type + "");
        params.put("day", start_time.getText().toString());
        params.put("name", name);
        if (type == 3) {
            params.put("num", tv_time.getText().toString());
        } else {
            if (Utils.isEmpty(et_num.getText().toString())) {
                if (type == 4) {
                    toast("请输入食物");
                } else {
                    toast("请输入计量");
                }
                return;
            }
            if (type == 4) {
                params.put("num", et_num.getText().toString());
            } else {
                params.put("num", et_num.getText().toString() + "ml");
            }
        }
        params.put("desc", et_desc.getText().toString().trim());
        showDialog();
        HttpUtils.postJSONObject(activity, Const.SetFeedAndIno, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo<FeedInoVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    EventBus.getDefault().post(new FeedNotesActivity.FreshEvent());
                    toast("记录成功");
                    finish();
                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            public void doFailed() {
                dismissDialog();
                activity.toast("链接服务器失败");
            }
        });
    }
}
