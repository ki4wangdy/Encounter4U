package com.imrub.shoulder.base.db.table;

import android.database.sqlite.SQLiteDatabase;

import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.DBConstant;
import com.imrub.shoulder.base.db.UserSqliteManager;

public class HotPoint extends TableItemBase{

	public interface HotPointConstant{
		// for the first big item
		public static final int F1_Status_MainHotpiont	=	1 << 0 ;
		
		// for the second item
		public static final int F2_Status_Cajian	=	1 << 0 ;
		public static final int F2_Status_Address	=	1 << 1 ;
		public static final int F2_Status_Chat		=	1 << 2 ;
		public static final int F2_Status_Yuanfen	=	1 << 3 ;
		public static final int F2_Status_Setting	=	1 << 4 ;
		
		public static final int F7_Status_User_Icon		=	1 << 0 ;
		
	}
	
	private int field1;
	private int field2;

	private int field3;
	private int field4;

	private int field5;
	private int field6;

	private int field7;
	private int userid;
	
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getField1() {
		return field1;
	}

	public void setField1(int field1) {
		this.field1 |= field1;
	}

	public void removeField1(int field1){
		this.field1 &= ~field1;
	}
	
	public int getField2() {
		return field2;
	}

	public void setField2(int field2) {
		this.field2 |= field2;
	}

	public void removeField2(int field2){
		this.field2 &= ~field2;
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

	public int getField5() {
		return field5;
	}

	public void setField5(int field5) {
		this.field5 = field5;
	}

	public int getField6() {
		return field6;
	}

	public void setField6(int field6) {
		this.field6 = field6;
	}

	public int getField7() {
		return field7;
	}

	public void setField7(int field7) {
		this.field7 |= field7;
	}
	
	public void removeField7(int f7){
		this.field7 &= ~f7;
	}

	@Override
	public String getPrimaryKeyName() {
		return "userid";
	}

	@Override
	public String getPrimaryKeyValue() {
		return UserInfo.getInstance().getUid();
	}
	
	@Override
	public String getTableName() {
		return DBConstant.UserDB.User_Hot_Point;
	}

	public void updateLocalDb(){
		SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), this);
	}
	
	public void insertLocalDb(){
		try {
			SqliteUtil.insertItem(UserSqliteManager.getInstance(), this);
		} catch (Exception e) {
		}
	}
	
	public static void onCreateDB(SQLiteDatabase db){
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS " + DBConstant.UserDB.User_Hot_Point + 
				" (_id integer primary key autoincrement, ");
		builder.append("field1 Integer not null default 0,");
		builder.append("field2 Integer not null default 0,");
		builder.append("field3 Integer not null default 0,");
		builder.append("field4 Integer not null default 0,");
		builder.append("field5 Integer not null default 0,");
		builder.append("field6 Integer not null default 0,");
		builder.append("field7 Integer not null default 0,");
		builder.append("userid Integer not null default 0");
		builder.append(");");
		db.execSQL(builder.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	}
	
}
