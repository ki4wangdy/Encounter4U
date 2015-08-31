package com.imrub.shoulder.module.login;



public class SinaWeiboManager {

//	private WeiboAuth mWeiboAuth;
//	private SsoHandler mSsoHandler;
//	
//	private Oauth2AccessToken mAccessToken;
//	
//	private final WeiboAuthListener mAuthListener = new WeiboAuthListener() {
//		@Override
//		public void onWeiboException(WeiboException arg0) {
//		}
//		
//		@Override
//		public void onComplete(Bundle bundle) {
//			onSinaAuthComplete(bundle);
//		}
//		
//		@Override
//		public void onCancel() {
//		}
//	};
//	
//	private final RequestListener mRequestListener = new RequestListener() {
//		@Override
//		public void onWeiboException(WeiboException arg0) {
//		}
//		
//		@Override
//		public void onComplete(String userText) {
//			parseUser(userText);
//		}
//	};
//	
//	private static SinaWeiboManager mInstance = new SinaWeiboManager();
//	private SinaWeiboManager() {
//	}
//
//	public static void onSinaLogin(final Context currentActivity){
//		if(mInstance.mWeiboAuth == null){
//			mInstance.mWeiboAuth = new WeiboAuth(currentActivity, ThirdpartyConstants.Sina_APP_KEY, ThirdpartyConstants.REDIRECT_URL,
//					ThirdpartyConstants.SCOPE);
//		}
//		mInstance.mSsoHandler.authorize(mInstance.mAuthListener);
//	}
//	
//	private void onSinaAuthComplete(final Bundle bundle){
//		mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
//		if (mAccessToken.isSessionValid()){
//			UsersAPI userAPI = new UsersAPI(mAccessToken);
//			userAPI.show(Long.parseLong(mAccessToken.getUid()), mRequestListener);
//		}
//	}
//	
//	private void parseUser(final String userText){
//		if(!TextUtils.isEmpty(userText)){
//			User user = User.parse(userText);
//			UserAccount account = new UserAccount();
//			account.copyFromUser(user,mAccessToken);
////			DBModule.updateOrInsertObjectOnAsync(account);
//		}
//	}
//	
}
