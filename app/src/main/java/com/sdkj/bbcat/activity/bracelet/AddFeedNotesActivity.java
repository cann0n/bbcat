package com.sdkj.bbcat.activity.bracelet;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.FeedInoVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mr.Yuan on 2016/1/12 0012.
 */
public class AddFeedNotesActivity extends SimpleActivity implements View.OnClickListener
{
    @ViewInject(R.id.afn_topleft)
    private ImageView backImg;
    @ViewInject(R.id.afn_topbettwen)
    private TextView  timeTv;
    @ViewInject(R.id.afn_topright)
    private TextView  complete;
    @ViewInject(R.id.afn_name)
    private EditText mName;
    @ViewInject(R.id.afn_num)
    private EditText mNum;


    public int setLayoutResID()
    {
        return R.layout.activity_addfeednote;
    }

    public void initBusiness()
    {
        timeTv.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        backImg.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == backImg)
        {
            finish();
        }

        else if (v == timeTv)
        {
            String[] strSz = timeTv.getText().toString().trim().split("-");
            int year = Integer.valueOf(strSz[0]);
            int month = Integer.valueOf(strSz[1]);
            int day = Integer.valueOf(strSz[2]);

            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener()
            {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    timeTv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                }
            }, year, month - 1, day)
            {
                protected void onStop()
                {
                }
            };
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }

        else if (v == complete)
        {
            if(!Utils.isEmpty(mName.getText().toString().trim()))
            {
                if (!Utils.isEmpty(mNum.getText().toString().trim()))
                {
                    final PostParams params = new PostParams();
                    params.put("type", "1");
                    params.put("day", timeTv.getText().toString().trim());
                    params.put("name", mName.getText().toString().trim());
                    params.put("num", mNum.getText().toString().trim());

                    HttpUtils.postJSONObject(activity, Const.SetFeedAndIno, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity)
                    {
                        public void getResp(JSONObject jsonObject)
                        {
                            dismissDialog();
                            RespVo<FeedInoVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                            if (respVo.isSuccess())
                            {
                                FeedInoVo feedInoVo =  respVo.getData(jsonObject, FeedInoVo.class);
                                finish();
                                toast("提交资料成功");
                            }

                            else
                            {
                                activity.toast(respVo.getMessage());
                            }
                        }

                        public void doFailed()
                        {
                            dismissDialog();
                            activity.toast("链接服务器失败");
                        }
                    });
                }
                else
                    toast("请完善'数量'信息后再提交资料");
            }
            else
                toast("请完善'喂养'信息后再提交资料");
        }
    }
}
