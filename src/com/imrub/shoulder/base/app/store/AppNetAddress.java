package com.imrub.shoulder.base.app.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.imrub.shoulder.base.app.AppContext;

public class AppNetAddress {

	private static final String SP_TAG 		= AppNetAddress.class.getSimpleName();

	public static final String AppBaseAddress	 	=	"121.42.10.208";
	public static final String AppLogoAddress		=	"http://121.42.10.208:8090";
	public static final String AppHttpAddress		=   "http://121.42.10.208:8080";
	
	public static final String AppBaseNetAddress 	= 	"AppBaseNetAddress";
	public static final String AppBaseLogoAddress	= 	"AppBaseLogoAddress";
	public static final String AppBaseHttpAddress	=	"AppBaeHttpAddress";
	
	private SharedPreferences config;
	
	private static AppNetAddress minstance;
	private AppNetAddress(){
		config = AppContext.getAppContext().getSharedPreferences(SP_TAG, Context.MODE_MULTI_PROCESS);
	}
	
	public static AppNetAddress getInstance(){
		if(minstance == null){
			minstance = new AppNetAddress();
		}
		return minstance;
	}
	
	public String getBaseNetAddress(){
		return getInstance().config.getString(AppBaseNetAddress, AppBaseAddress);
	}
	
	public void setBaseNetAddress(String address){
		getInstance().config.edit().putString(AppBaseNetAddress, address);
	}
	
	public String getHttpBaseNetAddress(){
		return getInstance().config.getString(AppBaseNetAddress, AppHttpAddress);
	}
	
	public String getHttpLogoNetAddress(){
		return getInstance().config.getString(AppBaseLogoAddress, AppLogoAddress);
	}
	
	
}
