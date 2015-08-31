package com.imrub.shoulder.base.app.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.imrub.shoulder.base.app.AppContext;

/**
 * 所有第一次的标示，都用这个类标示
 * @author ki
 *
 */
public class AppConfig {

	private static final String SP_TAG 		= AppConfig.class.getSimpleName();
	
	public static final String Item_IsFirstRun 				= 	"isFirstRun";
	public static final String Item_IsFirstUserLogin		= 	"isUserFirstLogin";
	public static final String Item_IsFirstClickSetting		=	"isSettingFirstClick";
	
	private SharedPreferences config;
	private static AppConfig minstance;
	private AppConfig(){
		config = AppContext.getAppContext().getSharedPreferences(SP_TAG, Context.MODE_MULTI_PROCESS);
	}
	
	public static AppConfig getInstance(){
		if(minstance == null){
			minstance = new AppConfig();
		}
		return minstance;
	}
	
	public void setIsAppFirstRun(boolean isFirstRun){
		getInstance().config.edit().putBoolean(Item_IsFirstRun, isFirstRun).commit();
	}
	
	public boolean isAppFirstRun(){
		return getInstance().config.getBoolean(Item_IsFirstRun, true);
	}
	
	public void setUserFirstRegister(boolean isFirstLogin){
		getInstance().config.edit().putBoolean(Item_IsFirstUserLogin, isFirstLogin).commit();
	}
	
	public boolean isUserFirstRegister(){
		return getInstance().config.getBoolean(Item_IsFirstUserLogin, true);
	}
	
	public void setUserFirstClickSetting(boolean isFirst){
		getInstance().config.edit().putBoolean(Item_IsFirstClickSetting, isFirst).commit();
	}
	
	public boolean isUserFirstClickSetting(){
		return getInstance().config.getBoolean(Item_IsFirstClickSetting, false);
	}
	
}
