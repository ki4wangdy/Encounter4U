package com.imrub.shoulder.module.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.module.setting.cajiansetting.SettingCajianActivity;
import com.imrub.shoulder.module.setting.cajiansetting.SettingCajianDetailActivity;
import com.imrub.shoulder.widget.Title;

public class SettingYuanfenActivity extends BaseActivity implements OnClickListener{

	private Title mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_yuanfen);
		initView();
	}
	
	private void initView(){
		initTitle();
		findViewById(R.id.setting_yuanfen_third_block).setOnClickListener(this);
		findViewById(R.id.setting_yuanfen_fourth_block).setOnClickListener(this);
	}
	
	private void initTitle(){
		View v = findViewById(R.id.setting_yuanfen_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.setting_yuanfen_title));
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, SettingCajianDetailActivity.class);
		int id = v.getId();
		switch (id) {
			case R.id.setting_yuanfen_third_block:
				intent.putExtra("title", SettingCajianActivity.TitleTypeNotSee);
				break;
			case R.id.setting_yuanfen_fourth_block:
				intent.putExtra("title", SettingCajianActivity.TitleTypeNotLetSee);
				break;
		}
		startActivity(intent);
	}
	
	
}
