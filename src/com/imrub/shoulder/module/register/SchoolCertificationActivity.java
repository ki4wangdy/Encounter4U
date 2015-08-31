package com.imrub.shoulder.module.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.ActionNull;
import com.imrub.shoulder.ActivityController;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.Md5;
import com.imrub.shoulder.base.app.store.AppConfig;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.module.im.client.IMClient;
import com.imrub.shoulder.module.main.MainUIActivity;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.registerAndLogin.RegisterRequestFacade;
import com.imrub.shoulder.module.request.registerAndLogin.UserInfoRegisterRequest.RegisterUserInfo;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.Title;

public class SchoolCertificationActivity extends BaseActivity implements IRequestResult<RegisterUserInfo>{

	private  Title mTitle;
	private Button mStart;

	private EditText mSchoolContent;
	private EditText mSchoolSubject;
	
	private TextView mGirl;
	private TextView mMan;
	
	private View mSchoolLine;
	private View mSchoolSubjectLine;
	
	private static final int GENDER_GIRL = 1;
	private static final int GENDER_MAN  = 2;
	
	private int mGender = 0;

	private String mNickName;
	private String mAccountName;
	private String mPassword;
	private int mType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.school_valid_view);
		initData();
		initView();
	}
	
	private void initData(){
		mNickName = getIntent().getExtras().getString("nickname");
		mAccountName = getIntent().getExtras().getString("accountname");
		mPassword = getIntent().getExtras().getString("password");
		mType = getIntent().getExtras().getInt("type");
	}
	
	private void initView(){
		View v = findViewById(R.id.school_verify_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.school_verify_information));
		
		mTitle.setOnBackListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onClickBack();
			}
		});
		
		mStart = (Button)findViewById(R.id.school_description_button);
		mStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickNext();
			}
		});
		
		mSchoolContent = (EditText)findViewById(R.id.school_choose_content);
		mSchoolContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				mSchoolLine.setBackgroundResource(hasFocus ? R.drawable.register_nick_block_bg : 
					R.drawable.register_nick_line_default);
				updateNextUI();
			}
		});
		
		mSchoolSubject = (EditText)findViewById(R.id.school_subject_content);
		mSchoolSubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				mSchoolSubjectLine.setBackgroundResource(hasFocus ? R.drawable.register_nick_block_bg : 
					R.drawable.register_nick_line_default);
				updateNextUI();
			}
		});
		
		mGirl = (TextView)findViewById(R.id.school_gender_girl_text);
		mGirl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateGender(true);
				updateNextUI();
			}
		});
		
		mMan = (TextView)findViewById(R.id.school_gender_man_text);
		mMan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateGender(false);
				updateNextUI();
			}
		});
		
		mSchoolLine = findViewById(R.id.school_choose_line);
		mSchoolSubjectLine = findViewById(R.id.school_subject_line);
	}

	private void onClickBack(){
		DialogFactory.showNoTitleConfirmDialog(getString(R.string.phone_verify_back_text),
				new ActionNull() {
					@Override
					public void onExecute() {
						finish();
					}
				}
		,getString(R.string.phone_verify_back_cancel_text)
		,getString(R.string.phone_verify_back_ok_text));
	}

	private void updateNextUI(){
		boolean isNotInput = false;
		if(mSchoolContent.getText().toString().length() == 0 || mGender == 0 || 
				mSchoolSubject.getText().toString().length() == 0){
			isNotInput = true;
		}
		
		if(isNotInput){
			mStart.setBackgroundResource(R.drawable.register_button_pre);
			mStart.setEnabled(false);
		} else {
			mStart.setBackgroundResource(R.drawable.register_button_bg);
			mStart.setEnabled(true);
		}
	}

	private void updateGender(boolean isGirl){
		if(isGirl){
			mGirl.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.school_gender_girl_hover), null,null,null);
			mGirl.setTextColor(getResources().getColor(R.color.login_zhanghao_textview_color));
			mMan.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.school_gender_man), null, null, null);
			mMan.setTextColor(getResources().getColor(R.color.register_nick_content_hint_text_color));
			mGender = GENDER_GIRL;
		} else { 
			mGirl.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.school_gender_girl), null,null,null);
			mGirl.setTextColor(getResources().getColor(R.color.register_nick_content_hint_text_color));
			mMan.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.school_gender_man_hover), null, null, null);
			mMan.setTextColor(getResources().getColor(R.color.login_zhanghao_textview_color));
			mGender = GENDER_MAN;
		}
	}
	
	@Override
	public void onError(int code, String msg) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		DialogFactory.showNoCancelConfirmDialog(getString(R.string.error_server_parse),msg, null);
	}

	@Override
	public void onSuccess(RegisterUserInfo userInfo) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		
		// APP first register
		AppConfig.getInstance().setUserFirstRegister(true);
		
		UserInfo.getInstance().putUid(userInfo.uid);
		UserInfo.getInstance().putToken(userInfo.token);
		UserInfo.getInstance().setImServer(userInfo.serverIp);
		
		IMClient.getInstance().registerUser();
		
		Intent intent = new Intent(this, MainUIActivity.class);
		startActivity(intent,R.anim.splash_anim_fade_in, R.anim.splash_anim_fade_out);
		ActivityController.getInstance().finishLoginActivity();
		ActivityController.getInstance().storeLoginActivity(null);
		finish();
	}
	
	private void onClickNext(){
		
		if(!isNetworkConnected()){
			return ;
		}
		
		showLoadingView(R.string.school_register_ing);
		
		String phone = "";
		String mail = "";
		
		if(mType == 1){
			phone = mAccountName;
		} else {
			mail = mAccountName;
		}
		
		String userLogo = UserInfo.getInstance().getUserLogo();
		String md5Password = Md5.getMD5FullStr(mPassword);
		RegisterRequestFacade.registerUserInfo(this, mNickName, 
				mail, phone, md5Password, userLogo, mSchoolContent.getText().toString(),1, mGender);
	}
	
}
