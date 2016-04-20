/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sdkj.bbcat.hx.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.SpUtil;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.FriendVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;
import com.sdkj.bbcat.hx.DemoHelper;

import org.json.JSONObject;
import com.huaxi100.networkapp.network.RespJSONArrayListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.hx.DemoHelper;

import org.json.JSONArray;
import com.huaxi100.networkapp.network.RespJSONArrayListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.hx.DemoHelper;
import com.sdkj.bbcat.widget.CircleImageView;

import org.json.JSONArray;

import java.util.List;

public class AddContactActivity extends SimpleActivity {
	private EditText editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText,mTextView;
	private Button searchBtn;
	private CircleImageView avatar;
	private InputMethodManager inputMethodManager;
	private String toAddUsername;
	private ProgressDialog progressDialog;

	@Override
	public void initBusiness() {
		mTextView = (TextView) findViewById(R.id.add_list_friends);

		editText = (EditText) findViewById(R.id.edit_note);
		String strAdd = getResources().getString(R.string.add_friend);
		mTextView.setText(strAdd);
		String strUserName = getResources().getString(R.string.user_name);
		editText.setHint(strUserName);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		avatar = (CircleImageView) findViewById(R.id.avatar);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public int setLayoutResID() {
		return R.layout.em_activity_add_contact;
	}


	/**
	 * 查找contact
	 * @param v
	 */
	public void searchContact(View v) {
		final String name = editText.getText().toString();
		String saveText = searchBtn.getText().toString();
		
		if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if(TextUtils.isEmpty(name)) {
				new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
				return;
			}
			
			// TODO 从服务器获取此contact,如果不存在提示不存在此用户
			findUser(toAddUsername);
			//服务器存在此用户，显示此用户和添加按钮
		} 
	}	
	
	/**
	 *  添加contact
	 * @param view
	 */
	public void addContact(final View view){
		if(EMChatManager.getInstance().getCurrentUser().equals(searchBtn.getText().toString())){
			new EaseAlertDialog(this, R.string.not_add_myself).show();
			return;
		}
		
		if(DemoHelper.getInstance().getContactList().containsKey(searchBtn.getText().toString())){
		    //提示已在好友列表中(在黑名单列表里)，无需添加
		    if(EMContactManager.getInstance().getBlackListUsernames().contains(searchBtn.getText().toString())){
		        new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
		        return;
		    }
			new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					//demo写死了个reason，实际应该让用户手动填入
					SpUtil sp=new SpUtil(activity,Const.SP_NAME);
					EMContactManager.getInstance().addContact(toAddUsername, "你好,我是"+sp.getStringValue(Const.NICKNAME)+"加个好友呗~");
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = getResources().getString(R.string.send_successful);
							Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
							((Button)view).setText("待验证");
							view.setClickable(false);
							view.setEnabled(false);
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	private void findUser(final String phone){
		showDialog();
		PostParams params=new PostParams();
		params.put("username",phone);
		HttpUtils.postJSONObject(activity, Const.FIND_FRIENDS, params, new RespJSONObjectListener(activity) {
			@Override
			public void getResp(JSONObject jsonObject) {
				dismissDialog();
				RespVo<FriendVo> respVo=GsonTools.getVo(jsonObject.toString(),RespVo.class);
				if(respVo.isSuccess()){
					searchedUserLayout.setVisibility(View.VISIBLE);
					List<FriendVo> friendVo=respVo.getListData(jsonObject, FriendVo.class);
					nameText.setText(friendVo.get(0).getNickname());
					Glide.with(activity.getApplicationContext()).load(SimpleUtils.getImageUrl(friendVo.get(0).getAvatar())).into(avatar);
				}
				
			}

			@Override
			public void doFailed() {
				dismissDialog();
				toast("未查到此用户");
			}
		});
	}
	
	public void back(View v) {
		finish();
	}
}
