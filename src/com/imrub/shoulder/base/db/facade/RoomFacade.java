package com.imrub.shoulder.base.db.facade;

import java.util.ArrayList;
import java.util.List;

import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.UserSqliteManager;
import com.imrub.shoulder.base.db.table.SqliteUtil;
import com.imrub.shoulder.base.db.table.chat.ChatInfoData;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.chat.INotifyChatRoomListener;
import com.imrub.shoulder.module.chat.INotifyListener;
import com.imrub.shoulder.module.hotpoint.ChatHotpointManager;
import com.imrub.shoulder.module.hotpoint.IMsgCountListener;
import com.imrub.shoulder.module.im.MessageIdUtils;
import com.imrub.shoulder.module.im.msg.MessageFactory;
import com.imrub.shoulder.widget.NotificationFactory;

public class RoomFacade {
	
	static boolean isInit = false;
	static{
		RoomFacade.getInstance().loadDB();
	}
	
	public static final int BUFFER_SIZE = 20;

	private INotifyChatRoomListener mNotifyListener;
	private IMsgCountListener mMsgCountListener;
	private INotifyListener mNotify;
	
	private String mCurrentRoomId;
	private volatile int mTotalMsgCount;
	
	private List<ChatInfoData> mData = new ArrayList<ChatInfoData>();
	private static RoomFacade mInstance;
	private RoomFacade(){
	}

	public static RoomFacade getInstance(){
		if(mInstance == null){
			mInstance = new RoomFacade();
		}
		return mInstance;
	}
	
	public void setChatInfoDataListener(INotifyListener listener){
		mNotify = listener;
	}
	
	/**
	 * 清空某个房间的消息
	 * @param id
	 */
	public void clearMsgCount(String id){
		for(ChatInfoData data : mData){
			if(data.getId().equalsIgnoreCase(id)){
				mTotalMsgCount -= data.getNewMsgCount();
				updateMsgCountInSubThread(mTotalMsgCount);
				data.setNewMsgCount(0);
				SqliteUtil.asyncUpdateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), data);
				if(mNotify != null){
					mNotify.notifyChange();
				}
				break;
			}
		}
	}
	
	public void setMsgUpdateListener(IMsgCountListener listener){
		this.mMsgCountListener = listener;
	}
	
	private void updateMsgCountInSubThread(final int msgCount){
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				if(mMsgCountListener != null){
					mMsgCountListener.onUpdateMsg(msgCount);
				}
			}
		});
	}
	
	public int getTotalMsgCount(){
		return this.mTotalMsgCount;
	}
	
	public String getCurrentChatRoomId(){
		return this.mCurrentRoomId;
	}
	
	public void setCurrentChatRoomId(String id){
		this.mCurrentRoomId = id;
	}
	
	public void setChatRoomListener(INotifyChatRoomListener listener){
		mNotifyListener = listener;
	}
	
	public void clearCacheData(final String id){
		List<Message> datas = MessageFacade.getInstance().getChatRoomMessage(id);
		if(datas != null){
			int size = datas.size();
			if(size > BUFFER_SIZE){
				size = size - BUFFER_SIZE;
				for(int i=0; i<size; i++){
					datas.remove(0);
				}
			}
		}
	}

	/**
	 * 获取房间数量
	 * @return
	 */
	public int getChatRoomCount(){
		return mData.size();
	}

	/**
	 * 获取某个房间的信息
	 * @param index
	 * @return
	 */
	public ChatInfoData getChatRoomData(int index){
		return mData.get(index);
	}

	/**
	 * 设置所有房间的信息
	 * @param data
	 */
	public void setChatRoomData(List<ChatInfoData> data){
		mData.addAll(data);
	}
	
	public void updateChatRoom(String roomId, String jid, Message message){
		ChatInfoData infoData = null;
		for(ChatInfoData data : mData){
			if(data.getId().equalsIgnoreCase(roomId)){
				infoData = data;
				break;
			}
		}

		int contentType = message.getMsgType();
		String msg = MessageFactory.getMessageContent(message);
		
		if(infoData == null){
			String userName = FriendFacade.getInstance().getFriendName(jid);
			infoData = new ChatInfoData(roomId, contentType , 
					userName == null ? jid : userName, msg , 
							Long.parseLong(message.getMessageTime()));
			infoData.setNewMsgCount(1);
			mData.add(0, infoData);
			SqliteUtil.asynInsertItem(UserSqliteManager.getInstance(), infoData);
		} else {
			mData.remove(infoData);
			infoData.setMsgType(contentType);
			infoData.setChatTime(Long.parseLong(message.getMessageTime()));
			infoData.setNewMsgCount(infoData.getNewMsgCount() + 1);
			infoData.setChatContent(msg);
			mData.add(0, infoData);
			SqliteUtil.asyncDeleteAndInsertItem(UserSqliteManager.getInstance(), infoData);
		}
		
		mTotalMsgCount += 1;
		if(mCurrentRoomId != null && mCurrentRoomId.equalsIgnoreCase(roomId)){
			infoData.setNewMsgCount(0);
			mTotalMsgCount -= 1;
			SqliteUtil.asyncDeleteAndInsertItem(UserSqliteManager.getInstance(), infoData);
		}
		
		// update total message count
		updateMsgCountInSubThread(mTotalMsgCount);
		
		if(mNotify != null){
			mNotify.notifyChange();
		}
	}
	
	private void loadChatInfoDataDB(){
		if("".equalsIgnoreCase(UserInfo.getInstance().getUid())){
			return ;
		}
		
		ChatInfoData data = new ChatInfoData();
		List<ChatInfoData> chatInfoData = SqliteUtil.queryTableItemBase(UserSqliteManager.getInstance(), data);

		mData.clear();
		mTotalMsgCount = 0;
		if(chatInfoData != null){
			for(ChatInfoData d : chatInfoData){
				mData.add(0, d);
				mTotalMsgCount += d.getNewMsgCount();
			}
		}
		
		if(mTotalMsgCount != 0){
			ChatHotpointManager.updateChatMsgCount(mTotalMsgCount);
		}
	}
	
	private void loadChatRoomDataDB(){
		if("".equalsIgnoreCase(UserInfo.getInstance().getUid())){
			return ;
		}
		
		if(mData.isEmpty()){
			return ;
		}

		Message data = new Message();
		for(ChatInfoData d : mData){
			data.setId(d.getId());
			List<Message> datas = SqliteUtil.queryTableItemBaseOnValue(UserSqliteManager.getInstance(), data, "time", BUFFER_SIZE);
			if(datas != null){
				MessageFacade.getInstance().putChatRoomMessage(d.getId(), datas);
			}
		}
	}
	
	public void loadDB(){
		synchronized (RoomFacade.class) {
			if(!isInit){
				loadChatInfoDataDB();
				loadChatRoomDataDB();
				isInit = true;
			}
		}
	}
	
	public void notify(String roomId, Message msg){
		// notify if needed
		String id = mCurrentRoomId;
		if(id == null || !id.equalsIgnoreCase(roomId)){
			String jid = MessageIdUtils.getToJid(roomId);
			String userName = FriendFacade.getInstance().getFriendName(jid);
			NotificationFactory.notify(roomId, userName == null ? jid : userName, 
					MessageFactory.getMessageContent(msg));
		}
	}
	
	public void notifyMessge(String id){
		if(mCurrentRoomId != null && mCurrentRoomId.equalsIgnoreCase(id)){
			if(mNotifyListener != null){
				mNotifyListener.notifyChange();
			}
		}
	}
	
	public void updateRoomLogo(String jid, String logo){
		String roomId = MessageIdUtils.createRoomId(jid);
		if(mData != null){
			ChatInfoData target = null;
			for(ChatInfoData d : mData){
				if(d.getId().equalsIgnoreCase(roomId)){
					d.setIconUrl(logo);
					target = d;
					break;
				}
			}
			
			// 更新数据库
			if(target != null){
				SqliteUtil.updateTableItemBaseOnPrimaryKey(UserSqliteManager.getInstance(), target);
			}
		}
	}
	
	
}
