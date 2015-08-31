package com.imrub.shoulder.module.addrlist.newfriend;

import com.imrub.shoulder.base.util.cache.DBCache;
import com.imrub.shoulder.module.model.detail.UserDetailInfo;

public class AddrNewFriendData {

	public static final int TypeFromAddNewFriend = 1;
	
	private static AddrNewFriendData mInstance;
	private AddrNewFriendData(){
	}
	
	public static AddrNewFriendData getInstance(){
		if(mInstance == null){
			mInstance = new AddrNewFriendData();
		}
		return mInstance;
	}
	
	public void setUserDetailDataFromAddFriend(UserDetailInfo data){
		DBCache.putObject(data.getUser_info().getUid()+"", data);
	}
	
	public UserDetailInfo getUserDetailDataFromAddFriend(String id){
		UserDetailInfo info = (UserDetailInfo)DBCache.getObject(id, UserDetailInfo.class);
		return info;
	}
	
}
