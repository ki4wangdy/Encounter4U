package com.imrub.shoulder.module.request.registerAndLogin;


public class LoginUserInfo {
	
    private String header_logo;
	private int sex;
	private String hometown;
	private String classes;
	private String school;
	private String favour;
	private String uid;
	private String nick_name;

	private String birth;
	private String constellation;
	private String description;
	
	private String serverIp;
	private String token;
	
	public LoginUserInfo(){
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

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getFavour() {
		return favour;
	}

	public void setFavour(String favour) {
		this.favour = favour;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
