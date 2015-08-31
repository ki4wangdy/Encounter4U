package com.imrub.shoulder.module.request.setting;

import com.alibaba.fastjson.JSONArray;
import com.imrub.shoulder.module.model.CJUserDetail;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.setting.UserGetBasicInfoRequest.UserRelativeInfo;

public class SettingRequestFacade {

	public static final void checkPasswdRequest(final IRequestResult<String> result, String uid, String token, String pw){
		CheckPasswdRequest.checkPasswd(result, uid, token, pw);
	}
	
	public static final void modifyUserPasswd(final IRequestResult<String> result, String uid, String token, String pw){
		ModifyUserPasswdRequest.modifyUserPasswd(result, uid, token, pw);
	}

	public static final void userLogout(final IRequestResult<String> result, String uid, String token){
		UserLogoutRequest.logoutRequest(result, uid, token);
	}

	public static final void userDetail(final IRequestResult<CJUserDetail> result, String uid, String token){
		UserDetailRequest.userDetail(result, uid, token);
	}
	
	public static final void setUserInfo(final IRequestResult<String> result, String uid, String token, JSONArray obj){
		UserInfoSettingRequest.userInfoSetting(result, uid, token, obj);
	}
	
	public static final void setUserBoolInfo(final IRequestResult<String> result, String uid, String token, JSONArray obj){
		UserBasicInfoRequest.userBasicInfo(result, uid, token, obj);
	}
	
	public static final void getUserRelativeInfoRequest(final IRequestResult<String> result, String uid, String token, int type){
		UserRelativeInfoRequest.getUserRelativeInfoRequest(result, uid, token, type);
	}
	
	public static final void getUserBoolInfo(final IRequestResult<UserRelativeInfo> result, String uid, String token){
		UserGetBasicInfoRequest.getUserBasicInfo(result, uid, token);
	}
	
}
