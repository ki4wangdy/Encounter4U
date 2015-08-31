package com.imrub.shoulder.module.request;

import com.imrub.shoulder.base.app.store.AppNetAddress;

public interface HttpUrlConstant {
	
	public static final String ProtocalVersion = "0.1";
	
	public static final String HttpUrlDebuge 	= "http://192.168.20.34";
	public static final String HttpUrlRelease 	= AppNetAddress.getInstance().getHttpBaseNetAddress(); 
	public static final String HttpUrlLogo		= AppNetAddress.getInstance().getHttpLogoNetAddress();

	public static final String HttpUrl			= HttpUrlRelease;	
	
	/* 用户注册登录模块*/
	public static final String HttpUrlCheckUser = HttpUrl+"/cajian/1/user/check_user_exist";
	public static final String HttpUrlSendVerification = HttpUrl+"/cajian/1/user/send_verification_code";
	
	public static final String HttpUrlCheckVerificationCode = HttpUrl+"/cajian/1/user/check_verification_code";
	public static final String HttpUrlRegister	=	HttpUrl+"/cajian/1/user/user_register";
	
	public static final String HttpUrlUserLogin		=	HttpUrl+"/cajian/1/user/user_login";
	public static final String HttpUrlResetPasswd	=	HttpUrl+"/cajian/1/user/reset_passwd";
	
	
	/* 用户设置模块*/
	public static final String HttpUrlCheckPasswd	=	HttpUrl + "/cajian/2/user/check_passwd";
	public static final String HttpUrlModifyPasswd	=	HttpUrl + "/cajian/2/user/modify_passwd";
	public static final String HttpUrlUserLogout	=	HttpUrl + "/cajian/2/user/user_logout";

	public static final String HttpUrlUserDetail	= 	HttpUrl + "/cajian/2/sns/my_details";
	public static final String HttpUrlSetUserInfo	=	HttpUrl + "/cajian/2/settings/set_userinfo";
	
	public static final String HttpUrlUserBasicInfo	=	HttpUrl + "/cajian/2/settings/update_user_settings";
	public static final String HttpUrlUserRelatInfo	=	HttpUrl + "/cajian/2/settings/get_relation";
	
	public static final String HttpUrlUserBasicInfoGet	=	HttpUrl + "/cajian/2/settings/get_user_settings";
	
	public static final String HttpUrlUserFriend	=	HttpUrl + "/cajian/2/sns/friends";
	
	/* 用户详情 */
	public static final String HttpUrlUserDetails	=	HttpUrl + "/cajian/2/sns/show_user_info";

	/* 用户求认识，同意*/
	public static final String HttpUrlQiuKnow		=	HttpUrl + "/cajian/2/request/create";
	public static final String HttpUrlReplyKnow		=	HttpUrl + "/cajian/2/request/reply";
	public static final String HttpUrlAgreeKnow		=	HttpUrl + "/cajian/2/request/agree";
	
	public static final String HttpMacUrl 			= 	HttpUrl + "/cajian/2/meet/push_mac";

	// 头像上传的地址
	public static final String HttpUploadLogo		=	HttpUrlLogo+ "/pictureServer/comm/uploadPicture";
	
}
