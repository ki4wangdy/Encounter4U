package com.imrub.shoulder.base.app.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.imrub.shoulder.base.app.AppContext;

public class SplashJpeg {

	private static final String SP_TAG 				= SplashJpeg.class.getSimpleName();
	
	private static final String SJ_ID				= "jpeg_id";
	private static final String SJ_Start_Time		= "jpeg_start_time";
	private static final String SJ_End_Time			= "jpeg_end_time";
	
	private static final String SJ_Name				= "jpeg_name";
	private static final String SJ_Check_Time		= "jpeg_once";
	
	private SharedPreferences config;
	private static SplashJpeg minstance;
	
	private SplashJpeg() {
		config = AppContext.getAppContext().getSharedPreferences(SP_TAG, Context.MODE_MULTI_PROCESS);
	}
	
	public static SplashJpeg getInstance(){
		if(minstance == null){
			minstance = new SplashJpeg();
		}
		return minstance;
	}
	
	public void setSplashIconId(String id){
		getInstance().config.edit().putString(SJ_ID, id).commit();
	}
	
	public String getSplashIconId(){
		return getInstance().config.getString(SJ_ID, "");
	}
	
	public void setStartTime(long time){
		getInstance().config.edit().putLong(SJ_Start_Time, time).commit();
	}
	
	public long getStartTime(){
		return getInstance().config.getLong(SJ_Start_Time, 0);
	}
	
	public void setEndTime(long time){
		getInstance().config.edit().putLong(SJ_End_Time, time).commit();
	}
	
	public long getEndTime(){
		return getInstance().config.getLong(SJ_End_Time, 0);
	}
	
	public String getSplashJpegName(){
		return SJ_Name;
	}
	
	public void setSplashOnceTime(long time){
		getInstance().config.edit().putLong(SJ_Check_Time, time).commit();
	}
	
	public long getSplashOnceTime(){
		return getInstance().config.getLong(SJ_Check_Time, 0);
	}
	
	
	
}
