package com.imrub.shoulder.base.db.table.cajian;

import android.database.sqlite.SQLiteDatabase;

import com.imrub.shoulder.base.db.DBConstant;
import com.imrub.shoulder.base.db.table.TableItemBase;

public class WifiCollection extends TableItemBase{

	// 时间
	private String field1;
	
	// MAC 地址集合
	private String field2;
	
	// flag 标记该数据是否已经上传了
	// 0标示未上传，1标示上传
	private int field3;
	
	// flag 标记是否是离线的
	// 0标示不是，1标示是
	private int field4;
	
	// MAC collection md5 value
	private String field5;
	
	private String field6;
	private String field7;
	private String field8;

	public WifiCollection(){
		this.field1 = "";
		this.field2 = "";
		this.field3 = 0;
		this.field4 = 0;
		this.field5 = "";
		this.field6 = "";
		this.field7 = "";
		this.field8 = "";
	}
	
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public int getField3() {
		return field3;
	}

	public void setField3(int field3) {
		this.field3 = field3;
	}

	public int getField4() {
		return field4;
	}

	public void setField4(int field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

	public String getField6() {
		return field6;
	}

	public void setField6(String field6) {
		this.field6 = field6;
	}

	public String getField7() {
		return field7;
	}

	public void setField7(String field7) {
		this.field7 = field7;
	}

	public String getField8() {
		return field8;
	}

	public void setField8(String field8) {
		this.field8 = field8;
	}

	@Override
	public String getPrimaryKeyName() {
		return "field1";
	}

	@Override
	public String getPrimaryKeyValue() {
		return getField1();
	}
	
	@Override
	public String getTableName() {
		return DBConstant.UserDB.User_Mac_Collect;
	}
	
	public static void onCreateDB(SQLiteDatabase db){
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS " + 
				DBConstant.UserDB.User_Mac_Collect + 
				" (_id integer primary key autoincrement, ");
		builder.append("field1 text not null default '',");
		builder.append("field2 text not null default '',");
		builder.append("field3 Integer not null default 0,");
		builder.append("field4 Integer not null default 0,");
		builder.append("field5 text not null default '',");
		builder.append("field6 text not null default '',");
		builder.append("field7 text not null default '',");
		builder.append("field8 text not null default ''");
		builder.append(");");
		db.execSQL(builder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	}
	
}
