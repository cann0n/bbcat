package com.sdkj.bbcat.activity.bracelet;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.news.DiaryListActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.HomeVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

/**
 * Created by Mr.Yuan on 2016/1/7 0007.
 */
public class BabyNotesActivity extends SimpleActivity implements View.OnClickListener
{
    @ViewInject(R.id.baby_normal)
    private TextView     mNormalBtn;
    @ViewInject(R.id.baby_first)
    private TextView     mFirstBtn;
    @ViewInject(R.id.baby_notetitle)
    private TextView     mNoteTitle;
    @ViewInject(R.id.baby_noteall)
    private LinearLayout mNoteAll;

    public int setLayoutResID()
    {
        return R.layout.activity_babynotes;
    }

    public void initBusiness()
    {
        new TitleBar(activity).setTitle("宝宝日记").back();
        queryData();
        mNoteTitle.setOnClickListener(this);
        mNormalBtn.setOnClickListener(this);
        mFirstBtn.setOnClickListener(this);
    }

    private void queryData()
    {
        HttpUtils.postJSONObject(activity,Const.HOME_PAGE,SimpleUtils.buildUrl(activity,new PostParams()), new RespJSONObjectListener(activity)
        {
            public void getResp(JSONObject obj)
            {
                ((BabyNotesActivity) activity).dismissDialog();
                RespVo<HomeVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess())
                {
                    HomeVo homeVo = respVo.getData(obj, HomeVo.class);
                    if (! Utils.isEmpty(homeVo.getNews()))
                    {
                        final HomeVo.Category category1 = homeVo.getNews().get(1);
                        mNoteTitle.setOnClickListener(new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                activity.skip(DiaryListActivity.class, category1.getCategory_id(), "宝宝日记");
                            }
                        });

                        for (final NewsVo newsVo : category1.getCategory_list())
                        {
                            View view = activity.makeView(R.layout.item_recommend);
                            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                            TextView tv_come_form = (TextView) view.findViewById(R.id.tv_come_form);
                            TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(iv_image);
                            tv_title.setText(newsVo.getTitle());
                            tv_come_form.setText(Utils.formatTime(newsVo.getCreate_time() + "000", "yyyy-MM-dd"));
                            tv_count.setText(newsVo.getView() + "");
                            view.setOnClickListener(new View.OnClickListener()
                            {
                                public void onClick(View view)
                                {
                                    activity.skip(NewsDetailActivity.class, newsVo.getId());
                                }
                            });
                            view.setBackgroundResource(R.color.color_white);
                            mNoteAll.addView(view);
                        }
                    }
                } else
                {
                    activity.toast(respVo.getMessage());
                }
            }

            public void doFailed()
            {
                ((BabyNotesActivity) activity).dismissDialog();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if(v == mNormalBtn)
        {
            toast("普通日记");
        }
        else if(v == mFirstBtn)
        {
            toast("宝宝第一次");
        }
    }
}