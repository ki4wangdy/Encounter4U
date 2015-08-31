package com.imrub.shoulder.module.register;

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
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.Title;

public class PhoneVerifyActivity extends BaseActivity{

	private Title mTitle;
	
	private EditText mEditText;
	private Button mButton;
	
	private TextView mTime;
	private TextView mTimeOver;
	
	private TextView mPhoneNumber;
	
//	private String mNickName;
	private String mAccountName;
//	private String mPassword;
	
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
		setContentView(R.layout.phone_verify_view);
		initData();
		initView();
	}
	
	private void initData(){
//		mNickName = getIntent().getExtras().getString("nickname");
		mAccountName = getIntent().getExtras().getString("accountname");
//		mPassword = getIntent().getExtras().getString("password");
	}
	
	private void initView(){
		
		View v = findViewById(R.id.phone_verify_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.phone_verify_title));
		
		mTitle.setOnBackListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onClickBack();
			}
		});

		TextView content = (TextView)findViewById(R.id.phone_verify_content);
		content.setText(Html.fromHtml(getString(R.string.phone_verify_part_1) + "<font color=#e63922>"+
				getString(R.string.phone_verify_block_text)+"</font>" + getString(R.string.phone_verify_part_3)));
		
		mEditText = (EditText)findViewById(R.id.phone_verify_edittext);
		mEditText.addTextChangedListener(mTextWatcher);
		
		mButton = (Button)findViewById(R.id.phone_verify_button);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickNext();
			}
		});
		
		mPhoneNumber = (TextView)findViewById(R.id.phone_verify_phone_number);
		mPhoneNumber.setText(" +86 " + mAccountName);
		
		mTime = (TextView)findViewById(R.id.phone_verify_time);
		mTime.setText(getString(R.string.phone_verify_time_text, 60));
		startTimer();
		
		mTimeOver = (TextView)findViewById(R.id.phone_verify_time_over);
		mTimeOver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mTime.setVisibility(View.VISIBLE);
				mTime.setText(getString(R.string.phone_verify_time_text, 60));
				mTimeOver.setVisibility(View.INVISIBLE);
				startTimer();
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
				}
				return isContinue;
			}
		});

	}
	
	private void onClickNext(){
		DialogFactory.showNoTitleNoCancelConfirmDialog(getString(R.string.phone_verify_error_code), 
				new ActionNull() {
			@Override
			public void onExecute() {
			}
		});
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
