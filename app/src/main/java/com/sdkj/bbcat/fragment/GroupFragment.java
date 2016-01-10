package com.sdkj.bbcat.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.EMValueCallBack;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.utils.SpUtil;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.community.ChatActivity;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.Constant;
import com.sdkj.bbcat.hx.GroupAdapter;
import com.sdkj.bbcat.hx.activity.GroupsActivity;
import com.sdkj.bbcat.hx.activity.NewGroupActivity;
import com.sdkj.bbcat.hx.activity.PublicGroupsActivity;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ${Rhino} on 2016/1/5 14:10
 * 群组列表
 */
public class GroupFragment extends BaseFragment{

    private ListView groupListView;
    protected List<EMGroup> grouplist;
    private GroupAdapter groupAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            swipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
                    Toast.makeText(activity, R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        };
    };
    
    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);
        
        grouplist = EMGroupManager.getInstance().getAllGroups();
        groupListView = (ListView) rootView.findViewById(R.id.list);
        //show group list
        groupAdapter = new GroupAdapter(activity, 1, grouplist);
        groupListView.setAdapter(groupAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout)rootView. findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                EMGroupManager.getInstance().asyncGetGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {

                    @Override
                    public void onSuccess(List<EMGroup> value) {
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onError(int error, String errorMsg) {
                        handler.sendEmptyMessage(1);
                    }
                });
            }
        });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 1) {
//                    // 新建群聊
//                    startActivityForResult(new Intent(activity, NewGroupActivity.class), 0);
//                } else if (position == 2) {
//                    // 添加公开群
//                    startActivityForResult(new Intent(activity, PublicGroupsActivity.class), 0);
//                } else {
                    // 进入群聊
                    Intent intent = new Intent(activity, ChatActivity.class);
                    // it is group chat
                    intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    intent.putExtra("userId", groupAdapter.getItem(position).getGroupId());
                SpUtil sp=new SpUtil(activity,Const.SP_NAME);
                intent.putExtra(Constant.EXTRA_USER_AVATAR, sp.getStringValue(Const.AVATAR));
                intent.putExtra(Constant.EXTRA_USER_NICKNAME, sp.getStringValue(Const.NICKNAME));    
                
                startActivityForResult(intent, 0);
//                }
            }
        });
    }

    public void onEventMainThread(RefreshGroupEvent event){
        
    }
    
    public static class RefreshGroupEvent{
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh(){
        grouplist = EMGroupManager.getInstance().getAllGroups();
        groupAdapter = new GroupAdapter(activity, 1, grouplist);
       
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    
    @Override
    protected int setLayoutResID() {
        return R.layout.em_fragment_groups;
    }
}
