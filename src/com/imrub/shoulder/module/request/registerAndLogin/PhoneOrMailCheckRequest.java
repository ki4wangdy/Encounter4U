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

public class PhoneOrMailCheckRequest {
	
	public static final int PhoneType = 1;
	public static final int MailType  = 2;
	
	public static void phoneOrMailCheckRequest(final IRequestResult<String> result, int registerType, String registerContent, boolean isSend){
		
		final String postData = RegisterRequestFactory.checkMailOrPhoneRquest(registerType, registerContent);
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlCheckUser, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				String msg = AppContext.getString(R.string.error_json_parse);
				try {
					JSONObject obj = new JSONObject(arg);
					code = obj.getInt(RequestBase.Code);
					if(code == RequestBase.CodeOk){
						int existFlag = obj.getInt(RegisterRequestFactory.ExistFlag);
						RequestUtils.doSuccessResultOnUIThread(result, ""+existFlag);
						return ;
					}
				} catch (JSONException e) {
				}
				RequestUtils.doErrorResultOnUIThread(result, code, msg);
			}
		});
	}
	
}
