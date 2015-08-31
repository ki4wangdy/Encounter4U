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

public class UserBasicInfoRequest {

	public static void userBasicInfo(final IRequestResult<String> result, String uid, String token, JSONArray obj){
		
		final String postData = SettingRequestFactory.userBasicInfo(uid, token, obj);
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlUserBasicInfo, postData, new IAction<String>() {
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
