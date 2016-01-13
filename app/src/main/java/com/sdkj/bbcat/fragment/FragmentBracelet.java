package com.sdkj.bbcat.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.MainActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.BraceletBotVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

/**
 * Created by Mr.Yuan on 2015/12/31 0031.
 */
public class FragmentBracelet extends BaseFragment
{
    @ViewInject(R.id.bra_bottomll)
    private LinearLayout bra_bottomll;

    protected int setLayoutResID()
    {
        return R.layout.bracelet_fragment;
    }

    protected void setListener()
    {
        queryData();
    }

    private void queryData()
    {
        HttpUtils.postJSONObject(activity, Const.GetBraBotDates,SimpleUtils.buildUrl(activity,new PostParams()), new RespJSONObjectListener(activity)
        {
            public void getResp(JSONObject obj)
            {
                ((MainActivity) activity).dismissDialog();
                RespVo<BraceletBotVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess())
                {
                    BraceletBotVo value = respVo.getData(obj, BraceletBotVo.class);
                    if (! Utils.isEmpty(value.getNewest()))
                    {
                        for (final NewsVo newsVo : value.getNewest())
                        {
                            View view = activity.makeView(R.layout.item_recommend);
                            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                            TextView tv_come_form = (TextView) view.findViewById(R.id.tv_come_form);
                            TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(iv_image);
                            tv_title.setText(newsVo.getTitle());
                            tv_come_form.setText(newsVo.getCategory_name());
                            tv_count.setText(newsVo.getView() + "");
                            view.setOnClickListener(new View.OnClickListener()
                            {
                                public void onClick(View view)
                                {
                                    activity.skip(NewsDetailActivity.class, newsVo);
                                }
                            });
                            view.setBackgroundResource(R.color.color_white);
                            bra_bottomll.addView(view);
                        }
                    }
                }
            }

            public void doFailed()
            {
                ((MainActivity) activity).dismissDialog();
            }
        });
    }
}
