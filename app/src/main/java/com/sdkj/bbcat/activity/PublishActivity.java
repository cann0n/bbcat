package com.sdkj.bbcat.activity;

import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONArrayListener;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.CircleTagVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.GridViewPage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONArrayListener;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.CircleTagVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class PublishActivity extends SimpleActivity {
    @ViewInject(R.id.rl_label)
    private RelativeLayout rl_label;


    @ViewInject(R.id.ll_publish)
    private LinearLayout ll_publish;

    private View popupView;
    private PopupWindow popupWindow;

    private int selectLabelIndex;

    @ViewInject(R.id.et_title)
    private EditText et_title;

    @ViewInject(R.id.et_content)
    private EditText et_content;

    @ViewInject(R.id.tv_address)
    private TextView tv_address;

    @ViewInject(R.id.tv_label)
    private TextView tv_label;

    private List<CircleTagVo> tags;

    @Override
    public void initBusiness() {

    }


    /**
     * 显示label的popupwindow
     *
     */
    private void showLabel() {
        if(Utils.isEmpty(tags)){
            queryLabel();
            return;
        }
        if (popupView == null) {
            popupView = makeView(R.layout.pupopwindow_selectlabel);
            Rect rect = new Rect();
            this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int statusBarHeight = rect.top;
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, AppUtils.getHeight(activity) - statusBarHeight);
            LinearLayout window = (LinearLayout) popupView.findViewById(R.id.ll_top);
            LinearLayout content = (LinearLayout) popupView.findViewById(R.id.ll_bottom);
            TextView tv_cancel = (TextView) popupView.findViewById(R.id.tv_cancel);
            TextView tv_submit = (TextView) popupView.findViewById(R.id.tv_submit);
            GridViewPage gridViewPage= (GridViewPage) popupView.findViewById(R.id.viewpage_label);

            //载入数据到gridViewPage中
            loadDataToGridViewPage(gridViewPage,tags);


            tv_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindow.dismiss();
                }
            });
            tv_submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    tv_label.setText(tags.get(selectLabelIndex).getTitle());
                    tv_label.setTag(tags.get(selectLabelIndex).getId());
                    popupWindow.dismiss();
                }
            });
            window.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindow.dismiss();
                }
            });
            content.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                }
            });
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAtLocation(ll_publish, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    //载入数据到gridViewPage中
    private void loadDataToGridViewPage(GridViewPage gridViewPage, final List<CircleTagVo> list) {
        gridViewPage.setModuleMenuRowCol(3,3);
        GridViewPage.GirdViewPageAdapter adapter = new GridViewPage.GirdViewPageAdapter(activity, list) {
            private int selectindex = selectLabelIndex;

            @Override
            public View getView(final int position, View convertView) {
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = activity.makeView(R.layout.item_label);
                    holder = new ViewHolder();
                    holder.tv = (TextView) convertView.findViewById(R.id.tv_item_label);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tv.setText(list.get(position).getTitle());
                if (selectindex == position) {
                    holder.tv.setBackgroundResource(R.drawable.bg_tv_orange);
                    holder.tv.setTextColor(getColorRes(R.color.color_white));
                } else {
                    holder.tv.setBackgroundResource(R.drawable.bg_tv_white);
                    holder.tv.setTextColor(getColorRes(R.color.color333));
                }
                holder.tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectindex = position;
                        selectLabelIndex=position;
                        notifyDataSetChanged(position);
                    }
                });
                return convertView;
            }

            /*ViewHolder类*/
            final class ViewHolder {
                public TextView tv;
            }

        };
        gridViewPage.setAdapter(adapter);
    }

    @OnClick(R.id.tv_publish)
    void publish(View view) {
        if (Utils.isEmpty(et_title.getText().toString())) {
            toast("请输入标题");
            return;
        }
        if (Utils.isEmpty(et_content.getText().toString())) {
            toast("请输入内容");
            return;
        }
        if (tv_label.getTag()==null){
            toast("请选择标签");
            return;
        }
        if(!SimpleUtils.isLogin(activity)){
            skip(LoginActivity.class);
            return;
        }
        PostParams params = new PostParams();
        showDialog();
        params.put("title", et_title.getText().toString());
        params.put("content", et_content.getText().toString());
        params.put("address", tv_address.getText().toString());
        params.put("category_id", tv_label.getTag().toString());//标签id
        params.put("pictures", "1357");
        HttpUtils.postJSONObject(activity, Const.PUBLIC_CIRCLE, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            
            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    toast("动态已发布");
                    finish();
                } else {
                    toast(respVo.getMessage());
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
            }
        });

    }

    @OnClick(R.id.rl_label)
    void selectLabel(View view) {
        //判断tags是否为空
        showLabel();
    }

    @OnClick(R.id.iv_back)
    void back(View view) {
        finish();
    }


    private void queryLabel() {
        showDialog();
        HttpUtils.getJSONObject(activity, Const.GET_TAGS, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo<CircleTagVo> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    tags = respVo.getListData(obj, CircleTagVo.class);
                    //显示label的popupwindow
                    showLabel();
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
        return R.layout.activity_publish;
    }
}
