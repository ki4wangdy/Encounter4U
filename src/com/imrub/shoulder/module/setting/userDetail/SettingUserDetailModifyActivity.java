package com.imrub.shoulder.module.setting.userDetail;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.Action;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.setting.SettingRequestFacade;
import com.imrub.shoulder.module.request.setting.UserInfoSettingRequest;
import com.imrub.shoulder.widget.TextWatcherImp;
import com.imrub.shoulder.widget.Title;
import com.imrub.shoulder.widget.ToastFactory;

public class SettingUserDetailModifyActivity extends BaseActivity implements IRequestResult<String>{

	private Title mTitle;
	private EditText mEdittext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_detail_modify);
		initView();
	}
	
	private void initView(){
		initTitle();
		mEdittext = (EditText)findViewById(R.id.setting_user_detail_modify_edittext);
		TextView numberLimit = (TextView)findViewById(R.id.setting_user_detail_modify_numberlimit);
		
		int maxNumber = 30;
		int type = getIntent().getIntExtra("type", -1);
		if(type != UserInfoSettingRequest.Type_description){
			maxNumber = TextWatcherImp.MaxNumber;
		}
		
		numberLimit.setText(""+maxNumber);
		mEdittext.addTextChangedListener(new TextWatcherImp(mEdittext, numberLimit, maxNumber));
		
		if(type == UserInfoSettingRequest.Type_nick){
			TextView text = (TextView)findViewById(R.id.setting_user_special);
			text.setText(getString(R.string.setting_user_detail_modify_special_text));
			numberLimit.setVisibility(View.GONE);
			mEdittext.setSingleLine(true);
		}
		
		if(type == UserInfoSettingRequest.Type_Class || 
				type == UserInfoSettingRequest.Type_habbit || 
				type == UserInfoSettingRequest.Type_Hometown || 
				type == UserInfoSettingRequest.Type_School || 
				type == UserInfoSettingRequest.Type_Xingzuo || 
				type == UserInfoSettingRequest.Type_birth){
			mEdittext.setSingleLine(true);
		}
		
	}
	
	private void initTitle(){
		final String title = getIntent().getStringExtra("title");
		View v = findViewById(R.id.setting_user_detail_modify_title);
		mTitle = new Title(v);
		mTitle.setContent(title);
		
		int type = getIntent().getIntExtra("type", -1);
		String titleName = null;
		if(type == UserInfoSettingRequest.Type_nick){
			titleName = getString(R.string.setting_user_detail_modify_title);
		} else {
			titleName = getString(R.string.setting_user_detail_modify_complete);
		}
		
		mTitle.setRightButton(titleName, new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onClickComplete();
			}
		});
	}
	
	private void onClickComplete(){
		int type = getIntent().getIntExtra("type", -1);
		
		String value = mEdittext.getText().toString();
		if(value.isEmpty()){
			return ;
		}
		
		showLoadingView(R.string.setting_user_detail_modify_ing);
		
		String uid = UserInfo.getInstance().getUid();
		String token = UserInfo.getInstance().getToken();
		
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("value", value);
		
		switch (type) {
			case UserInfoSettingRequest.Type_nick:
				obj.put("type", UserInfoSettingRequest.Type_nick);
				break;
			case UserInfoSettingRequest.Type_description:
				obj.put("type", UserInfoSettingRequest.Type_description);
				break;
			case UserInfoSettingRequest.Type_birth:
				obj.put("type", UserInfoSettingRequest.Type_birth);
				break;
			case UserInfoSettingRequest.Type_Xingzuo:
				obj.put("type", UserInfoSettingRequest.Type_Xingzuo);
				break;
			case UserInfoSettingRequest.Type_School:
				obj.put("type", UserInfoSettingRequest.Type_School);
				break;
			case UserInfoSettingRequest.Type_Class:
				obj.put("type", UserInfoSettingRequest.Type_Class);
				break;
			case UserInfoSettingRequest.Type_Hometown:
				obj.put("type", UserInfoSettingRequest.Type_Hometown);
				break;
			case UserInfoSettingRequest.Type_habbit:
				obj.put("type", UserInfoSettingRequest.Type_habbit);
				break;
		}
		array.add(obj);
		SettingRequestFacade.setUserInfo(this, uid, token, array);
	}
	
	@Override
	public void onError(int code, String msg) {
		if(isLoadingViewShow()){
			dismissLoadingView();
		}
		ToastFactory.showMsg(this, R.string.setting_user_detail_modify_error);
	}

	@Override
	public void onSuccess(String t) {
		if(isLoadingViewShow()){
			dismissLoadingView();
		}
		ToastFactory.showMsg(this, R.string.setting_user_detail_modify_success);
		setResult(RESULT_OK);
		finish();
	}
	
}
