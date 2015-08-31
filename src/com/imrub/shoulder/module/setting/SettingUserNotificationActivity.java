package com.imrub.shoulder.module.setting;

import android.os.Bundle;
import android.view.View;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.widget.Title;

public class SettingUserNotificationActivity extends BaseActivity{

	private Title mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_notification);
		initView();
	}
	
	private void initView(){
		initTitle();
	}
	
	private void initTitle(){
		View v = findViewById(R.id.setting_user_notification_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.setting_notification_title));
	}
	
	
}
