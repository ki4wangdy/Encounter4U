package com.imrub.shoulder.module.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.Md5;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.login.LoginActivity;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.registerAndLogin.RegisterRequestFacade;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.Title;
import com.imrub.shoulder.widget.ToastFactory;

public class ForgetNewPwActivity extends BaseActivity implements IRequestResult<String>{

	private Title mTitle;
	private TextView mTitleContent;
	
	private String mAccount;
	private int mType;
	
	private EditText mFirstPw;
	private EditText mSecondPw;
	
	private View mFirstPwLine;
	private View mSecondPwLine;
	
	private Button mNext;
	
	private final OnFocusChangeListener mFocusListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			final int id = v.getId();
			switch (id) {
				case R.id.forget_newpw_edittext:
					updateFocuseBackground(mFirstPwLine, hasFocus);
					break;
				case R.id.forget_renewpw_edittext:
					updateFocuseBackground(mSecondPwLine, hasFocus);
					break;
			}
		}
	};
	
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
			if(mFirstPw.getText().length() < 6 || mSecondPw.getText().length() < 6 ){
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
		setContentView(R.layout.forget_new_pw);
		initData();
		initView();
	}
	
	private void initData(){
		mAccount = getIntent().getExtras().getString("accountname");
		mType = getIntent().getExtras().getInt("type");
	}
	
	private void initView(){
		View v = findViewById(R.id.forget_newpw_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.forget_pw_title));
		
		mTitleContent = (TextView)findViewById(R.id.forget_newpw_content);
		mTitleContent.setText(Html.fromHtml(getString(R.string.forget_renewpw_part1) + 
				"<font color=#e63922>"+getString(R.string.forget_renewpw_part2)+"</font>"));
		
		mFirstPw = (EditText)findViewById(R.id.forget_newpw_edittext);
		mFirstPw.setOnFocusChangeListener(mFocusListener);
		mFirstPw.addTextChangedListener(mTextWatcher);
		
		mSecondPw = (EditText)findViewById(R.id.forget_renewpw_edittext);
		mSecondPw.setOnFocusChangeListener(mFocusListener);
		mSecondPw.addTextChangedListener(mTextWatcher);
		
		mFirstPwLine = findViewById(R.id.forget_newpw_line);
		mSecondPwLine = findViewById(R.id.forget_renewpw_line);
		
		mNext = (Button)findViewById(R.id.forget_renewpw_button);
		mNext.setEnabled(false);
		mNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickNext();
			}
		});
	}
	
	private void onClickNext(){
		String fpw = mFirstPw.getText().toString().trim();
		String spw = mSecondPw.getText().toString().trim();
		
		if(!fpw.equalsIgnoreCase(spw)){
			DialogFactory.showNoCancelConfirmDialog(
					getString(R.string.forget_newpw_error),
					getString(R.string.forget_newpw_content_), 
					null);
			return ;
		}
		sendChangePwRequest();
	}
	
	@Override
	public void onError(int code, String msg) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		DialogFactory.showNoCancelConfirmDialog(getString(R.string.error_server_parse), 
				getString(R.string.error_forget_reset), null);
	}

	@Override
	public void onSuccess(String t) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		
		if(!"0".equalsIgnoreCase(t)){
			ToastFactory.showMsg(AppContext.getAppContext(), R.string.forget_modify_error);
			return ;
		}
		
		Intent intent = new Intent(this,LoginActivity.class);
		intent.putExtra("content", mAccount);
		startActivity(intent);
		finish();
		ToastFactory.showMsg(this, R.string.forget_modify_success);
	}
	
	private void sendChangePwRequest(){
		showLoadingView(R.string.forget_resetpw_ing);
		RegisterRequestFacade.resetForgetPwRequest(this, mType, mAccount, Md5.getMD5FullStr(mFirstPw.getText().toString()));
	}
	
	private void updateFocuseBackground(View v, boolean hasFocus){
		v.setBackgroundResource(hasFocus ? R.drawable.register_nick_block_bg : 
			R.drawable.register_nick_line_default);
	}
	
}
