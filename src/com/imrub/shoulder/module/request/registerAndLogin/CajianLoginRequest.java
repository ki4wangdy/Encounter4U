package com.imrub.shoulder.module.request.registerAndLogin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.DBModule;
import com.imrub.shoulder.base.thread.IAction;
import com.imrub.shoulder.base.util.TimeUtil;
import com.imrub.shoulder.module.im.client.IMClient;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.RequestBase;
import com.imrub.shoulder.module.request.RequestUtils;

public class CajianLoginRequest {

	private CajianLoginRequest(){
	}

	public static void cajianLoginRequest(final IRequestResult<LoginUserInfo> result, int loginType, String userName, String password){
		
		final String postData = RegisterRequestFactory.cajianLoginRequest(loginType, userName, password);
		
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlUserLogin, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				String msg = AppContext.getString(R.string.error_json_parse);
				try {
					JSONObject obj = (JSONObject)JSON.parse(arg);
					code = obj.getIntValue(RequestBase.Code);
					if(code == RequestBase.CodeOk){
						String token = obj.getString(RequestBase.Token);
						String serverIp = obj.getString(RequestBase.ServerIp);
						JSONObject body = obj.getJSONObject(RequestBase.Info);
						LoginUserInfo userinfo = JSONObject.parseObject(body.toJSONString(), LoginUserInfo.class);
						userinfo.setToken(token);
						userinfo.setServerIp(serverIp);
						
						// 1. set the user account basic info
						UserInfo.getInstance().putUid(userinfo.getUid());
						UserInfo.getInstance().putToken(userinfo.getToken());
						UserInfo.getInstance().setImServer(userinfo.getServerIp());
						UserInfo.getInstance().initUserInfo(userinfo);

						// 2. initialize the DB Module
						DBModule.reset();
						
						// 3. IM module login
						IMClient.getInstance().login();
						TimeUtil.sleep(1000);
						
						doSuccessResultOnUIThread(result, userinfo);
						return ;
					}
				} catch (Exception e) {
				}
				doErrorResultOnUIThread(result, code, msg);
			}
		});
		
	}
	
	private static void doSuccessResultOnUIThread(final IRequestResult<LoginUserInfo> result, final LoginUserInfo user){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onSuccess(user);
			}
		});
	}
	
	private static void doErrorResultOnUIThread(final IRequestResult<LoginUserInfo> result, final int code, final String msg){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onError(code, msg);
			}
		});
	}	
	
}
