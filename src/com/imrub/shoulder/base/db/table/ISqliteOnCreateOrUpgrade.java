package com.imrub.shoulder.base.db.table;

import android.database.sqlite.SQLiteDatabase;

public interface ISqliteOnCreateOrUpgrade {
	public void onCreate(SQLiteDatabase db);
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
