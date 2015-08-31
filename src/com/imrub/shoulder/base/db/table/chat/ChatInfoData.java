package com.imrub.shoulder.base.db.table.chat;

import android.database.sqlite.SQLiteDatabase;

import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.DBConstant;
import com.imrub.shoulder.base.db.facade.FriendFacade;
import com.imrub.shoulder.base.db.table.TableItemBase;
import com.imrub.shoulder.module.im.MessageIdUtils;

public class ChatInfoData extends TableItemBase{

	public static interface MsgState{
		public static final int Msg_State_Over	= 0;
		public static final int Msg_State_Ing	= 1;
		public static final int Msg_State_Error	= 2;
		public static final int Msg_State_Com	= 3;
	}
	
	private String id;
	private String iconUrl ;
	private String chatTitle ;
	private String chatTime;
	private String chatContent ;
	
	private int msgState ;
	private int newMsgCount ;
	private int msgType;
	
	public ChatInfoData(){
	}
	
	public ChatInfoData(String id, int msgType, String chatTitle, String msg, long time){
		this.id = MessageIdUtils.createRoomId(id);
		String jid = MessageIdUtils.getToJid(id);
		this.iconUrl = FriendFacade.getInstance().getFriendLogo(jid);
		this.chatContent = msg;
		this.chatTitle = chatTitle;
		this.chatTime = time + "";
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public String getIconUrl() {
		return this.iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public int getMsgState() {
		return msgState;
	}

	public void setMsgState(int msgState) {
		this.msgState = msgState;
	}

	public int getNewMsgCount() {
		return newMsgCount;
	}

	public void setNewMsgCount(int newMsgCount) {
		this.newMsgCount = newMsgCount;
	}

	public String getChatTitle() {
		return chatTitle;
	}

	public void setChatTitle(String chatTitle) {
		this.chatTitle = chatTitle;
	}

	public String getChatContent() {
		return chatContent;
	}

	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}

	public long getChatTime() {
		return Long.parseLong(chatTime);
	}

	public void setChatTime(long chatTime) {
		this.chatTime = chatTime + "";
	}

	public static int getMsgStateOver() {
		return MsgState.Msg_State_Over;
	}

	public static int getMsgStateIng() {
		return MsgState.Msg_State_Ing;
	}

	public static int getMsgStateError() {
		return MsgState.Msg_State_Error;
	}

	public static int getMsgStateCom() {
		return MsgState.Msg_State_Com;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	@Override
	public String getPrimaryKeyName() {
		return "id";
	}

	@Override
	public String getPrimaryKeyValue() {
		return getId();
	}
	
	@Override
	public String getTableName() {
		return  DBConstant.UserDB.User_ChatInfo;
	}

	public static void onCreateDB(SQLiteDatabase db){
		if("".equalsIgnoreCase(UserInfo.getInstance().getUid())){
			return ;
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS " + DBConstant.UserDB.User_ChatInfo + 
				" (_id integer primary key autoincrement, ");
		builder.append("id text not null default '',");
		builder.append("iconUrl text not null default '',");
		builder.append("msgState Integer not null default 0,");
		builder.append("newMsgCount Integer not null default 0,");
		builder.append("chatTitle text not null default '',");
		builder.append("chatContent text not null default '',");
		builder.append("chatTime text not null default '',");
		builder.append("msgType Integer not null default 0");
		builder.append(");");
		db.execSQL(builder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	}
	
}
