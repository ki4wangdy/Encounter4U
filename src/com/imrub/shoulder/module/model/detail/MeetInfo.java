package com.imrub.shoulder.module.model.detail;

import com.alibaba.fastjson.JSONObject;

public class MeetInfo {

	private int meet_id; // ≤¡ºÁid
	private int meet_time; // ≤¡ºÁ ±º‰
	
	public int getMeet_id() {
		return meet_id;
	}
	
	public void setMeet_id(int meet_id) {
		this.meet_id = meet_id;
	}
	
	public int getMeet_time() {
		return meet_time;
	}
	
	public void setMeet_time(int meet_time) {
		this.meet_time = meet_time;
	}
	
	public String toJsonString(){
		JSONObject obj = new JSONObject();
		obj.put("meet_id", meet_id);
		obj.put("meet_time", meet_time);
		return obj.toString();
	}
	
}
