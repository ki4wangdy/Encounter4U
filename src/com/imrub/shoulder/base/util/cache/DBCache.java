package com.imrub.shoulder.base.util.cache;

import com.imrub.shoulder.base.app.AppContext;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.KeyIterator;
import com.snappydb.SnappydbException;

public class DBCache {

	private static boolean isOpen = false;
	private static DB mDBCache;
	
	private DBCache(){
	}

	public static DB getDB(){
		open();
		return mDBCache;
	}
	
	private static void open(){
		if(isOpen){
			return ;
		}
		try {
			mDBCache = DBFactory.open(AppContext.getAppContext());
			isOpen = true;
		} catch (SnappydbException e) {
			isOpen = false;
		}
	}
	
	public static void close(){
		if(isOpen){
			try {
				mDBCache.close();
				isOpen = false;
			} catch (SnappydbException e) {
			}
		}
	}
	
	public static void putObject(String key, Object obj){
		open();
		try {
			if(mDBCache != null && isOpen){
				mDBCache.put(key, obj);
			}
		} catch (SnappydbException e) {
		}
	}
	
	public static Object getObject(String id, Class<?> className){
		open();
		try {
			if(mDBCache != null && isOpen){
				return mDBCache.getObject(id, className);
			}
		} catch (SnappydbException e) {
		}
		return null;
	}
	
	public static void deletKey(String id){
		try{
			if(mDBCache != null && isOpen){
				mDBCache.del(id);
			}
		}catch(SnappydbException e){
		}
	}
	
	public static void deleteAllKeys(){
		open();
		if(mDBCache != null && isOpen){
			KeyIterator iter = null;
			try {
				iter = mDBCache.allKeysIterator();
				while(iter.hasNext()){
					String[] strs = iter.next(100);
					if(strs != null){
						for(String str : strs){
							deletKey(str);
						}
					}
				}
			} catch (SnappydbException e) {
			} finally{
				if(iter != null){
					iter.close();
				}
			}
		}
	}
	
}
