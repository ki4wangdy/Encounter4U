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

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.util.RegularUtil;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.registerAndLogin.PhoneOrMailCheckRequest;
import com.imrub.shoulder.module.request.registerAndLogin.RegisterRequestFacade;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.Title;
import com.imrub.shoulder.widget.ToastFactory;

public class ForgetPwActivity extends BaseActivity implements IRequestResult<String>{

	private Title mTitle;
	
	private TextView mContent;
	private EditText mEditContent;
	
	private Button mNext;
	private boolean mIsPhone;
	
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
			updateloginBg();
		}
		
		private void updateloginBg(){
			boolean isNotInput = false;
			if(mEditContent.getText().length() == 0){
				isNotInput = true;
			}
			
			if(isNotInput){
				mNext.setBackgroundResource(R.drawable.register_button_pre);
				mNext.setEnabled(false);
			} else {
				mNext.setBackgroundResource(R.drawable.register_button_bg);
				mNext.setEnabled(true);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_pw);
		initView();
	}

	private void initView(){
		View v = findViewById(R.id.forget_pw_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.forget_pw_title));

		mContent = (TextView)findViewById(R.id.forget_pw_content);
		mContent.setText(Html.fromHtml(getString(R.string.forget_pw_part1) + 
				"<font color=#e63922>"+getString(R.string.forget_pw_part2)+"</font>" + 
				getString(R.string.forget_pw_part3) + 
				"<font color=#e63922>"+getString(R.string.forget_pw_part4)+"</font>"));
		
		mEditContent = (EditText)findViewById(R.id.forget_pw_edittext);
		mEditContent.addTextChangedListener(mTextWatcher);
		
		mNext = (Button)findViewById(R.id.forget_pw_button);
		mNext.setEnabled(false);
		mNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickNext();
			}
		});
	}
	
	private void onClickNext(){
		if(!isNetworkConnected()){
			return ;
		}
		
		String account = mEditContent.getText().toString().trim();
		checkMailOrPhone(account);
		showLoadingView(mIsPhone ? 
				getString(R.string.loading_textview_text) : 
				getString(R.string.loading_textview_text_mail));
		
		if(!checkvalided(account)){
			dismissLoadingView();
			return ;
		}
		
		sendMailOrPhone(account);
	}
	
	@Override
	public void onError(int code, String msg) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		DialogFactory.showNoCancelConfirmDialog(getString(R.string.error_server_parse), 
				getString(R.string.error_forget_pw), null);
	}

	@Override
	public void onSuccess(String t) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		
		if(!"0".equalsIgnoreCase(t)){
			ToastFactory.showMsg(AppContext.getAppContext(), R.string.forget_send_mail_phone);
			return ;
		}
		
		Intent intent = null;
		if(mIsPhone){
			intent = new Intent(this, PhoneVerifyActivity.class);
		} else {
			intent = new Intent(this, MailVerifyActivity.class);
		}
		intent.putExtra("nickname", "");
		intent.putExtra("accountname", mEditContent.getText().toString().trim());
		intent.putExtra("password", "");
		intent.putExtra("type", mIsPhone ? 1 : 2);
		startActivity(intent);
		finish();
	}
	
	private void sendMailOrPhone(String account){
//		ForgetRequestFacade.forgetPwRequest(this, mIsPhone ? PhoneOrMailCheckRequest.PhoneType 
//				: PhoneOrMailCheckRequest.MailType, account);
		RegisterRequestFacade.mailOrPhoneSendRequest(this, mIsPhone ? PhoneOrMailCheckRequest.PhoneType 
				: PhoneOrMailCheckRequest.MailType, account, true);
	}
	
	private void checkMailOrPhone(String account){
		try{
			long phoneNumber = Long.parseLong(account);
			boolean isValided = RegularUtil.isMobileMatch(phoneNumber + "");
			mIsPhone = isValided ? true : false;
			return ;
		}catch(Exception e){
		}
		mIsPhone = false;
	}
	
	private boolean checkvalided(String account){
		try{
			long phoneNumber = Long.parseLong(account);
			boolean isValided = RegularUtil.isMobileMatch(phoneNumber + "");
			mIsPhone = isValided ? true : false;
			if(!isValided){
				DialogFactory.showNoCancelConfirmDialog(
						getString(R.string.register_phone_error),
						getString(R.string.register_phone_error_content), 
						null);
				return false;
			}
			return true;
		}catch(Exception e){
		}
		
		// Here we check the mail address
		mIsPhone = false;
		if(!RegularUtil.isMailMatch(account)){
			DialogFactory.showNoCancelConfirmDialog(
					getString(R.string.register_mail_error),
					getString(R.string.register_mail_content), 
					null);
			return false;
		}
		return true;
	}
	
}
