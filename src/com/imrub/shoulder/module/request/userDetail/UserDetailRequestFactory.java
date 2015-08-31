package com.imrub.shoulder.module.request.userDetail;

import org.json.JSONObject;

import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.module.request.HttpUrlConstant;

public class UserDetailRequestFactory {

	public static final String PV				=	"pv";
	public static final String UID				=	"uid";
	public static final String Token			=	"token";		
	public static final String UserDetailUid	=	"detail_uid";
	
	public static String userDetailRequest(int uid){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, UserInfo.getInstance().getUid());
			body.put(Token, UserInfo.getInstance().getToken());
			body.put(UserDetailUid, uid);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
}
