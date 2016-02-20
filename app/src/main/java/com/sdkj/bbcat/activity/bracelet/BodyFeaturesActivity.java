package com.sdkj.bbcat.activity.bracelet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.BabyVo;
import com.sdkj.bbcat.bean.GrowthVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.HorizontalRulerView;
import com.sdkj.bbcat.widget.VerticalRulerView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by Mr.Yuan on 2016/1/7 0007.
 */
public class BodyFeaturesActivity extends SimpleActivity implements View.OnClickListener {
    @ViewInject(R.id.iv_left)
    private ImageView backImg;
    @ViewInject(R.id.iv_bt)
    private TextView timeTv;
    @ViewInject(R.id.iv_right)
    private TextView complete;
    @ViewInject(R.id.bodyfea_height)
    private TextView topHeight;
    @ViewInject(R.id.bodyfea_weight)
    private TextView topWeight;
    @ViewInject(R.id.bodyfea_head)
    private TextView topHead;

    @ViewInject(R.id.ruler_heightall)
    private RelativeLayout height_all;
    @ViewInject(R.id.ruler_heighttv)
    private TextView height_tv;
    @ViewInject(R.id.ruler_heightrl)
    private VerticalRulerView height_rl;

    @ViewInject(R.id.ruler_weightall)
    private RelativeLayout weight_all;
    @ViewInject(R.id.ruler_weighttv)
    private TextView weight_tv;
    @ViewInject(R.id.ruler_weightrl)
    private HorizontalRulerView weight_rl;

    @ViewInject(R.id.ruler_headall)
    private RelativeLayout head_all;
    @ViewInject(R.id.ruler_headtv)
    private TextView head_tv;
    @ViewInject(R.id.ruler_headrl)
    private HorizontalRulerView head_rl;
    private boolean isfirst = true;
    private GrowthVo.BobyState state;

     AlertDialog alertDialog;

    public int setLayoutResID() {
        return R.layout.activity_bodyfeatures;
    }

    public void initBusiness() {
        state = (GrowthVo.BobyState) getVo("0");
        if (state != null) state = new GrowthVo.BobyState();
        if (state.getMin_height() == 0) state.setMin_height(40);
        if (state.getMax_height() == 0) state.setMax_height(100);
        if (state.getMin_weight() == 0) state.setMin_weight(1);
        if (state.getMax_weight() == 0) state.setMax_weight(5);
        if (state.getMin_head() == 0) state.setMin_head(10);
        if (state.getMax_head() == 0) state.setMax_head(30);

        timeTv.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        height_rl.setOnScrollListener(new VerticalRulerView.onScrollListener() {
            public void onScroll(int currentValue) {
                topHeight.setText(((float) currentValue) / 10 + "");
                height_tv.setText(((float) currentValue) / 10 + "cm");
            }
        });

        weight_rl.setOnScrollListener(new HorizontalRulerView.onScrollListener() {
            public void onScroll(int currentValue) {
                topWeight.setText(((float) currentValue) / 10 + "");
                weight_tv.setText(((float) currentValue) / 10 + "kg");
            }
        });

        head_rl.setOnScrollListener(new HorizontalRulerView.onScrollListener() {
            public void onScroll(int currentValue) {
                topHead.setText(((float) currentValue) / 10 + "");
                head_tv.setText(((float) currentValue) / 10 + "cm");
            }
        });

        backImg.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        complete.setOnClickListener(this);
        topHeight.setOnClickListener(this);
        topWeight.setOnClickListener(this);
        topHead.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == backImg) {
            finish();
        } else if (v == timeTv) {
            String[] strSz = timeTv.getText().toString().trim().split("-");
            int year = Integer.valueOf(strSz[0]);
            int month = Integer.valueOf(strSz[1]);
            int day = Integer.valueOf(strSz[2]);

            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    timeTv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                }
            }, year, month - 1, day) {
                protected void onStop() {
                }
            };
//            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        } else if (v == complete) {
            if(alertDialog==null){
                alertDialog = new AlertDialog.Builder(activity).create();
            }
            if(alertDialog.isShowing()){
                return;
            }
            
            alertDialog.show();
            View view = LayoutInflater.from(activity).inflate(R.layout.inflater_babyinfos, null);
            alertDialog.setContentView(view);
            ImageView sgimg = (ImageView) view.findViewById(R.id.sh_sgimg);
            TextView sgtvup = (TextView) view.findViewById(R.id.sh_sgtvup);
            TextView sgtvdown = (TextView) view.findViewById(R.id.sh_sgtvdown);
            ImageView tzimg = (ImageView) view.findViewById(R.id.sh_tzimg);
            TextView tztvup = (TextView) view.findViewById(R.id.sh_tztvup);
            TextView tztvdown = (TextView) view.findViewById(R.id.sh_tztvdown);
            ImageView twimg = (ImageView) view.findViewById(R.id.sh_twimg);
            TextView twtvup = (TextView) view.findViewById(R.id.sh_twtvup);
            TextView twtvdown = (TextView) view.findViewById(R.id.sh_twtvdown);
            TextView btn = (TextView) view.findViewById(R.id.sh_btn);

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showDialog();
                    final PostParams params = new PostParams();
                    params.put("day", timeTv.getText().toString());
                    params.put("height", topHeight.getText().toString());
                    params.put("weight", topWeight.getText().toString());
                    params.put("head", topHead.getText().toString());

                    HttpUtils.postJSONObject(activity, Const.SetBabyFeatureInfos, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
                        public void getResp(JSONObject jsonObject) {
                            dismissDialog();
                            RespVo<BabyVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                            if (respVo.isSuccess()) {
                                alertDialog.dismiss();
                                EventBus.getDefault().post(new FootPrintActivity.RefreshEvent());
                                finish();
                            } else {
                                activity.toast(respVo.getMessage());
                            }
                        }

                        public void doFailed() {
                            toast("提交资料失败");
                            dismissDialog();
                        }
                    });
                }
            });

            sgtvup.setText("身高:" + topHeight.getText().toString().trim() + "cm");
            if (Float.valueOf(topHead.getText().toString()) < Float.valueOf(state.getMin_height())) {
                sgimg.setBackgroundResource(R.drawable.zhuyi_orange);
                sgtvdown.setText("低于正常范围之内，请加强宝宝的饮食营养!");
            } else if (Float.valueOf(topHead.getText().toString()) > Float.valueOf(state.getMax_height())) {
                sgimg.setBackgroundResource(R.drawable.zhuyi_orange);
                sgtvdown.setText("高于正常范围之内，请减弱宝宝的饮食营养!");
            } else {
                sgimg.setBackgroundResource(R.drawable.zhengchang_green);
                sgtvdown.setText("在正常范围之内。");
            }

            tztvup.setText("体重:" + topWeight.getText().toString().trim() + "kg");
            if (Float.valueOf(topWeight.getText().toString().trim()) < Float.valueOf(state.getMin_weight())) {
                tzimg.setBackgroundResource(R.drawable.zhuyi_orange);
                tztvdown.setText("低于正常范围之内，请加强宝宝的饮食营养!");
            } else if (Float.valueOf(topWeight.getText().toString().trim()) > Float.valueOf(state.getMax_weight())) {
                tzimg.setBackgroundResource(R.drawable.zhuyi_orange);
                tztvdown.setText("高于正常范围之内，请减弱宝宝的饮食营养!");
            } else {
                tzimg.setBackgroundResource(R.drawable.zhengchang_green);
                tztvdown.setText("在正常范围之内。");
            }

            twtvup.setText("头围:" + topHead.getText().toString().trim() + "cm");
            if (Float.valueOf(topHead.getText().toString().trim()) < Float.valueOf(state.getMin_head())) {
                twimg.setBackgroundResource(R.drawable.zhuyi_orange);
                twtvdown.setText("低于正常范围之内，请加强宝宝的饮食营养!");
            } else if (Float.valueOf(topHead.getText().toString().trim()) > Float.valueOf(state.getMax_head())) {
                twimg.setBackgroundResource(R.drawable.zhuyi_orange);
                twtvdown.setText("高于正常范围之内，请减弱宝宝的饮食营养!");
            } else {
                twimg.setBackgroundResource(R.drawable.zhengchang_green);
                twtvdown.setText("在正常范围之内。");
            }
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            Window window = alertDialog.getWindow();
            window.setWindowAnimations(R.style.BabyInfosDialogAnim);
            WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
            params.width = displayMetrics.widthPixels;
            params.height = alertDialog.getWindow().getAttributes().height;
            params.gravity = Gravity.BOTTOM;
            alertDialog.getWindow().setAttributes(params);

        } else if (v == topHeight) {
            height_all.setVisibility(View.VISIBLE);
            weight_all.setVisibility(View.GONE);
            head_all.setVisibility(View.GONE);
        } else if (v == topWeight) {
            height_all.setVisibility(View.GONE);
            weight_all.setVisibility(View.VISIBLE);
            head_all.setVisibility(View.GONE);
        } else if (v == topHead) {
            height_all.setVisibility(View.GONE);
            weight_all.setVisibility(View.GONE);
            head_all.setVisibility(View.VISIBLE);
        }
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        height_rl.initView(height_all.getHeight());
        weight_rl.initView(height_all.getWidth());
        head_rl.initView(height_all.getWidth());
    }
}
