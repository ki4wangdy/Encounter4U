package com.imrub.shoulder.module.setting.userAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.Title;

public class SettingUserAccountActivity extends BaseActivity implements OnClickListener{

	private Title mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_account_view);
		initView();
	}
	
	private void initView(){
		initTile();
		findViewById(R.id.setting_account_block).setOnClickListener(this);
		findViewById(R.id.setting_account_security_block).setOnClickListener(this);
		findViewById(R.id.setting_exit_block).setOnClickListener(this);
	}
	
	private void initTile(){
		View v = findViewById(R.id.setting_about_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.setting_about_account_text));
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.setting_account_block:
				startActivity(new Intent(this, SettingChangePhoneUIActivity.class));
				break;
			case R.id.setting_account_security_block:
				DialogFactory.showModifyPasswordDialog(null);
				break;
			case R.id.setting_exit_block:
				DialogFactory.showNoTitleConfirmDialog(getString(R.string.setting_about_exit_text), null);
				break;
		}
	}
	
}
