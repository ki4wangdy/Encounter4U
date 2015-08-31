package com.imrub.shoulder.base.app.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.request.registerAndLogin.LoginUserInfo;

public class UserInfo {

	private static final String SP_TAG 				= UserInfo.class.getSimpleName();
	
	public static final String UserInfoName 		= "user_info";
	public static final String UserUid				= "user_uid";
	public static final String UserToken			= "user_token";
	
	public static final String ImServer				= "imServer";
	public static final String UserName				= "userName";
	public static final String HeaderLogo			= "headerLogo";
	public static final String Sex					= "sex";
	public static final String HomeTown				= "homeTown";
	public static final String Classes				= "classes";
	
	public static final String School				= "school";
	public static final String Favour				= "favour";
	public static final String Birth				= "birth";
	public static final String Constellation		= "constellation";
	public static final String Desc					= "description";
	
	public static final String logo					= "logo";

	private SharedPreferences config;
	private static UserInfo minstance;
	
	private UserInfo(){
		config = AppContext.getAppContext().getSharedPreferences(SP_TAG, Context.MODE_MULTI_PROCESS);
	}
	
	public static UserInfo getInstance(){
		if(minstance == null){
			minstance = new UserInfo();
		}
		return minstance;
	}
	
	public void putUid(String uid){
		getInstance().config.edit().putString(UserUid,uid).apply();
	}

	public String getUid(){
		return getInstance().config.getString(UserUid, "");
	}
	
	public void putToken(String token){
		getInstance().config.edit().putString(UserToken, token).apply();
	}
	
	public String getToken(){
		return getInstance().config.getString(UserToken, "");
	}
	
	public void setImServer(String imServer){
		getInstance().config.edit().putString(ImServer, imServer).apply();
	}
	
	public String getImServer(){
		return getInstance().config.getString(ImServer, "");
	}
	
	public void setUserName(String userName){
		getInstance().config.edit().putString(UserName, userName).apply();
	}
	
	public String getUserName(){
		return getInstance().config.getString(UserName, "");
	}
	
	public void setUserLogo(String userLogo){
		getInstance().config.edit().putString(HeaderLogo, userLogo).apply();
	}
	
	public String getUserLogo(){
		return getInstance().config.getString(HeaderLogo, "");
	}
	
	public void setSex(String sex){
		getInstance().config.edit().putString(Sex, sex).apply();
	}
	
	public String getSex(){
		return getInstance().config.getString(Sex, "");
	}
	
	public void setHomeTown(String homeTown){
		getInstance().config.edit().putString(HomeTown, homeTown).apply();
	}
	
	public String getHomeTown(){
		return getInstance().config.getString(HomeTown, "");
	}
	
	public void setClassess(String classes){
		getInstance().config.edit().putString(Classes, classes).apply();
	}
	
	public String getClasses(){
		return getInstance().config.getString(Classes, "");
	}

	public void setSchool(String school){
		getInstance().config.edit().putString(School, school).apply();
	}
	
	public String getSchool(){
		return getInstance().config.getString(School, "");
	}
	
	public void setFavour(String favour){
		getInstance().config.edit().putString(Favour, favour).apply();
	}
	
	public String getFavour(){
		return getInstance().config.getString(Favour, "");
	}

	public void setBirth(String birth){
		getInstance().config.edit().putString(Birth, birth).apply();
	}
	
	public String getBirth(){
		return getInstance().config.getString(Birth, "");
	}
	
	public void putConste(String conste){
		getInstance().config.edit().putString(Constellation, conste).apply();
	}
	
	public String getConste(){
		return getInstance().config.getString(Constellation, "");
	}

	public void putDesc(String desc){
		getInstance().config.edit().putString(Desc, desc).apply();
	}
	
	public String getDesc(){
		return getInstance().config.getString(Desc, "");
	}

	public void initUserInfo(LoginUserInfo info){
		UserInfo.getInstance().setUserName(info.getNick_name());
		UserInfo.getInstance().setUserLogo(info.getHeader_logo());
		
		UserInfo.getInstance().setBirth(info.getBirth());
		UserInfo.getInstance().putConste(info.getConstellation());
		UserInfo.getInstance().putDesc(info.getDescription());
		UserInfo.getInstance().setSex(info.getSex()+"");

		UserInfo.getInstance().setHomeTown(info.getHometown());
		UserInfo.getInstance().setClassess(info.getClasses());
		UserInfo.getInstance().setSchool(info.getSchool());
		UserInfo.getInstance().setFavour(info.getFavour());
		UserInfo.getInstance().setUserLogo(info.getHeader_logo());
	}
	
}
