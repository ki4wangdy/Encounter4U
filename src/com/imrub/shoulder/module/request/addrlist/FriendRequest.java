package com.imrub.shoulder.module.request.addrlist;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.AddrFacade;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.base.thread.IAction;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.base.util.TimeUtil;
import com.imrub.shoulder.module.CrashHandler;
import com.imrub.shoulder.module.addrlist.IFriend;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.RequestBase;
import com.imrub.shoulder.module.request.RequestUtils;

public class FriendRequest {

	public static final String Tag = "FriendRequest";
	
	public static void getFriend(final IFriend callback){
		
		String uid = UserInfo.getInstance().getUid();
		final String postData = AddrlistRequestFactory.createFriendRequest(Integer.parseInt(uid));
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlUserFriend, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				List<UserFriend> friends = null;
				try {
					JSONObject obj = (JSONObject)JSON.parse(arg);
					code = obj.getIntValue(RequestBase.Code);
					if(code == RequestBase.CodeOk){
						JSONArray array = obj.getJSONArray("friends");
						if(array != null && !array.isEmpty()){
							friends = JSONObject.parseArray(array.toJSONString(), UserFriend.class);
							for(UserFriend f : friends){
								f.updatePinyin();
							}
							AddrFacade.insertAllFriends(friends);
						}
					}
				} catch (Exception e) {
					Logger.print(Tag, CrashHandler.getExceptionMessage(e));
				}
				TimeUtil.sleep(1000);
				doSuccessResultOnUIThread(callback, friends);
			}
		});
		
	}
	
	private static void doSuccessResultOnUIThread(final IFriend callback, final List<UserFriend> friends){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				callback.onFinished(friends);
			}
		});
	}
	
}
