package com.imrub.shoulder.module.setting;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.ImageSizeUtils;
import com.imrub.shoulder.base.util.bitmaploader.BitmapLoaderManager;
import com.imrub.shoulder.module.ProgressBarAnimationListener;
import com.imrub.shoulder.module.hotpoint.SettingHotpointManager;
import com.imrub.shoulder.module.main.MainUIActivity;
import com.imrub.shoulder.module.setting.about.SettingAboutActivity;
import com.imrub.shoulder.module.setting.cajiansetting.SettingCajianActivity;
import com.imrub.shoulder.module.setting.userAccount.SettingUserAccountActivity;
import com.imrub.shoulder.module.setting.userChat.SettingChatActivity;
import com.imrub.shoulder.module.setting.userDetail.SettingUserDetailActivity;

public class SettingViewProxy extends Fragment implements OnClickListener, IUserLogo{

	public static final int Request_Code_User_Detail = 1 << 3;
	public static final int Request_Code_User_Logo	 = 1 << 4;
 
	private ImageView mUserIconHotpoint;
	private ImageView mUserIcon;
	
	private boolean isinited = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.main_setting_view, null);
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(!isinited && isVisibleToUser){
			ThreadFacade.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateMainUI();
				}
			},MainUIActivity.LazyLoadTime);
			isinited = true;
		}
	}
	
	@Override
	public void onUserLogoChange(String url) {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				initData(getView());
			}
		});
	}
	
	private void updateMainUI(){
		// 如果是第一次进入，那么需要把setting上的红点给取点，参考设置文档
		SettingHotpointManager.firstClickSetting();
		UserLogoManager.getInstance().registerListener(this);
		
		initListener(getView());
		initData(getView());
		setInVisiableView();
	}
	
	private void initData(View view){
		mUserIcon = (ImageView)view.findViewById(R.id.setting_user_image);
		int width = AppContext.getDimensionPixelSize(R.dimen.setting_user_image_width_height);
		String url = ImageSizeUtils.get140x140(UserInfo.getInstance().getUserLogo());
		BitmapLoaderManager.getInstance().loadBitmap(url,
				mUserIcon, width, width, BitmapFactory.decodeResource(AppContext.getResource(), R.drawable.default_icon_140));
	}
	
	private void initListener(View v){
		ViewStub stub = (ViewStub)v.findViewById(R.id.main_setting_block);
		stub.inflate();
		
		initView(v);
		
		mUserIconHotpoint = (ImageView)v.findViewById(R.id.setting_icon_hotpoint);
		mUserIconHotpoint.setVisibility(SettingHotpointManager.
				isUserIconShow() ? View.VISIBLE : View.GONE);
		
		v.findViewById(R.id.setting_user).setOnClickListener(this);
		v.findViewById(R.id.setting_content_first_block).setOnClickListener(this);
		v.findViewById(R.id.setting_content_second_block).setOnClickListener(this);
		v.findViewById(R.id.setting_content_third_block).setOnClickListener(this);
		v.findViewById(R.id.setting_content_fourth_block).setOnClickListener(this);
		v.findViewById(R.id.setting_third_second_block).setOnClickListener(this);
		v.findViewById(R.id.setting_third_first_block).setOnClickListener(this);
	}
	
	private void initView(View v){
		TextView userName = (TextView)v.findViewById(R.id.setting_user_name);
		userName.setText(UserInfo.getInstance().getUserName());
		
		TextView cajianId = (TextView)v.findViewById(R.id.setting_user_gexing);
		String id = UserInfo.getInstance().getUid();
		cajianId.setText(AppContext.getAppContext().getString(R.string.setting_user_gexing, id));
	}
	
	private void setInVisiableView(){
		final View v = getView().findViewById(R.id.progress_bar);
		Animation animation = AnimationUtils.loadAnimation(AppContext.getAppContext(), 
				R.anim.default_progressbar_out);
		animation.setAnimationListener(new ProgressBarAnimationListener(v));
		v.startAnimation(animation);
	}
	
	public void onClick(View view){
		switch (view.getId()) {
			case R.id.setting_user:
				startActivityForResult(new Intent(this.getActivity(), 
						SettingUserDetailActivity.class), Request_Code_User_Detail);
				break;
			case R.id.setting_content_first_block:
				startActivity(new Intent(this.getActivity(), SettingUserNotificationActivity.class));
				break;
			case R.id.setting_content_second_block:
				startActivity(new Intent(this.getActivity(), SettingCajianActivity.class));
				break;
			case R.id.setting_content_third_block:
				startActivity(new Intent(this.getActivity(), SettingChatActivity.class));
				break;
			case R.id.setting_content_fourth_block:
				startActivity(new Intent(this.getActivity(), SettingYuanfenActivity.class));
				break;
			case R.id.setting_third_first_block:
				startActivity(new Intent(this.getActivity(), SettingAboutActivity.class));
				break;
			case R.id.setting_third_second_block:
				startActivity(new Intent(this.getActivity(), SettingUserAccountActivity.class));
				break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case Request_Code_User_Detail:
				final boolean isShow = SettingHotpointManager.isUserIconShow();
				if(!isShow){
					mUserIconHotpoint.setVisibility(View.GONE);
				}
				break;
		}
	}
	
}
