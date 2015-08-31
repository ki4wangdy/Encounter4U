package com.imrub.shoulder.module.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.ActionNull;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.util.Timer;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.registerAndLogin.PhoneOrMailCheckRequest;
import com.imrub.shoulder.module.request.registerAndLogin.RegisterRequestFacade;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.Title;

public class MailVerifyActivity extends BaseActivity implements IRequestResult<String>{

	private Title mTitle;
	
	private EditText mEditText;
	private Button mButton;
	
	private TextView mTime;
	private TextView mTimeOver;
	private TextView mReSend;
	
	private TextView mMaillAddr;
	
	private String mNickName;
	private String mAccountName;
	private String mPassword;
	private int mType;
	
	private static final int VERIFICATION_TYPE  = 1;
	private static final int RESEND_TYPE		= 2;
	private int type = -1;
	
	private final TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			updateloginBg();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
		
		private void updateloginBg(){
			boolean isNotInput = false;
			if(mEditText.getText().toString().length() == 0){
				isNotInput = true;
			}
			
			if(isNotInput){
				mButton.setBackgroundResource(R.drawable.register_button_pre);
				mButton.setEnabled(false);
			} else {
				mButton.setBackgroundResource(R.drawable.register_button_bg);
				mButton.setEnabled(true);
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mail_verify_view);
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
		
		View v = findViewById(R.id.mail_verify_title);
		mTitle = new Title(v);
		mTitle.setContent(mPassword.length() == 0 ? getString(R.string.forget_pw_title) : getString(R.string.mail_verify_title));
		
		mTitle.setOnBackListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onClickBack();
			}
		});

		TextView content = (TextView)findViewById(R.id.mail_verify_content);
		content.setText(Html.fromHtml(getString(R.string.mail_verify_part_1) + "<font color=#e63922>"+
				getString(R.string.phone_verify_block_text)+"</font>" + getString(R.string.mail_verify_part_3)));
		
		mEditText = (EditText)findViewById(R.id.mail_verify_edittext);
		mEditText.addTextChangedListener(mTextWatcher);
		
		mButton = (Button)findViewById(R.id.mail_verify_button);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickNext();
			}
		});
		
		mMaillAddr = (TextView)findViewById(R.id.mail_verify_mail_address);
		mMaillAddr.setText(mAccountName);
		
		mTime = (TextView)findViewById(R.id.mail_verify_time);
		mTime.setText(getString(R.string.phone_verify_time_text, 60));
		startTimer();
		
		mTimeOver = (TextView)findViewById(R.id.mail_verify_time_over);
		mReSend = (TextView)findViewById(R.id.mail_verify_time_send);
		
		mReSend.setText(Html.fromHtml("<font color=#00479d>"+getString(R.string.mail_verify_resend_text_part_2)+"</font>"));
		mReSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reSendVerification();
			}
		});
	}

	private void startTimer(){
		Timer.start(new Timer.ITimerListener() {
			@Override
			public void onTime(int time) {
				mTime.setText(getString(R.string.phone_verify_time_text, 60 - time));
			}
			
			@Override
			public boolean isContinue(int time) {
				boolean isContinue = time == 60 ? false : true;
				if(!isContinue){
					mTime.setVisibility(View.INVISIBLE);
					mTimeOver.setVisibility(View.VISIBLE);
					mReSend.setVisibility(View.VISIBLE);
				}
				return isContinue;
			}
		});
	}
	
	private void onClickNext(){
		sendVerificationCode();
	}
	
	@Override
	public void onError(int code, String msg) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		type = -1;
		DialogFactory.showNoCancelConfirmDialog(getString(R.string.error_verification_title), 
				getString(R.string.error_verification_code_error), null);
	}

	@Override
	public void onSuccess(String t) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		
		if(type == VERIFICATION_TYPE){
			if("0".equalsIgnoreCase(t)){
				// 注册流程或者找回密码流程，如果是注册，跳转到SchoolCertificationActivity.class， 如果是找回密码，跳转到ForgetNewPwActivity.class
				if("".equalsIgnoreCase(mPassword)){
					toNewPwActivity();
				} else {
					toLastActivity();
				}
			} 
			// 过期
			else if("1".equalsIgnoreCase(t)){
				DialogFactory.showNoCancelConfirmDialog(getString(R.string.error_verification_title), 
						getString(R.string.error_verification_time_out), null);
			} 
			// 发生错我
			else if("2".equalsIgnoreCase(t)){
				DialogFactory.showNoCancelConfirmDialog(getString(R.string.error_verification_title), 
						getString(R.string.error_verification_code_error), null);
			}
			return ;
		}
		
		if(type == RESEND_TYPE){
			mTime.setVisibility(View.VISIBLE);
			mTime.setText(getString(R.string.phone_verify_time_text, 60));
			mTimeOver.setVisibility(View.INVISIBLE);
			mReSend.setVisibility(View.INVISIBLE);
			startTimer();
		}
	}
	
	private void toLastActivity(){
		Intent intent = new Intent(this, SchoolCertificationActivity.class);
		intent.putExtra("nickname", mNickName);
		intent.putExtra("accountname", mAccountName);
		intent.putExtra("password", mPassword);
		intent.putExtra("type", mType);
		startActivity(intent);
		finish();
	}
	
	private void toNewPwActivity(){
		Intent intent = new Intent(this, ForgetNewPwActivity.class);
		intent.putExtra("accountname", mAccountName);
		intent.putExtra("type", mType);
		startActivity(intent);
		finish();
	}
	
	private void reSendVerification(){
		type = RESEND_TYPE;
		showLoadingView(getString(R.string.loading_textview_for_mail));
		RegisterRequestFacade.mailOrPhoneSendRequest(this,PhoneOrMailCheckRequest.MailType, 
				mAccountName, true);
	}
	
	private void sendVerificationCode(){
		
		if(!isNetworkConnected()){
			return ;
		}
		
		String code = mEditText.getText().toString();
		int intCode = 0;
		try{
			intCode = Integer.parseInt(code);
		} catch(Exception e){
			DialogFactory.showNoCancelConfirmDialog(getString(R.string.error_verification_title), 
					getString(R.string.error_verification_code_error), null);
			return ;
		}
		
		showLoadingView(R.string.register_check_verification_code);
		
		type = VERIFICATION_TYPE;
		RegisterRequestFacade.checkVerificationCodeRequest(this,mAccountName,intCode);
	}
	
	private void onClickBack(){
		DialogFactory.showNoTitleConfirmDialog(getString(R.string.mail_verify_back_text),
				new ActionNull() {
					@Override
					public void onExecute() {
						finish();
					}
				}
		,getString(R.string.mail_verify_back_cancel_text)
		,getString(R.string.mail_verify_back_ok_text));
	}

	@Override
	public void onBackPressed() {
		onClickBack();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Timer.stop();
	}
	
}
