package com.imrub.shoulder.module.addrlist;

import java.util.List;

import com.imrub.shoulder.base.db.table.addr.UserFriend;

public interface IFriend {
	public void onFinished(List<UserFriend> friends);
}
