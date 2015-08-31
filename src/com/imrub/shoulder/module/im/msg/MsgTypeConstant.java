package com.imrub.shoulder.module.im.msg;

public interface MsgTypeConstant {

	public static final int ReplyNewAddrMsgType 	=	1 << 0;
	public static final int AddFriendMsgType		=	1 << 4;
	
	public static final int TextMsgType 	= 1 << 1;
	public static final int SoundMsgType 	= 1 << 2;
	public static final int ImageMsgType	= 1 << 3;
	
	public static final int SystemMsgType   = 1 << 5;
	public static final int UserLogoMsgType	= 1 << 6;
	
	public static final String TagR 	= "r";
	
	public static final String TagMsg 	= "msg";
	public static final String AttrType = "type";
	public static final String AttrTime = "time";
	
	public static final String AttrFrom = "from";
	public static final String AttrName	= "name";	
	public static final String AttrTo 	= "to";
	
}
