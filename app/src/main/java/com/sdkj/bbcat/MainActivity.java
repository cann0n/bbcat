package com.sdkj.bbcat;

import android.os.Bundle;
import android.view.KeyEvent;

import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.utils.SpUtil;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.bean.UserInfosBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.fragment.BraceletPage;
import com.sdkj.bbcat.fragment.CommunityPage;
import com.sdkj.bbcat.fragment.FragmentMine;
import com.sdkj.bbcat.fragment.HomePage;
import com.sdkj.bbcat.fragment.NewsPage;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class MainActivity extends TabUiActivity {
    
    public static int Flag=0;

    @Override
    public List<String> initTabNames() {
        List<String> names = new ArrayList<>();
        names.add("首页");
        names.add("育儿知识");
        names.add("手环");
        names.add("我的圈圈");
        names.add("个人中心");
        return names;
    }

    @Override
    public List<Integer> initTabIconResids() {
        List<Integer> icons = new ArrayList<>();
        icons.add(R.drawable.icon_home_pressed);
        icons.add(R.drawable.icon_class_pressed);
        icons.add(R.drawable.icon_msg_pressed);
        icons.add(R.drawable.icon_myself_pressed);
        icons.add(R.drawable.icon_info_press);
        return icons;
    }

    @Override
    public List<Integer> initUnCheckedTabIconResids() {
        List<Integer> icons = new ArrayList<>();
        icons.add(R.drawable.icon_home);
        icons.add(R.drawable.icon_class);
        icons.add(R.drawable.icon_msg);
        icons.add(R.drawable.icon_myself);
        icons.add(R.drawable.icon_people);
        return icons;
    }

    @Override
    public String initCheckedTextColor() {
        return "#ea5413";
    }

    @Override
    public String initUnCheckedTextColor() {
        return "#bababa";
    }

    @Override
    public BaseFragment initPage1() {
        return new HomePage();
    }

    @Override
    public BaseFragment initPage2() {
        return new NewsPage();
    }

    @Override
    public BaseFragment initPage3() {
        return new BraceletPage();
    }

    @Override
    public BaseFragment initPage4() {
        return new CommunityPage();
    }

    @Override
    public BaseFragment initPage5() {
        return new FragmentMine();
    }

    @Override
    public void initBusiness() {
        super.initBusiness();
        if (SimpleUtils.isLogin(activity)) {
            UserInfosBean infosBean = new UserInfosBean();
            SpUtil sp_login = new SpUtil(activity, Const.SP_NAME);
            infosBean.setBirthday(sp_login.getStringValue("birthday"));
            infosBean.setNickname(sp_login.getStringValue(Const.NICKNAME));
            infosBean.setSex(sp_login.getStringValue("sex"));
            infosBean.setBaby_status(sp_login.getStringValue("baby_status"));
            infosBean.setAvatar(sp_login.getStringValue(Const.AVATAR));
            LoginBean bean = new LoginBean();
            bean.setUserInfo(infosBean);
            bean.setToken(sp_login.getStringValue(Const.TOKEN));
            bean.setUid(sp_login.getStringValue(Const.UID));
            ((BbcatApp) getApplication()).setmUser(bean);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void switchFragment(int viewId) {
        super.switchFragment(viewId);
//        if (viewId == R.id.tv_tab3) {
//            BraceletPage.ChangeEvent event = new BraceletPage.ChangeEvent();
//            event.setPosition(0);
//            EventBus.getDefault().post(event);
//        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    toast("再按一次退出程序");
                    firstTime = secondTime;
                    return true;
                } else {
                    try {
                        BbcatApp app = (BbcatApp) getApplication();
                        app.finishAll();
                        finish();
                    } catch (Exception ex) {

                    }
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
