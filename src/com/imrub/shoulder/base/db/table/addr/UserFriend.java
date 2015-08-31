package com.imrub.shoulder.base.db.table.addr;

import java.util.Comparator;

import android.database.sqlite.SQLiteDatabase;

import com.imrub.shoulder.base.db.DBConstant;
import com.imrub.shoulder.base.db.table.TableItemBase;
import com.imrub.shoulder.module.addrlist.HanziToPinyin;

public class UserFriend extends TableItemBase implements Comparator<UserFriend>{

	private String uid;
	private String nick_name;
	private String header_logo;
	private String meet_count;
	private String pinyin;
	
	public UserFriend(){
		this.uid = "";
		this.nick_name = "";
		this.header_logo = "";
		this.meet_count = "0";
		this.pinyin = "";
	}

	public UserFriend(String uid, String name, String logo, String count){
		this.uid = uid;
		this.nick_name = name;
		this.header_logo = logo;
		this.meet_count = count;
		updatePinyin();
	}
	
	public void updatePinyin(){
		pinyin = HanziToPinyin.getPinYin4J(this.nick_name).toLowerCase();
	}
	
	public String getPinyin() {
		if(pinyin == null || pinyin.length() == 0){
			updatePinyin();
		}
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public String getMeet_count() {
		return meet_count;
	}

	public void setMeet_count(String meet_count) {
		this.meet_count = meet_count;
	}

	@Override
	public String getTableName() {
		return DBConstant.UserDB.User_Friend;
	}
	
	@Override
	public String getPrimaryKeyName() {
		return "uid";
	}

	@Override
	public String getPrimaryKeyValue() {
		return getUid();
	}
	
	@Override
	public int compare(UserFriend friend1, UserFriend friend2) {
		return friend1.getPinyin().compareTo(friend2.getPinyin());
	}
	
	public static void onCreateDB(SQLiteDatabase db){
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS " + DBConstant.UserDB.User_Friend + 
				" (_id integer primary key autoincrement, ");
		builder.append("uid Integer not null default 0,");
		builder.append("nick_name text not null default '',");
		builder.append("header_logo text not null default '',");
		builder.append("meet_count Integer not null default 0,");
		builder.append("pinyin text not null default ''");
		builder.append(");");
		db.execSQL(builder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	}
	
	
}
