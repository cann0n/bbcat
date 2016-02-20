package com.sdkj.bbcat.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.sdkj.bbcat.activity.bracelet.DiseaseRecordActivity;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.bean.CircleTagVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.bean.UploadFileVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.FlowLayout;
import com.sdkj.bbcat.widget.GlideImageLoader;
import com.sdkj.bbcat.widget.GridViewPage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import java.util.Map;
import java.util.Set;

import cn.finalteam.galleryfinal.GalleryConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.greenrobot.event.EventBus;

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

    @ViewInject(R.id.fl_pics)
    private FlowLayout fl_pics;

    private Map<String, String> ids = new HashMap<>();

    @Override
    public void initBusiness() {
        startBaiduLocation();
        int width = (AppUtils.getWidth(activity) - 80) / 3;
        View view = makeView(R.layout.item_photo);
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
        lp.leftMargin = 18;
        view.setLayoutParams(lp);

        iv_image.setImageResource(R.drawable.icon_addzhaopian);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryConfig.Builder builder = new GalleryConfig.Builder(activity);
                builder.imageloader(new GlideImageLoader());
                builder.singleSelect();
                builder.enableEdit();
                builder.enableRotate();
                builder.showCamera();
                GalleryConfig config = builder.build();
                GalleryFinal.open(config);
            }
        });
        fl_pics.addView(view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryFinal.GALLERY_REQUEST_CODE) {
            if (resultCode == GalleryFinal.GALLERY_RESULT_SUCCESS) {
                List<PhotoInfo> photoInfoList = (List<PhotoInfo>) data.getSerializableExtra(GalleryFinal.GALLERY_RESULT_LIST_DATA);
                if (photoInfoList != null) {
                    String photo = photoInfoList.get(0).getPhotoPath();
                    uploadImage(photo);
                }
            }
        }
    }

    private void uploadImage(final String path) {
        showDialog();
        final PostParams params = new PostParams();
        params.put("file", new File(path));
        HttpUtils.postJSONObject(activity, Const.UPLOAD_IMAGE, params, new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                dismissDialog();
                RespVo<UploadFileVo> resp = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (resp.isSuccess()) {
                    UploadFileVo fileVo = resp.getData(jsonObject, UploadFileVo.class);
                    ids.put(fileVo.getId() + "", fileVo.getId() + "");
                    int width = (AppUtils.getWidth(activity) - 80) / 3;
                    View view = makeView(R.layout.item_photo);
                    ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
                    lp.leftMargin = 18;
                    view.setLayoutParams(lp);
                    view.setTag(path);
                    iv_image.setImageBitmap(Utils.getLoacalBitmap(path));
                    fl_pics.addView(view, 0);
                    if (fl_pics.getChildCount() == 4) {
                        fl_pics.removeViewAt(3);
                    }
                } else {
                    toast("获取图片失败,请重试");
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
            }
        });
    }

    /**
     * 显示label的popupwindow
     */
    private void showLabel() {
        if (Utils.isEmpty(tags)) {
            queryLabel();
            return;
        } else {
            CircleTagVo tag1 = new CircleTagVo();
            tag1.setTitle("普通日记");
            tag1.setType("1");
//            CircleTagVo tag2 = new CircleTagVo();
//            tag2.setTitle("宝宝第一次");
//            tag2.setType("2");
            CircleTagVo tag3 = new CircleTagVo();
            tag3.setTitle("疾病记录");
            tag3.setType("3");
            tags.add(tag1);
//            tags.add(tag2);
            tags.add(tag3);
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
            GridViewPage gridViewPage = (GridViewPage) popupView.findViewById(R.id.viewpage_label);

            //载入数据到gridViewPage中
            loadDataToGridViewPage(gridViewPage, tags);


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
                    if (Utils.isEmpty(tags.get(selectLabelIndex).getId())) {
                        tv_label.setTag(R.id.tag_first, tags.get(selectLabelIndex).getType());
                    } else {
                        tv_label.setTag(R.id.tag_two, tags.get(selectLabelIndex).getId());
                    }
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
        gridViewPage.setModuleMenuRowCol(3, 3);
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
                        selectLabelIndex = position;
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
        if (tv_label.getTag(R.id.tag_first) == null && tv_label.getTag(R.id.tag_two) == null) {
            toast("请选择标签");
            return;
        }
        if (!SimpleUtils.isLogin(activity)) {
            skip(LoginActivity.class);
            return;
        }
        PostParams params = new PostParams();
        showDialog();
        params.put("title", et_title.getText().toString());
        params.put("content", et_content.getText().toString());
        params.put("address", tv_address.getText().toString());
        params.put("private", "0");//标签id
        if (tv_label.getTag(R.id.tag_two) != null) {
            params.put("category_id", tv_label.getTag(R.id.tag_two).toString());//标签id
        }
        if (tv_label.getTag(R.id.tag_first) != null) {
            params.put("type", tv_label.getTag(R.id.tag_first).toString());//标签id
            params.put("category_id", "2");//标签id
            params.put("private", "1");//标签id
        }

        String pics = "";
        Set keys = ids.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String id = ids.get(key);
            if (Utils.isEmpty(pics)) {
                pics = id;
            } else {
                pics = pics + "," + id;
            }
        }
        params.put("pictures", pics);
        HttpUtils.postJSONObject(activity, Const.PUBLIC_CIRCLE, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {

            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    toast("动态已发布");
                    EventBus.getDefault().post(new DiseaseRecordActivity.RefreshEvent());
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

    private void startBaiduLocation() {
        LocationClient mLocationClient = new LocationClient(activity.getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener(mLocationClient));    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private class MyLocationListener implements BDLocationListener {

        LocationClient mLocationClient;

        public MyLocationListener(LocationClient mLocationClient) {
            this.mLocationClient = mLocationClient;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (!Utils.isEmpty(location.getLatitude() + "") && !Utils.isEmpty(location.getLongitude() + "")) {
                mLocationClient.stop();
                tv_address.setText(location.getCity());
            }
        }
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_publish;
    }
}
