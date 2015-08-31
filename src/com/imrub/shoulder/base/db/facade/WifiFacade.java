package com.imrub.shoulder.base.db.facade;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.imrub.shoulder.base.db.DBConstant;
import com.imrub.shoulder.base.db.UserSqliteManager;
import com.imrub.shoulder.base.db.table.SqliteUtil;
import com.imrub.shoulder.base.db.table.cajian.WifiCollection;
import com.imrub.shoulder.base.db.table.cajian.WifiUserData;

public class WifiFacade {

	/**
	 * ��ѯ����û���ϴ���wifi��ַ
	 * @return
	 */
	public static List<WifiCollection> queryAllNotUploadWifiCollection(){
		WifiCollection collection = new WifiCollection();
		List<WifiCollection> data = SqliteUtil.queryTableItemBaseOnValue(UserSqliteManager.getInstance(),
			collection, "field3","0");
		return data;
	}
	
	/**
	 * ��ʾ����wifi�Ѿ��ϴ�
	 */
	public static void setAllWifiCollectionUploaded(List<WifiCollection> data){
		SQLiteDatabase database = UserSqliteManager.getInstance().getWritableDatabase();
		try{
			database.beginTransaction();
			for(WifiCollection item : data){
				item.setField3(1);
				SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), item);
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}
	
	/**
	 * ��ѯ�Ƿ���wifi����
	 * @return
	 */
	public static boolean isWifiDataEmpty(){
		return SqliteUtil.queryTableCount(UserSqliteManager.getInstance(), DBConstant.UserDB.User_Mac_Data) == 0 ? true : false;
	}
	
	
	/**
	 * �����������û�
	 */
	public static void insertAllWifiUser(List<WifiUserData> data){
		SQLiteDatabase database = UserSqliteManager.getInstance().getWritableDatabase();
		try{
			database.beginTransaction();
			for(WifiUserData item : data){
				SqliteUtil.insertItem(UserSqliteManager.getInstance(), item);
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}

	/**
	 * ��������������wifi�û�
	 * @param data
	 */
	public static void updateAllWifiUser(List<WifiUserData> data){
		SQLiteDatabase database = UserSqliteManager.getInstance().getWritableDatabase();
		try{
			database.beginTransaction();
			for(WifiUserData item : data){
				SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), item);
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}
	
	/**
	 * ��ѯ�û��Ƿ����
	 * @param data
	 * @return
	 */
	public static boolean queryWifiUserIsExist(WifiUserData data){
		return SqliteUtil.queryTableCountBasedonPrimaryKey(UserSqliteManager.getInstance(), data) == 0 ? false : true;
	}
	

	/**
	 * ɾ������wifi����
	 */
	public static void deleteAllUploadedWifiData(){
		SqliteUtil.deleteItemBaseOnKeyAndValue(UserSqliteManager.getInstance(), DBConstant.UserDB.User_Mac_Collect, "field3", "1");
	}
	
	/**
	 * ����wifi����
	 */
	public static void insertWifiData(WifiCollection data){
		SqliteUtil.insertItem(UserSqliteManager.getInstance(), data);
	}
	
	/**
	 * ��ѯ����wifi���ݵ�����
	 * @return
	 */
	public static int queryAllWifiCollectionCount(){
		WifiCollection item = new WifiCollection();
		return SqliteUtil.queryTableCount(UserSqliteManager.getInstance(), item);
	}

	/**
	 * ��ѯĳ��wifi����
	 * @param item
	 * @return
	 */
	public static WifiCollection queryWifiCollection(WifiCollection item){
		return SqliteUtil.queryTableItemOneCountBaseOnPrimaryKey(UserSqliteManager.getInstance(), item);
	}

	/**
	 * ��ѯ����WifiUser����
	 * @return
	 */
	public static List<WifiUserData> queryAllWifiUserData(){
		int count = SqliteUtil.queryTableCount(UserSqliteManager.getInstance(), DBConstant.UserDB.User_Mac_Data);
		if(count == 0){
			return null;
		}
		
		WifiUserData data = new WifiUserData();
		List<WifiUserData> allData = SqliteUtil.queryAllItem(UserSqliteManager.getInstance(), data);
		return allData;
	}
	
	
	/**
	 * ���²����û���ͷ��
	 * @param uid
	 * @param newLogo
	 */
	public static void updateWifiUserLogo(String uid, String newLogo){
		WifiUserData data = new WifiUserData();
		data.setMeet_uid(Integer.parseInt(uid));
		List<WifiUserData> d = SqliteUtil.queryTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), data);
		if(d != null && d.size() == 1){
			data = d.get(0);
			data.setHeader_logo(newLogo);
			SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), data);
		}
	}
	
}
