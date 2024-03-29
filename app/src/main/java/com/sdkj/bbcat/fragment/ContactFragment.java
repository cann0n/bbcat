package com.sdkj.bbcat.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.widget.EaseContactList;
import com.easemob.easeui.widget.EaseSidebar;
import com.easemob.exceptions.EaseMobException;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.utils.SpUtil;
import com.huaxi100.networkapp.utils.Utils;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.TabUiActivity;
import com.sdkj.bbcat.activity.community.ChatActivity;
import com.sdkj.bbcat.bean.FriendVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.Constant;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.hx.DemoHelper;
import com.sdkj.bbcat.hx.InviteMessgeDao;
import com.sdkj.bbcat.hx.UserDao;
import com.sdkj.bbcat.hx.activity.NewFriendsMsgActivity;
import com.sdkj.bbcat.widget.ContactItemView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by ${Rhino} on 2016/1/5 18:06
 * 通讯录
 */
public class ContactFragment extends BaseFragment {

    //    private ContactSyncListener contactSyncListener;
//    private BlackListSyncListener blackListSyncListener;
//    private ContactInfoSyncListener contactInfoSyncListener;
//    private View loadingView;
    private ContactItemView applicationItem;

    private InviteMessgeDao inviteMessgeDao;
    protected List<EaseUser> contactList;

    @ViewInject(R.id.list)
    protected ListView listView;

    @ViewInject(R.id.sidebar)
    private EaseSidebar sideBar;

    @ViewInject(R.id.floating_header)
    private TextView floating_header;

    protected EaseContactAdapter adapter;
    protected List<String> blackList;
    protected Handler handler = new Handler();
    protected EaseUser toBeProcessUser;
    private int position;
    protected String toBeProcessUsername;
    protected EaseContactList contactListLayout;

    protected FrameLayout contentContainer;

    private Map<String, EaseUser> contactsMap;

    protected int primaryColor;
    protected int primarySize;
    protected boolean showSiderBar;
    protected Drawable initialLetterBg;
    protected int initialLetterColor;


    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);

//        TypedArray ta = activity.obtainStyledAttributes(attrs, com.easemob.easeui.R.styleable.EaseContactList);
//        primaryColor = ta.getColor(com.easemob.easeui.R.styleable.EaseContactList_ctsListPrimaryTextColor, 0);
//        primarySize = ta.getDimensionPixelSize(com.easemob.easeui.R.styleable.EaseContactList_ctsListPrimaryTextSize, 0);
//        showSiderBar = ta.getBoolean(com.easemob.easeui.R.styleable.EaseContactList_ctsListShowSiderBar, true);
//        initialLetterBg = ta.getDrawable(com.easemob.easeui.R.styleable.EaseContactList_ctsListInitialLetterBg);
//        initialLetterColor = ta.getColor(com.easemob.easeui.R.styleable.EaseContactList_ctsListInitialLetterColor, 0);
//        ta.recycle();
        contactList = new ArrayList<>();
        adapter = new EaseContactAdapter(activity, 0, contactList);
        adapter.setPrimaryColor(primaryColor).setPrimarySize(primarySize).setInitialLetterBg(initialLetterBg).setInitialLetterColor(initialLetterColor);
        listView.setAdapter(adapter);

        sideBar.setListView(listView);


        contentContainer = (FrameLayout) rootView.findViewById(R.id.content_container);

        contactListLayout = (EaseContactList) rootView.findViewById(R.id.contact_list);
//        listView = contactListLayout.getListView();


        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        //添加headerview
        listView.addHeaderView(headerView);
        //添加正在加载数据提示的loading view
//        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
//        contentContainer.addView(loadingView);
        //注册上下文菜单
        registerForContextMenu(listView);

        //设置联系人数据
//        setContactsMap(DemoHelper.getInstance().getContactList());

        //黑名单列表
//        blackList = EMContactManager.getInstance().getBlackListUsernames();
//        contactList = new ArrayList<EaseUser>();
        // 获取设置contactlist
//        agree();
        refresh();

//        getContactList();

//        contactSyncListener = new ContactSyncListener();
//        DemoHelper.getInstance().addSyncContactListener(contactSyncListener);

//        blackListSyncListener = new BlackListSyncListener();
//        DemoHelper.getInstance().addSyncBlackListListener(blackListSyncListener);
//
//        contactInfoSyncListener = new ContactInfoSyncListener();
//        DemoHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);

//        if (!DemoHelper.getInstance().isContactsSyncedWithServer()) {
//            loadingView.setVisibility(View.VISIBLE);
//        } else {
//            loadingView.setVisibility(View.GONE);
//        }


        if (listItemClickListener != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EaseUser user = (EaseUser) listView.getItemAtPosition(position);
                    listItemClickListener.onListItemClicked(user);
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((EaseUser) listView.getItemAtPosition(position)).getUsername();
                // demo中直接进入聊天页面，实际一般是进入用户详情页
                if (username.equals(EMChatManager.getInstance().getCurrentUser())) {
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(activity, ChatActivity.class);
                SpUtil sp = new SpUtil(activity, Const.SP_NAME);

                intent.putExtra(Constant.EXTRA_USER_ID, username);
                intent.putExtra(Constant.EXTRA_USER_AVATAR, sp.getStringValue(Const.AVATAR));
                intent.putExtra(Constant.EXTRA_USER_NICKNAME, sp.getStringValue(Const.NICKNAME));
                startActivity(intent);

                TabUiActivity.MainEvent mainEvent = new TabUiActivity.MainEvent();
                mainEvent.setType(3);
                EventBus.getDefault().post(mainEvent);
            }
        });
        refresh();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (contactSyncListener != null) {
//            DemoHelper.getInstance().removeSyncContactListener(contactSyncListener);
//            contactSyncListener = null;
//        }
//
//        if (blackListSyncListener != null) {
//            DemoHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
//        }
//
//        if (contactInfoSyncListener != null) {
//            DemoHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
//        }
        EventBus.getDefault().unregister(this);
    }

    protected class HeaderItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 进入申请与通知页面
                    if (inviteMessgeDao == null) {
                        inviteMessgeDao = new InviteMessgeDao(getActivity());
                    }
                    inviteMessgeDao.saveUnreadMessageCount(0);
                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                    break;
                default:
                    break;
            }
        }

    }

    public void deleteContact(final EaseUser tobeDeleteUser) {
        String st1 = getResources().getString(R.string.deleting);
        final String st2 = getResources().getString(R.string.Delete_failed);
//        final ProgressDialog pd = new ProgressDialog(getActivity());
//        pd.setMessage(st1);
//        pd.setCanceledOnTouchOutside(false);
//        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
                    // 删除db和内存中此用户的数据
                    UserDao dao = new UserDao(getActivity());
                    dao.deleteContact(tobeDeleteUser.getUsername());
                    DemoHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            adapter.notifyDataSetChanged();
                            contactList.remove(tobeDeleteUser);
                        }
                    });

                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
//                            pd.dismiss();
                            Toast.makeText(getActivity(), st2 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }).start();

    }

//    class ContactSyncListener implements DemoHelper.DataSyncListener {
//        @Override
//        public void onSyncComplete(final boolean success) {
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//                    getActivity().runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            if (success) {
//                                loadingView.setVisibility(View.GONE);
//                                refresh();
//                            } else {
//                                String s1 = getResources().getString(R.string.get_failed_please_check);
//                                Toast.makeText(getActivity(), s1, Toast.LENGTH_SHORT).show();
//                                loadingView.setVisibility(View.GONE);
//                            }
//                        }
//
//                    });
//                }
//            });
//        }
//
//    }

//    class BlackListSyncListener implements DemoHelper.DataSyncListener {
//
//        @Override
//        public void onSyncComplete(boolean success) {
//            getActivity().runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    blackList = EMContactManager.getInstance().getBlackListUsernames();
//                    refresh();
//                }
//
//            });
//        }
//
//    }

    ;
//
//    class ContactInfoSyncListener implements DemoHelper.DataSyncListener {
//
//        @Override
//        public void onSyncComplete(final boolean success) {
//            getActivity().runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    loadingView.setVisibility(View.GONE);
//                    if (success) {
//                        refresh();
//                    }
//                }
//            });
//        }
//
//    }

    public void refresh() {
//        getContactList();
//        contactListLayout.refresh();
        showInviteMsg();
        agree();
    }

    public void showInviteMsg() {
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
            applicationItem.showUnreadMsgView();
        } else {
            applicationItem.hideUnreadMsgView();
        }
    }

    /**
     * 获取联系人列表，并过滤掉黑名单和排序
     */
    protected void getContactList() {
        contactList.clear();
        synchronized (contactList) {
            //获取联系人列表
            if (contactsMap == null) {
                return;
            }
            Iterator<Map.Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, EaseUser> entry = iterator.next();
                //兼容以前的通讯录里的已有的数据显示，加上此判断，如果是新集成的可以去掉此判断
                if (!entry.getKey().equals("item_new_friends") && !entry.getKey().equals("item_groups") && !entry.getKey().equals("item_chatroom") && !entry.getKey().equals("item_robots")) {
                    if (!blackList.contains(entry.getKey())) {
                        //不显示黑名单中的用户
                        EaseUser user = entry.getValue();
                        EaseCommonUtils.setUserInitialLetter(user);
                        contactList.add(user);
                    }
                }
            }
            // 排序
            Collections.sort(contactList, new Comparator<EaseUser>() {

                @Override
                public int compare(EaseUser lhs, EaseUser rhs) {
                    if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
                        return lhs.getNick().compareTo(rhs.getNick());
                    } else {
                        if ("#".equals(lhs.getInitialLetter())) {
                            return 1;
                        } else if ("#".equals(rhs.getInitialLetter())) {
                            return -1;
                        }
                        return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                    }

                }
            });
        }

    }

    private void agree() {
        HttpUtils.getJSONObject(activity, SimpleUtils.buildUrl(activity, Const.GET_FRIENDS), new RespJSONObjectListener(activity) {
            public void getResp(JSONObject jsonObject) {
                RespVo<FriendVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (respVo.isSuccess()) {
                    List<FriendVo> data = respVo.getListData(jsonObject, FriendVo.class);
                    if (!Utils.isEmpty(data)) {
                        contactList.clear();
                        for (FriendVo friendVo : data) {
                            EaseUser user = new EaseUser(friendVo.getMobile());
                            user.setAvatar(friendVo.getAvatar());
                            user.setNick(friendVo.getNickname());
                            EaseCommonUtils.setUserInitialLetter(user);
                            contactList.add(user);
                        }

                        Collections.sort(contactList, new Comparator<EaseUser>() {

                            @Override
                            public int compare(EaseUser lhs, EaseUser rhs) {
                                if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
                                    return lhs.getNick().compareTo(rhs.getNick());
                                } else {
                                    if ("#".equals(lhs.getInitialLetter())) {
                                        return 1;
                                    } else if ("#".equals(rhs.getInitialLetter())) {
                                        return -1;
                                    }
                                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                                }

                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                } else {

                }
            }

            @Override
            public void doFailed() {
            }
        });
    }

    private void delete(final EaseUser tobeDeleteUser, final int position) {
        PostParams params = new PostParams();
        params.put("phone", tobeDeleteUser.getUsername());
        HttpUtils.postJSONObject(activity, Const.DELETE_FRIENDS, SimpleUtils.buildUrl(activity, params), new RespJSONObjectListener(activity) {
                    public void getResp(JSONObject jsonObject) {
                        RespVo<FriendVo> respVo = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                        if (respVo.isSuccess()) {
                            deleteContact(tobeDeleteUser);
                        }
                    }

                    @Override
                    public void doFailed() {
                    }
                }

        );
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
        toBeProcessUsername = toBeProcessUser.getUsername();
        this.position = position;
        getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_contact) {
            try {
                // 删除此联系人
//                deleteContact(toBeProcessUser);
                // 删除相关的邀请消息
//                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
//                dao.deleteMessage(toBeProcessUser.getUsername());
                delete(toBeProcessUser, position);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if (item.getItemId() == R.id.add_to_blacklist) {
            moveToBlacklist(toBeProcessUsername);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 把user移入到黑名单
     */
    protected void moveToBlacklist(final String username) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st1 = getResources().getString(com.easemob.easeui.R.string.Is_moved_into_blacklist);
        final String st2 = getResources().getString(com.easemob.easeui.R.string.Move_into_blacklist_success);
        final String st3 = getResources().getString(com.easemob.easeui.R.string.Move_into_blacklist_failure);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    //加入到黑名单
                    EMContactManager.getInstance().addUserToBlackList(username, false);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    });
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st3, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    public void onEventMainThread(CommunityPage.ConnectEvent event) {
        if ( event.getType() == 4) {
            refresh();
        }
    }

    private EaseContactListItemClickListener listItemClickListener;

    /**
     * 设置需要显示的数据map，key为环信用户id
     *
     * @param
     */
//    public void setContactsMap(Map<String, EaseUser> contactsMap) {
//        this.contactsMap = contactsMap;
//    }

    public interface EaseContactListItemClickListener {
        /**
         * 联系人listview item点击事件
         *
         * @param user 被点击item所对应的user对象
         */
        void onListItemClicked(EaseUser user);

    }

    /**
     * 设置listview item点击事件
     *
     * @param listItemClickListener
     */
    public void setContactListItemClickListener(EaseContactListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }


    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_contact_list;
    }
}
