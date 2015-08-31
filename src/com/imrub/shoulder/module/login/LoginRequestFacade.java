package com.imrub.shoulder.module.login;

import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.registerAndLogin.CajianLoginRequest;
import com.imrub.shoulder.module.request.registerAndLogin.LoginUserInfo;

public class LoginRequestFacade {

	public static void cajianLoginRequest(final IRequestResult<LoginUserInfo> result, int loginType, String userName, String password){
		CajianLoginRequest.cajianLoginRequest(result, loginType, userName, password);
	}
	
}
