package com.imrub.shoulder.module.setting.userAccount;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.ActionNull;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.Title;

public class SettingChangePhoneUIActivity extends BaseActivity{

	private Title mTitle;
	private EditText mEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_phone_ui);
		initView();
	}

	private void initView(){
		initTitle();
		mEditText = (EditText)findViewById(R.id.setting_phone_ui_number);
		Button button = (Button)findViewById(R.id.setting_phone_ui_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickNext();
			}
		});
	}
	
	private void initTitle(){
		View v = findViewById(R.id.setting_phone_ui_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.setting_phone_text3));
		mTitle.setOnBackListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onClickBack();
			}
		});
	}
	
	private void onClickBack(){
		DialogFactory.showNoTitleConfirmDialog(getString(R.string.setting_phone_ui_back), new ActionNull() {
			@Override
			public void onExecute() {
				finish();
			}
		});
	}

	private void onClickNext(){
		String phone = mEditText.getText().toString();
		if(phone.trim().isEmpty()){
			return ;
		}
		DialogFactory.showConfirmDialog(getString(R.string.dialog_common_title) , getString(R.string.dialog_confirm_phone_text) ,
				"+86  " + phone, 
				new ActionNull() {
					@Override
					public void onExecute() {
					}
				}
		);
	}
	
}
