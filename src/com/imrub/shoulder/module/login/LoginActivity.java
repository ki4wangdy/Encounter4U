package com.imrub.shoulder.module.login;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imrub.shoulder.ActivityController;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.Md5;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.module.addrlist.IFriend;
import com.imrub.shoulder.module.login.thirdparty.QQManager;
import com.imrub.shoulder.module.main.MainUIActivity;
import com.imrub.shoulder.module.register.ForgetPwActivity;
import com.imrub.shoulder.module.register.RegisterActivity;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.addrlist.FriendRequest;
import com.imrub.shoulder.module.request.registerAndLogin.LoginUserInfo;
import com.imrub.shoulder.widget.DialogFactory;
import com.tencent.connect.common.Constants;

public class LoginActivity extends BaseActivity implements IRequestResult<LoginUserInfo>{

	public static final String tag = "LoginActivity";
	
	private Button mQQButton;
	
	private Button mLogin;
	private Button mSignUp;
	
	private TextView mForgetText;
	
	private EditText mUserName;
	private EditText mPassword;
	
	private TextWatcher mTextWatcher = new TextWatcher() {
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
			if(mUserName.getText().length() == 0 || mPassword.getText().length() == 0){
				isNotInput = true;
			}
			
			if(isNotInput){
				mLogin.setEnabled(false);
			} else {
				mLogin.setEnabled(true);
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		ActivityController.getInstance().storeLoginActivity(this);
		initView();
		initListener();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
		handleNewIntent(intent);
	}
	
	private void handleNewIntent(Intent intent){
		String content = intent.getExtras().getString("content");
		mUserName.setText(content);
		mPassword.requestFocus();
	}
	
	private void initView(){
		
		mLogin = (Button)findViewById(R.id.login_button);
		mSignUp = (Button)findViewById(R.id.login_signup_button);
		
		mQQButton = (Button)findViewById(R.id.login_qq);
		mForgetText = (TextView)findViewById(R.id.login_forget_textview);
		
		mUserName = (EditText)findViewById(R.id.login_user_content);
		mPassword = (EditText)findViewById(R.id.login_password_content);
		
	}
	
	private void initListener(){
		mLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickLogin();
			}
		});
		
		mSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickRegister();
			}
		});
		
		mQQButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickQQLogin();
			}
		});
		
		mForgetText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickForgetPw();
			}
		});

		mUserName.addTextChangedListener(mTextWatcher);
		mPassword.addTextChangedListener(mTextWatcher);

		mForgetText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickForgetPw();
			}
		});
	}
	
	@Override
	public void onError(int code, String msg) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		DialogFactory.showNoCancelConfirmDialog(getString(R.string.error_login), 
				getString(R.string.login_username_password_error), null);
	}

	@Override
	public void onSuccess(LoginUserInfo t) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		
		// 二、获取好友信息
		showLoadingView(R.string.login_get_friend_info);
		getFriendInfo();
		
	}

	private void getFriendInfo(){
		FriendRequest.getFriend(new IFriend() {
			@Override
			public void onFinished(List<UserFriend> friends) {
				getUserInfo();
			}
		});
	}

	private void getUserInfo(){
		// 三、获取用户信息
//		getUserInfo();
		dismissLoadingView();
		// 四、跳转单主界面
		forwardToMainActivity();
	}
	
	private void forwardToMainActivity(){
		startActivity(new Intent(this, MainUIActivity.class),
				R.anim.splash_anim_fade_in, R.anim.splash_anim_fade_out);
		ActivityController.getInstance().storeLoginActivity(null);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.REQUEST_API) {
			if(resultCode == Constants.RESULT_LOGIN) {
				QQManager.getInstance().handleLoginData(data);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void onClickForgetPw(){
		startActivity(new Intent(this, ForgetPwActivity.class));
	}
	
	private void onClickRegister(){
		startActivity(new Intent(this, RegisterActivity.class));
	}
	
	private void onClickQQLogin(){
		QQManager.getInstance().login(this);
	}
	
	private void onClickLogin(){
		if(!isNetworkConnected()){
			return ;
		}
		showLoadingView(R.string.login_verification_ing);
		LoginRequestFacade.cajianLoginRequest(this, 2, mUserName.getText().toString().trim(), 
				Md5.getMD5FullStr(mPassword.getText().toString()));
	}
	
	@Override
	public void onBackPressed() {
		moveTaskToBack(false);
	}
	
}
