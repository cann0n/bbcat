package com.sdkj.bbcat.activity.doctor;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huaxi100.networkapp.adapter.ParentVo;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONArrayListener;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.activity.MedicalOnlineActivity;
import com.sdkj.bbcat.adapter.AreaListAdapter;
import com.sdkj.bbcat.bean.AreaVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/3 0003.
 * 选择城市
 */
public class SelectAreaActivity extends SimpleActivity {
    @ViewInject(R.id.el_chapter)
    private ExpandableListView lv_list;

    private AreaListAdapter adapter;

    @Override
    public void initBusiness() {
        new TitleBar(activity).back().setTitle("选择城市");
        adapter = new AreaListAdapter(activity, new ArrayList<AreaVo>());
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AreaVo vo = (AreaVo) adapterView.getAdapter().getItem(i);
                MedicalOnlineActivity.AreaEvent event = new MedicalOnlineActivity.AreaEvent();
                event.setAreaVo(vo);
                EventBus.getDefault().post(event);
                finish();
            }
        });

        lv_list.setGroupIndicator(null);
        lv_list.setAdapter(adapter);

        lv_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(final int i) {
                if (Utils.isEmpty(((ParentVo) adapter.getGroup(i)).getChild()) && !((AreaVo) adapter.getGroup(i)).isShown()) {
                    showDialog();
                    getCity(((AreaVo) adapter.getGroup(i)).getId(), i);
                }

            }
        });
        getCity("", 0);
    }

    private void getCity(final String id, final int position) {
        PostParams params = new PostParams();
        if (!Utils.isEmpty(id)) {
            params.put("id", id);
        }
        HttpUtils.postJSONObject(activity, Const.GET_CITY, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject obj) {
                dismissDialog();
                RespVo<AllCities> respVo = GsonTools.getVo(obj.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    AllCities cities = respVo.getData(obj, AllCities.class);
                    if (Utils.isEmpty(id)) {
                        adapter.addParents(cities.getAllProvince());
                    } else {
                        if (!Utils.isEmpty(cities.getAllProvince())) {
                            adapter.addChilds(position, cities.getAllProvince());
                        } else {
                            ((AreaVo) adapter.getGroup(position)).setIsShown(true);
                        }
                    }
                }
            }

            @Override
            public void doFailed() {
                dismissDialog();
            }

        });
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_select_area;
    }

    public static class AllCities {
        private List<AreaVo> hotCity;
        private List<AreaVo> allCity;
        private List<AreaVo> allProvince;

        public List<AreaVo> getAllProvince() {
            return allProvince;
        }

        public void setAllProvince(List<AreaVo> allProvince) {
            this.allProvince = allProvince;
        }

        public List<AreaVo> getHotCity() {
            return hotCity;
        }

        public void setHotCity(List<AreaVo> hotCity) {
            this.hotCity = hotCity;
        }

        public List<AreaVo> getAllCity() {
            return allCity;
        }

        public void setAllCity(List<AreaVo> allCity) {
            this.allCity = allCity;
        }
    }
}
