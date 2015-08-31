package com.imrub.shoulder.module.request.setting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.IAction;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.RequestBase;
import com.imrub.shoulder.module.request.RequestUtils;

public class UserGetBasicInfoRequest {

	public static void getUserBasicInfo(final IRequestResult<UserRelativeInfo> result, String uid, String token){
		
		final String postData = SettingRequestFactory.getUserBasicInfo(uid, token);
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlUserBasicInfoGet, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				String msg = AppContext.getString(R.string.error_json_parse);
				try {
					JSONObject obj = (JSONObject)JSON.parse(arg);
					code = obj.getInteger(RequestBase.Code);
					if (code == RequestBase.CodeOk) {
						UserRelativeInfo info = new UserRelativeInfo();
						info.matchType = obj.getInteger("matchType");
						info.friendsSwitch = obj.getInteger("friendsSwitch");
						info.meetSwitch = obj.getInteger("meetSwitch");
						doSuccessResultOnUIThread(result, info);
						return ;
					}
				} catch (Exception e) {
				}
				doErrorResultOnUIThread(result, code, msg);
			}
		});
	}
	
	private static void doSuccessResultOnUIThread(final IRequestResult<UserRelativeInfo> result, final UserRelativeInfo user){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onSuccess(user);
			}
		});
	}
	
	private static void doErrorResultOnUIThread(final IRequestResult<UserRelativeInfo> result, final int code, final String msg){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onError(code, msg);
			}
		});
	}
	
	public static class UserRelativeInfo{
		public int matchType;
		public int friendsSwitch;
		public int meetSwitch;
	}
	
}
