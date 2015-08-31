package com.imrub.shoulder.module.model.detail;

import com.alibaba.fastjson.JSONObject;

public class RequestInfo {

	public static final int TypeRequest = 2;
	public static final int TypeReply 	= 1;
	
	// 回复内容
	private String content;
	// 回复时间
	private int request_time; 
	private int type;
	
	public RequestInfo(){
	}
	
	public RequestInfo(int type, String content){
		this.type = type;
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getRequest_time() {
		return request_time;
	}
	
	public void setRequest_time(int request_time) {
		this.request_time = request_time;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String toJsonString(){
		JSONObject obj = new JSONObject();
		obj.put("content", content);
		obj.put("request_time", request_time);
		obj.put("type", type);
		return obj.toString();
	}
	
}
