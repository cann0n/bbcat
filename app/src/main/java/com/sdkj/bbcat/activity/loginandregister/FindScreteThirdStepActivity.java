package com.sdkj.bbcat.activity.loginandregister;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.baseBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

/**
 * Created by Mr.Yuan on 2015/12/25 0025.
 */
public class FindScreteThirdStepActivity extends BaseActivity
{
    @ViewInject(R.id.findscretethird_scone)
    private EditText screteOne;
    @ViewInject(R.id.findscretethird_sctwo)
    private EditText screteTwo;
    @ViewInject(R.id.findscretethird_btn)
    private Button   screteBtn;
    private String phoneNum;

    private AlertDialog alertDialog;

    @Override
    public int setLayoutResID()
    {
        return R.layout.activity_findscretethirdstep;
    }

    @Override
    public void initBusiness()
    {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCanceledOnTouchOutside(false);
        new TitleBar(activity).setTitle("找回密码").back();
        phoneNum = (String) getVo("0");

        screteBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (! Utils.isEmpty(screteOne.getText().toString().trim()) && ! Utils.isEmpty(screteTwo.getText().toString().trim()))
                {
                    if (screteOne.getText().toString().trim().equals(screteTwo.getText().toString().trim()))
                    {
                        PostParams params = new PostParams();
                        params.put("confirmPwd", screteTwo.getText().toString().trim());
                        params.put("newPwd", screteOne.getText().toString().trim());
                        params.put("phone", phoneNum);

                        HttpUtils.getJSONObject(FindScreteThirdStepActivity.this, Const.FindScreteEnd + "?" + params.bindUrl(), new RespJSONObjectListener(FindScreteThirdStepActivity.this)
                        {
                            @Override
                            public void getResp(JSONObject jsonObject)
                            {
                                baseBean bean = GsonTools.getVo(jsonObject.toString(), baseBean.class);
                                if (bean.getReturnCode().equals("SUCCESS"))
                                {
                                    showCompleteDialog();
                                } else
                                {
                                  toast(bean.getMessage());
                                }
                            }

                            @Override
                            public void doFailed()
                            {
                                toast("链接服务器失败");
                            }
                        });
                    } else
                    {
                        toast("两次输入的密码不一致");
                    }
                } else
                {
                    toast("不允许为空哟");
                }
            }
        });
    }

    public void showCompleteDialog()
    {
        alertDialog.show();
        View view = LayoutInflater.from(this).inflate(R.layout.inflater_registerdialog, null);
        alertDialog.setContentView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.registerdialog_img);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
                Intent intent = new Intent(FindScreteThirdStepActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if(!alertDialog.isShowing())
        {
            finish();
        }
    }
}
