package com.sdkj.bbcat.activity.doctor;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easemob.chat.core.ac;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.HospitalDetailVo;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by ${Rhino} on 2016/3/18 18:17
 */
public class DoctorDetailActivity extends SimpleActivity {
    HospitalDetailVo.Expert expert;
    
    @ViewInject(R.id.iv_avatar)
    private ImageView iv_avatar;
    
    @ViewInject(R.id.tv_name)
    private TextView tv_name;

    @ViewInject(R.id.tv_subject)
    private TextView tv_subject;

    @ViewInject(R.id.tv_job)
    private TextView tv_job;

    @ViewInject(R.id.tv_desc)
    private TextView tv_desc;
    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("专家详情");
        expert= (HospitalDetailVo.Expert) getVo("0");
        tv_desc.setText(expert.getExport_desc());
        tv_name.setText(expert.getExport_name());
        tv_subject.setText(expert.getExport_position());
        tv_job.setText(expert.getExport_depart());
        Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(expert.getAvatar())).centerCrop().into(iv_avatar);
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_doctor_detail;
    }
}
