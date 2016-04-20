package com.sdkj.bbcat.fragment;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.easeui.domain.EaseUser;
import com.huaxi100.networkapp.adapter.FragPagerAdapter;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.fragment.FragmentVo;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.AppUtils;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.PublishActivity;
import com.sdkj.bbcat.activity.community.AroundPeopleActivity;
import com.sdkj.bbcat.bean.FriendVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.hx.DemoDBManager;
import com.sdkj.bbcat.hx.UserDao;
import com.sdkj.bbcat.hx.activity.AddContactActivity;
import com.sdkj.bbcat.hx.activity.NewGroupActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 */
public class CommunityPage extends BaseFragment {

    @ViewInject(R.id.vp_pager)
    private ViewPager vp_pager;

    @ViewInject(R.id.tv_guys)
    private TextView tv_guys;


    @ViewInject(R.id.tv_club)
    private TextView tv_club;

    @ViewInject(R.id.v_line1)
    private View v_line1;

    @ViewInject(R.id.v_line2)
    private View v_line2;

    @ViewInject(R.id.iv_edit)
    private ImageView iv_edit;

    private View popupClub;

    private PopupWindow popupWindowClub;

    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);
        
        ArrayList<FragmentVo> pageVo = new ArrayList<FragmentVo>();
        pageVo.add(new FragmentVo(new FragmentMycircle(), "我的圈"));
        pageVo.add(new FragmentVo(new FragmentMyFriend(), "猫友"));
        FragPagerAdapter adapter = new FragPagerAdapter(activity.getSupportFragmentManager(), pageVo);
        vp_pager.setAdapter(adapter);
        vp_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeBtn(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        changeBtn(0);
        EMChatManager.getInstance().addConnectionListener(connectionListener);
    }
    

    @OnClick(R.id.tv_guys)
    void left(View view) {
        vp_pager.setCurrentItem(0);
    }

    @OnClick(R.id.tv_club)
    void right(View view) {
        vp_pager.setCurrentItem(1);
    }

    @OnClick(R.id.iv_edit)
    void select(View view) {
        showClubWindow();
    }

    private void changeBtn(int position) {
        if (position == 0) {
            tv_guys.setTextColor(Color.parseColor("#ffffff"));
            tv_club.setTextColor(Color.parseColor("#ffffff"));

            v_line2.setVisibility(View.INVISIBLE);
            v_line1.setVisibility(View.VISIBLE);
        } else {
            tv_guys.setTextColor(Color.parseColor("#ffffff"));
            tv_club.setTextColor(Color.parseColor("#ffffff"));

            v_line2.setVisibility(View.VISIBLE);
            v_line1.setVisibility(View.INVISIBLE);
        }
    }

    private void showClubWindow() {
        if (popupClub == null) {
            int w = AppUtils.getWidth(activity) * 2 / 5;
            popupClub = activity.makeView(R.layout.club_popup_dialog);
            popupWindowClub = new PopupWindow(popupClub, w, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindowClub.setFocusable(true);
            popupWindowClub.setOutsideTouchable(true);
            popupWindowClub.setOutsideTouchable(true);
            popupWindowClub.setBackgroundDrawable(new BitmapDrawable());
            popupWindowClub.setAnimationStyle(R.style.AnimationPreview);

            LinearLayout window = (LinearLayout) popupClub.findViewById(R.id.ll_window);
            final TextView tv_default = (TextView) popupClub.findViewById(R.id.tv_default);
            final TextView tv_rock_bar = (TextView) popupClub.findViewById(R.id.tv_rock_bar);
            final TextView tv_disco = (TextView) popupClub.findViewById(R.id.tv_disco);

            tv_default.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowClub.dismiss();
                    activity.skip(AddContactActivity.class);
                }
            });
            tv_rock_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowClub.dismiss();
                    activity.skip(AroundPeopleActivity.class);
                }
            });
            tv_disco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowClub.dismiss();
                    activity.skip(NewGroupActivity.class);
                }
            });

            window.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    popupWindowClub.dismiss();
                }
            });
        }
        if (popupWindowClub.isShowing()) {
            popupWindowClub.dismiss();
        } else {
            popupWindowClub.showAsDropDown(iv_edit, -220, 20);
        }
    }

    @OnClick(R.id.iv_left)
    void publish(View viewREAD_PHONE_STATE) {
        activity.skip(PublishActivity.class,"");
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_community;
    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.CONNECTION_CONFLICT) {
                handler.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(1);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(2);
        }
    };

    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    EventBus.getDefault().post(new ConnectEvent(0));
                    EventBus.getDefault().post(new ConnectEvent(3));
                    break;
                case 2:
                    EventBus.getDefault().post(new ConnectEvent(2));
                    break;
                case 1:
                    EventBus.getDefault().post(new ConnectEvent(1));
                    break;

                default:
                    break;
            }
        }
    };
    
    public void onEventMainThread(CommunityPage.ConnectEvent event){
        if(event.getType()==3){
            activity.toast("账号已经在其他设备登录");
            SimpleUtils.loginOut(activity);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EMChatManager.getInstance().removeConnectionListener(connectionListener);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    
    
    public  static class ConnectEvent{
        /**
         * type=0:已经被在其他地方登陆，执行退出操作
         * type=1:断开服务器
         * type=2:服务器重连
         * type=3:退出登录
         * type=4:有有好友邀请
         */
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public ConnectEvent(int type) {
            this.type = type;
        }
    }
}
