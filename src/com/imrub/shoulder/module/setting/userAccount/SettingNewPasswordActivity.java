package com.imrub.shoulder.module.setting.userAccount;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.widget.Title;

public class SettingNewPasswordActivity extends BaseActivity{

	private Title mTitle;
	
	private EditText mNewPassword;
	private EditText mConfirmNewPassword;
	
	private View mFirstLine;
	private View mSecondLine;
	
	private final OnFocusChangeListener mFocusListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			final int id = v.getId();
			switch (id) {
				case R.id.setting_about_resetpassword_newpassword:
					updateFocuseBackground(mFirstLine, hasFocus);
					break;
				case R.id.setting_about_resetpassword_newpassword2:
					updateFocuseBackground(mSecondLine, hasFocus);
					break;
			}
		}
	};
	
	private void updateFocuseBackground(View v, boolean hasFocus){
		v.setBackgroundResource(hasFocus ? R.drawable.register_nick_block_bg : 
			R.drawable.register_nick_line_default);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_resetpassword);
		initView();
	}
	
	private void initView(){
		initTitle();
		initEditText();
	}

	private void initTitle(){
		View v = findViewById(R.id.setting_about_resetpassword_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.setting_about_newpassword_title));
		mTitle.setRightButton(getString(R.string.setting_about_newpassword_complete), new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onClickComplete();
			}
		});
	}

	private void onClickComplete(){
	}
	
	private void initEditText(){
		mNewPassword = (EditText)findViewById(R.id.setting_about_resetpassword_newpassword);
		mNewPassword.setOnFocusChangeListener(mFocusListener);
		mConfirmNewPassword = (EditText)findViewById(R.id.setting_about_resetpassword_newpassword2);
		mConfirmNewPassword.setOnFocusChangeListener(mFocusListener);
		
		mFirstLine = findViewById(R.id.setting_about_resetpassword_newpassword_bottom_line);
		mSecondLine = findViewById(R.id.setting_about_resetpassword_newpassword_bottom_line2);
		
	}
	
}
