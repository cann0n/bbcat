package com.sdkj.bbcat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.activity.loginandregister.LoginActivity;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import de.greenrobot.event.EventBus;


public abstract class TabUiActivity extends SimpleActivity {

    private TextView tv_tab1;

    private TextView tv_tab2;

    private TextView tv_tab3;

    private TextView tv_tab4;

    private TextView tv_tab5;

    private TextView tv_unread_count;

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
        EventBus.getDefault().register(this);
        tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
        tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
        tv_tab3 = (TextView) findViewById(R.id.tv_tab3);
        tv_tab4 = (TextView) findViewById(R.id.tv_tab4);
        tv_tab5 = (TextView) findViewById(R.id.tv_tab5);
        tv_unread_count = (TextView) findViewById(R.id.tv_unread_count);
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
        startUploadLocal();
    }

    private void startUploadLocal() {
        Intent intent = new Intent(Const.ACTION_UPLOAD_CAR_LOCAL);
        PendingIntent sendIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(sendIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10 * 1000, sendIntent);
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
        if (viewId == R.id.tv_tab3 || viewId == R.id.tv_tab4 || viewId == R.id.tv_tab5) {
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

    public void onEventMainThread(MainEvent event) {
        if (event.getType() == 3) {
            showUnreadCount();
        }
        if (event.getResId() != 0) {
            if(!show){
                showCusPopUp(event.getResId(), event.getTitle());
            }
        }
    }

    public static class MainEvent {
        /* * 3:显示是否有未读消息
         * 4:隐藏是否有未读消息
         */
        private int type;

        private int resId;
        private String title;

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public Fragment getFragment(int viewId) {
        showUnreadCount();
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


    void showUnreadCount() {
        List<EMConversation> list = loadConversationList();

        int temp = 0;
        if (Utils.isEmpty(list)) {
            temp = 0;
        } else {
            temp = list.get(0).getUnreadMsgCount();
        }

        hideCount(temp);

    }

    void hideCount(int temp) {
        if (temp > 0) {
            tv_unread_count.setVisibility(View.VISIBLE);
            tv_unread_count.setText(temp + "");
        } else {
            tv_unread_count.setVisibility(View.GONE);
            tv_unread_count.setText("");
        }
    }

    protected List<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //if(conversation.getType() != EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    //}
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    boolean show=false;
    private void showCusPopUp(int resID, String title) {
        show=true;
        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_nitify, null);
        final PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, 800);
        TextView tv_title = (TextView) popupView.findViewById(R.id.tv_title);
        tv_title.setText(title);


        ImageView iv_image = (ImageView) popupView.findViewById(R.id.iv_image);
        iv_image.setImageResource(resID);
        TextView tv_cancel = (TextView) popupView.findViewById(R.id.tv_cancel);
        TextView tv_know = (TextView) popupView.findViewById(R.id.tv_know);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                show=false;
            }
        });
        tv_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                show=false;
            }
        });
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
        window.showAtLocation(getWindow().getDecorView(), Gravity.CENTER_VERTICAL, 0, 0);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
