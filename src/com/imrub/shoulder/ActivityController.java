package com.imrub.shoulder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityController {

	private Activity mLoginActivity;
	private Context mContext;
	
	private static ActivityController mInstance;
	private ActivityController(){
	}

	public static ActivityController getInstance(){
		if(mInstance == null){
			mInstance = new ActivityController();
		}
		return mInstance;
	}

	public void setCurrentActivity(final Context activity){
		if(!(activity instanceof Activity)){
			throw new RuntimeException("this context is not activity!");
		}
		mContext = activity;
	}
	
	public Context getCurrentActivity(){
		return getInstance().mContext;
	}

	public void storeLoginActivity(Activity loginActivity){
		mLoginActivity = loginActivity;
	}
	
	public void finishLoginActivity(){
		if(mLoginActivity != null){
			mLoginActivity.finish();
			mLoginActivity = null;
		}
	}
	
	public static void startActivity(final Intent intent){
		final Context currentActivty = getInstance().getCurrentActivity();
		if(currentActivty != null){
			currentActivty.startActivity(intent);
		}
	}
	
}
