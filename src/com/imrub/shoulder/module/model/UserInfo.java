package com.imrub.shoulder.module.model;

import com.alibaba.fastjson.JSONObject;

public class UserInfo{

	private int uid;
	private String nick_name;
	private String header_logo;
	
	private String description;
	private String constellation;
	
	private String birth;
	private String school;
	private String classes;
	
	private String hometown;
	private String favour;
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public String getNick_name() {
		return nick_name;
	}
	
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	
	public String getHeader_logo() {
		return header_logo;
	}
	
	public void setHeader_logo(String header_logo) {
		this.header_logo = header_logo;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getConstellation() {
		return constellation;
	}
	
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	
	public String getBirth() {
		return birth;
	}
	
	public void setBirth(String birth) {
		this.birth = birth;
	}
	
	public String getSchool() {
		return school;
	}
	
	public void setSchool(String school) {
		this.school = school;
	}
	
	public String getClasses() {
		return classes;
	}
	
	public void setClasses(String classes) {
		this.classes = classes;
	}
	
	public String getHometown() {
		return hometown;
	}
	
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	
	public String getFavour() {
		return favour;
	}
	
	public void setFavour(String favour) {
		this.favour = favour;
	}
	
	public String toJsonString(){
		JSONObject obj = new JSONObject();
		obj.put("uid", uid);
		obj.put("nick_name", nick_name);
		obj.put("header_logo", header_logo);
		obj.put("description", description);
		obj.put("constellation", constellation);
		obj.put("birth", birth);
		obj.put("school", school);
		obj.put("classes", classes);
		obj.put("hometown", hometown);
		obj.put("favour", favour);
		return obj.toJSONString();
	}

}
