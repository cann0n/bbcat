package com.sdkj.bbcat.fragment;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMChatRoomChangeListener;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMCursorResult;
import com.easemob.exceptions.EaseMobException;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.activity.community.ChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Rhino} on 2016/1/5 17:19
 * 聊天室列表
 */
public class FragmentChatRoom extends BaseFragment{

    private ListView listView;
    private ChatRoomAdapter adapter;

    private List<EMChatRoom> chatRoomList;
    private boolean isLoading;
    private boolean isFirstLoading = true;
    private boolean hasMoreData = true;
    private String cursor;
    private final int pagesize = 50;
    private LinearLayout footLoadingLayout;
    private ProgressBar footLoadingPB;
    private TextView footLoadingText;
    private List<EMChatRoom> rooms;
    @Override
    protected void setListener() {
        listView = (ListView)rootView. findViewById(R.id.list);
        chatRoomList = new ArrayList<EMChatRoom>();
        rooms = new ArrayList<EMChatRoom>();

        View footView = activity.makeView(R.layout.em_listview_footer_view);
        footLoadingLayout = (LinearLayout) footView.findViewById(R.id.loading_layout);
        footLoadingPB = (ProgressBar)footView.findViewById(R.id.loading_bar);
        footLoadingText = (TextView) footView.findViewById(R.id.loading_text);
        listView.addFooterView(footView, null, false);
        footLoadingLayout.setVisibility(View.GONE);

        //获取及显示数据
        loadAndShowData();

        EMChatManager.getInstance().addChatRoomChangeListener(new EMChatRoomChangeListener(){
            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                chatRoomList.clear();
                if(adapter != null){
                    activity.runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(adapter != null){
                                adapter.notifyDataSetChanged();
                                loadAndShowData();
                            }
                        }

                    });
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                                       String participant) {

            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                                       String participant) {
            }

        });

        //设置item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final EMChatRoom room = adapter.getItem(position);
                startActivity(new Intent(activity, ChatActivity.class).putExtra("chatType", 3).
                        putExtra("userId", room.getId()));

            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    if(cursor != null){
                        int lasPos = view.getLastVisiblePosition();
                        if(hasMoreData && !isLoading && lasPos == listView.getCount()-1){
                            loadAndShowData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void loadAndShowData(){
        new Thread(new Runnable() {

            public void run() {
                try {
                    isLoading = true;
                    final EMCursorResult<EMChatRoom> result = EMChatManager.getInstance().fetchPublicChatRoomsFromServer(pagesize, cursor);
                    //获取group list
                    final List<EMChatRoom> chatRooms = result.getData();
                    activity.runOnUiThread(new Runnable() {

                        public void run() {
                            chatRoomList.addAll(chatRooms);
                            if (chatRooms.size() != 0) {
                                //获取cursor
                                cursor = result.getCursor();
//                                if(chatRooms.size() == pagesize)
//                                    footLoadingLayout.setVisibility(View.VISIBLE);
                            }
                            if (isFirstLoading) {
                                isFirstLoading = false;
                                //设置adapter
                                adapter = new ChatRoomAdapter(activity, 1, chatRoomList);
                                listView.setAdapter(adapter);
                                rooms.addAll(chatRooms);
                            } else {
                                if (chatRooms.size() < pagesize) {
                                    hasMoreData = false;
                                    footLoadingLayout.setVisibility(View.VISIBLE);
                                    footLoadingPB.setVisibility(View.GONE);
                                    footLoadingText.setText(getResources().getString(R.string.no_more_messages));
                                }
                                adapter.notifyDataSetChanged();
                            }
                            isLoading = false;
                        }
                    });
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            isLoading = false;
                            footLoadingLayout.setVisibility(View.GONE);
                            Toast.makeText(activity, getResources().getString(R.string.failed_to_load_data), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
    /**
     * adapter
     *
     */
    private class ChatRoomAdapter extends ArrayAdapter<EMChatRoom> {

        private LayoutInflater inflater;
        private RoomFilter filter;

        public ChatRoomAdapter(Context context, int res, List<EMChatRoom> rooms) {
            super(context, res, rooms);
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.em_row_group, null);
            }

            ((TextView) convertView.findViewById(R.id.name)).setText(getItem(position).getName());

            return convertView;
        }

        @Override
        public Filter getFilter(){
            if(filter == null){
                filter = new RoomFilter();
            }
            return filter;
        }

        private class RoomFilter extends Filter{

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if(constraint == null || constraint.length() == 0){
                    results.values = rooms;
                    results.count = rooms.size();
                }else{
                    List<EMChatRoom> roomss = new ArrayList<EMChatRoom>();
                    for(EMChatRoom chatRoom : rooms){
                        if(chatRoom.getName().contains(constraint)){
                            roomss.add(chatRoom);
                        }
                    }
                    results.values = roomss;
                    results.count = roomss.size();
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                chatRoomList.clear();
                chatRoomList.addAll((List<EMChatRoom>)results.values);
                notifyDataSetChanged();
            }

        }
    }
    @Override
    protected int setLayoutResID() {
        return  R.layout.em_activity_public_groups;
    }
}
