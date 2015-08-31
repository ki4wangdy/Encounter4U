package com.imrub.shoulder.base.db.table.cajian;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.db.DBConstant;
import com.imrub.shoulder.base.db.table.TableItemBase;
import com.imrub.shoulder.module.model.UserInfo;
import com.imrub.shoulder.module.model.detail.MeetInfo;
import com.imrub.shoulder.module.model.detail.RequestInfo;
import com.imrub.shoulder.module.model.detail.SettingSwitch;
import com.imrub.shoulder.module.model.detail.UserDetailInfo;

public class UserDetailInfoData extends TableItemBase{

	private int uid;
	private int isFriend;

	private String userInfo;
	private String newsImage;
	
	private String meetInfo;
	private String requestInfo;
	
	private String settingsSwitch;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public int getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(int isFriend) {
		this.isFriend = isFriend;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getNewsImage() {
		return newsImage;
	}

	public void setNewsImage(String newsImage) {
		this.newsImage = newsImage;
	}

	public String getMeetInfo() {
		return meetInfo;
	}

	public void setMeetInfo(String meetInfo) {
		this.meetInfo = meetInfo;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(String requestInfo) {
		this.requestInfo = requestInfo;
	}

	@Override
	public String getPrimaryKeyName() {
		return "uid";
	}

	@Override
	public String getPrimaryKeyValue() {
		return uid+"";
	}

	public String getSettingsSwitch() {
		return settingsSwitch;
	}

	public void setSettingsSwitch(String settingsSwitch) {
		this.settingsSwitch = settingsSwitch;
	}
	
	public UserDetailInfo toUserDetailInfo(){
		
		UserDetailInfo info = new UserDetailInfo();
		info.setIs_friend(this.getIsFriend());
		
		if(!"".equalsIgnoreCase(this.newsImage)){
			List<String> newImages = JSON.parseArray(this.newsImage, String.class);
			info.setNews_Image(newImages);
		}
		
		if(!"".equalsIgnoreCase(this.meetInfo)){
			ArrayList<MeetInfo> meets = new ArrayList<MeetInfo>();
			List<String> mi = JSON.parseArray(this.meetInfo, String.class);
			for(String str : mi){
				meets.add(JSON.parseObject(str, MeetInfo.class));
			}
			info.setMeet_info(meets);
		}

		if(!"".equalsIgnoreCase(this.requestInfo)){
			ArrayList<RequestInfo> meets = new ArrayList<RequestInfo>();
			List<String> mi = JSON.parseArray(this.requestInfo, String.class);
			for(String str : mi){
				meets.add(JSON.parseObject(str, RequestInfo.class));
			}
			info.setRequest_info(meets);
		}
		
		if(!"".equalsIgnoreCase(this.settingsSwitch)){
			SettingSwitch settingSwitch = JSONObject.parseObject(this.settingsSwitch, SettingSwitch.class);
			info.setSettings_switch(settingSwitch);
		}
		
		if(!"".equalsIgnoreCase(this.userInfo)){
			UserInfo userInfo = JSONObject.parseObject(this.userInfo, UserInfo.class);
			info.setUser_info(userInfo);
		}
		
		return info;
	}
	
	@Override
	public String getTableName() {
		return DBConstant.UserDB.User_Detail_Info;
	}

	public static void onCreateDB(SQLiteDatabase db){
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS " + 
				DBConstant.UserDB.User_Detail_Info + 
				" (_id integer primary key autoincrement, ");
		builder.append("uid Integer not null default '',");
		builder.append("isFriend Integer not null default 0,");
		builder.append("userInfo text not null default '',");
		builder.append("newsImage text not null default '',");
		builder.append("meetInfo text not null default '',");
		builder.append("settingsSwitch text not null default '',");
		builder.append("requestInfo text not null default ''");
		builder.append(");");
		db.execSQL(builder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	}
	
}
