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

		// ��һ����������������˵�������APPID���ڶ�����ȫ�ֵ�Context�����ģ���仰ʵ���˵���QQ��¼
		mTencent = Tencent.createInstance(ThirdpartyConstant.QQ_APP_ID,
				AppContext.getAppContext());

		/**
		 * ͨ�������룬SDKʵ����QQ�ĵ�¼�����������������������һ��������context�����ģ��ڶ�������SCOPO
		 * ��һ��String���͵��ַ�������ʾһЩȨ�� �ٷ��ĵ��е�˵����Ӧ����Ҫ�����ЩAPI��Ȩ�ޣ��ɡ������ָ������磺SCOPE =
		 * ��get_user_info,add_t��������Ȩ���á�all��
		 * ��������������һ���¼���������IUiListener�ӿڵ�ʵ���������õ��Ǹýӿڵ�ʵ����
		 */
		if (!mTencent.isSessionValid()) {
			mTencent.login((Activity) context, "all", new BaseUiListener());
		}
	}

	private void onQQComplete(Object obj) {
		// ��õ�������JSON��ʽ�ģ���������õ�����
		// ����㲻֪�����ܻ��ʲô����һ�������LOG
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
	    "nickname": "С��",
	    "yellow_vip_level": "0",
	    "msg": "",
	    "figureurl_1": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/50",
	    "vip": "0",
	    "level": "0",
	    "figureurl_2": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
	    "is_yellow_vip": "0",
	    "gender": "��",
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
	 * ���Զ���ļ�����ʵ��IUiListener�ӿں󣬱���Ҫʵ�ֽӿڵ����������� onComplete onCancel onError
	 * �ֱ��ʾ��������¼�ɹ���ȡ�� ������
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
