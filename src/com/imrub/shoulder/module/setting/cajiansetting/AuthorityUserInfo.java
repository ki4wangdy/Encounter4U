package com.imrub.shoulder.module.setting.cajiansetting;

public class AuthorityUserInfo {

	private String uid;
	private String nick_name;
	private String user_name;
	private int sex;
	private String header_logo;
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getHeader_logo() {
		return header_logo;
	}

	public void setHeader_logo(String header_logo) {
		this.header_logo = header_logo;
	}
	
	public String getNick_name() {
		return nick_name;
	}
	
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	
	public String getUser_name() {
		return user_name;
	}
	
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public int getSex() {
		return sex;
	}
	
	public void setSex(int sex) {
		this.sex = sex;
	}
	
}
