package com.imrub.shoulder.module.setting.userAccount;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.widget.Title;

public class SettingChangePhoneActivity extends BaseActivity{

	private Title mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_phone);
		initView();
	}
	
	private void initView(){
		initTitle();
		
		Button button = (Button)findViewById(R.id.setting_phone_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickButton();
			}
		});
		
	}
	
	private void initTitle(){
		View v = findViewById(R.id.setting_phone_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.setting_phone_text3));
	}
	
	private void onClickButton(){
	}
	
}
