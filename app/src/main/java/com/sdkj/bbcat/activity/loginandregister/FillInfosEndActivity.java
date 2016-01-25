package com.sdkj.bbcat.activity.loginandregister;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mr.Yuan on 2015/12/27 0027.
 */
public class FillInfosEndActivity extends SimpleActivity {
    @ViewInject(R.id.infos_img)
    private ImageView img;
    @ViewInject(R.id.infos_time)
    private TextView time;
    private int state = 0;
    private int sex = 0;
    private String timeStr = "";
    private String[] hbStrSz;

    private Double mLatitude = 0d;
    private Double mLongitude = 0d;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_fillinfosend;
    }

    @Override
    public void initBusiness() {
        TitleBar titleBar = new TitleBar(activity).setTitle("宝宝生日").back().showRight("完成", new View.OnClickListener() {
            public void onClick(View v) {
                PostParams params = new PostParams();
                params.put("baby_status", state + "");
                params.put("sex", sex + "");
                params.put("birthday", timeStr);

                SpUtil sp = new SpUtil(activity, Const.SP_NAME);
                params.put("mobile", sp.getStringValue(Const.PHONE));
                if (mLatitude != 0 && mLongitude != 0) {
                    params.put("lat", mLatitude + "");
                    params.put("lng", mLongitude + "");
                }
                HttpUtils.postJSONObject(activity, Const.SaveUserInfos, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
                    public void getResp(JSONObject obj) {
                        dismissDialog();
                        RespVo<String> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                        if (respVo.isSuccess()) {
                            try {
                                SpUtil sp_login = new SpUtil(activity, Const.SP_NAME);
                                sp_login.setValue("sex", sex + "");
                                sp_login.setValue("birthday", timeStr);
                                sp_login.setValue("baby_status", state + "");
                                toast("恭喜注册完成,请登录");
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            activity.toast(respVo.getMessage());
                        }
                    }

                    public void doFailed() {
                    }
                });
            }
        });
        hbStrSz = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())).split("-");
        timeStr = hbStrSz[0] + "-" + hbStrSz[1] + "-" + hbStrSz[2];
        time.setText(hbStrSz[0] + "年" + hbStrSz[1] + "月" + hbStrSz[2] + "日");
        state = (int) getVo("0");
        sex = (int) getVo("1");
        if (sex == 1) {
            img.setBackgroundResource(R.drawable.nan);
        } else if (sex == 2) {
            img.setBackgroundResource(R.drawable.nv);
        }

        startBaiduLocation();
    }

    @OnClick(R.id.infos_time)
    void selectTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int year;
        final int month;
        int day;

        if (hbStrSz != null && hbStrSz.length == 3) {
            year = Integer.valueOf(hbStrSz[0]);
            month = Integer.valueOf(hbStrSz[1]);
            day = Integer.valueOf(hbStrSz[2]);
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                timeStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                time.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
            }
        }, year, month - 1, day) {
            protected void onStop() {
            }
        };
        datePickerDialog.show();
    }

    private void startBaiduLocation() {
        LocationClient mLocationClient = new LocationClient(activity.getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener(mLocationClient));    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private class MyLocationListener implements BDLocationListener {
        LocationClient mLocationClient;

        public MyLocationListener(LocationClient mLocationClient) {
            this.mLocationClient = mLocationClient;
        }

        public void onReceiveLocation(BDLocation location) {
            if (!Utils.isEmpty(location.getLatitude() + "") && !Utils.isEmpty(location.getLongitude() + "")) {
                mLatitude = location.getLatitude();
                mLatitude = location.getLongitude();
            }
            mLocationClient.stop();
        }
    }

}
