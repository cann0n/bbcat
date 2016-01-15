package com.sdkj.bbcat.activity.loginandregister;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.BbcatApp;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.bracelet.BabyNotesActivity;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.hx.PreferenceManager;
import com.sdkj.bbcat.widget.CircleImageView;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by Mr.Yuan on 2016/1/13 0013.
 */
public class PersonInfosActivity extends SimpleActivity implements View.OnClickListener
{
    @ViewInject(R.id.personinfos_headall)
    private LinearLayout    mHeadall;
    @ViewInject(R.id.personinfos_head)
    private CircleImageView mHead;
    @ViewInject(R.id.personinfos_nickall)
    private LinearLayout    mNickAll;
    @ViewInject(R.id.personinfos_nick)
    private TextView        mNick;
    @ViewInject(R.id.personinfos_sexall)
    private LinearLayout    mSexAll;
    @ViewInject(R.id.personinfos_sex)
    private TextView        mSex;
    @ViewInject(R.id.personinfos_birthall)
    private LinearLayout    mBirthAll;
    @ViewInject(R.id.personinfos_birth)
    private TextView        mBirth;
    @ViewInject(R.id.personinfos_stateall)
    private LinearLayout    mStateAll;
    @ViewInject(R.id.personinfos_state)
    private TextView        mState;
    @ViewInject(R.id.personinfos_addressall)
    private LinearLayout    mAddressAll;
    @ViewInject(R.id.personinfos_address)
    private TextView        mAddress;
    @ViewInject(R.id.personinfos_addressupdate)
    private TextView        mAddressUpdate;
    private boolean         mLocationing;
    private LoginBean       mLoginBean;

    private Double mLatitude = 0d;
    private Double mLongitude = 0d;

    private boolean IsAlreadyMody = false;
    public static final int NICKNAME = 0X0001;
    public static final int SEX = 0X0002;
    public static final int STATE = 0X0003;


    public int setLayoutResID()
    {
        return R.layout.activity_personinfos;
    }

    public void initBusiness()
    {
        new TitleBar(activity).setTitle("个人信息").back().showRight("提交", new View.OnClickListener()
        {
            public void onClick(View v)
            {
                PostParams params = new PostParams();
                try
                {
                    params.put("nick", URLEncoder.encode(mNick.getText().toString().trim(), "utf-8"));
                    if(mState.getText().toString().trim().equals("怀孕中"))
                        params.put("baby_status","1");
                    else if(mState.getText().toString().trim().equals("备孕中"))
                        params.put("baby_status","2");
                    else if(mState.getText().toString().trim().equals("已出生"))
                        params.put("baby_status","3");
                    if(mSex.getText().toString().trim().equals("男"))
                         params.put("sex","1");
                    else if(mSex.getText().toString().trim().equals("女"))
                        params.put("sex","2");
                    if(mBirth.getText().toString().trim().length()!=0)
                        params.put("birthday",mBirth.getText().toString().trim());
                    else
                    {
                        toast("请选择宝宝的出生日期后再提交资料");return;
                    }
                    if(mAddress.getText().toString().trim().length()!=0)
                        params.put("lat & lng", String.valueOf(mLatitude) + "&" + String.valueOf(mLoginBean));
                    else
                    {
                        toast("定位失败，请重新定位后再提交资料");
                        return;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                HttpUtils.postJSONObject(activity, Const.SaveUserInfos, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity)
                {
                    public void getResp(JSONObject obj)
                    {
                        dismissDialog();
                        RespVo<String> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                        if (respVo.isSuccess())
                        {
                            try
                            {
                                /**修改本地数据*/
                                mLoginBean.getUserInfo().setNickname(mNick.getText().toString().trim());
                                if(mState.getText().toString().trim().equals("怀孕中"))
                                    mLoginBean.getUserInfo().setBaby_status("1");
                                else if(mState.getText().toString().trim().equals("备孕中"))
                                    mLoginBean.getUserInfo().setBaby_status("2");
                                else if(mState.getText().toString().trim().equals("已出生"))
                                    mLoginBean.getUserInfo().setBaby_status("3");
                                if(mSex.getText().toString().trim().equals("男"))
                                    mLoginBean.getUserInfo().setSex("1");
                                else if(mSex.getText().toString().trim().equals("女"))
                                    mLoginBean.getUserInfo().setSex("2");
                                mLoginBean.getUserInfo().setBirthday(mBirth.getText().toString().trim());
                                mLoginBean.getUserInfo().setAvatar( mLoginBean.getUserInfo().getAvatar());
                                /**修改share数据*/
                                SpUtil sp_login =  new SpUtil(activity,Const.SP_NAME);
                                sp_login.setValue("sex", mLoginBean.getUserInfo().getSex());
                                sp_login.setValue("birthday", mLoginBean.getUserInfo().getBirthday());
                                sp_login.setValue("baby_status", mLoginBean.getUserInfo().getBaby_status());
                                sp_login.setValue(Const.NICKNAME, mLoginBean.getUserInfo().getNickname());
                                sp_login.setValue(Const.AVATAR, mLoginBean.getUserInfo().getAvatar());
                                PreferenceManager.getInstance().setCurrentUserAvatar(SimpleUtils.getImageUrl(mLoginBean.getUserInfo().getAvatar()));
                                PreferenceManager.getInstance().setCurrentUserNick(mLoginBean.getUserInfo().getNickname());
                                toast(obj.getJSONObject("data").getString("message"));
                                Intent intent = new Intent();
                                intent.putExtra("alreadymody",true);
                                setResult(0,intent);
                                finish();
                                /**在下面修改头像*/
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            activity.toast(respVo.getMessage());
                        }
                    }

                    public void doFailed()
                    {
                        ((BabyNotesActivity) activity).dismissDialog();
                    }
                });
            }
        });

        mLoginBean = ((BbcatApp) getApplication()).getmUser();
        Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl((mLoginBean.getUserInfo().getAvatar()))).into(mHead);
        mNick.setText(mLoginBean.getUserInfo().getNickname());
        mBirth.setText(mLoginBean.getUserInfo().getBirthday());
        if(mLoginBean.getUserInfo().getSex().equals("2"))
            mSex.setText("女");
        else if(mLoginBean.getUserInfo().getSex().equals("1"))
            mSex.setText("男");
        else
            mSex.setText("未设定");
        if(mLoginBean.getUserInfo().getBaby_status().equals("1"))
            mState.setText("怀孕中");
        else if(mLoginBean.getUserInfo().getBaby_status().equals("2"))
            mState.setText("备孕中");
        else if(mLoginBean.getUserInfo().getBaby_status().equals("3"))
            mState.setText("已出生");
        else
            mState.setText("未设定");

        /**在这里尝试获取用户地址*/
        startBaiduLocation();
        mAddress.setText("正在获取地址");

        mHeadall.setOnClickListener(this);
        mNickAll.setOnClickListener(this);
        mSexAll.setOnClickListener(this);
        mBirthAll.setOnClickListener(this);
        mStateAll.setOnClickListener(this);
        mAddressUpdate.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        if(v == mHeadall)
        {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.show();

            View view = LayoutInflater.from(this).inflate(R.layout.inflater_photocamera,null);
            alertDialog.setContentView(view);
            TextView localPhoto = (TextView)view.findViewById(R.id.photocamera_bendizhaopian);
            TextView camera = (TextView)view.findViewById(R.id.photocamera_zhaoxiang);
            TextView quxiao = (TextView)view.findViewById(R.id.photocamera_quxiao);
            localPhoto.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    startPicLib();
                    alertDialog.dismiss();
                }
            });

            camera.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    startCamera();
                    alertDialog.dismiss();
                }
            });

            quxiao.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    alertDialog.dismiss();
                }
            });

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            Window window = alertDialog.getWindow();
            window.setWindowAnimations(R.style.PhotoCameraDialogAnim);

            WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
            params.width = displayMetrics.widthPixels;
            params.gravity = Gravity.BOTTOM;
            alertDialog.getWindow().setAttributes(params);
        }

        else if(v == mNickAll)
        {
            Intent intent = new Intent(this,NickNameActivity.class);
            intent.putExtra("nickname",mNick.getText().toString().trim());
            startActivityForResult(intent, NICKNAME);
        }

        else if(v == mSexAll)
        {
            Intent intent = new Intent(this,SexActivity.class);
            if(mSex.getText().toString().trim().equals("男"))
                intent.putExtra("sex","1");
            else if(mSex.getText().toString().trim().equals("女"))
                intent.putExtra("sex","2");
            else
                intent.putExtra("sex", "3");
            startActivityForResult(intent, SEX);
        }

        else if(v == mBirthAll)
        {
            Calendar calendar = Calendar.getInstance();
            int year;
            int month;
            int day;
            String[] strSz = mBirth.getText().toString().trim().split("-");;
            if(strSz != null && strSz.length == 3)
            {
                year = Integer.valueOf(strSz[0]);
                month = Integer.valueOf(strSz[1]);
                day = Integer.valueOf(strSz[2]);
            }

            else
            {
                year =  calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH)+1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener()
            {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    IsAlreadyMody = true;
                    mBirth.setText(year +"-"+ (monthOfYear+1) +"-"+ dayOfMonth);
                }
            },year,month-1,day) {protected void onStop() {}};
            datePickerDialog.show();
        }

        else if(v == mStateAll)
        {
            Intent intent = new Intent(this,StateActivity.class);
            if(mState.getText().toString().trim().equals("怀孕中"))
                intent.putExtra("state","1");
            else if(mState.getText().toString().trim().equals("备孕中"))
                intent.putExtra("state","2");
            else if(mState.getText().toString().trim().equals("已出生"))
                intent.putExtra("state","3");
            else
                intent.putExtra("state","4");
            startActivityForResult(intent,STATE);
        }

        else if(v == mAddressUpdate)
        {
            if(!mLocationing)
            {
                startBaiduLocation();
            }
        }
    }

    private void startPicLib()
    {

    }

    private void startCamera()
    {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null)
        {
            IsAlreadyMody = true;
            switch (requestCode)
            {
                case NICKNAME:mNick.setText(data.getStringExtra("nickname"));break;
                case SEX:
                {
                    if (data.getStringExtra("sex").equals("1"))
                        mSex.setText("男");
                    else if (data.getStringExtra("sex").equals("2"))
                        mSex.setText("女");
                }break;

                case STATE:
                {
                    if (data.getStringExtra("state").equals("1"))
                        mState.setText("怀孕中");
                    else if (data.getStringExtra("state").equals("2"))
                        mState.setText("备孕中");
                    else if (data.getStringExtra("state").equals("3"))
                        mState.setText("已出生");
                }break;
            }
        }
    }

    private void startBaiduLocation()
    {
        LocationClient mLocationClient = new LocationClient(activity.getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener(mLocationClient));    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationing = true;
    }

    private class MyLocationListener implements BDLocationListener
    {
        LocationClient mLocationClient;
        public MyLocationListener(LocationClient mLocationClient)
        {
            this.mLocationClient = mLocationClient;
        }

        public void onReceiveLocation(BDLocation location)
        {
            if (! Utils.isEmpty(location.getLatitude() + "") && !Utils.isEmpty(location.getLongitude() + ""))
            {
                mLatitude = location.getLatitude();
                mLatitude = location.getLongitude();
                mAddress.setText(location.getCity());
                mLocationClient.stop();
                mLocationing = false;
            }
        }
    }
}
