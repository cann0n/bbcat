package com.sdkj.bbcat.activity;

import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.widget.GridViewPage;

import java.util.ArrayList;
import java.util.List;

public class PublishActivity extends BaseActivity {
    @ViewInject(R.id.rl_label)
    private RelativeLayout rl_label;

    @ViewInject(R.id.tv_label)
    private TextView tv_label;

    @ViewInject(R.id.ll_publish)
    private LinearLayout ll_publish;

    private View popupView;
    private PopupWindow popupWindow;

    private int selectLabelIndex;

    //标签数据
    private List<String> labellist;

    @Override
    public void initBusiness() {
        labellist=new ArrayList<>();
        //模拟数据
        for (int i=0;i<20;i++){

            labellist.add("孕前检查"+i);
        }
    }

    @OnClick(R.id.rl_label)
    void clickLabel(View view) {
        showLabel();
    }

    /**
     * 显示label的popupwindow
     *
     */
    private void showLabel() {
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
            loadDataToGridViewPage(gridViewPage,labellist);


            tv_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindow.dismiss();
                }
            });
            tv_submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    tv_label.setText(labellist.get(selectLabelIndex));
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
    private void loadDataToGridViewPage(GridViewPage gridViewPage, final List<String> list) {
        gridViewPage.setModuleMenuRowCol(3,3);
        GridViewPage.GirdViewPageAdapter adapter = new GridViewPage.GirdViewPageAdapter(activity, labellist) {
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
                holder.tv.setText(list.get(position));
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


    @OnClick(R.id.iv_back)
    void back(View view) {
        finish();
    }


    @Override
    public int setLayoutResID() {
        return R.layout.activity_publish;
    }
}
