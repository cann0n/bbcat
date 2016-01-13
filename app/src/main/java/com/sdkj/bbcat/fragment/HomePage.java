package com.sdkj.bbcat.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.MainActivity;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.HospitalDetailActivity;
import com.sdkj.bbcat.activity.MedicalOnlineActivity;
import com.sdkj.bbcat.activity.SearchActivity;
import com.sdkj.bbcat.activity.loginandregister.PersonalCenter;
import com.sdkj.bbcat.activity.news.DiaryListActivity;
import com.sdkj.bbcat.activity.news.NewsDetailActivity;
import com.sdkj.bbcat.activity.news.NewsListActivity;
import com.sdkj.bbcat.bean.HomeVo;
import com.sdkj.bbcat.bean.NewsVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONObject;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 */
public class HomePage extends BaseFragment {

    @ViewInject(R.id.ll_recommend)
    private LinearLayout ll_recommend;
    @ViewInject(R.id.tv_recommend_more)
    private TextView tv_recommend_more;


    @ViewInject(R.id.ll_diary)
    private LinearLayout ll_diary;
    @ViewInject(R.id.tv_diary_more)
    private TextView tv_diary_more;

    @ViewInject(R.id.ll_doctor)
    private LinearLayout ll_doctor;
    @ViewInject(R.id.tv_doctor_more)
    private TextView tv_doctor_more;

    @ViewInject(R.id.ll_knowledge)
    private LinearLayout ll_knowledge;
    @ViewInject(R.id.tv_knowledge_more)
    private TextView tv_knowledge_more;

    @ViewInject(R.id.ll_circle)
    private LinearLayout ll_circle;
    @ViewInject(R.id.tv_my_circle)
    private TextView tv_my_circle;

    @ViewInject(R.id.ll_guess)
    private LinearLayout ll_guess;
    @ViewInject(R.id.tv_guess)
    private TextView tv_guess;

    @ViewInject(R.id.iv_banner)
    private ImageView iv_banner;

    @Override
    protected void setListener() {
        ((MainActivity) activity).showDialog();
        queryData();
    }

    private void queryData() {
        HttpUtils.getJSONObject(activity, SimpleUtils.buildUrl(activity,Const.HOME_PAGE), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                ((MainActivity) activity).dismissDialog();
                RespVo<HomeVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    HomeVo homeVo = respVo.getData(obj, HomeVo.class);
                    if (!Utils.isEmpty(homeVo.getSlider())) {
                        HomeVo.Banner banner = homeVo.getSlider().get(0);
                        Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(banner.getThumb())).into(iv_banner);
                    } else {
                        iv_banner.setVisibility(View.GONE);
                    }

                    if (!Utils.isEmpty(homeVo.getNews())) {
                        final HomeVo.Category category0 = homeVo.getNews().get(0);
                        tv_recommend_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.skip(NewsListActivity.class, category0.getCategory_id(), "推荐");
                            }
                        });
                        for (final NewsVo newsVo : category0.getCategory_list()) {
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
                                    activity.skip(NewsDetailActivity.class, newsVo);
                                }
                            });
                            ll_recommend.addView(view);
                        }

                        final HomeVo.Category category1 = homeVo.getNews().get(1);
                        tv_diary_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.skip(DiaryListActivity.class, category1.getCategory_id(), "宝宝日记");
                            }
                        });
                        for (final NewsVo newsVo : category1.getCategory_list()) {
                            View view = activity.makeView(R.layout.item_diary);
                            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                            TextView tv_come_form = (TextView) view.findViewById(R.id.tv_come_form);
                            TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(iv_image);
                            tv_title.setText(newsVo.getTitle());
                            tv_come_form.setText(Utils.formatTime(newsVo.getCreate_time() + "000", "yyyy-MM-dd"));
                            tv_count.setText(newsVo.getView() + "");
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    activity.skip(NewsDetailActivity.class, newsVo);
                                }
                            });
                            ll_diary.addView(view);
                        }

                        final HomeVo.Category category2 = homeVo.getNews().get(2);
                        tv_doctor_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.skip(MedicalOnlineActivity.class, "6");
                            }
                        });
                        for (final NewsVo newsVo : category2.getCategory_list()) {
                            View view = activity.makeView(R.layout.item_dedical_online);
                            ImageView iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
                            TextView tv_hospital_name = (TextView) view.findViewById(R.id.tv_hospital_name);
                            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
                            TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
                            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getCover())).into(iv_thumb);
                            tv_hospital_name.setText(newsVo.getTitle());
                            if (!Utils.isEmpty(newsVo.getLevel())) {
                                ratingBar.setRating(Float.parseFloat(newsVo.getLevel()));
                            } else {
                                ratingBar.setRating(3);
                            }
                            tv_address.setText(newsVo.getAddress());
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    activity.skip(HospitalDetailActivity.class,newsVo.getId());
                                }
                            });
                            ll_doctor.addView(view);
                        }


                        final HomeVo.Category category3 = homeVo.getNews().get(3);
                        tv_knowledge_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.skip(NewsListActivity.class, category3.getCategory_id(), "育儿知识");
                            }
                        });
                        for (final NewsVo newsVo : category3.getCategory_list()) {
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
                                    activity.skip(NewsDetailActivity.class, newsVo);
                                }
                            });
                            ll_knowledge.addView(view);
                        }

                        final HomeVo.Category category4 = homeVo.getNews().get(4);
                        tv_my_circle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.skip(NewsListActivity.class, category4.getCategory_id(), "我的圈圈");
                            }
                        });
                        for (final NewsVo newsVo : category4.getCategory_list()) {
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
                                    activity.skip(NewsDetailActivity.class, newsVo);
                                }
                            });
                            ll_circle.addView(view);
                        }

                        final HomeVo.Category category5 = homeVo.getNews().get(5);
                        tv_guess.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.skip(NewsListActivity.class, category5.getCategory_id(), "猜你喜欢的");
                            }
                        });
                        for (final NewsVo newsVo : category5.getCategory_list()) {
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
                                    activity.skip(NewsDetailActivity.class, newsVo);
                                }
                            });
                            ll_guess.addView(view);
                        }

                    }

                } else {
                    activity.toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                ((MainActivity) activity).dismissDialog();
            }
        });
    }


    @OnClick(R.id.tv_doctor)
    void doctor(View view) {
        activity.skip(MedicalOnlineActivity.class, "6");
    }

    @OnClick(R.id.iv_right)
    void search(View view) {
        activity.skip(SearchActivity.class);
    }

    @OnClick(R.id.tv_knowledge)
    void knowledge(View view) {
        activity.skip(NewsListActivity.class, "3", "育儿知识");
    }

    @OnClick(R.id.tv_circle)
    void tv_circle(View view) {
        activity.skip(NewsListActivity.class, "4", "我的圈圈");
    }

    @OnClick(R.id.iv_info)
    void showInfo(View view)
    {
        activity.skip(PersonalCenter.class);
    }
    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_page;
    }
}
