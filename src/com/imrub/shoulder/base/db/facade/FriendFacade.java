package com.imrub.shoulder.base.db.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.UserSqliteManager;
import com.imrub.shoulder.base.db.table.SqliteUtil;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.module.addrlist.FriendComparator;
import com.imrub.shoulder.module.addrlist.IFriendChange;
import com.imrub.shoulder.module.addrlist.IFriendLogo;

public class FriendFacade {

	private ArrayList<IFriendLogo> mLogoListener = new ArrayList<IFriendLogo>();
	
	private IFriendChange mListener;
	private List<UserFriend> friends;
	
	private static FriendFacade mInstance;
	private FriendFacade(){
	}

	public static FriendFacade getInstance(){
		if(mInstance == null){
			mInstance = new FriendFacade();
		}
		return mInstance;
	}
	
	public void loadDBFriend(){
		if("".equalsIgnoreCase(UserInfo.getInstance().getUid())){
			return ;
		}
		
		UserFriend friend = new UserFriend();
		friends = SqliteUtil.queryAllItem(UserSqliteManager.getInstance(), friend);
		sortData();
		
		if(friends == null){
			friends = new ArrayList<UserFriend>();
		}
	}

	private void sortData(){
		if(friends != null){
			Collections.sort(friends, new FriendComparator());
		}
	}
	
	public void addListener(IFriendChange listener){
		mListener = listener;
	}
	
	public List<UserFriend> getFriends(){
		if(friends == null){
			loadDBFriend();
		}
		return friends;
	}
	
	public void addFriend(UserFriend user){
		friends.add(user);
		sortData();
		
		SqliteUtil.insertItem(UserSqliteManager.getInstance(), user);
		if(mListener != null){
			mListener.onFriendChange(user);
		}
	}
	
	/**
	 * ��ȡ��������
	 * @param uid
	 * @return
	 */
	public String getFriendName(String uid){
		if(friends == null){
			loadDBFriend();
		}
		for(UserFriend f : friends){
			if(f.getUid().equalsIgnoreCase(uid)){
				return f.getNick_name();
			}
		}
		return null;
	}
	
	public String getFriendLogo(String uid){
		if(friends == null){
			loadDBFriend();
		}
		for(UserFriend f : friends){
			if(f.getUid().equalsIgnoreCase(uid)){
				return f.getHeader_logo();
			}
		}
		return "";
	}
	
	public UserFriend getUserFriend(String uid){
		if(friends == null){
			loadDBFriend();
		}
		for(UserFriend f : friends){
			if(f.getUid().equalsIgnoreCase(uid)){
				return f;
			}
		}
		return null;
	}
	
	public void registerLogoChangeListener(IFriendLogo listener){
		mLogoListener.add(listener);
	}
	
	public void unRegisterLogoChangeListener(IFriendLogo listener){
		mLogoListener.remove(listener);
	}

	public void onLogoChangeFire(String uid, String newLogo){
		
		// 1.���º��е�ͷ��
		updateUserFriendLogo(uid, newLogo);
		
		// 2.��������ʶ��ͷ��
		QiuKnowUserFacade.updateQiuknowUserLogo(uid, newLogo);
		
		// 3.�������������ͷ��
		RoomFacade.getInstance().updateRoomLogo(uid, newLogo);

		// 4.���²�����ѵ�ͷ��
		WifiFacade.updateWifiUserLogo(uid, newLogo);
		
		for(IFriendLogo listener : mLogoListener){
			listener.onLogoChange(uid, newLogo);
		}
		
	}
	
	private void updateUserFriendLogo(String uid, String newLogo){
		UserFriend friend = FriendFacade.getInstance().getUserFriend(uid);
		if(friend == null){
			return ;
		}
		
		// ���º����ڴ��ͷ��
		friend.setHeader_logo(newLogo);
		
		// �������ݿ����ͷ��
		SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), friend);
		
	}
	
}
