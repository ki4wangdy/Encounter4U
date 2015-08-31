package com.imrub.shoulder.module.im.client;

import com.imrub.shoulder.base.db.facade.MessageFacade;
import com.imrub.shoulder.base.db.facade.RoomFacade;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.module.im.IMsgControllerListener;
import com.imrub.shoulder.module.im.MessageIdUtils;
import com.imrub.shoulder.module.im.msg.MessageFactory;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;

public class IMDataManager implements IMsgControllerListener{

	private Message message;
	private String mJid;
	
	private static IMDataManager mInstance;
	private IMDataManager(){
	}

	public static final IMDataManager getInstance(){
		if(mInstance == null){
			mInstance = new IMDataManager();
		}
		return mInstance;
	}
	
	@Override
	public void onDelayedReceivedMsg(int msgRoomType, String stmp, String jid,
			String msg) {
		// check the Message type
		if(!isMessageType(msgRoomType, stmp, jid, msg)){
			return ;
		}
		handleMessage();
	}
	
	@Override
	public void onRecevicedMsg(int msgRoomType, String jid, String msg) {
		if(!isMessageType(msgRoomType, "", jid, msg)){
			return ;
		}
		handleMessage();
		
		String roomId = MessageIdUtils.createRoomId(mJid);
		RoomFacade.getInstance().notify(roomId,message);
	}
	
	@Override
	public void sendMsg(String jid, String msg) {
		IMClient.getInstance().sendMsg(jid+"@cajian.cc", msg);
	}
	
	private void handleMessage(){
		// set userIcon userName and UID
		String msgId = MessageIdUtils.getMessageId();
		message.setId(msgId);
		message.setUid(mJid);
		
		String roomId = MessageIdUtils.createRoomId(mJid);
		
		// update ChatRoomData
		MessageFacade.getInstance().putChatRoomMessage(roomId, message);
		
		// update ChatInfoData
		RoomFacade.getInstance().updateChatRoom(roomId, mJid, message);
		
	}
	
	private boolean isMessageType(int msgRoomType, String stmp, String jid,
			String msg){
		// check the Message type
		message = MessageFactory.parseMessage(msg);
		if(message == null || message.getMsgType() != 
				MsgTypeConstant.TextMsgType){
			return false;
		}
		
		mJid = jid.substring(0, jid.indexOf("@cajian.cc"));
		return true;
	}

}
