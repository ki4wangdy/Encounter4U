package com.imrub.shoulder.module.model.detail;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.db.table.cajian.UserDetailInfoData;
import com.imrub.shoulder.module.model.UserInfo;

public class UserDetailInfo {

	private int is_friend;
	
	private UserInfo user_info;
	private List<String> news_image;
	private List<MeetInfo> meet_info;
	private List<RequestInfo> request_info;
	private SettingSwitch settings_switch;
	
	public int isIs_friend() {
		return is_friend;
	}

	public void setIs_friend(int is_friend) {
		this.is_friend = is_friend;
	}

	public UserInfo getUser_info() {
		return user_info;
	}

	public void setUser_info(UserInfo user_info) {
		this.user_info = user_info;
	}

	public List<String> getNews_Image() {
		return news_image;
	}

	public void setNews_Image(List<String> news_image) {
		this.news_image = news_image;
	}

	public List<MeetInfo> getMeet_info() {
		return meet_info;
	}

	public void setMeet_info(List<MeetInfo> meet_info) {
		this.meet_info = meet_info;
	}

	public List<RequestInfo> getRequest_info() {
		return request_info;
	}

	public void setRequest_info(List<RequestInfo> request_info) {
		this.request_info = request_info;
	}

	public SettingSwitch getSettings_switch() {
		return settings_switch;
	}

	public void setSettings_switch(SettingSwitch settings_switch) {
		this.settings_switch = settings_switch;
	}
	
	public UserDetailInfoData toUserDetailInfoData(){
		UserDetailInfoData data = new UserDetailInfoData();
		
		data.setIsFriend(this.is_friend);
		
		if(user_info != null){
			data.setUid(user_info.getUid());
			data.setUserInfo(user_info.toJsonString());
		}
		
		if(news_image != null && !news_image.isEmpty()){
			data.setNewsImage(toJsonArray(news_image));
		} else {
			data.setNewsImage("");
		}
		
		if(meet_info != null && !meet_info.isEmpty()){
			data.setMeetInfo(toJsonArrayFromMeetInfos(meet_info));
		} else {
			data.setMeetInfo("");
		}
		
		if(request_info != null && !request_info.isEmpty()){
			data.setRequestInfo(toJsonArrayFromRequestInfos(request_info));
		} else {
			data.setRequestInfo("");
		}
		
		if(settings_switch != null && settings_switch != null){
			data.setSettingsSwitch(toJsonObject(settings_switch));
		} else {
			data.setSettingsSwitch("");
		}
		
		return data;
	}

	private static <T> String toJsonArray(List<T> strings){
		JSONArray array = new JSONArray();
		for(T str : strings){
			array.add(str);
		}
		return array.toJSONString();
	}
	
	private static String toJsonObject(SettingSwitch switchs){
		JSONObject obj = new JSONObject();
		obj.put("switch1", switchs.getSwitch1());
		obj.put("switch2", switchs.getSwitch2());
		obj.put("switch3", switchs.getSwitch3());
		obj.put("switch4", switchs.getSwitch4());
		obj.put("switch5", switchs.getSwitch5());
		return obj.toJSONString();
	}
	
	private static String toJsonArrayFromMeetInfos(List<MeetInfo> meetInfos){
		JSONArray array = new JSONArray();
		for(MeetInfo info : meetInfos){
			array.add(info.toJsonString());
		}
		return array.toString();
	}
	
	public static String toJsonArrayFromRequestInfos(List<RequestInfo> requestInfos){
		JSONArray array = new JSONArray();
		for(RequestInfo info : requestInfos){
			array.add(info.toJsonString());
		}
		return array.toString();
	}
	
	
}
