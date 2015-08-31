package com.imrub.shoulder.module.request.setting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.IAction;
import com.imrub.shoulder.module.model.CJUserDetail;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.RequestBase;
import com.imrub.shoulder.module.request.RequestUtils;

public class UserDetailRequest {

	public static void userDetail(final IRequestResult<CJUserDetail> result, String uid, String token){
		
		final String postData = SettingRequestFactory.userDetail(uid, token);
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlUserDetail, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				String msg = AppContext.getString(R.string.error_json_parse);
				try {
					JSONObject obj = (JSONObject)JSON.parse(arg);
					code = obj.getInteger(RequestBase.Code);
					if (code == RequestBase.CodeOk) {
						CJUserDetail user = JSON.parseObject(obj.toJSONString(), CJUserDetail.class);
						doSuccessResultOnUIThread(result, user);
						return ;
					}
				} catch (Exception e) {
				}
				doErrorResultOnUIThread(result, code, msg);
			}
		});
	}
	
	public static void doSuccessResultOnUIThread(final IRequestResult<CJUserDetail> result, final CJUserDetail user){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onSuccess(user);
			}
		});
	}
	
	public static void doErrorResultOnUIThread(final IRequestResult<CJUserDetail> result, final int code, final String msg){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onError(code, msg);
			}
		});
	}
	
}
