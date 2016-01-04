package com.sdkj.bbcat;

import android.os.Bundle;

import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.utils.SpUtil;
import com.sdkj.bbcat.bean.LoginBean;
import com.sdkj.bbcat.bean.UserInfosBean;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.fragment.BraceletPage;
import com.sdkj.bbcat.fragment.CommunityPage;
import com.sdkj.bbcat.fragment.HomePage;
import com.sdkj.bbcat.fragment.NewsPage;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends TabUiActivity {

    @Override
    public List<String> initTabNames() {
        List<String> names = new ArrayList<>();
        names.add("首页");
        names.add("育儿知识");
        names.add("手环");
        names.add("社区");
        return names;
    }

    @Override
    public List<Integer> initTabIconResids() {
        List<Integer> icons = new ArrayList<>();
        icons.add(R.drawable.icon_home_pressed);
        icons.add(R.drawable.icon_class_pressed);
        icons.add(R.drawable.icon_msg_pressed);
        icons.add(R.drawable.icon_myself_pressed);
        return icons;
    }

    @Override
    public List<Integer> initUnCheckedTabIconResids() {
        List<Integer> icons = new ArrayList<>();
        icons.add(R.drawable.icon_home);
        icons.add(R.drawable.icon_class);
        icons.add(R.drawable.icon_msg);
        icons.add(R.drawable.icon_myself);
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
        return null;
    }

    @Override
    public void initBusiness()
    {
        super.initBusiness();
        SpUtil sp_login =  new SpUtil(activity,Const.AL_LOGIN);
        if(sp_login.getBoolValueFalse("isLogin"))
        {
            UserInfosBean infosBean = new UserInfosBean();
            infosBean.setBirthday(sp_login.getStringValue("birthday"));
            infosBean.setNickname(sp_login.getStringValue("nickname"));
            infosBean.setSex(sp_login.getStringValue("sex"));
            LoginBean bean = new LoginBean();
            bean.setUserInfo(infosBean);
            bean.setToken(sp_login.getStringValue("token"));
            ((BbcatApp)getApplication()).setmUser(bean);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {

    }
}
