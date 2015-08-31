package com.imrub.shoulder.module.im;

public interface INewSubscriber {
	public static final int JidTypeNone	= 	1;
	public static final int JidTypeTo 	= 	4;
	public static final int JidFrom		=	6;
	public static final int JidFriend	=	8;
	public void onHandleSubscribeRequest(String jid, int type, String msg);
}
