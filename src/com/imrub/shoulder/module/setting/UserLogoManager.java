package com.imrub.shoulder.module.setting;

public class UserLogoManager {

	private IUserLogo mListener;
	
	private static UserLogoManager mInstance;
	private UserLogoManager(){
	}

	public static final UserLogoManager getInstance(){
		if(mInstance == null){
			mInstance = new UserLogoManager();
		}
		return mInstance;
	}
	
	public void registerListener(IUserLogo listener){
		mListener = listener;
	}
	
	public void fireLogoChange(String url){
		if(mListener != null){
			mListener.onUserLogoChange(url);
		}
	}
	
}
