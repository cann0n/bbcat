package com.sdkj.bbcat.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMConversation;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.community.ChatActivity;
import com.sdkj.bbcat.activity.doctor.DoctorActActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.HospitalDetailVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.Constant;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

public class HospitalDetailActivity extends SimpleActivity {

    private String id;

    @ViewInject(R.id.iv_thumb)
    private ImageView iv_thumb;

    @ViewInject(R.id.tv_hospital_name)
    private TextView tv_hospital_name;

    @ViewInject(R.id.ratingBar)
    private RatingBar ratingBar;

    @ViewInject(R.id.tv_desc)
    private TextView tv_desc;

    @ViewInject(R.id.tv_address)
    private TextView tv_address;

    @ViewInject(R.id.tv_tel)
    private TextView tv_tel;

    @ViewInject(R.id.ll_doctor_container)
    private LinearLayout ll_doctor_container;

    @ViewInject(R.id.ll_time_container)
    private LinearLayout ll_time_container;

    private String phoneno;

    HospitalDetailVo detail;

    @ViewInject(R.id.rl_huodong)
    private RelativeLayout rl_huodong;

    @ViewInject(R.id.rl_bida)
    private RelativeLayout rl_bida;

    @Override
    public void initBusiness() {
        new TitleBar(activity).setTitle("医院详情").back();
        id = (String) getVo("0");
        PostParams params = new PostParams();
        params.put("hospital_id", id);
        showDialog();
        HttpUtils.postJSONObject(activity, Const.HOSPITAL_detail, params, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo<HospitalDetailVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    detail = respVo.getData(obj, HospitalDetailVo.class);
                    if (1 == detail.getHospital_activity()) {
                        rl_huodong.setVisibility(View.VISIBLE);
                    }
                    if (1 == detail.getChat_open()) {
                        rl_bida.setVisibility(View.VISIBLE);
                    }
                    Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(detail.getHospital_detail().getCover())).into(iv_thumb);
                    tv_hospital_name.setText(detail.getHospital_detail().getTitle());
                    ratingBar.setRating(detail.getHospital_detail().getLevel());
                    tv_desc.setText(Html.fromHtml(detail.getHospital_detail().getDetail()));
                    tv_address.setText(detail.getHospital_detail().getAddress());
                    tv_tel.setText(detail.getHospital_detail().getContact_phone());
                    phoneno = detail.getHospital_detail().getContact_phone();
//                    int width= (AppUtils.getWidth(activity)-80)/3;
                    if (!Utils.isEmpty(detail.getHospital_expert())) {
                        for (int i = 0; i < detail.getHospital_expert().size(); i++) {
                            HospitalDetailVo.Expert expert = detail.getHospital_expert().get(i);
                            View doctorView = makeView(R.layout.item_hospital_doctor);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.weight = 1;
                            doctorView.setLayoutParams(lp);
                            ImageView iv_avatar = (ImageView) doctorView.findViewById(R.id.iv_avatar);
                            TextView tv_name = (TextView) doctorView.findViewById(R.id.tv_name);
                            TextView tv_subject = (TextView) doctorView.findViewById(R.id.tv_subject);
                            TextView tv_job = (TextView) doctorView.findViewById(R.id.tv_job);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(expert.getAvatar())).into(iv_avatar);
                            tv_name.setText(expert.getExport_name());
                            tv_subject.setText(expert.getExport_depart());
                            tv_job.setText(expert.getExport_position());
                            doctorView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    skip(DoctorListActivity.class, id);
                                }
                            });
                            ll_doctor_container.addView(doctorView);


                            View timeView = makeView(R.layout.item_hospital_time);

                            TextView tv_name1 = (TextView) timeView.findViewById(R.id.tv_name);
                            TextView tv_job1 = (TextView) timeView.findViewById(R.id.tv_job);
                            TextView tv_time_desc = (TextView) timeView.findViewById(R.id.tv_time_desc);
                            TextView tv_time = (TextView) timeView.findViewById(R.id.tv_time);

                            tv_name1.setText(expert.getExport_name());
                            tv_job1.setText(expert.getExport_position());
                            tv_time_desc.setText(expert.getExport_desc());
                            tv_time.setText(expert.getExport_time());
                            timeView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    skip(DoctorListActivity.class, id);
                                }
                            });
                            ll_time_container.addView(timeView);
                        }
                    }

                } else {
                    toast(respVo.getMessage());
                }

            }

            @Override
            public void doFailed() {
                dismissDialog();
                toast("查询医院详情失败");
                finish();
            }
        });
    }


    @OnClick(R.id.ll_doctor_more)
    void doctor_more(View view) {
        skip(DoctorListActivity.class, id);
    }

    @OnClick(R.id.rl_tel)
    void makeCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneno));
        startActivity(intent);
    }

    @OnClick(R.id.rl_bida)
    void bida(View view) {
//        skip(AskActivity.class);
        if (!SimpleUtils.isLogin(activity)) {
            skip(LoginActivity.class);
            return;
        }
        Intent intent = new Intent(getActivity(), ChatActivity.class);
//        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
        // it's single chat]
        intent.putExtra(Constant.EXTRA_USER_ID, detail.getHospital_detail().getHxchat_id());
        SpUtil sp = new SpUtil(activity, Const.SP_NAME);
        intent.putExtra(Constant.EXTRA_USER_AVATAR, sp.getStringValue(Const.AVATAR));
        intent.putExtra(Constant.EXTRA_USER_AVATAR, sp.getStringValue(Const.AVATAR));
        intent.putExtra(Constant.TO_USER_NICKNAME, detail.getHospital_detail().getTitle());
        startActivity(intent);
    }

    @OnClick(R.id.rl_huodong)
    void newAct(View view) {
        skip(DoctorActActivity.class, id);
//        BNDemoGuideActivity
    }
    
    @OnClick(R.id.rl_address)
    void showGuide(View view){
//        skip(BNDemoGuideActivity.class);
    }
            

    @Override
    public int setLayoutResID() {
        return R.layout.activity_hospitaldetail;
    }
}
