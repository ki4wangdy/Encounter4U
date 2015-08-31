package com.imrub.shoulder.module.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.ActionNull;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.path.EnvirPath;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.io.file.UploadFileToolkit;
import com.imrub.shoulder.base.util.RegularUtil;
import com.imrub.shoulder.module.login.LoginActivity;
import com.imrub.shoulder.module.photopicker.PhotoPicker;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.registerAndLogin.PhoneOrMailCheckRequest;
import com.imrub.shoulder.module.request.registerAndLogin.RegisterRequestFacade;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.Title;

public class RegisterActivity extends BaseActivity implements IRequestResult<String>{

	private TextView mDescription;
	private ImageView mPhotoView;
	
	private EditText mNickEdit;
	
	private ImageView mDeleteAccountIconView;
	private EditText mAccountEdit;
	
	private ImageView mPasswordIconView;
	private EditText mPasswordEdit;
	
	private Title mTitle;
	
	private View mNickLine;
	private View mAccountLine;
	private View mPasswordLine;
	
	private Button mRegisterButton;
	private boolean mIsPhone;
	
	private static final int Check_First = 1;
	private static final int Check_Seoncd = 2;
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
			updateUI(s);
		}
		
		private void updateUI(Editable s){
			if(mAccountEdit.isFocused()){
				mDeleteAccountIconView.setVisibility(android.text.TextUtils.isEmpty(mAccountEdit.getText().
						toString())? View.GONE : View.VISIBLE);
			}
		}
		
		private void updateloginBg(){
			boolean isNotInput = false;
			if(mNickEdit.getText().length() == 0 || mPasswordEdit.getText().length() == 0 
					|| mAccountEdit.getText().length() == 0 || mPasswordEdit.getText().length() < 6){
				isNotInput = true;
			}
			
			if(isNotInput){
				mRegisterButton.setBackgroundResource(R.drawable.register_button_pre);
				mRegisterButton.setEnabled(false);
			} else {
				mRegisterButton.setBackgroundResource(R.drawable.register_button_bg);
				mRegisterButton.setEnabled(true);
			}
		}
		
	};
	
	private final OnFocusChangeListener mFocusListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			final int id = v.getId();
			switch (id) {
				case R.id.register_nick_content:
					updateFocuseBackground(mNickLine, hasFocus);
					break;
				case R.id.register_account_content:
					updateFocuseBackground(mAccountLine, hasFocus);
					if(!hasFocus){
						mDeleteAccountIconView.setVisibility(View.GONE);
					} else {
						mDeleteAccountIconView.setVisibility(android.text.TextUtils.isEmpty(mAccountEdit.getText().
								toString())? View.GONE : View.VISIBLE);
					}
					break;
				case R.id.register_password_content:
					updateFocuseBackground(mPasswordLine, hasFocus);
					break;
			}
		}
	};
	
	private void updateFocuseBackground(View v, boolean hasFocus){
		v.setBackgroundResource(hasFocus ? R.drawable.register_nick_block_bg : 
			R.drawable.register_nick_line_default);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_view);
		initView();
		initListener();
	}
	
	private void initView(){
		
		View v = findViewById(R.id.register_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.title_title_content));
		
		mDescription = (TextView)findViewById(R.id.register_text_content);
		mDescription.setText(Html.fromHtml(getString(R.string.register_text_content_part1) + "<font color=#00479d>"+
				getString(R.string.register_text_content_part2)+"</font>" + getString(R.string.register_text_content_part3)));
		
		mPhotoView = (ImageView)findViewById(R.id.register_photo);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickPhotoView();
			}
		});
		
		mNickEdit = (EditText)findViewById(R.id.register_nick_content);
		mNickEdit.setOnFocusChangeListener(mFocusListener);
		mNickEdit.addTextChangedListener(mTextWatcher);
		
		mAccountEdit = (EditText)findViewById(R.id.register_account_content);
		mAccountEdit.addTextChangedListener(mTextWatcher);
		mAccountEdit.setOnFocusChangeListener(mFocusListener);
		
		mDeleteAccountIconView = (ImageView)findViewById(R.id.register_delete_icon);
		mDeleteAccountIconView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickDeleteIconView();
			}
		});
		
		mPasswordIconView = (ImageView)findViewById(R.id.register_password_icon);
		mPasswordIconView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final boolean isClickPassword = isClickPasswordIcon(v);
				onClickPasswordIconView(isClickPassword);
				v.setTag(R.string.register_delete_icon_tag, isClickPassword ? false : true);
			}
		});
	
		mPasswordEdit = (EditText)findViewById(R.id.register_password_content);
		mPasswordEdit.setOnFocusChangeListener(mFocusListener);
		mPasswordEdit.addTextChangedListener(mTextWatcher);
		
		mNickLine = findViewById(R.id.register_nick_line);
		mAccountLine = findViewById(R.id.register_account_line);
		mPasswordLine = findViewById(R.id.register_password_line);
		
		mRegisterButton = (Button)findViewById(R.id.register_button);
		mRegisterButton.setEnabled(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			Bitmap bitmap = BitmapFactory.decodeFile(EnvirPath.getPhotoPickerCapturePath());
			if(bitmap != null){
				mPhotoView.setImageBitmap(bitmap);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void initListener(){
		mRegisterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickRegister();
			}
		});
	}
	
	private void onClickRegister(){
		if(!isNetworkConnected()){
			return ;
		}
		
		String account = mAccountEdit.getText().toString().trim();
		checkMailOrPhone(account);
		showLoadingView(mIsPhone ? 
				getString(R.string.loading_textview_text) : 
				getString(R.string.loading_textview_text_mail));
		
		if(!checkvalided(account)){
			dismissLoadingView();
			return ;
		}
		
		verificatePhoneOrMail(account);
	}
	
	@Override
	public void onError(int code, String msg) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		DialogFactory.showNoCancelConfirmDialog(getString(R.string.error_server_parse), 
				getString(R.string.dialog_error_phoneormail), null);
	}

	private void relocationLoginActivity(){
		DialogFactory.showNoTitleConfirmDialog(
				mIsPhone ? getString(R.string.dialog_confirm_phone_registered) : getString(R.string.dialog_confirm_mail_registered), 
				new ActionNull() {
			@Override
			public void onExecute() {
				toLoginActivity();
			}
		});
	}
	
	private void toLoginActivity(){
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra("content", mAccountEdit.getText().toString().trim());
		startActivity(intent);
	}
	
	@Override
	public void onSuccess(String t) {
		if(!isLoadingViewShow()){
			return ;
		}
		dismissLoadingView();
		
		// 保存一份用户名
		UserInfo.getInstance().setUserName(mNickEdit.getText().toString());
		
		// 发送验证码成功，跳转下一个界面
		if(type == Check_Seoncd){
			Intent intent = null;
			if(mIsPhone){
				intent = new Intent(this, PhoneVerifyActivity.class);
			} else {
				intent = new Intent(this, MailVerifyActivity.class);
			}
			intent.putExtra("nickname", mNickEdit.getText().toString());
			intent.putExtra("accountname", mAccountEdit.getText().toString().trim());
			intent.putExtra("password", mPasswordEdit.getText().toString());
			intent.putExtra("type", mIsPhone ? 1 : 2);
			uploadImage();
			startActivity(intent);
			finish();
			return ;
		}
		
		// 检查用户是否存在, ExistFlag
		if("0".equalsIgnoreCase(t)){
			DialogFactory.showConfirmDialog(mIsPhone ? getString(R.string.dialog_common_title) : getString(R.string.dialog_confirm_mail_title), 
					mIsPhone ? getString(R.string.dialog_confirm_phone_text) : getString(R.string.dialog_confirm_mail_text),
							mAccountEdit.getText().toString().trim(),
							new ActionNull() {
						@Override
						public void onExecute() {
							sendMailOrPhone();
						}
					});
		} else if("1".equalsIgnoreCase(t)){
			relocationLoginActivity();
		}
	}

	private void sendMailOrPhone(){
		showLoadingView(mIsPhone ? getString(R.string.register_phone_send_ing) : getString(R.string.register_mail_send_ing));
		type = Check_Seoncd;
		RegisterRequestFacade.mailOrPhoneSendRequest(this, mIsPhone ? PhoneOrMailCheckRequest.PhoneType 
				: PhoneOrMailCheckRequest.MailType, mAccountEdit.getText().toString().trim(), true);
	}
	
	
	private void verificatePhoneOrMail(String content){
		type = Check_First;
		RegisterRequestFacade.mailOrPhoneCheckRequest(this, mIsPhone ? PhoneOrMailCheckRequest.PhoneType 
				: PhoneOrMailCheckRequest.MailType, content, false);
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
	
	private void onClickPhotoView(){
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
			startActivityForResult(new Intent(this, PhotoPicker.class), 101);
		}
	}
	
	private void onClickDeleteIconView(){
		mAccountEdit.setText("");
		mDeleteAccountIconView.setVisibility(View.GONE);
	}
	
	private void onClickPasswordIconView(boolean isClickPasswordIcon){
		int end = mPasswordEdit.getSelectionEnd();
		if(isClickPasswordIcon){
			mPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			mPasswordIconView.setImageResource(R.drawable.register_password_bg_hover);
		} else {
			mPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
			mPasswordIconView.setImageResource(R.drawable.register_password_bg);
		}
		mPasswordEdit.setSelection(end);
	}
	
	private boolean isClickPasswordIcon(View v){
		Object obj = v.getTag(R.string.register_delete_icon_tag);
		if(obj == null){
			return true;
		} else {
			if(obj instanceof Boolean){
				Boolean o = (Boolean)obj;
				return o.booleanValue();
			}
		}
		return false;
	}

	private void uploadImage(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				UploadFileToolkit.uploadFile(HttpUrlConstant.HttpUploadLogo,EnvirPath.getappcacheuserimagelogo(),null);
			}
		}).start();
	}
	
}
