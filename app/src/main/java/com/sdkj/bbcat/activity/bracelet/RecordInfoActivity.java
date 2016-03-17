//package com.sdkj.bbcat.activity.bracelet;
//
//import android.view.View;
//import android.widget.Chronometer;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.huaxi100.networkapp.network.HttpUtils;
//import com.huaxi100.networkapp.network.PostParams;
//import com.huaxi100.networkapp.network.RespJSONObjectListener;
//import com.huaxi100.networkapp.utils.GsonTools;
//import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
//import com.sdkj.bbcat.R;
//import com.sdkj.bbcat.SimpleActivity;
//import com.sdkj.bbcat.bean.FeedInoVo;
//import com.sdkj.bbcat.bean.RespVo;
//import com.sdkj.bbcat.constValue.Const;
//import com.sdkj.bbcat.constValue.SimpleUtils;
//import com.sdkj.bbcat.widget.TitleBar;
//
//import org.json.JSONObject;
//
//import de.greenrobot.event.EventBus;
//
///**
// * Created by ${Rhino} on 2016/3/14 15:03
// * 记录详情
// */
//public class RecordInfoActivity extends SimpleActivity{
//
//    @ViewInject(R.id.tv_type)
//    private TextView tv_type;
//
//    @ViewInject(R.id.tv_num)
//    private TextView tv_num;
//
//    @ViewInject(R.id.start_time)
//    private TextView start_time;
//
//    @ViewInject(R.id.rl_bottom)
//    RelativeLayout rl_bottom;
//
//    @ViewInject(R.id.et_desc)
//    private TextView et_desc;
//
//    @ViewInject(R.id.et_num)
//    private TextView et_num;
//
//    @ViewInject(R.id.rl_time)
//    RelativeLayout rl_time;
//
//    @ViewInject(R.id.rl_desc)
//    RelativeLayout rl_desc;
//
//    @ViewInject(R.id.rl_num)
//    LinearLayout rl_num;
//
//    private int type = 0;
//    
//    FeedInoVo.FeedInfo vo;
//    
//    @Override
//    public void initBusiness() {
//        new TitleBar(activity).back().setTitle("删除").showRight("删除", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delete();
//            }
//        });
//        vo= (FeedInoVo.FeedInfo) getVo("0");
//        type=vo.getType();
//        if(type==1){
//            tv_num.setText("时长");
//        }
//        start_time.setText(vo.getDay());
//        tv_type.setText(vo.getName());
//        et_num.setText(vo.getNum());
//        et_desc.setText(vo.getDesc());
//    }
//
//    private void delete(){
//        showDialog();
//        PostParams params=new PostParams();
//        params.put("id", vo.getId()+"");
//        HttpUtils.postJSONObject(activity, Const.DELETE_FEED_RECORD, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
//            @Override
//            public void getResp(JSONObject jsonObject) {
//                dismissDialog();
//                RespVo respVo= GsonTools.getVo(jsonObject.toString(),RespVo.class);
//                if(respVo.isSuccess()){
//                    EventBus.getDefault().post(new FeedNotesActivity.FreshEvent());
//                    finish();
//                }else{
//                    toast(respVo.getMessage());
//                }
//            }
//
//            @Override
//            public void doFailed() {
//                dismissDialog();
//            }
//        });
//    }
//    
//    @Override
//    public int setLayoutResID() {
//        return R.layout.activity_record_info;
//    }
//}
