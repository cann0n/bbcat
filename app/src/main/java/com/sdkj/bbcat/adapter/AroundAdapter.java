package com.sdkj.bbcat.adapter;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.AroundPeopleVo;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.hx.DemoHelper;

import java.util.List;

/**
 * Created by ${Rhino} on 2015/12/10 14:24
 */
public class AroundAdapter extends UltimatCommonAdapter<AroundPeopleVo, AroundAdapter.ViewHolder> {

    public AroundAdapter(BaseActivity activity, List<AroundPeopleVo> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.item_around);
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final AroundPeopleVo newsVo = getItem(position);

            Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(newsVo.getAvatar())).into(holder.iv_avatar);
            holder.tv_desc.setText(newsVo.getDistance());

            holder.tv_name.setText(newsVo.getNickname());
            holder.tv_jiaru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public void addContact( final String mobile) {
        if (EMChatManager.getInstance().getCurrentUser().equals(mobile)) {
            new EaseAlertDialog(activity, R.string.not_add_myself).show();
            return;
        }

        if (DemoHelper.getInstance().getContactList().containsKey(mobile)) {
            //提示已在好友列表中(在黑名单列表里)，无需添加
            if (EMContactManager.getInstance().getBlackListUsernames().contains(mobile)) {
                new EaseAlertDialog(activity, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(activity, R.string.This_user_is_already_your_friend).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        String stri = activity.getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo写死了个reason，实际应该让用户手动填入
                    String s = activity.getResources().getString(R.string.Add_a_friend);
                    EMContactManager.getInstance().addContact(mobile, s);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = activity.getResources().getString(R.string.send_successful);
                            activity.toast(s1);
                        }
                    });
                } catch (final Exception e) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = activity.getResources().getString(R.string.Request_add_buddy_failure);
                            activity.toast(s2 + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {

        ImageView iv_avatar;
        TextView tv_name;
        TextView tv_desc;
        TextView tv_jiaru;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
