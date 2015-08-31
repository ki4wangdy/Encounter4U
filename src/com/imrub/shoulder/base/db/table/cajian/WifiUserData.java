package com.imrub.shoulder.base.db.table.cajian;

import android.database.sqlite.SQLiteDatabase;

import com.imrub.shoulder.base.db.DBConstant;
import com.imrub.shoulder.base.db.table.TableItemBase;

public class WifiUserData extends TableItemBase{

	private int meet_uid;
	private String nick_name;
	private String header_logo;
	private String description;
	
	private int meet_count;
	private int meet_time;
	
	public int getMeet_uid() {
		return meet_uid;
	}

	public void setMeet_uid(int meet_uid) {
		this.meet_uid = meet_uid;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getHeader_logo() {
		return header_logo;
	}

	public void setHeader_logo(String header_logo) {
		this.header_logo = header_logo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMeet_count() {
		return meet_count;
	}

	public void setMeet_count(int meet_count) {
		this.meet_count = meet_count;
	}

	public int getMeet_time() {
		return meet_time;
	}

	public void setMeet_time(int meet_time) {
		this.meet_time = meet_time;
	}

	@Override
	public String getPrimaryKeyName() {
		return "meet_uid";
	}
	
	@Override
	public String getPrimaryKeyValue() {
		return meet_uid+"";
	}
	
	@Override
	public String getTableName() {
		return DBConstant.UserDB.User_Mac_Data;
	}
	
	public void copyFromData(WifiUserData data){
		this.nick_name = data.nick_name;
		this.header_logo = data.header_logo;
		this.description = data.description;
		this.meet_count = data.meet_count;
		this.meet_time = data.meet_time;
	}
	
	public static void onCreateDB(SQLiteDatabase db){
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS " + 
				DBConstant.UserDB.User_Mac_Data + 
				" (_id integer primary key autoincrement, ");
		builder.append("meet_uid Integer not null default 0,");
		builder.append("nick_name text not null default '',");
		builder.append("header_logo text not null default '',");
		builder.append("description text not null default '',");
		builder.append("meet_count text not null default '',");
		builder.append("meet_time text not null default ''");
		builder.append(");");
		db.execSQL(builder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	}
	
	
}
