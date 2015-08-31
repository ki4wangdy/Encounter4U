package com.imrub.shoulder.base.db;

public class DBConstant {

	public static final String DATABASE_SUFFIX	= ".db";
	
	// IMMSG DB
	public static final String DATABASE_IM_PREFIX_NAME 	= "immsg";
	public static final int DATABASE_IM_DB_VERSION 		= 1;
	
	// USER DB
	public static final String DATABASE_USER_PREFIX_NAME	=	"user";
	public static final int DATABASE_USER_DB_VERSION		=	1;
	
	public interface UserDB{
		public static final String User_Hot_Point 		= 	"hotpiont";
		public static final String User_New_Address		=	"newAddrUser";
		public static final String User_Detail_Info		=	"userDetailInfoData";
		public static final String User_New_Account		=	"userAccount";
		public static final String User_Mac_Collect		=	"wifiCollection";
		public static final String User_Mac_Data		=	"wifiUserData";
		public static final String User_Friend			=	"userFriend";
		public static final String User_ChatInfo		=	"chatroomInfo";
		
		// …Ë÷√œ‡πÿtable
		public static final String User_Notify_Setting	=	"notifySetting";
	}
	
}
