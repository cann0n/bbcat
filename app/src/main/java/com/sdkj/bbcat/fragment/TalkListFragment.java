package com.sdkj.bbcat.fragment;

import android.widget.ListView;

import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.adapter.TalkAdapter;

import java.util.ArrayList;

/**
 * Created by ${Rhino} on 2015/12/23 18:20
 * 猫友:会话，群主，聊天室，黑名单
 */
public class TalkListFragment extends BaseFragment {
    
    @ViewInject(R.id.talk_list)
    private ListView talk_list;
    
    private TalkAdapter adapter;
    
    @Override
    protected void setListener() {
        adapter=new TalkAdapter(new ArrayList<String>(),activity);
        talk_list.setAdapter(adapter);
        adapter.addItem(new String());
        adapter.addItem(new String());
        adapter.addItem(new String());
        adapter.addItem(new String());
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_talk_list;
    }
}
