package com.imrub.shoulder.base.db.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.imrub.shoulder.base.db.UserSqliteManager;
import com.imrub.shoulder.base.db.table.SqliteUtil;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.module.im.MessageIdUtils;
import com.imrub.shoulder.module.im.client.IMDataManager;
import com.imrub.shoulder.module.im.msg.MessageFactory;

public class MessageFacade {

	public static final int TIME_INTERNAL = 60 * 1000 * 5;
	
	//�������з������Ϣ
	private HashMap<String, List<Message>> mChatRoom = new HashMap<String, List<Message>>();
	
	private static MessageFacade mInstance;
	private MessageFacade(){
	}
	
	public static MessageFacade getInstance(){
		if(mInstance == null){
			mInstance = new MessageFacade();
		}
		return mInstance;
	}
	
	/**
	 * ��ѯ�����count����¼
	 * @param time
	 * @param count
	 * @return
	 */
	public static List<Message> queryMessageCountBaseonTime(String roomId, Message msg, int count){
		List<Message> datas = SqliteUtil.queryTableItemBaseOnValue(UserSqliteManager.getInstance(), 
				(Message)msg, roomId, "time", msg.getMessageTime(), count);
		return datas;
	}
	
	
	/**
	 * ��ʼ��message��
	 * @param id
	 */
	public static void initMessageTable(String id){
		Message.onCreateDB(UserSqliteManager.getInstance().getWritableDatabase(), id);
	}

	/**
	 * ��ȡĳ���������Ϣ��
	 * @param id
	 * @return
	 */
	public int getChatRoomMessageCount(String id){
		List<Message> data = mChatRoom.get(id);
		return data == null ? 0 : data.size();
	}

	/**
	 * ��ȡ�����index��Ϣ
	 * @param id
	 * @param index
	 * @return
	 */
	public Message getChatRoomMessage(String id, int index){
		List<Message> data = mChatRoom.get(id);
		return data == null ? null : data.get(index);
	}

	/**
	 * ����ĳ�������������Ϣ
	 * @param id
	 * @param data
	 */
	public void putChatRoomMessages(String id, List<Message> data){
		List<Message> cachData = mChatRoom.get(id);
		if(cachData == null){
			mChatRoom.put(id, data);
		} else {
			cachData.addAll(data);
		}
	}
	
	/**
	 * ���ĳ���������Ϣ
	 * @param id
	 * @param datas
	 */
	public void putChatRoomMessage(String id, List<Message> datas){
		List<Message> cache = mChatRoom.get(id);
		if(cache == null){
			mChatRoom.put(id, datas);
			return ;
		}
		cache.addAll(0, datas);
	}

	/**
	 * ���ظ÷������е���Ϣ
	 * @param id
	 * @return
	 */
	public List<Message> getChatRoomMessage(String id){
		List<Message> messages = mChatRoom.get(id);
		return messages;
	}
	
	/**
	 * ���ĳ��������Ϣ
	 * @param id
	 * @param content
	 * @param type
	 */
	public void putChatRoomMessage(String id, String content, int type){
		
		String roomId = MessageIdUtils.createRoomId(id);
		String toJid = MessageIdUtils.getToJid(id);
		
		// update chatInfo Data
		Message msg = MessageFactory.createMessge(type, content);
		
		// update chat Room message
		MessageFacade.getInstance().putChatRoomMessage(roomId, msg);
		
		// update the chat room
		RoomFacade.getInstance().updateChatRoom(roomId, toJid, msg);
		
		// send the data to remote server
		IMDataManager.getInstance().sendMsg(toJid, msg.getMessageJsonString());
	}

	public void putChatRoomMessage(String roomId, Message msg){
		List<Message> data = mChatRoom.get(roomId);
		if(data == null){
			// ��һ�� �����ñ�
			MessageFacade.initMessageTable(roomId);
			List<Message> d = new ArrayList<Message>();
			msg.setShowTime(1);
			d.add(msg);
			mChatRoom.put(roomId, d);
		} else {
			// judge the time whether or not show
			Message d = data.get(data.size()-1);
			if (Long.parseLong(msg.getMessageTime()) - 
					Long.parseLong(d.getMessageTime()) > TIME_INTERNAL) {
				msg.setShowTime(1);
			}
			data.add(msg);
		}
		
		// insert this item to id's table
		SqliteUtil.insertItemForMessage(UserSqliteManager.getInstance(), roomId, msg);
		RoomFacade.getInstance().notifyMessge(roomId);
		
	}
	
}
