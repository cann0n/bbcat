package com.sdkj.bbcat.activity.bracelet;

import android.os.SystemClock;
import android.text.InputType;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

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

    @ViewInject(R.id.tv_delete)
    private TextView tv_delete;

    @ViewInject(R.id.et_desc)
    private EditText et_desc;

    @ViewInject(R.id.et_num)
    private EditText et_num;

    @ViewInject(R.id.iv_time)
    private ImageView iv_time;

    @ViewInject(R.id.iv_input)
    private ImageView iv_input;

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

    @OnClick(R.id.iv_time)
    void startTime(View view) {
        if (isStart) {
            ct_long.stop();
            recordingTime = SystemClock.elapsedRealtime() - ct_long.getBase();
            isStart = false;
            iv_time.setImageResource(R.drawable.icon_jishi);
            rl_time.setVisibility(View.VISIBLE);
            int temp = (int) (recordingTime / 1000);
            if (temp > 60) {
                tv_time.setText(temp / 60 + "分钟" + temp % 60 + "秒");
            } else {
                tv_time.setText(temp + "秒");
            }
            iv_time.setVisibility(View.GONE);
            iv_input.setVisibility(View.VISIBLE);
            ct_long.setVisibility(View.GONE);
            recordingTime = 0;
        } else {
            isStart = true;
            ct_long.setBase(SystemClock.elapsedRealtime());
            ct_long.setVisibility(View.VISIBLE);
            //开始计时  
            ct_long.start();
            iv_time.setImageResource(R.drawable.icon_end_time);
            tv_delete.setVisibility(View.VISIBLE);
            ct_long.setVisibility(View.VISIBLE);
            start_time.setText(Utils.formatTime(System.currentTimeMillis() + ""));
            iv_input.setVisibility(View.GONE);
            tv_time.setText("");
        }
    }


    @OnClick(R.id.iv_input)
    void showInput(View view) {
        iv_input.setVisibility(View.GONE);
        iv_time.setVisibility(View.GONE);
        rl_desc.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_delete)
    void delete(View view) {
        if (rl_desc.isShown()) {
            rl_desc.setVisibility(View.GONE);
            iv_input.setVisibility(View.VISIBLE);
            et_desc.setText("");
            return;
        }
        if (rl_time.isShown()) {
            rl_time.setVisibility(View.GONE);
            iv_time.setVisibility(View.VISIBLE);
            tv_delete.setVisibility(View.GONE);
            iv_input.setVisibility(View.GONE);
            return;
        }
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
        tv_type.setText(name);
        start_time.setText(Utils.formatTime(System.currentTimeMillis() + ""));
        if (type != 1) {
            iv_input.setVisibility(View.GONE);
            tv_delete.setVisibility(View.GONE);
            iv_time.setVisibility(View.GONE);
            rl_desc.setVisibility(View.VISIBLE);
            rl_num.setVisibility(View.VISIBLE);
        }
        if (type == 4) {
            et_num.setInputType(InputType.TYPE_CLASS_TEXT);
            et_num.setHint("请输入食物");
        }
    }

    public void save() {
        if (Utils.isEmpty(tv_time.getText().toString().trim())) {
            toast("请先计时");
            return;
        }
        if (Utils.isEmpty(et_desc.getText().toString().trim())) {
            toast("请输入喂养情况");
            return;
        }
        final PostParams params = new PostParams();
        params.put("type", type + "");
        params.put("day", start_time.getText().toString());
        params.put("name", name);
        if (type == 1) {
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
