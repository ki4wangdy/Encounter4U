package com.imrub.shoulder.module.setting.cajiansetting;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.module.ProgressBarAnimationListener;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.setting.SettingRequestFacade;
import com.imrub.shoulder.widget.Title;
import com.imrub.shoulder.widget.ToastFactory;

public class SettingCajianDetailActivity extends BaseActivity implements IRequestResult<String>{

	private Title mTitle;

	private ListView mListView;
	private SettingCajianDetailAdapter mDapter;
	
	private ImageView mDefaultLoadingView;
	private View mDefaultLoadingBlockView;
	
	private int mType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_cajiansetting_2);
		initType();
		initView();
		asyncRequest();
	}

	private void initType(){
		int type = getIntent().getExtras().getInt("title", -1);
		if(type == -1){
			return ;
		}
		
		switch (type) {
			case SettingCajianActivity.TitleTypeMiddle:
				mType = 3;
				break;
			case SettingCajianActivity.TitleTypeMiddle2:
				mType = 4;
				break;
			case SettingCajianActivity.TitleTypeFinal:
				mType = 5;
				break;
			case SettingCajianActivity.TitleTypeNotSee:
				mType = 1;
				break;
			case SettingCajianActivity.TitleTypeNotLetSee:
				mType = 2;
				break;
		}
		
	}
	
	private void initView(){
		initTitle();
		initDefaultLoadingView();
	}

	private void initDefaultLoadingView(){
		ViewStub stub = (ViewStub)findViewById(R.id.setting_cajiansetting_default_loading);
		stub.setVisibility(View.VISIBLE);
		
		mDefaultLoadingView = (ImageView)findViewById(R.id.loading_image);
		mDefaultLoadingBlockView = findViewById(R.id.loading_view);
		
		Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.default_setting_loading);  
		LinearInterpolator lin = new LinearInterpolator();  
		operatingAnim.setInterpolator(lin);  

		mDefaultLoadingView.startAnimation(operatingAnim);
		
	}
	
	private void asyncRequest(){
		String uid = UserInfo.getInstance().getUid();
		String token = UserInfo.getInstance().getToken();
		SettingRequestFacade.getUserRelativeInfoRequest(this, uid, token, mType);
	}

	@Override
	public void onError(int code, String msg) {
		ToastFactory.showMsg(this, R.string.setting_user_fail);
	}

	@Override
	public void onSuccess(String t) {
		hideLoadingView();
		if("".equalsIgnoreCase(t)){
			initEmptyView();
			return ;
		}
		List<AuthorityUserInfo> userInfos = JSONObject.parseArray(t, AuthorityUserInfo.class);
		initListView();
		mDapter = new SettingCajianDetailAdapter(userInfos);
		mListView.setAdapter(mDapter);
	}
	
	private void hideLoadingView(){
		Animation animation = AnimationUtils.loadAnimation(AppContext.getAppContext(), 
				R.anim.default_progressbar_out);
		animation.setAnimationListener(new ProgressBarAnimationListener(mDefaultLoadingBlockView));
		mDefaultLoadingView.startAnimation(animation);
	}
	
	private void initEmptyView(){
		ViewStub stub = (ViewStub)findViewById(R.id.setting_cajiansetting_empty_view);
		stub.setVisibility(View.VISIBLE);
		
		TextView text1 = (TextView)findViewById(R.id.setting_default_text1);
		TextView text2 = (TextView)findViewById(R.id.setting_default_text2);
		
		switch (mType) {
			case 3:
				text1.setText(getString(R.string.setting_auth_type3_text1));
				text2.setText(getString(R.string.setting_auth_type3_text2));
				break;
			case 4:
				text1.setText(getString(R.string.setting_auth_type4_text1));
				text2.setText(getString(R.string.setting_auth_type4_text2));
				break;
			case 5:
				text1.setText(getString(R.string.setting_auth_type5_text1));
				text2.setText(getString(R.string.setting_auth_type5_text2));
				break;
			case 1:
				text1.setText(getString(R.string.setting_auth_type1_text1));
				text2.setText(getString(R.string.setting_auth_type1_text2));
				break;
			case 2:
				text1.setText(getString(R.string.setting_auth_type2_text1));
				text2.setText(getString(R.string.setting_auth_type2_text2));
				break;
		}
		
	}
	
	private void initListView(){
		TextView header = (TextView)findViewById(R.id.setting_cajiansetting_detail_header);
		header.setVisibility(View.VISIBLE);
		int type = getIntent().getIntExtra("title", 0);
		switch (type) {
			case SettingCajianActivity.TitleTypeMiddle:
				header.setText(getString(R.string.setting_cajiansetting_detail_midle_content));
				break;
			case SettingCajianActivity.TitleTypeMiddle2:
				break;
			case SettingCajianActivity.TitleTypeFinal:
				header.setText(getString(R.string.setting_cajiansetting_detail_final_content));
				break;
			case SettingCajianActivity.TitleTypeNotSee:
				header.setText(getString(R.string.setting_yuanfen_notsee_content));
				break;
			case SettingCajianActivity.TitleTypeNotLetSee:
				header.setText(getString(R.string.setting_yuanfen_notletsee_content));
				break;
		}
		
		mListView = (ListView)findViewById(R.id.setting_cajiansetting_detail_listview);
		mListView.setVisibility(View.VISIBLE);
	}
	
	private void initTitle(){
		View v = findViewById(R.id.setting_cajiansetting_detail_title);
		mTitle = new Title(v);
		
		int type = getIntent().getIntExtra("title", 0);
		switch (type) {
			case SettingCajianActivity.TitleTypeMiddle:
				mTitle.setContent(getString(R.string.setting_cajiansetting_detail_midle_title));
				break;
			case SettingCajianActivity.TitleTypeMiddle2:
				mTitle.setContent(getString(R.string.setting_cajiansetting_detail_midle2_title));
				break;
			case SettingCajianActivity.TitleTypeFinal:
				mTitle.setContent(getString(R.string.setting_cajiansetting_detail_final_title));
				break;
			case SettingCajianActivity.TitleTypeNotSee:
				mTitle.setContent(getString(R.string.setting_yuanfen_third_text));
				break;
			case SettingCajianActivity.TitleTypeNotLetSee:
				mTitle.setContent(getString(R.string.setting_yuanfen_fourth_text));
				break;
		}
	}
	
}
