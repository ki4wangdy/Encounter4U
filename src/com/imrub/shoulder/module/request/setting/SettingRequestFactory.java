package com.imrub.shoulder.module.request.setting;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.module.request.HttpUrlConstant;

public class SettingRequestFactory {

	public static final String PV			=	"pv";
	public static final String UID			=	"uid";
	public static final String Token		=	"token";
	public static final String Passwd		=	"passwd";
	public static final String Info			=	"info";
	public static final String Type			=	"type";
	
	public static String checkPasswdRequest(String uid, String token, String password){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, uid);
			body.put(Token, token);
			body.put(Passwd, password);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String modifyUserPasswd(String uid, String token, String password){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, uid);
			body.put(Token, token);
			body.put(Passwd, password);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static String userLogout(String uid, String token){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, uid);
			body.put(Token, token);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static String userDetail(String uid, String token){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, uid);
			body.put(Token, token);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static String userInfoSetting(String uid, String token, JSONArray obj){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, uid);
			body.put(Token, token);
			body.put(Info, obj);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String userBasicInfo(String uid, String token, JSONArray obj){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, uid);
			body.put(Token, token);
			body.put(Info, obj);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static String userRelativeInfo(String uid, String token, int type){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, uid);
			body.put(Token, token);
			body.put(Type, type);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static String getUserBasicInfo(String uid, String token){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, uid);
			body.put(Token, token);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
}
