package com.imrub.shoulder.module.request.setting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.IAction;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.RequestBase;
import com.imrub.shoulder.module.request.RequestUtils;

public class UserInfoSettingRequest {

	// 用户头像
	public static final int Type_Logo			=	1;
	// 用户昵称
	public static final int Type_nick			=	2;
	// 个人介绍
	public static final int Type_description	=	3;
	// 生日
	public static final int Type_birth			=	4;
	// 星座
	public static final int Type_Xingzuo		=	5;
	// 学校
	public static final int Type_School			=	6;
	// 班级
	public static final int Type_Class			=	7;
	// 家乡
	public static final int Type_Hometown		=	8;
	// 爱好
	public static final int Type_habbit			=	9;

	public static void userInfoSetting(final IRequestResult<String> result, String uid, String token, JSONArray obj){
		
		final String postData = SettingRequestFactory.userInfoSetting(uid, token, obj);
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlSetUserInfo, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				String msg = AppContext.getString(R.string.error_json_parse);
				try {
					JSONObject obj = (JSONObject)JSON.parse(arg);
					code = obj.getInteger(RequestBase.Code);
					if (code == RequestBase.CodeOk) {
						RequestUtils.doSuccessResultOnUIThread(result, code+"");
						return ;
					}
				} catch (Exception e) {
				}
				RequestUtils.doErrorResultOnUIThread(result, code, msg);
			}
		});
	}
	
	
}
