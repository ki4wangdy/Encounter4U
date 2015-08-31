package com.imrub.shoulder.module.setting.cajiansetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.setting.SettingRequestFacade;
import com.imrub.shoulder.widget.Title;

public class SettingCajianActivity extends BaseActivity implements OnClickListener, IRequestResult<String>{

	public static final int TitleTypeMiddle 	= 1;
	public static final int TitleTypeMiddle2 	= 2;
	public static final int TitleTypeFinal 		= 3;
	
	public static final int TitleTypeNotSee		= 4;
	public static final int TitleTypeNotLetSee	= 5;
	
	private Title mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_cajiansetting);
		initView();
	}

	private void initView(){
		initTitle();
		initListener();
	}
	
	private void initListener(){
		findViewById(R.id.setting_cajian_up_block).setOnClickListener(this);
		findViewById(R.id.setting_cajian_middle_block).setOnClickListener(this);
		findViewById(R.id.setting_cajian_middle_second_block).setOnClickListener(this);
		findViewById(R.id.setting_cajian_final_block).setOnClickListener(this);
	}
	
	private void initTitle(){
		View v = findViewById(R.id.setting_cajiansetting_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.setting_cajiansetting_title));
	}
	
	@Override
	public void onClick(View v) {
		if(R.id.setting_cajian_up_block == v.getId()){
			onClickSex();
			return ;
		}
		Intent intent = new Intent(this, SettingCajianDetailActivity.class);
		int id = v.getId();
		switch (id) {
			case R.id.setting_cajian_middle_block:
				intent.putExtra("title", TitleTypeMiddle);
				break;
			case R.id.setting_cajian_middle_second_block:
				intent.putExtra("title", TitleTypeMiddle2);
				break;
			case R.id.setting_cajian_final_block:
				intent.putExtra("title", TitleTypeFinal);
				break;
		}
		startActivity(intent);
	}
	
	@Override
	public void onError(int code, String msg) {
	}

	@Override
	public void onSuccess(String t) {
	}
	
	private void onClickSex(){
		String uid = UserInfo.getInstance().getUid();
		String token = UserInfo.getInstance().getToken();
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("type", 1);
		obj.put("value", 2);
		array.add(obj);
		SettingRequestFacade.setUserBoolInfo(this, uid, token, array);
	}
	
}
