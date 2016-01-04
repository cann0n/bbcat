package com.sdkj.bbcat.hx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.sdkj.bbcat.R;

public class EditActivity extends BaseActivity {
	private EditText editText;

	@Override
	public void initBusiness() {
		editText = (EditText) findViewById(R.id.edittext);
		String title = getIntent().getStringExtra("title");
		String data = getIntent().getStringExtra("data");
		if(title != null)
			((TextView)findViewById(R.id.tv_title)).setText(title);
		if(data != null)
			editText.setText(data);
		editText.setSelection(editText.length());
	}

	@Override
	public int setLayoutResID() {
		return R.layout.em_activity_edit;
	}


	public void save(View view){
		setResult(RESULT_OK,new Intent().putExtra("data", editText.getText().toString()));
		finish();
	}
}
