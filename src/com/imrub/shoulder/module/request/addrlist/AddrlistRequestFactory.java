package com.imrub.shoulder.module.request.addrlist;

import org.json.JSONObject;

import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.module.request.HttpUrlConstant;

public class AddrlistRequestFactory {

	public static final String PV				=	"pv";
	public static final String UID				=	"uid";
	public static final String Token			=	"token";		
	
	public static final String ToUid			=	"to_uid";
	public static final String FromUid			=	"from_uid";
	public static final String ReplyComment		=	"reply_comment";
	public static final String RequestComment	=	"request_comtent";
	
	public static String createFriendRequest(int toUid){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, UserInfo.getInstance().getUid());
			body.put(Token, UserInfo.getInstance().getToken());
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String createKnowRequest(int toUid, String comment){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, UserInfo.getInstance().getUid());
			body.put(Token, UserInfo.getInstance().getToken());
			body.put(ToUid, toUid);
			body.put(RequestComment, comment);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String replyCreateKnowRequest(int fromUid, String comment){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, UserInfo.getInstance().getUid());
			body.put(Token, UserInfo.getInstance().getToken());
			body.put(FromUid, fromUid);
			body.put(ReplyComment, comment);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static String agreeCreateKnowRequest(int fromUid){
		JSONObject body = new JSONObject();
		try {
			body.put(PV, HttpUrlConstant.ProtocalVersion);
			body.put(UID, UserInfo.getInstance().getUid());
			body.put(Token, UserInfo.getInstance().getToken());
			body.put(FromUid, fromUid);
			return body.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
}
