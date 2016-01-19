package com.sdkj.bbcat.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.news.NewsListActivity;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by ${Rhino} on 2016/1/13 19:57
 */
public class MoreTagsActivity extends SimpleActivity {

    @ViewInject(R.id.ll_label)
    private LinearLayout ll_label;

    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("热门标签");
        queryLabel();
    }

    private void queryLabel() {
        showDialog();
        HttpUtils.getJSONObject(activity, Const.HOT_TAGS, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo<CircleVo.HotTagsVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<CircleVo.HotTagsVo> tags = respVo.getListData(obj, CircleVo.HotTagsVo.class);
                    if (Utils.isEmpty(tags)) {
                        return;
                    }
                    ll_label.removeAllViews();
                    for (final CircleVo.HotTagsVo tag : tags) {
                        View view = activity.makeView(R.layout.item_qunzu);
                        ImageView iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
                        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                        TextView tv_desc = (TextView) view.findViewById(R.id.tv_desc);
                        TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
                        TextView tv_jiaru = (TextView) view.findViewById(R.id.tv_jiaru);
                        tv_num.setVisibility(View.GONE);
                        tv_jiaru.setVisibility(View.GONE);
                        Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(tag.getCover())).into(iv_avatar);
                        tv_name.setText(tag.getTitle());
                        tv_desc.setText(tag.getJoined_person() + "人参与");

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.skip(NewsListActivity.class, tag.getId(), tag.getTitle());
                            }
                        });
                        ll_label.addView(view);
                    }
                } else {
                    toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {

            }
        });
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_more_tags;
    }
}
