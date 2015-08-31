package com.imrub.shoulder.module.request.registerAndLogin;

import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.registerAndLogin.UserInfoRegisterRequest.RegisterUserInfo;

public class RegisterRequestFacade {

	public static void mailOrPhoneCheckRequest(IRequestResult<String> result, int registerType, String registerContent, boolean isSend){
		PhoneOrMailCheckRequest.phoneOrMailCheckRequest(result, registerType, registerContent, isSend);
	}
	
	public static void mailOrPhoneSendRequest(IRequestResult<String> result, int registerType, String registerContent, boolean isSend){
		PhoneOrMailSendRequest.phoneOrMailSendRequest(result, registerType, registerContent, isSend);
	}
	
	public static void checkVerificationCodeRequest(IRequestResult<String> result, String registerContent, int registerCode){
		VerificationCodeRequest.sendVerificationCodeRequest(result, registerContent, registerCode);
	}
	
	public static void registerUserInfo(final IRequestResult<RegisterUserInfo> result, String nickName, String mail, 
			String phone, String password, String logoUrl, String userSchool, int userSubject, int userGender){
		UserInfoRegisterRequest.registerUserInfo(result, nickName, mail, phone, password, logoUrl, userSchool, userSubject, userGender);
	}
	
	public static void resetForgetPwRequest(final IRequestResult<String> result, int forgetType, String forgetContent, String pw){
		ForgetResetPwRequest.forgetPwRequest(result, forgetType, forgetContent, pw);
	}
	
}
