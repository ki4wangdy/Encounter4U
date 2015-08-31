package com.imrub.shoulder.base.db.table.im;

import android.database.sqlite.SQLiteDatabase;

import com.imrub.shoulder.base.db.table.TableItemBase;

public class Message extends TableItemBase {

	private String id;
	private String uid;
	
	private String msg;
	private String time;
	
	private int msgType;
	private int showTime;
	
	public Message(){
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getShowTime() {
		return showTime;
	}

	public void setShowTime(int showTime) {
		this.showTime = showTime;
	}

	public String getMessageTime() {
		return time;
	}

	public String getUid() {
		return uid;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public Message getMessageTypeObject(String msg){
		return null;
	}
	
	public String getMessageJsonString(){
		return "";
	}
	
	@Override
	public String getTableName() {
		return getId();
	}
	
	public static void onCreateDB(SQLiteDatabase db, String tableName){
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS '" + tableName + 
				"' (_id integer primary key autoincrement, ");
		builder.append("id text not null default '',");
		builder.append("uid text not null default '',");
		builder.append("showTime Integer not null default 0,");
		builder.append("msg text not null default '',");
		builder.append("msgType Integer not null default 0,");
		builder.append("time text not null default ''");
		builder.append(");");
		db.execSQL(builder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	}
	
}
