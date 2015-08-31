package com.imrub.shoulder.base.db.facade;

import java.util.ArrayList;
import java.util.List;

import com.imrub.shoulder.base.db.UserSqliteManager;
import com.imrub.shoulder.base.db.table.SqliteUtil;
import com.imrub.shoulder.base.db.table.addr.AddrlistNewData;
import com.imrub.shoulder.base.db.table.im.FriendMessage;
import com.imrub.shoulder.base.db.table.im.ReplyMessage;
import com.imrub.shoulder.module.addrlist.more.AddrlistNewConstant;
import com.imrub.shoulder.module.addrlist.more.AddrlistUtils;

public class QiuKnowUserFacade {

	/**
	 * 查询某个求认识用户
	 * @param uid
	 * @return
	 */
	public static AddrlistNewData queryQiukonwUserInfo(String uid){
		UserDetailFacade.queryUserDetailInfo(uid);
		AddrlistNewData data = new AddrlistNewData();
		data.setUid(uid);
		List<AddrlistNewData> item = SqliteUtil.queryTableItemBaseOnPrimaryKey(
				UserSqliteManager.getInstance(), data);
		return item == null ? null : item.get(0);
	}
	
	/**
	 * 查询数据库里所有的求认识用户
	 * @return
	 */
	public static List<AddrlistNewData> queryAllQiuknowUserInfo(){
		AddrlistNewData data = new AddrlistNewData();
		List<AddrlistNewData> dbDatas = SqliteUtil.queryTableItemBase(
				UserSqliteManager.getInstance(), data);
		if (dbDatas == null) {
			dbDatas = new ArrayList<AddrlistNewData>();
		}
		return dbDatas;
	}
	
	/**
	 * 更新某个求认识用户信息
	 * @param data
	 */
	public static void updateQiukonwUserInfo(AddrlistNewData data){
		SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), data);
	}

	public static List<AddrlistNewData> queryAllNewQiuknowRequest(){
		AddrlistNewData data = new AddrlistNewData();
		List<AddrlistNewData> newData = SqliteUtil.queryTableItemBaseOnValue(UserSqliteManager.
				getInstance(), data, "isNewRequest", "1");
		return newData;
	}
	
	public static void updateAgreeQiuknowUser(String jid,ReplyMessage replyMsg){
		AddrlistNewData datas = AddrlistUtils.parseToObject(jid, replyMsg.getMessageJsonString());
		datas.setIsnew(1);
		datas.setIsNewRequest(1);
		datas.setUid(replyMsg.getUid());
		List<AddrlistNewData> oldData = SqliteUtil.queryTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), datas);
		if(oldData != null && !oldData.isEmpty()){
			AddrlistNewData oldD = oldData.get(0);
			oldD.copyFromReply(datas);
			SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), oldD);
		} else {
			// judge the relation and set the status
			datas.setStatus(AddrlistNewConstant.StatusType.Status_ToAgree);
			// insert the database item
			SqliteUtil.insertItem(UserSqliteManager.getInstance(), datas);
		}
	}
	
	public static void updateAgreeNotNewQiuknowUser(String jid, FriendMessage replyMsg){
		AddrlistNewData datas = AddrlistUtils.parseToObject(jid, replyMsg.getMessageJsonString());
		datas.setIsnew(0);
		datas.setIsNewRequest(0);
		datas.setUid(replyMsg.getUid());
		
		// judge the relation and set the status
		datas.setStatus(AddrlistNewConstant.StatusType.Status_HasAgree);
		
		// update the database 
		List<AddrlistNewData> oldData = SqliteUtil.queryTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), datas);
		if(oldData == null || oldData.isEmpty()){
			SqliteUtil.insertItem(UserSqliteManager.getInstance(), datas);
		} else {
			AddrlistNewData old = oldData.get(0);
			old.copy(datas);
			SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(),old);
		}
	}
	
	public static void updateQiuknowUserLogo(String jid, String url){
		AddrlistNewData data = new AddrlistNewData();
		data.setUid(jid);
		List<AddrlistNewData> d = SqliteUtil.queryTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), data);
		if(d != null && d.size() == 1){
			data = d.get(0);
			data.setIconUrl(url);
			SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), data);
		}
	}
	
	
}


