package com.imrub.shoulder.base.db.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public class TableDataField {

	public static final String INT 		= "int";
	public static final String STRING 	= "String";
	public static final String LONG   	= "long";
	
	public static enum TableDataFieldType{
		UNKNOWN,Integer,Long,Text,Clob,Blob;
	}
	
	private String name;
	private TableDataFieldType nameType;
	
	public TableDataField(final String name, final TableDataFieldType nameType){
		this.name = name;
		this.nameType = nameType;
	}
	
	public String getDataName(){
		return this.name;
	}
	
	public TableDataFieldType getDataType(){
		return this.nameType;
	}
	
	public static List<TableDataField> getTableDataFields(final Class<?> className){
		List<TableDataField> list = new ArrayList<TableDataField>();
		Field[] fields = className.getDeclaredFields();
		for(Field f : fields){
			TableDataFieldType type = ToDataType(f.getType().getSimpleName());
			if(type != TableDataFieldType.UNKNOWN){
				list.add(new TableDataField(f.getName(), type));
			}
		}
		return list;
	}

	// support three type now!
	public static TableDataFieldType ToDataType(final String filedTypeSimpleName){
		if(filedTypeSimpleName.equalsIgnoreCase(INT)){
			return TableDataFieldType.Integer;
		} else if(filedTypeSimpleName.equalsIgnoreCase(STRING)){
			return TableDataFieldType.Text;
		} else if(filedTypeSimpleName.equalsIgnoreCase(LONG)){
			return TableDataFieldType.Long;
		}
		return TableDataFieldType.UNKNOWN;
	}
	
	public static ContentValues UpdateContentValue(ContentValues values, TableDataField field, Object fieldValue){
		TableDataFieldType type = field.getDataType();
		if(type == TableDataFieldType.Integer){
			values.put(field.getDataName(), (Integer) fieldValue);
		} else if(type == TableDataFieldType.Long){
			values.put(field.getDataName(), (Long) fieldValue);
		} else if(type == TableDataFieldType.Text){
			values.put(field.getDataName(), (String)fieldValue);
		} else {
			throw new RuntimeException("Fail to  update Content Value, because the type is not found!");
		}
		return values;
	}
	
	public static Object UpdateContentValue(TableDataField field, final Cursor cursor){
		TableDataFieldType type = field.getDataType();
		Object obj = null;
		if(type == TableDataFieldType.Integer){
			obj = cursor.getInt(cursor.getColumnIndex(field.getDataName()));
		} else if(type == TableDataFieldType.Long){
			obj = cursor.getLong(cursor.getColumnIndex(field.getDataName()));
		} else if(type == TableDataFieldType.Text){
			obj = cursor.getString(cursor.getColumnIndex(field.getDataName()));
		} else {
			throw new RuntimeException("Fail to update ContentValue, because the type is not found!");
		}
		return obj;
	}
	
}
