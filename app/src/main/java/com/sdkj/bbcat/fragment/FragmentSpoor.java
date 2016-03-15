package com.sdkj.bbcat.fragment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.MainActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.bracelet.AllBodyFeaActivity;
import com.sdkj.bbcat.activity.bracelet.BabyFoodsActivity;
import com.sdkj.bbcat.activity.bracelet.BabyNotesActivity;
import com.sdkj.bbcat.activity.bracelet.DiseaseRecordActivity;
import com.sdkj.bbcat.activity.bracelet.FeedNotesActivity;
import com.sdkj.bbcat.activity.bracelet.FootPrintActivity;
import com.sdkj.bbcat.activity.bracelet.RecordInfoActivity;
import com.sdkj.bbcat.activity.bracelet.VaccineActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.bean.FeedInoVo;
import com.sdkj.bbcat.bean.GrowthVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.bean.VaccineVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Mr.Yuan on 2015/12/31 0031.
 */
public class FragmentSpoor extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.sh_bodyfeatures)
    private TextView mBodyFeatures;
    @ViewInject(R.id.sh_babyfnotes)
    private TextView mBobyNotes;
    @ViewInject(R.id.sh_foodnotes)
    private TextView mFoodNotes;
    @ViewInject(R.id.sh_inoculation)
    private TextView mInoculation;

    @ViewInject(R.id.sh_sttztv)
    private TextView bodyFeatures;

    @ViewInject(R.id.sh_sgimg)
    private ImageView mHeightImg;
    @ViewInject(R.id.sh_sgtvup)
    private TextView mHeightTvUp;
    @ViewInject(R.id.sh_sgtvdown)
    private TextView mHeightTvDown;

    @ViewInject(R.id.tv_record)
    private TextView tv_record;

    @ViewInject(R.id.tv_foods)
    private TextView tv_foods;

    @ViewInject(R.id.sh_tzimg)
    private ImageView mWeightImg;
    @ViewInject(R.id.sh_tztvup)
    private TextView mWeightTvUp;
    @ViewInject(R.id.sh_tztvdown)
    private TextView mWeightTvDown;

    @ViewInject(R.id.sh_twimg)
    private ImageView mHeadImg;
    @ViewInject(R.id.sh_twtvup)
    private TextView mHeadtTvUp;
    @ViewInject(R.id.sh_twtvdown)
    private TextView mHeadTvDown;


    @ViewInject(R.id.sh_bbrj)
    private TextView babyNote;
    @ViewInject(R.id.sh_bbrjll)
    private LinearLayout babyNotell;
    @ViewInject(R.id.sh_wyjl)
    private TextView feedNote;
    @ViewInject(R.id.sh_wyjlll)
    private LinearLayout feedNotell;
    @ViewInject(R.id.sh_yfjz)
    private TextView inoculation;
    @ViewInject(R.id.sh_yfjzll)
    private LinearLayout inoculationll;

    @ViewInject(R.id.sh_bodyfeaturesquanall)
    private LinearLayout bodyfeaturesquanall;
    @ViewInject(R.id.sh_babynotequanall)
    private LinearLayout babynotequanall;
    @ViewInject(R.id.sh_feednotequanall)
    private LinearLayout feednotequanall;
    @ViewInject(R.id.sh_inoculationquanall)
    private LinearLayout inoculationquanall;

    private GrowthVo mGrowthVo;

    protected int setLayoutResID() {
        return R.layout.spoor_fragment;
    }

    protected void setListener() {
        EventBus.getDefault().register(this);
        queryBodyFeatures();
        mBodyFeatures.setOnClickListener(this);
        bodyFeatures.setOnClickListener(this);
        mBobyNotes.setOnClickListener(this);
        babyNote.setOnClickListener(this);
        mFoodNotes.setOnClickListener(this);
        feedNote.setOnClickListener(this);
        mInoculation.setOnClickListener(this);
        inoculation.setOnClickListener(this);
    }

    private void queryBodyFeatures() {
        HttpUtils.postJSONObject(activity, Const.GetGrowthDatas, SimpleUtils.buildUrl(activity, new PostParams()), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject obj) {
                ((MainActivity) activity).dismissDialog();
                RespVo<GrowthVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    mGrowthVo = respVo.getData(obj, GrowthVo.class);
                    if (mGrowthVo != null && mGrowthVo.getBaby_status() != null && !Utils.isEmpty(mGrowthVo.getBaby_status().getHead())) {
                        GrowthVo.BobyState state = mGrowthVo.getBaby_status();
                        float heightMin = state.getMin_height();
                        float heightMax = state.getMax_height();
                        float weightMin = state.getMin_weight();
                        float weightMax = state.getMax_weight();
                        float headMin = state.getMin_head();
                        float headMax = state.getMax_head();

                        mHeightTvUp.setText("身高:" + state.getHeight() + "cm");
                        if (Float.valueOf(state.getHeight()) < heightMin) {
                            mHeightImg.setBackgroundResource(R.drawable.zhuyi_orange);
                            mHeightTvDown.setText("低于正常范围之内，请加强宝宝的饮食营养!");
                        } else if (Float.valueOf(state.getHeight()) > heightMax) {
                            mHeightImg.setBackgroundResource(R.drawable.zhuyi_orange);
                            mHeightTvDown.setText("高于正常范围之内，请减弱宝宝的饮食营养!");
                        } else {
                            mHeightImg.setBackgroundResource(R.drawable.zhengchang_green);
                            mHeightTvDown.setText("在正常范围之内。");
                        }

                        mWeightTvUp.setText("体重:" + state.getWeight() + "kg");
                        if (Float.valueOf(state.getWeight()) < weightMin) {
                            mWeightImg.setBackgroundResource(R.drawable.zhuyi_orange);
                            mWeightTvDown.setText("低于正常范围之内，请加强宝宝的饮食营养!");
                        } else if (Float.valueOf(state.getWeight()) > weightMax) {
                            mWeightImg.setBackgroundResource(R.drawable.zhuyi_orange);
                            mWeightTvDown.setText("高于正常范围之内，请减弱宝宝的饮食营养!");
                        } else {
                            mWeightImg.setBackgroundResource(R.drawable.zhengchang_green);
                            mWeightTvDown.setText("在正常范围之内。");
                        }

                        mHeadtTvUp.setText("头围:" + state.getHead() + "cm");
                        if (Float.valueOf(state.getHead()) < headMin) {
                            mHeadImg.setBackgroundResource(R.drawable.zhuyi_orange);
                            mHeadTvDown.setText("低于正常范围之内，请加强宝宝的饮食营养!");
                        } else if (Float.valueOf(state.getHead()) > headMax) {
                            mHeadImg.setBackgroundResource(R.drawable.zhuyi_orange);
                            mHeadTvDown.setText("高于正常范围之内，请减弱宝宝的饮食营养!");
                        } else {
                            mHeadImg.setBackgroundResource(R.drawable.zhengchang_green);
                            mHeadTvDown.setText("在正常范围之内。");
                        }
                    } else {
                        bodyfeaturesquanall.setVisibility(View.GONE);
                    }

                    if (mGrowthVo != null && !Utils.isEmpty(mGrowthVo.getNews())) {
                        babyNotell.removeAllViews();
                        for (final NewsVo newsVo : mGrowthVo.getNews()) {
                            View view = activity.makeView(R.layout.item_recommend);
                            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                            TextView tv_come_form = (TextView) view.findViewById(R.id.tv_come_form);
                            TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(iv_image);
                            tv_title.setText(newsVo.getTitle());
                            tv_come_form.setText(newsVo.getCategory_name());
                            tv_count.setText(newsVo.getView() + "");
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    activity.skip(NewsDetailActivity.class, newsVo.getId());
                                }
                            });
                            view.setBackgroundResource(R.color.color_white);
                            babyNotell.addView(view);
                        }
                    } else {
                        babynotequanall.setVisibility(View.GONE);
                    }

                    if (mGrowthVo != null && !Utils.isEmpty(mGrowthVo.getFeed_log())) {
                        feedNotell.removeAllViews();
                        for (final FeedInoVo.FeedInfo vo : mGrowthVo.getFeed_log()) {
                            View view = activity.makeView(R.layout.item_feed_list);
                            ImageView iv_type = (ImageView) view.findViewById(R.id.iv_type);
                            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                            TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
                            TextView tv_long = (TextView) view.findViewById(R.id.tv_long);
                            TextView tv_desc = (TextView) view.findViewById(R.id.tv_desc);
                            if (vo.getType() == 1) {
                                iv_type.setImageResource(R.drawable.icon_naiping);
                            } else if (vo.getType() == 2) {
                                iv_type.setImageResource(R.drawable.icon_naifen);
                            } else if (vo.getType() == 3) {
                                iv_type.setImageResource(R.drawable.icon_muru);
                            } else if (vo.getType() == 4) {
                                iv_type.setImageResource(R.drawable.icon_fushi);
                            }
                            tv_name.setText(vo.getTime());
                            tv_type.setText(vo.getName());
                            tv_long.setText(vo.getNum());
                            tv_desc.setText(vo.getDesc());
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    activity.skip(RecordInfoActivity.class, vo);
                                }
                            });
                            feedNotell.addView(view);
                        }
                    } else {
                        feednotequanall.setVisibility(View.GONE);
                    }

                    if (mGrowthVo != null && !Utils.isEmpty(mGrowthVo.getVac_log())) {
                        inoculationll.removeAllViews();
                        for (final VaccineVo.VaccineChildVo child : mGrowthVo.getVac_log()) {
                            View childView = activity.makeView(R.layout.item_vaccine_child);
                            RelativeLayout rl_child_item = (RelativeLayout) childView.findViewById(R.id.rl_child_item);
                            final CheckBox cb_select = (CheckBox) childView.findViewById(R.id.cb_select);
                            if ("1".equals(child.getUser_vac_ed())) {
                                cb_select.setChecked(true);
                            } else {
                                cb_select.setChecked(false);
                            }
                            TextView tv_title = (TextView) childView.findViewById(R.id.tv_title);
                            tv_title.setText(child.getTitle());

                            TextView tv_desc = (TextView) childView.findViewById(R.id.tv_desc);
                            tv_desc.setText(child.getDesc());
                            TextView tv_status = (TextView) childView.findViewById(R.id.tv_status);
                            if ("1".equals(child.getMust())) {
                                tv_status.setVisibility(View.VISIBLE);
                                tv_status.setText("必打");
                            } else {
                                tv_status.setVisibility(View.INVISIBLE);
                            }
                            TextView tv_times = (TextView) childView.findViewById(R.id.tv_times);
                            tv_times.setText(child.getTimes());
                            inoculationll.addView(childView);
                        }
                    } else {
                        inoculationquanall.setVisibility(View.GONE);
                    }
                } else {
                    bodyfeaturesquanall.setVisibility(View.GONE);
                    babynotequanall.setVisibility(View.GONE);
                    feednotequanall.setVisibility(View.GONE);
                    inoculationquanall.setVisibility(View.GONE);
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                ((MainActivity) activity).dismissDialog();
                bodyfeaturesquanall.setVisibility(View.GONE);
                babynotequanall.setVisibility(View.GONE);
                feednotequanall.setVisibility(View.GONE);
                inoculationquanall.setVisibility(View.GONE);
                activity.toast("链接服务器失败");
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == mBodyFeatures) {
            if (mGrowthVo != null && mGrowthVo.getBaby_status() != null) {

//                activity.skip(BodyFeaturesActivity.class, mGrowthVo.getBaby_status());
                activity.skip(FootPrintActivity.class, mGrowthVo.getBaby_status());
            } else {
//                activity.skip(BodyFeaturesActivity.class, new GrowthVo.BobyState());
                activity.skip(FootPrintActivity.class, new GrowthVo.BobyState());
            }
        } else if (v == bodyFeatures) {
            if (mGrowthVo != null && mGrowthVo.getBaby_status() != null)
                activity.skip(AllBodyFeaActivity.class, mGrowthVo.getBaby_status());
            else activity.skip(AllBodyFeaActivity.class, new GrowthVo.BobyState());
        } else if (v == mBobyNotes || v == babyNote) {
            activity.skip(BabyNotesActivity.class);
        } else if (v == mFoodNotes || v == feedNote) {
            activity.skip(FeedNotesActivity.class);
//            activity.skip(NewFoodRecordActivity.class);
        } else if (v == mInoculation || v == inoculation) {
//            activity.skip(InoculationActivity.class);
            activity.skip(VaccineActivity.class);
        }
    }

    @OnClick(R.id.tv_record)
    void showRecord(View view) {
        activity.skip(DiseaseRecordActivity.class);
    }


    public void onEventMainThread(FeedNotesActivity.FreshEvent event) {
        queryBodyFeatures();
    }

    @OnClick(R.id.tv_foods)
    void showFoods(View view) {
        activity.skip(BabyFoodsActivity.class);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
