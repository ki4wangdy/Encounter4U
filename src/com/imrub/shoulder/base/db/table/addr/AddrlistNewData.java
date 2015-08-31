package com.imrub.shoulder.base.db.table.addr;

import android.database.sqlite.SQLiteDatabase;

import com.imrub.shoulder.base.db.DBConstant;
import com.imrub.shoulder.base.db.table.TableItemBase;

public class AddrlistNewData extends TableItemBase{

	private String uid;
	private String name;
	private String iconUrl;

	private String signature;
	private String time;
	
	private int status;
	private int isnew;
	private int isNewRequest;
	
	public AddrlistNewData(){
		this("","","","",0,"",1,1);
	}
	
	public AddrlistNewData(String uid, String name, String iconUrl, 
			String signature, int status, String time, int isNew, int isNewRequest){
		this.uid = uid;
		this.name = name;
		this.iconUrl = iconUrl;
		this.signature = signature;
		this.status = status;
		this.time = time;
		this.isnew = isNew;
		this.isNewRequest = isNewRequest;
	}
	
	public int getIsNewRequest() {
		return isNewRequest;
	}

	public void setIsNewRequest(int isNewRequest) {
		this.isNewRequest = isNewRequest;
	}
	
	public int getIsnew() {
		return isnew;
	}

	public void setIsnew(int isnew) {
		this.isnew = isnew;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	@Override
	public String getTableName() {
		return DBConstant.UserDB.User_New_Address;
	}
	
	@Override
	public String getPrimaryKeyName() {
		return "uid";
	}

	@Override
	public String getPrimaryKeyValue() {
		return uid;
	}
	
	public void copyFromReply(AddrlistNewData data){
		this.iconUrl = data.iconUrl;
		this.isnew = data.isnew;
		this.name = data.name;
		this.signature = data.signature;
		this.isNewRequest = data.isNewRequest;
		this.time = data.time;
	}
	
	public void copy(AddrlistNewData data){
		this.iconUrl = data.iconUrl;
		this.isnew = data.isnew;
		this.name = data.name;
		this.signature = data.signature;
		this.status = data.status;
		this.uid = data.uid;
		this.isNewRequest = data.isNewRequest;
		this.time = data.time;
	}
	
	public static void onCreateDB(SQLiteDatabase db){
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS " + 
				DBConstant.UserDB.User_New_Address + 
				" (_id integer primary key autoincrement, ");
		builder.append("uid text not null default '',");
		builder.append("name text not null default '',");
		builder.append("iconUrl text not null default '',");
		builder.append("signature text not null default '',");
		builder.append("status Integer not null default 0,");
		builder.append("time text not null default '',");
		builder.append("isnew Integer not null default 0,");
		builder.append("isNewRequest Integer not null default 0");
		builder.append(");");
		db.execSQL(builder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	}
	
}
