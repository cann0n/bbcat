package com.sdkj.bbcat.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.HospitalDetailVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

public class HospitalDetailActivity extends SimpleActivity{
    
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
    
    
    @Override
    public void initBusiness() {
        new TitleBar(activity).setTitle("医院详情").back();
        id= (String) getVo("0");
        PostParams params=new PostParams();
        params.put("hospital_id",id);
        showDialog();
        HttpUtils.postJSONObject(activity, Const.HOSPITAL_detail, params, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                RespVo<HospitalDetailVo> respVo= GsonTools.getVo(obj.toString(),RespVo.class);
                if(respVo.isSuccess()){
                    HospitalDetailVo detail=respVo.getData(obj,HospitalDetailVo.class);
                }else{
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


    @OnClick(R.id.iv_doctor_more)
    void doctor_more(View view){
        skip(DoctorListActivity.class);
    }

    @OnClick(R.id.rl_bida)
    void bida(View view){
        skip(AskActivity.class);
    }
    @Override
    public int setLayoutResID() {
        return R.layout.activity_hospitaldetail;
    }
}
