package com.sdkj.bbcat.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.BodyFeaBean;
import com.sdkj.bbcat.bean.CircleVo;
import com.sdkj.bbcat.bean.FollowVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.CircleImageView;

import org.json.JSONObject;

import java.util.List;


public class FollowAdapter extends UltimatCommonAdapter<FollowVo, FollowAdapter.ViewHolder> {
    public FollowAdapter(BaseActivity activity, List<FollowVo> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_follow);
    }

    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, final int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final FollowVo vo = getItem(position);
            holder.tv_name.setText(vo.getNickname());
            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(vo.getAvatar())).into(holder.iv_avatar);
            holder.tv_jiaru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity).setTitle("确认不在关注" + vo.getNickname() + "吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doLike(vo.getUid(), position);
                        }
                    }).setNegativeButton("取消", null).show();
                }
            });
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {
        CircleImageView iv_avatar;
        TextView tv_name;
        TextView tv_jiaru;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void doLike(final String id, final int follow) {
        SpUtil sp = new SpUtil(activity, Const.SP_NAME);
        if (sp.getStringValue(Const.UID).equals(id)) {
            activity.toast("不能对自己进行操作");
            return;
        }
        PostParams param = new PostParams();
        param.put("to_uid", id);
        param.put("uid", sp.getStringValue(Const.UID));
        HttpUtils.postJSONObject(activity, Const.DO_FOLLOW, SimpleUtils.buildUrl(activity, param), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                RespVo<String> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    remove(follow);
                } else if (respVo.isNeedLogin()) {
                    activity.skip(LoginActivity.class);
                }
            }

            @Override
            public void doFailed() {

            }
        });
    }
}