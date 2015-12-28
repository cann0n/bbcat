package com.sdkj.bbcat.activity.loginandregister;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

/**
 * Created by Mr.Yuan on 2015/12/25 0025.
 */
public class FindScreteThirdStepActivity extends SimpleActivity
{
    @ViewInject(R.id.findscretethird_scone)
    private EditText screteOne;
    @ViewInject(R.id.findscretethird_sctwo)
    private EditText screteTwo;
    @ViewInject(R.id.findscretethird_btn)
    private Button   screteBtn;
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

        screteBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (! Utils.isEmpty(screteOne.getText().toString().trim()) && ! Utils.isEmpty(screteTwo.getText().toString().trim()))
                {
                    if (screteOne.getText().toString().trim().equals(screteTwo.getText().toString().trim()))
                    {
                        if(screteOne.getText().toString().trim().length() >= 6 && screteOne.getText().toString().trim().length() <= 11)
                        {
                            showDialog();
                            PostParams params = new PostParams();
                            params.put("mobile",(String)getVo("0"));
                            params.put("vid",(String)getVo("1"));
                            params.put("password", screteOne.getText().toString().trim());
                            params.put("repassword", screteTwo.getText().toString().trim());
                            params.put("verifyCode","123456");

                            HttpUtils.postJSONObject(FindScreteThirdStepActivity.this, Const.FindScrete,params, new RespJSONObjectListener(FindScreteThirdStepActivity.this)
                            {
                                @Override
                                public void getResp(JSONObject jsonObject)
                                {
                                    dismissDialog();
                                    RespVo<?> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                                    if (respVo.isSuccess())
                                    {
                                        toast("修改密码成功");
                                        Intent intent = new Intent(FindScreteThirdStepActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else
                                    {
                                        toast(respVo.getMessage());
                                    }
                                }

                                @Override
                                public void doFailed()
                                {
                                    dismissDialog();
                                    toast("链接服务器失败");
                                }
                            });
                        }
                        else {
                            toast("请输入6-11位字母数字符号组合作为密码");
                        }
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

   /* public void showCompleteDialog()
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
    }*/

   /* @Override
    public void onBackPressed()
    {
        if(!alertDialog.isShowing())
        {
            finish();
        }
    }*/
}
