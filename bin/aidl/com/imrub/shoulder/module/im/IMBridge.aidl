
package com.imrub.shoulder.module.im;

import com.imrub.shoulder.module.im.IMCallBack;

interface IMBridge{
	void sendMsg(String jid, String msg);
	void setImCallBack(IMCallBack callBack);
	
	void login(String uid, String password, String server);
	void register(String uid, String password, String server);
	
	void sendPing(String jid);
	
	boolean isToFriend(String jid);
	
}