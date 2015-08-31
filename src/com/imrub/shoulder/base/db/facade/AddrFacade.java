package com.imrub.shoulder.base.db.facade;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.imrub.shoulder.base.db.DBConstant;
import com.imrub.shoulder.base.db.UserSqliteManager;
import com.imrub.shoulder.base.db.table.SqliteUtil;
import com.imrub.shoulder.base.db.table.addr.AddrlistNewData;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.module.addrlist.more.AddrlistNewConstant;

public class AddrFacade {

	/**
	 * 出谁啊所有好友
	 * @param friends
	 */
	public static void insertAllFriends(List<UserFriend> friends){
		SqliteUtil.deleteTable(UserSqliteManager.getInstance(), DBConstant.UserDB.User_Friend);
		SQLiteDatabase database = UserSqliteManager.getInstance().getWritableDatabase();
		try{
			database.beginTransaction();
			for(UserFriend friend : friends){
				SqliteUtil.insertItem(UserSqliteManager.getInstance(), friend);
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}
	
	public static void insertOrUpdateQiuknowFriend(String mUid, String message,
			String targetName){
		AddrlistNewData data = new AddrlistNewData();
		data.setUid(mUid);
		int count = SqliteUtil.queryTableItemCountBaseOnPrimaryKey(UserSqliteManager.getInstance(), data);
		if(count != 0){
			List<AddrlistNewData> oldDatas = SqliteUtil.queryTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), data);
			if(oldDatas != null){
				AddrlistNewData oldData = oldDatas.get(0);
				oldData.setIsnew(0);
				oldData.setIsNewRequest(0);
				oldData.setSignature(message);
				oldData.setStatus(AddrlistNewConstant.StatusType.Status_NeedCheck);
				SqliteUtil.updateOrInsertBasedOnPrimaryKey(UserSqliteManager.getInstance(), oldData);
			}
		} else {
			data = new AddrlistNewData(mUid, targetName, "", message, 
					AddrlistNewConstant.StatusType.Status_NeedCheck, System.currentTimeMillis()+"", 0, 0);
			SqliteUtil.updateOrInsertBasedOnPrimaryKey(UserSqliteManager.getInstance(), data);
		}
	}
	
}
