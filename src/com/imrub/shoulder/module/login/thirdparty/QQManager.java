package com.imrub.shoulder.module.login.thirdparty;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.imrub.shoulder.base.app.AppContext;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQManager {

	private QQUserInfo mUserInfo;
	
	private Tencent mTencent;
	public static QQAuth mQQAuth;

	private static QQManager mInstance;
	private QQManager() {
	}

	public static QQManager getInstance() {
		if (mInstance == null) {
			mInstance = new QQManager();
		}
		return mInstance;
	}

	@SuppressWarnings("deprecation")
	public void handleLoginData(Intent intent) {
		mTencent.handleLoginData(intent, new BaseUiListener());
	}

	public void login(Context context) {

		// 第一个参数就是上面所说的申请的APPID，第二个是全局的Context上下文，这句话实现了调用QQ登录
		mTencent = Tencent.createInstance(ThirdpartyConstant.QQ_APP_ID,
				AppContext.getAppContext());

		/**
		 * 通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO
		 * 是一个String类型的字符串，表示一些权限 官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE =
		 * “get_user_info,add_t”；所有权限用“all”
		 * 第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类
		 */
		if (!mTencent.isSessionValid()) {
			mTencent.login((Activity) context, "all", new BaseUiListener());
		}
	}

	private void onQQComplete(Object obj) {
		// 获得的数据是JSON格式的，获得你想获得的内容
		// 如果你不知道你能获得什么，看一下下面的LOG
		QQToken token = mTencent.getQQToken();

		mUserInfo = new QQUserInfo();
		mUserInfo.expireTime = mTencent.getExpiresIn();
		mUserInfo.accessToken = token.getAccessToken();
		mUserInfo.openId = token.getOpenId();
		
		UserInfo userInfo = new UserInfo(AppContext.getAppContext(), mTencent.getQQToken());
		userInfo.getUserInfo(new BaseApiListener());
	}

	/**
	 * {
	    "is_yellow_year_vip": "0",
	    "ret": 0,
	    "figureurl_qq_1": "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/40",
	    "figureurl_qq_2": "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
	    "nickname": "小罗",
	    "yellow_vip_level": "0",
	    "msg": "",
	    "figureurl_1": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/50",
	    "vip": "0",
	    "level": "0",
	    "figureurl_2": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
	    "is_yellow_vip": "0",
	    "gender": "男",
	    "figureurl": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/30"
		}
	 * @param obj
	 */
	private void onQQInfoComplete(Object obj){
		JSONObject json = (JSONObject)obj;
		
		try{
			if(mUserInfo != null){
				mUserInfo.nickName = json.getString("nickname");
				mUserInfo.gender = json.getString("gender");
				mUserInfo.figureUrl = json.getString("figureurl_qq_2");
			}
		}catch(Exception e){
		}
		
	}
	
	/**
	 * 当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法， onComplete onCancel onError
	 * 分别表示第三方登录成功，取消 ，错误。
	 */
	private class BaseUiListener implements IUiListener {
		@Override
		public void onCancel() {
		}

		@Override
		public void onComplete(Object arg0) {
			onQQComplete(arg0);
		}

		@Override
		public void onError(UiError error) {
			Toast.makeText(AppContext.getAppContext(), error.errorMessage,
					Toast.LENGTH_LONG).show();
		}
	}
	
	private class BaseApiListener implements IUiListener{
		@Override
		public void onCancel() {
		}
		
		@Override
		public void onComplete(Object obj) {
			onQQInfoComplete(obj);
		}
		
		@Override
		public void onError(UiError arg0) {
		}
	}
}
