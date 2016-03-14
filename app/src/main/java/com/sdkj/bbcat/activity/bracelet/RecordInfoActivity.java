package com.sdkj.bbcat.activity.bracelet;

import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.FeedInoVo;
import com.sdkj.bbcat.widget.TitleBar;

/**
 * Created by ${Rhino} on 2016/3/14 15:03
 * 记录详情
 */
public class RecordInfoActivity extends SimpleActivity{

    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    
    @ViewInject(R.id.tv_type)
    private TextView tv_type;

    @ViewInject(R.id.start_time)
    private TextView start_time;

    @ViewInject(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    @ViewInject(R.id.et_desc)
    private EditText et_desc;

    @ViewInject(R.id.et_num)
    private EditText et_num;

    @ViewInject(R.id.rl_time)
    RelativeLayout rl_time;

    @ViewInject(R.id.rl_desc)
    RelativeLayout rl_desc;

    @ViewInject(R.id.rl_num)
    LinearLayout rl_num;

    private int type = 0;
    
    FeedInoVo.FeedInfo vo;
    
    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("删除").showRight("删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        vo= (FeedInoVo.FeedInfo) getVo("0");
        type=vo.getType();
        start_time.setText(vo.getDay());
        tv_type.setText(vo.getName());
        et_num.setText(vo.getNum());
        et_desc.setText(vo.getDesc());
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_record_info;
    }
}
