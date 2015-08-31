package com.imrub.shoulder.module.request.registerAndLogin;

import org.json.JSONException;
import org.json.JSONObject;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.IAction;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.RequestBase;
import com.imrub.shoulder.module.request.RequestUtils;

public class VerificationCodeRequest {

	public static void sendVerificationCodeRequest(final IRequestResult<String> result, String registerContent, int registerCode){
		
		final String postData = RegisterRequestFactory.checkVerificationCodeRequest(registerContent, registerCode);
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlCheckVerificationCode, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				String msg = AppContext.getString(R.string.error_json_parse);
				try {
					JSONObject obj = new JSONObject(arg);
					code = obj.getInt(RequestBase.Code);
					if(code == RequestBase.CodeOk){
//						int results = obj.getInt(RegisterRequestFactory.Result);
						RequestUtils.doSuccessResultOnUIThread(result, ""+0);
						return ;
					}
				} catch (JSONException e) {
				}
				RequestUtils.doErrorResultOnUIThread(result, code, msg);
			}
		});
		
	}
}
