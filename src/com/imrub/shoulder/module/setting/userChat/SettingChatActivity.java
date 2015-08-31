package com.imrub.shoulder.module.setting.userChat;

import android.os.Bundle;
import android.view.View;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.widget.Title;

public class SettingChatActivity extends BaseActivity implements View.OnClickListener{

	private Title mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_chat);
		initView();
	}
	
	private void initView(){
		initTitle();
		initListener();
	}
	
	private void initListener(){
		findViewById(R.id.setting_chat_first_block).setOnClickListener(this);
		findViewById(R.id.setting_chat_second_block).setOnClickListener(this);
	}
	
	private void initTitle(){
		View v = findViewById(R.id.setting_chat_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.setting_cajiansetting_chat_title));
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.setting_chat_first_block:
				break;
			case R.id.setting_chat_second_block:
				break;
		}
	}
	
}
