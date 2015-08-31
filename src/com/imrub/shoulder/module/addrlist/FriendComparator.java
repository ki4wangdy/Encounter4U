package com.imrub.shoulder.module.addrlist;

import java.util.Comparator;

import com.imrub.shoulder.base.db.table.addr.UserFriend;

public class FriendComparator implements Comparator<UserFriend>{

	public int compare(UserFriend lhs, UserFriend rhs) {
		return lhs.getPinyin().compareTo(rhs.getPinyin());
	};
	
}
