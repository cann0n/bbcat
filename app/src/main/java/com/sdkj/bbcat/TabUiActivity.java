package com.sdkj.bbcat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.List;


public abstract class TabUiActivity extends SimpleActivity {

    private TextView tv_tab1;

    private TextView tv_tab2;

    private TextView tv_tab3;

    private TextView tv_tab4;

    private TextView tv_tab5;

    private SparseArray<Fragment> fragContainer = new SparseArray<Fragment>();

    private static Fragment lastFragment;

    private List<String> tabNames;

    private List<Integer> tabIconResids;
    private List<Integer> unCheckedTabIconResids;
    private String checkedTextColor;
    private String unCheckedTextColor;


    public abstract List<String> initTabNames();

    public abstract List<Integer> initTabIconResids();

    public abstract List<Integer> initUnCheckedTabIconResids();

    public abstract String initCheckedTextColor();

    public abstract String initUnCheckedTextColor();

    @Override
    public void initBusiness() {
        tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
        tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
        tv_tab3 = (TextView) findViewById(R.id.tv_tab3);
        tv_tab4 = (TextView) findViewById(R.id.tv_tab4);
        tv_tab5 = (TextView) findViewById(R.id.tv_tab5);

        tv_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(R.id.tv_tab1);
            }
        });
        tv_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(R.id.tv_tab2);
            }
        });
        tv_tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(R.id.tv_tab3);
            }
        });
        tv_tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(R.id.tv_tab4);
            }
        });
        tv_tab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(R.id.tv_tab5);
            }
        });


        tabNames = initTabNames();
        tabIconResids = initTabIconResids();
        unCheckedTabIconResids = initUnCheckedTabIconResids();
        checkedTextColor = initCheckedTextColor();
        unCheckedTextColor = initUnCheckedTextColor();
        if (Utils.isEmpty(tabNames) || Utils.isEmpty(tabIconResids)) {
            throw new RuntimeException("请传入底部导航栏的tab 的名字或者resid图标list");
        }

        int tabsSize = tabIconResids.size();
        if (tabsSize < 3 || tabsSize > 5) {
            throw new RuntimeException("tab的数量为3到5个，请检查是否在3到5这个范围");
        }

        initTabs(tabsSize);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = initPage1();
        lastFragment = fragment;
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragContainer.put(R.id.tv_tab1, fragment);
        fragmentTransaction.commit();

        switchFragment(R.id.tv_tab1);
    }

    private void initTabs(int tabsSize) {

        tv_tab1.setText(tabNames.get(0));

        tv_tab2.setText(tabNames.get(1));
        tv_tab3.setText(tabNames.get(2));
        if (tabsSize == 3) {
            tv_tab4.setVisibility(View.GONE);
            tv_tab5.setVisibility(View.GONE);

            tabNames.add("tab4");
            tabNames.add("tab5");
            tabIconResids.add(R.drawable.icon_default);
            tabIconResids.add(R.drawable.icon_default);

            unCheckedTabIconResids.add(R.drawable.icon_default);
            unCheckedTabIconResids.add(R.drawable.icon_default);

        } else if (tabsSize == 4) {
            tv_tab4.setText(tabNames.get(3));
            tv_tab5.setVisibility(View.GONE);

            tabNames.add("tab4");
            tabIconResids.add(R.drawable.icon_default);

            unCheckedTabIconResids.add(R.drawable.icon_default);
        } else {
            tv_tab4.setText(tabNames.get(3));
            tv_tab5.setText(tabNames.get(4));
        }
    }


    public void switchFragment(int viewId) {
        if (viewId == R.id.tv_3 || viewId == R.id.tv_tab4|| viewId == R.id.tv_tab5) {
            if (!SimpleUtils.isLogin(activity)) {
                skip(LoginActivity.class);
                return;
            }
        }
        changeTextStatus(viewId);
        Fragment fragment = null;
        if (viewId == R.id.tv_tab1) {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage1();
                fragContainer.put(viewId, fragment);
            }
        } else if (viewId == R.id.tv_tab2) {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage2();
                fragContainer.put(viewId, fragment);
            }
        } else if (viewId == R.id.tv_tab3) {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage3();
                fragContainer.put(viewId, fragment);
            }
        } else if (viewId == R.id.tv_tab4) {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage4();
                fragContainer.put(viewId, fragment);
            }
        } else {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage5();
                fragContainer.put(viewId, fragment);
            }
        }
        switchContent(lastFragment, fragment);
    }

    public void switchContent(Fragment pre, Fragment next) {
        if (lastFragment != next) {
            lastFragment = next;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!next.isAdded()) {
                transaction.hide(pre).add(R.id.fl_content, next).commit();
            } else {
                transaction.hide(pre).show(next).commit();
            }
        }
    }

    public Fragment getFragment(int viewId) {
        Fragment fragment = null;
        if (viewId == R.id.tv_tab1) {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage1();
                fragContainer.put(viewId, fragment);
            }
        } else if (viewId == R.id.tv_tab2) {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage2();
                fragContainer.put(viewId, fragment);
            }
        } else if (viewId == R.id.tv_tab3) {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage3();
                fragContainer.put(viewId, fragment);
            }
        } else if (viewId == R.id.tv_tab4) {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage4();
                fragContainer.put(viewId, fragment);
            }
        } else {
            fragment = fragContainer.get(viewId);
            if (fragment == null) {
                fragment = initPage5();
                fragContainer.put(viewId, fragment);
            }
        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (fragment.isAdded()) {
//            transaction.show(fragment).commit();
//        } else {
//            transaction.add(R.id.fl_content, fragment).commit();
//        }
        
        return fragment;
    }

    private void changeTextStatus(int resId) {
        if (resId == R.id.tv_tab1) {
            tv_tab1.setTextColor(Color.parseColor(checkedTextColor));
            tv_tab2.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab3.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab4.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab5.setTextColor(Color.parseColor(unCheckedTextColor));

            tv_tab1.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(tabIconResids.get(0)), null, null);
            tv_tab2.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(1)), null, null);
            tv_tab3.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(2)), null, null);
            tv_tab4.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(3)), null, null);
            tv_tab5.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(4)), null, null);
        } else if (resId == R.id.tv_tab2) {

            tv_tab2.setTextColor(Color.parseColor(checkedTextColor));
            tv_tab1.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab3.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab4.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab5.setTextColor(Color.parseColor(unCheckedTextColor));

            tv_tab1.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(0)), null, null);
            tv_tab2.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(tabIconResids.get(1)), null, null);
            tv_tab3.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(2)), null, null);
            tv_tab4.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(3)), null, null);
            tv_tab5.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(4)), null, null);

        } else if (resId == R.id.tv_tab3) {

            tv_tab3.setTextColor(Color.parseColor(checkedTextColor));
            tv_tab2.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab1.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab4.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab5.setTextColor(Color.parseColor(unCheckedTextColor));

            tv_tab1.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(0)), null, null);
            tv_tab2.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(1)), null, null);
            tv_tab3.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(tabIconResids.get(2)), null, null);
            tv_tab4.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(3)), null, null);
            tv_tab5.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(4)), null, null);

        } else if (resId == R.id.tv_tab4) {

            tv_tab4.setTextColor(Color.parseColor(checkedTextColor));
            tv_tab1.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab3.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab2.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab5.setTextColor(Color.parseColor(unCheckedTextColor));

            tv_tab1.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(0)), null, null);
            tv_tab2.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(1)), null, null);
            tv_tab3.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(2)), null, null);
            tv_tab4.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(tabIconResids.get(3)), null, null);
            tv_tab5.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(4)), null, null);
        } else if (resId == R.id.tv_tab5) {
            tv_tab5.setTextColor(Color.parseColor(checkedTextColor));
            tv_tab2.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab3.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab4.setTextColor(Color.parseColor(unCheckedTextColor));
            tv_tab1.setTextColor(Color.parseColor(unCheckedTextColor));

            tv_tab1.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(0)), null, null);
            tv_tab2.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(1)), null, null);
            tv_tab3.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(2)), null, null);
            tv_tab4.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(unCheckedTabIconResids.get(3)), null, null);
            tv_tab5.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableRes(tabIconResids.get(4)), null, null);
        }
    }

    public abstract BaseFragment initPage1();

    public abstract BaseFragment initPage2();

    public abstract BaseFragment initPage3();

    public abstract BaseFragment initPage4();

    public abstract BaseFragment initPage5();

    @Override
    public int setLayoutResID() {
        return R.layout.activity_tab_ui;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}
