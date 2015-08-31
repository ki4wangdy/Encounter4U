package com.imrub.shoulder.module.request.registerAndLogin;

import org.json.JSONObject;

import com.imrub.shoulder.module.request.HttpUrlConstant;

public class RegisterRequestFactory {

	public static final String PV			=	"pv";
	
	public static final String LoginId		=	"loginId";
	public static final String ExistFlag	=	"existFlag";
	
	public static final String LoginIdType 	= 	"loginIdType";
	public static final String VerifyCode	=	"verifyCode";
	
	public static final String Result		=	"result";

	public static final String NickName		=	"nickName";
	public static final String Passwd		=	"passwd";
	public static final String HeaderLogo	=	"headerLogo";
	public static final String Sex			=	"sex";
	public static final String School		=	"school";
	public static final String Discipline	=	"discipline";
	
	public static final String ThirdPartyFlag = "thirdPartyFlag";
	
	public static String resetPwRequest(int loginType, String userName, String password){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(LoginId, userName);
			body.put(Passwd, password);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String cajianLoginRequest(int loginType, String userName, String password){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(LoginId, userName);
			body.put(Passwd, password);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String checkMailOrPhoneRquest(int registerType, String registerContent){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(LoginId, registerContent);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String sendMailOrPhoneRquest(int registerType, String registerContent){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(LoginId, registerContent);
			body.put(LoginIdType, registerType);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String checkVerificationCodeRequest(String registerContent, int registerCode){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(LoginId, registerContent);
			body.put(VerifyCode, registerCode);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String registerUserInfo(String nickName, String mail, 
			String phone, String password, String logoUrl, String userSchool, 
			int userSubject, int userGender){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			
			if("".equalsIgnoreCase(mail)){
				body.put(LoginId, phone);
			} else {
				body.put(LoginId, mail);
			}
			
			body.put(NickName, nickName);
			body.put(Passwd, password);
			body.put(HeaderLogo, logoUrl);
			
			body.put(Sex, userGender);
			body.put(School, userSchool);
			body.put(Discipline, userSubject);
			body.put(ThirdPartyFlag, 1);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
}
