package com.sdkj.bbcat.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.MainActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.HomeVo;
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
        HttpUtils.getJSONObject(activity, SimpleUtils.buildUrl(activity,Const.HOME_PAGE), new RespJSONObjectListener(activity)
        {
            @Override
            public void getResp(JSONObject obj)
            {
                ((MainActivity) activity).dismissDialog();
                RespVo<HomeVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess())
                {
                    HomeVo homeVo = respVo.getData(obj, HomeVo.class);
                    if (! Utils.isEmpty(homeVo.getNews()))
                    {
                        final HomeVo.Category category0 = homeVo.getNews().get(0);
                        for (final NewsVo newsVo : category0.getCategory_list())
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
                                @Override
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

            @Override
            public void doFailed()
            {
                ((MainActivity) activity).dismissDialog();
            }
        });
    }
}
