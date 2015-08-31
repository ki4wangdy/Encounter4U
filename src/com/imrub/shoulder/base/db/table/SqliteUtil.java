package com.imrub.shoulder.base.db.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.module.CrashHandler;

public class SqliteUtil {

	public static final String Tag = "SqliteUtil";
	
	private SqliteUtil(){
	}

	public static <X extends TableItemBase> void asyncDeleteAndInsertItem(final SQLiteOpenHelper sqliteHelper, final X item){
		deleteItemBaseOnPrimaryKey(sqliteHelper, item);
		insertItem(sqliteHelper, item);
	}
	
	public static void deleteTable(SQLiteOpenHelper sqliteHelper, String tableName){
		String sql = "delete from " + tableName;
		sqliteHelper.getWritableDatabase().execSQL(sql);
	}
	
	public static void deleteItemBaseOnPrimaryKey(SQLiteOpenHelper sqliteHelper, final TableItemBase item){
		String sql = "delete from " + item.getTableName() + " where " + item.getPrimaryKeyName() + " = '" + item.getPrimaryKeyValue()+"'";
		sqliteHelper.getWritableDatabase().execSQL(sql);
	}
	
	public static void deleteItemBaseOnKeyAndValue(SQLiteOpenHelper sqliteHelper, final String tableName, final String key, final String value){
		String sql = "delete from " + tableName + " where " + key + " = " + value;
		sqliteHelper.getWritableDatabase().execSQL(sql);
	}

	public static <X extends TableItemBase > void asynInsertItem(final SQLiteOpenHelper sqliteHelper,final X item){
		try{
			ContentValues values = new ContentValues();
			List<TableDataField> fields = TableDataField.getTableDataFields(item.getClass());
			for (TableDataField field : fields) {
				Field clazzField = item.getClass().getDeclaredField(field.getDataName());
				clazzField.setAccessible(true);
				Object fieldValue = clazzField.get(item);
				TableDataField.UpdateContentValue(values, field, fieldValue);
			}
			long tid = sqliteHelper.getWritableDatabase().insert(item.getTableName(), null, values);
			item.setTid(tid);
		}catch(Exception ex){
			Logger.print(Tag, CrashHandler.getExceptionMessage(ex));
		}
	}
	
	public static <X extends TableItemBase > void insertItem(SQLiteOpenHelper sqliteHelper,final X item){
		try{
			ContentValues values = new ContentValues();
			List<TableDataField> fields = TableDataField.getTableDataFields(item.getClass());
			for (TableDataField field : fields) {
				Field clazzField = item.getClass().getDeclaredField(field.getDataName());
				clazzField.setAccessible(true);
				Object fieldValue = clazzField.get(item);
				TableDataField.UpdateContentValue(values, field, fieldValue);
			}
			long tid = sqliteHelper.getWritableDatabase().insert(item.getTableName(), null, values);
			item.setTid(tid);
		}catch(Exception e){
		}
	}
	
	public static <X extends TableItemBase > void insertItemForMessage(SQLiteOpenHelper sqliteHelper, String roomId, final X item){
		try{
			ContentValues values = new ContentValues();
			List<TableDataField> fields = TableDataField.getTableDataFields(item.getClass().getSuperclass());
			for (TableDataField field : fields) {
				Field clazzField = item.getClass().getSuperclass().getDeclaredField(field.getDataName());
				clazzField.setAccessible(true);
				Object fieldValue = clazzField.get(item);
				TableDataField.UpdateContentValue(values, field, fieldValue);
			}
			long tid = sqliteHelper.getWritableDatabase().insert(roomId, null, values);
			item.setTid(tid);
		}catch(Exception e){
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <X extends TableItemBase> List<X> queryAllItem(SQLiteOpenHelper sqliteHelper, final TableItemBase item){
		String sql = "select * from " + item.getTableName() ;
		final Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery(sql, null);
		try{
			if(cursor != null && cursor.moveToFirst()){
				return parseCursor(cursor, (Class<X>)item.getClass());
			}
		}catch(Exception e){
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return null;
	}
	
	public static int queryTableCount(SQLiteOpenHelper sqliteHelper,final TableItemBase item) {
		String sql = "select 1 from " + item.getTableName() ;
		Cursor cursor = sqliteHelper.getReadableDatabase().rawQuery(sql, null);
		try {
			if(cursor != null ){
				return cursor.getCount();
			}
		} finally {
			if(cursor != null){
				cursor.close();
			}
		}
		return 0;
	}

	public static int queryTableCount(SQLiteOpenHelper sqliteHelper,final String tableName) {
		String sql = "select 1 from " + tableName ;
		Cursor cursor = sqliteHelper.getReadableDatabase().rawQuery(sql, null);
		try {
			if(cursor != null ){
				return cursor.getCount();
			}
		} finally {
			if(cursor != null){
				cursor.close();
			}
		}
		return 0;
	}
	
	public static int queryTableCountBasedonPrimaryKey(SQLiteOpenHelper sqliteHelper,final TableItemBase item) {
		String sql = "select * from " + item.getTableName() + " where " + item.getPrimaryKeyName() +" == " + item.getPrimaryKeyValue();
		final Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery(sql, null);
		try{
			if(cursor != null){
				return cursor.getCount();
			}
		}catch(Exception e){
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return 0;
	}
	
	public static <X extends TableItemBase> void updateOrInsertBasedOnPrimaryKey(SQLiteOpenHelper sqliteHelper,
			final X item){
		List<X> x = queryTableItemBaseOnPrimaryKey(sqliteHelper,item);
		if(x == null || x.isEmpty()){
			try{
				insertItem(sqliteHelper,item);
			} catch(Exception e){
			}
		} else {
			if(x.size() > 1){
				throw new RuntimeException("Fail to updateOrInsertBasedOnPrimaryKey,because the item is more than 2!");
			}
			updateTableItemBaseOnPrimaryKey(sqliteHelper,item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <X extends TableItemBase> List<X> queryTableItemBaseOnPrimaryKey(SQLiteOpenHelper sqliteHelper,
			final X item){
		String sql = "select * from " + item.getTableName() + " where " + item.getPrimaryKeyName() +" == " + item.getPrimaryKeyValue();
		final Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery(sql, null);
		try{
			if(cursor != null && cursor.moveToFirst()){
				return parseCursor(cursor, (Class<X>)item.getClass());
			}
		}catch(Exception e){
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <X extends TableItemBase> X queryTableItemOneCountBaseOnPrimaryKey(SQLiteOpenHelper sqliteHelper,
			final X item){
		String sql = "select * from " + item.getTableName() + " where " + item.getPrimaryKeyName() +" == " + item.getPrimaryKeyValue();
		final Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery(sql, null);
		try{
			if(cursor != null && cursor.moveToFirst()){
				return parseOneCursor(cursor, (Class<X>)item.getClass());
			}
		}catch(Exception e){
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return null;
	}

	
	public static <X extends TableItemBase> int queryTableItemCountBaseOnPrimaryKey(SQLiteOpenHelper sqliteHelper,
			final X item){
		String sql = "select * from " + item.getTableName() + " where " + item.getPrimaryKeyName() +" == " + item.getPrimaryKeyValue();
		final Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery(sql, null);
		try{
			if(cursor != null && cursor.moveToFirst()){
				return cursor.getCount();
			}
		}catch(Exception e){
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public static <X extends TableItemBase> List<X> queryTableItemBase(SQLiteOpenHelper sqliteHelper, X item){
		String sql = "select * from " + item.getTableName();
		final Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery(sql, null);
		try{
			if(cursor != null && cursor.moveToFirst()){
				return parseCursor(cursor, (Class<X>)item.getClass());
			}
		}catch(Exception e){
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <X extends TableItemBase > List<X> queryTableItemBaseOnValue(SQLiteOpenHelper sqliteHelper,
			final X item, final String key, final String value){
		String sql = "select * from " + item.getTableName() + " where " + key +" == " + value;
		final Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery(sql, null);
		try{
			if(cursor != null && cursor.moveToFirst()){
				return parseCursor(cursor, (Class<X>)item.getClass());
			}
		}catch(Exception e){
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <X extends TableItemBase> List<X> queryTableItemBaseOnValue(SQLiteOpenHelper sqliteHelper,
			final X item, final String key, int count){
		String sql = "select * from " + item.getTableName() + " order by " +  key  + " DESC " + " limit 0," + count;
		final Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery(sql, null);
		try{
			if(cursor != null){
				if(cursor.getCount() == 0){
					return null;
				}
			}
			cursor.moveToFirst();
			return parseCursorReverse(cursor, (Class<X>)item.getClass());
		}catch(Exception e){
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return null;
	}

	
	@SuppressWarnings("unchecked")
	public static <X extends TableItemBase> List<X> queryTableItemBaseOnValue(SQLiteOpenHelper sqliteHelper,
			 final X item, final String tableName, final String key, final String value, int count){
		String sql = "select * from '" + tableName + "' where " + key + " < " 
			+ value + " order by " +  key  + " DESC " + " limit 0," + count;
		final Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery(sql, null);
		try{
			if(cursor != null){
				if(cursor.getCount() == 0){
					return null;
				}
			}
			cursor.moveToFirst();
			return parseCursorReverse(cursor, (Class<X>)item.getClass());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return null;
	}

	
	public static <X extends TableItemBase> void asyncUpdateTableItemBaseOnPrimaryKey(final SQLiteOpenHelper sqliteHelper,
			final X dataItem) {
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				try {
					ContentValues values = new ContentValues();
					List<TableDataField> fields = TableDataField.getTableDataFields(dataItem.getClass());
					for (TableDataField field : fields) {
						Field clazzField = dataItem.getClass().getDeclaredField(
								field.getDataName());
						clazzField.setAccessible(true);
						Object fieldValue = clazzField.get(dataItem);
						TableDataField.UpdateContentValue(values, field, fieldValue);
					}
					sqliteHelper.getWritableDatabase().update(dataItem.getTableName(),values, 
							dataItem.getPrimaryKeyName() + " =? ", new String[]{dataItem.getPrimaryKeyValue()});					
				} catch(Exception ex) {
				}
			}
		});
	}
	
	public static <X extends TableItemBase> boolean updateTableItemBaseOnPrimaryKey(SQLiteOpenHelper sqliteHelper,
			final X dataItem) {
		try {
			ContentValues values = new ContentValues();
			List<TableDataField> fields = TableDataField.getTableDataFields(dataItem.getClass());
			for (TableDataField field : fields) {
				Field clazzField = dataItem.getClass().getDeclaredField(
						field.getDataName());
				clazzField.setAccessible(true);
				Object fieldValue = clazzField.get(dataItem);
				TableDataField.UpdateContentValue(values, field, fieldValue);
			}
			return sqliteHelper.getWritableDatabase().update(dataItem.getTableName(), 
					values, dataItem.getPrimaryKeyName()+" =? " , new String[]{dataItem.getPrimaryKeyValue()}) 
					> 0 ? true : false;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	private static <X extends TableItemBase> List<X> parseCursorReverse(final Cursor cursor, final Class<X> tableName) throws Exception{
		List<X> items = new ArrayList<X>(cursor.getCount());
		do{
			final X item = createInstance(tableName);
			if(item != null){
				List<TableDataField> fields = TableDataField.getTableDataFields(tableName);
				Object value = null;
				for (TableDataField field : fields) {
					value = TableDataField.UpdateContentValue(field, cursor);
					Field clazzField = tableName.getDeclaredField(field.getDataName());
					clazzField.setAccessible(true);
					clazzField.set(item, value);
				}
				item.setTid(cursor.getInt(0));
				items.add(0,item);
			}
		}while(cursor.moveToNext());
		return items;
	}
	
	private static <X extends TableItemBase> X parseOneCursor(final Cursor cursor, final Class<X> tableName) throws Exception{
		final X item = createInstance(tableName);
		if(item != null){
			List<TableDataField> fields = TableDataField.getTableDataFields(tableName);
			Object value = null;
			for (TableDataField field : fields) {
				value = TableDataField.UpdateContentValue(field, cursor);
				Field clazzField = tableName.getDeclaredField(field.getDataName());
				clazzField.setAccessible(true);
				clazzField.set(item, value);
			}
			item.setTid(cursor.getInt(0));
		}
		return item;
	}
	
	
	private static <X extends TableItemBase> List<X> parseCursor(final Cursor cursor, final Class<X> tableName) throws Exception{
		List<X> items = new ArrayList<X>(cursor.getCount());
		do{
			final X item = createInstance(tableName);
			if(item != null){
				List<TableDataField> fields = TableDataField.getTableDataFields(tableName);
				Object value = null;
				for (TableDataField field : fields) {
					value = TableDataField.UpdateContentValue(field, cursor);
					Field clazzField = tableName.getDeclaredField(field.getDataName());
					clazzField.setAccessible(true);
					clazzField.set(item, value);
				}
				item.setTid(cursor.getInt(0));
				items.add(item);
			}
		}while(cursor.moveToNext());
		return items;
	}
	
	private static <X> X createInstance(final Class<X> tableName) {
		try {
			return (X)tableName.newInstance();
		} catch (Exception ex) {
		}
		return null;
	}
	
}
