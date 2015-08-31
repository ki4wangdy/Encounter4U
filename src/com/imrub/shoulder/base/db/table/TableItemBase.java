package com.imrub.shoulder.base.db.table;

public abstract class TableItemBase {

	public static interface TableBasicInfo{
		public static final String TableKey = "_id";
	}
	
	protected long _id;
	
	public void setTid(long id){
		this._id = id;
	}
	
	public long getTid(){
		return _id;
	}
	
	public String getPrimaryKeyName(){
		return TableBasicInfo.TableKey;
	}
	
	public String getPrimaryKeyValue(){
		return getTid()+"";
	}
	
	public abstract String getTableName();
	
}
