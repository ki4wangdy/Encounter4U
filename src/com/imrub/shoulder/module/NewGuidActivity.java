package com.imrub.shoulder.module;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.base.app.store.AppConfig;
import com.imrub.shoulder.module.login.LoginActivity;

public class NewGuidActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final NewGuidFragment newGuideView = new NewGuidFragment(this);
		newGuideView.onEnterAction = new Runnable() {
			@Override
			public void run() {
				navigateToMain();
			}
		};
		
		AppConfig.getInstance().setIsAppFirstRun(false);
		AppConfig.getInstance().setUserFirstClickSetting(true);
		setContentView(newGuideView);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void navigateToMain(){
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
	
}
