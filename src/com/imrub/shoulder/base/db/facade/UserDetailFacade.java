package com.imrub.shoulder.base.db.facade;

import java.util.List;

import com.imrub.shoulder.base.db.UserSqliteManager;
import com.imrub.shoulder.base.db.table.SqliteUtil;
import com.imrub.shoulder.base.db.table.cajian.UserDetailInfoData;

public class UserDetailFacade {

	/**
	 * 查询用户信息
	 * @param uid
	 * @return
	 */
	public static UserDetailInfoData queryUserDetailInfo(String uid){
		UserDetailInfoData data = new UserDetailInfoData();
		data.setUid(Integer.parseInt(uid));
		List<UserDetailInfoData> info = SqliteUtil.queryTableItemBaseOnPrimaryKey(
				UserSqliteManager.getInstance(), data);
		return info == null ? null : info.get(0);
	}
	
	/**
	 * 更新用户详细信息
	 * @param data
	 */
	public static void updateUserDetainInfo(UserDetailInfoData data){
		SqliteUtil.updateOrInsertBasedOnPrimaryKey(UserSqliteManager.getInstance(), data);
	}
	
}
