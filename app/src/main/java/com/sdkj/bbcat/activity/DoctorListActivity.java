package com.sdkj.bbcat.activity;

import android.widget.ListView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.adapter.DoctorAdapter;
import com.sdkj.bbcat.bean.HospitalDetailVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorListActivity extends SimpleActivity {

    private DoctorAdapter adapter;

    @ViewInject(R.id.doctor_list)
    private ListView doctor_list;

    @Override
    public void initBusiness() {
        new TitleBar(activity).setTitle("专家详情").back();
        adapter = new DoctorAdapter(activity, new ArrayList<HospitalDetailVo.Expert>());

        doctor_list.setAdapter(adapter);
        
        showDialog();
        PostParams params = new PostParams();
        params.put("hospital_id", (String) getVo("0"));
        HttpUtils.postJSONObject(activity, Const.EXPERT_LIST,params, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo respVo= GsonTools.getVo(jsonObject.toString(),RespVo.class);
                if(respVo.isSuccess()){
                    List<HospitalDetailVo.Expert> data=respVo.getListData(jsonObject,HospitalDetailVo.Expert.class);
                    if(Utils.isEmpty(data)){
                        toast("该医院暂无专家信息");
                    }else{
                        adapter.addItems(data);
                    }
                    
                }else{
                    toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
                toast("获取专家列表失败");
            }
        });
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_doctorlist;
    }
}
