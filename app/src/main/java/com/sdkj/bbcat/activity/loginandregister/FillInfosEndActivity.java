package com.sdkj.bbcat.activity.loginandregister;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.widget.TitleBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mr.Yuan on 2015/12/27 0027.
 */
public class FillInfosEndActivity extends SimpleActivity implements View.OnClickListener
{
    @ViewInject(R.id.infos_img)
    private ImageView img;
    @ViewInject(R.id.infos_time)
    private TextView time;
    private int state = 0;
    private int sex = 0;
    private String timeStr = "";
    private String[] hbStrSz;

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_fillinfosend;
    }

    @Override
    public void initBusiness()
    {
        TitleBar titleBar = new TitleBar(activity).setTitle("宝宝生日").back().showRight("完成", new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toast("修改成功");
                finish();
            }
        });
        hbStrSz = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())).split("-");
        timeStr = hbStrSz[0] +"-"+ hbStrSz[1] +"-"+ hbStrSz[2];
        time.setText(hbStrSz[0]+ "年" + hbStrSz[1] + "月" + hbStrSz[2] +"日");
    }

    @Override
    public void onClick(View v)
    {
        state = (int)getVo("0");
        sex = (int)getVo("1");
        if(sex == 1)
            img.setBackgroundResource(R.drawable.nan);
        else if(sex == 2)
            img.setBackgroundResource(R.drawable.nv);
        time.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Calendar calendar = Calendar.getInstance();
                int year;
                final int month;
                int day;

                if(hbStrSz != null && hbStrSz.length == 3)
                {
                    year = Integer.valueOf(hbStrSz[0]);
                    month = Integer.valueOf(hbStrSz[1]);
                    day = Integer.valueOf(hbStrSz[2]);
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
                        timeStr = year +"-"+ (monthOfYear+1) +"-"+ dayOfMonth;
                        time.setText(year+ "年" + (monthOfYear+1) + "月" + dayOfMonth +"日");
                    }
                },year,month-1,day) {protected void onStop() {}};
                datePickerDialog.show();
            }
        });
    }
}
