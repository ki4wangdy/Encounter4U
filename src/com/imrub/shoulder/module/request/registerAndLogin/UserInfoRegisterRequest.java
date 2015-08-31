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

public class UserInfoRegisterRequest {

	public static void registerUserInfo(final IRequestResult<RegisterUserInfo> result, String nickName, String mail, 
			String phone, String password, String logoUrl, String userSchool, int userSubject, int userGender){
		
		final String postData = RegisterRequestFactory.registerUserInfo(nickName,mail,phone,password,logoUrl,userSchool,userSubject,userGender);
		
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlRegister, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				String msg = AppContext.getString(R.string.error_json_parse);
				try {
					JSONObject obj = new JSONObject(arg);
					code = obj.getInt(RequestBase.Code);
					if(code == RequestBase.CodeOk){
						String token = obj.getString(RequestBase.Token);
						String serverIp = obj.getString(RequestBase.ServerIp);
						String uid = obj.getString(RequestBase.UID);
						RegisterUserInfo info = new RegisterUserInfo(uid, token, serverIp);
						doSuccessResultOnUIThread(result, info);
						return ;
					}
				} catch (JSONException e) {
				}
				doErrorResultOnUIThread(result, code, msg);
			}
		});
		
	}
	
	private static void doSuccessResultOnUIThread(final IRequestResult<RegisterUserInfo> result, final RegisterUserInfo str){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onSuccess(str);
			}
		});
	}
	
	private static void doErrorResultOnUIThread(final IRequestResult<RegisterUserInfo> result, final int code, final String msg){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onError(code, msg);
			}
		});
	}
	
	public static class RegisterUserInfo{
		public String uid;
		public String token;
		public String serverIp;
		public RegisterUserInfo(String uid ,String token, String serverIp){
			this.uid = uid;
			this.token = token;
			this.serverIp = serverIp;
		}
	}
	
}
