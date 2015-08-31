package com.imrub.shoulder.module.request.userDetail;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.IAction;
import com.imrub.shoulder.module.model.UserInfo;
import com.imrub.shoulder.module.model.detail.MeetInfo;
import com.imrub.shoulder.module.model.detail.RequestInfo;
import com.imrub.shoulder.module.model.detail.SettingSwitch;
import com.imrub.shoulder.module.model.detail.UserDetailInfo;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.RequestBase;
import com.imrub.shoulder.module.request.RequestUtils;

public class UserDetailRequest {

	public static void userDetailRequest(final IRequestResult<UserDetailInfo> result, int uid){
		
		final String postData = UserDetailRequestFactory.userDetailRequest(uid);
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlUserDetails, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				String msg = AppContext.getString(R.string.error_json_parse);
				try {
					JSONObject obj = (JSONObject)JSON.parse(arg);
					code = obj.getIntValue(RequestBase.Code);
					if(code == RequestBase.CodeOk){
						UserDetailInfo userDetailInfo = new UserDetailInfo();
						
						userDetailInfo.setIs_friend(obj.getIntValue("is_friend"));
						userDetailInfo.setUser_info(obj.getObject("user_info", UserInfo.class));
						
						JSONArray array = obj.getJSONArray("news_image");
						if(array != null){
							List<String> newImages = JSON.parseArray(array.toJSONString(), String.class);
							userDetailInfo.setNews_Image(newImages);
						}
						
						JSONArray meetInfos = obj.getJSONArray("meet_info");
						if(meetInfos != null){
							List<MeetInfo> MeetInfo = JSON.parseArray(meetInfos.toJSONString(), MeetInfo.class);
							userDetailInfo.setMeet_info(MeetInfo);
						}

						JSONArray requestInfos = obj.getJSONArray("request_info");
						if(requestInfos != null){
							List<RequestInfo> requestInfo = JSON.parseArray(requestInfos.toJSONString(), RequestInfo.class);
							userDetailInfo.setRequest_info(requestInfo);
						}
						
						JSONObject settingSwitchs = obj.getJSONObject("settings_switch");
						SettingSwitch settingSwtich = JSONObject.parseObject(settingSwitchs.toJSONString(), SettingSwitch.class);
						if(settingSwtich != null){
							userDetailInfo.setSettings_switch(settingSwtich);
						}
						
						doSuccessResultOnUIThread(result, userDetailInfo);
						return ;
					}
				} catch (Exception e) {
				}
				doErrorResultOnUIThread(result, code, msg);
			}
		});
		
	}
	
	private static void doSuccessResultOnUIThread(final IRequestResult<UserDetailInfo> result, final UserDetailInfo user){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onSuccess(user);
			}
		});
	}
	
	private static void doErrorResultOnUIThread(final IRequestResult<UserDetailInfo> result, final int code, final String msg){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onError(code, msg);
			}
		});
	}	
	
}
