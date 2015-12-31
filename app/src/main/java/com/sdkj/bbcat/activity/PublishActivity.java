package com.sdkj.bbcat.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONArrayListener;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.CircleTagVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class PublishActivity extends SimpleActivity {

    @ViewInject(R.id.et_title)
    private EditText et_title;

    @ViewInject(R.id.et_content)
    private EditText et_content;

    @ViewInject(R.id.tv_address)
    private TextView tv_address;

    @ViewInject(R.id.tv_label)
    private TextView tv_label;

    private List<CircleTagVo> tags;

    @Override
    public void initBusiness() {

    }

    @OnClick(R.id.tv_publish)
    void publish(View view) {
        if (Utils.isEmpty(et_title.getText().toString())) {
            toast("请输入标题");
            return;
        }
        if (Utils.isEmpty(et_content.getText().toString())) {
            toast("请输入内容");
            return;
        }
        
        if(!SimpleUtils.isLogin(activity)){
            skip(LoginActivity.class);
            return;
        }
        PostParams params = new PostParams();
        showDialog();
        params.put("title", et_title.getText().toString());
        params.put("content", et_content.getText().toString());
        params.put("address", tv_address.getText().toString());
        params.put("category_id", "19");//标签id
        params.put("pictures", "1357");
        HttpUtils.postJSONOArray(activity, Const.PUBLIC_CIRCLE, SimpleUtils.buildUrl(activity,params), new RespJSONArrayListener(activity) {
            @Override
            public void getResp(JSONArray obj) {
                dismissDialog();
                RespVo respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    toast("动态已发布");
                    finish();
                }else{
                    toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
            }
        });

    }

    @OnClick(R.id.rl_label)
    void selectLabel(View view) {
        //判断tags是否为空
    }

    @OnClick(R.id.iv_back)
    void back(View view) {
        finish();
    }


    private void queryLabel() {
        showDialog();
        HttpUtils.getJSONObject(activity, Const.GET_TAGS, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo<CircleTagVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    tags = respVo.getListData(obj, CircleTagVo.class);
                } else {
                    toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {

            }
        });
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_publish;
    }
}
