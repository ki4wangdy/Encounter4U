package com.imrub.shoulder.module.request;

import org.json.JSONObject;

import com.imrub.shoulder.base.app.store.UserInfo;

public class RequestBase {

	public static final int CodeOk			= 0;
	
	public static final String Code			= "code";
	public static final String Token		= "token";
	public static final String ServerIp		= "serverIp";
	public static final String Info			= "info";
	
	public static final String Header 		= "header";
	public static final String JsonType 	= "json_type";
	public static final String JsonVersion	= "json_version";
	
	public static final String Error_Code	= "error_code";
	public static final String Error_Msg	= "error_string";
	
	public static final String BODY			= "body";
	
	public static final String UID			= "uid";
	public static final String TOKEN		= "token";
	public static final String IMINFO		= "iminfo";
	
	public static final String NickName		= "nickName";
	public static final String UserName		= "userName";
	public static final String Sex			= "sex";
	
	public static final int JSON_OK			= 0;
	
	public static JSONObject toHeaderJson(int jsonType) throws Exception{
		JSONObject obj = new JSONObject();
		JSONObject header = new JSONObject();
		header.put(JsonVersion, "1");
		header.put(JsonType, jsonType);
		header.put(UID, UserInfo.getInstance().getUid());
		header.put(TOKEN, UserInfo.getInstance().getToken());
		obj.put(Header, header);
		return obj;
	}
	
	public static String toBodyJson(int jsonType, JSONObject body) throws Exception{
		JSONObject obj = toHeaderJson(jsonType);
		obj.put(BODY, body);
		return obj.toString();
	}
	
}
