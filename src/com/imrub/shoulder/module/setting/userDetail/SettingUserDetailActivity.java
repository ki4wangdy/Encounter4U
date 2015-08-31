package com.imrub.shoulder.module.setting.userDetail;

import java.net.HttpURLConnection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.ActionNull;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.net.NetworkManager;
import com.imrub.shoulder.base.app.path.EnvirPath;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.io.file.UploadFileToolkit;
import com.imrub.shoulder.base.util.ImageSizeUtils;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.base.util.bitmaploader.BitmapLoaderManager;
import com.imrub.shoulder.module.addrlist.newfriend.UserLogoMsgManager;
import com.imrub.shoulder.module.photopicker.PhotoPicker;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.setting.SettingRequestFacade;
import com.imrub.shoulder.module.request.setting.UserInfoSettingRequest;
import com.imrub.shoulder.module.setting.UserLogoManager;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.Title;
import com.imrub.shoulder.widget.ToastFactory;

public class SettingUserDetailActivity extends BaseActivity implements View.OnClickListener,IRequestResult<String>{

	public static final String Tag = "SettingUserDetailActivity";
	
	private Title mTitle;
	private ImageView mUserLogo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_user_detail);
		initView();
	}
	
	private void initView(){
		initTitle();
		initListener();
		initData();
	}

	private void initData(){
		long time = System.currentTimeMillis();
		mUserLogo = (ImageView)findViewById(R.id.setting_user_image);
		int width = AppContext.getDimensionPixelSize(R.dimen.setting_user_image_width_height);
		BitmapLoaderManager.getInstance().loadBitmap(ImageSizeUtils.get140x140(UserInfo.getInstance().getUserLogo()),
				mUserLogo, width, width, BitmapFactory.decodeResource(AppContext.getResource(), R.drawable.default_icon_140));
		long userTime = System.currentTimeMillis() - time;
		Logger.print(Tag, " SettingUserDetailActivity init imageView use:"+userTime);
	}
	
	private void initListener(){
		findViewById(R.id.setting_user_image_block).setOnClickListener(this);
		findViewById(R.id.setting_user_nickname_block).setOnClickListener(this);
		findViewById(R.id.setting_user_gexing_block).setOnClickListener(this);
		
		findViewById(R.id.setting_user_birthday_block).setOnClickListener(this);
		findViewById(R.id.setting_user_xingzuo_block).setOnClickListener(this);
		findViewById(R.id.setting_user_school_block).setOnClickListener(this);
		findViewById(R.id.setting_user_class_block).setOnClickListener(this);
		
		findViewById(R.id.setting_user_hometown_block).setOnClickListener(this);
		findViewById(R.id.setting_user_habbit_block).setOnClickListener(this);
	}
	
	private void initTitle(){
		View v = findViewById(R.id.setting_user_detail_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.setting_user_detail_title));
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		// 头像
		if(R.id.setting_user_image_block == id){
			if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
				startActivityForResult(new Intent(this, PhotoPicker.class), UserInfoSettingRequest.Type_Logo);
			}
			return ;
		}

		// 其他情况
		Intent intent = new Intent(this, SettingUserDetailModifyActivity.class);
		switch (id) {
			case R.id.setting_user_nickname_block:
				intent.putExtra("type", UserInfoSettingRequest.Type_nick);
				intent.putExtra("title", getString(R.string.setting_user_nickname_text2));
				break;
			case R.id.setting_user_gexing_block:
				intent.putExtra("type", UserInfoSettingRequest.Type_description);
				intent.putExtra("title", getString(R.string.setting_user_gexing_text));
				break;
			case R.id.setting_user_birthday_block:
				intent.putExtra("type", UserInfoSettingRequest.Type_birth);
				intent.putExtra("title", getString(R.string.setting_user_birthday_text));
				break;
			case R.id.setting_user_xingzuo_block:
				intent.putExtra("type", UserInfoSettingRequest.Type_Xingzuo);
				intent.putExtra("title", getString(R.string.setting_user_xingzuo_text));
				break;
			case R.id.setting_user_school_block:
				intent.putExtra("type", UserInfoSettingRequest.Type_School);
				intent.putExtra("title", getString(R.string.setting_user_school_text));
				break;
			case R.id.setting_user_class_block:
				intent.putExtra("type", UserInfoSettingRequest.Type_Class);
				intent.putExtra("title", getString(R.string.setting_user_class_text));
				break;
			case R.id.setting_user_hometown_block:
				intent.putExtra("type", UserInfoSettingRequest.Type_Hometown);
				intent.putExtra("title", getString(R.string.setting_user_hometown_text));
				break;
			case R.id.setting_user_habbit_block:
				intent.putExtra("type", UserInfoSettingRequest.Type_habbit);
				intent.putExtra("title", getString(R.string.setting_user_habbit_text));
				break;
		}
		
		if(id == R.id.setting_user_birthday_block){
			DialogFactory.showTwoListviewDialog(
					getString(R.string.setting_user_birthday_text),
					getString(R.string.dialog_common_title) , 
					getString(R.string.dialog_confirm_phone_text) ,
					new ActionNull() {
						@Override
						public void onExecute() {
						}
					}
			);
			
			return ;
		}
		
		startActivityForResult(intent, UserInfoSettingRequest.Type_nick);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch (requestCode) {
				case UserInfoSettingRequest.Type_nick:
					break;
				case UserInfoSettingRequest.Type_Logo:
					Bitmap bitmap = BitmapFactory.decodeFile(EnvirPath.getPhotoPickerCapturePath());
					if(bitmap != null){
						mUserLogo.setImageBitmap(bitmap);
					}
					uploadUserLogo();
					break;
			}
		}
	}
	
	private void uploadUserLogo(){
		boolean isConnected = NetworkManager.isNetworkConnected(AppContext.getAppContext());
		if(!isConnected){
			return ;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				int code = UploadFileToolkit.uploadFile(HttpUrlConstant.HttpUploadLogo,EnvirPath.getappcacheuserimagelogo(),null);
				if(code == HttpURLConnection.HTTP_OK){
					UserLogoMsgManager.getInstance().notifyAllUsers(UserInfo.getInstance().getUserLogo());
					updateHttp();
					return ;
				}
				ToastFactory.showMsg(AppContext.getAppContext(), R.string.msg_failed_upload_userlogo);
			}
		}).start();
	}
	
	private void updateHttp(){
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("value", UserInfo.getInstance().getUserLogo());
		obj.put("type", UserInfoSettingRequest.Type_Logo);
		array.add(obj);
		SettingRequestFacade.setUserInfo(this, UserInfo.getInstance().getUid(), 
				UserInfo.getInstance().getToken(), array);
	}
	
	@Override
	public void onError(int code, String msg) {
		ToastFactory.showMsg(AppContext.getAppContext(), R.string.msg_failed_upload_userlogo);
	}

	@Override
	public void onSuccess(String t) {
		UserLogoManager.getInstance().fireLogoChange(UserInfo.getInstance().getUserLogo());
		ToastFactory.showMsg(AppContext.getAppContext(), R.string.msg_success_upload_userlogo);
	}
	
}
