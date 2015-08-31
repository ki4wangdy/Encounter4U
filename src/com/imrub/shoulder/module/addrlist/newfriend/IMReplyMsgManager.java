package com.imrub.shoulder.module.addrlist.newfriend;

import java.util.ArrayList;

import com.imrub.shoulder.base.db.facade.QiuKnowUserFacade;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.base.db.table.im.ReplyMessage;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.hotpoint.AddrHotpointManager;
import com.imrub.shoulder.module.im.IMsgControllerListener;
import com.imrub.shoulder.module.im.client.IMClient;
import com.imrub.shoulder.module.im.msg.MessageFactory;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;

public class IMReplyMsgManager implements IMsgControllerListener{

	private ArrayList<IReplyMessage> mListener = new ArrayList<IReplyMessage>();
	
	private static IMReplyMsgManager mInstance;
	private IMReplyMsgManager(){
	}

	public static final IMReplyMsgManager getInstance(){
		if(mInstance == null){
			mInstance = new IMReplyMsgManager();
		}
		return mInstance;
	}

	public void registerListener(IReplyMessage listener){
		this.mListener.add(listener);
	}
	
	public void unRegisterListener(IReplyMessage listener){
		this.mListener.remove(listener);
	}
	
	@Override
	public void onRecevicedMsg(int msgRoomType, final String jid,final String msg) {
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				onReceivedReplyMessage(jid, msg);
			}
		});
	}
	
	@Override
	public void onDelayedReceivedMsg(int msgRoomType, String stmp, final String jid,
			final String msg) {
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				onReceivedReplyMessage(jid, msg);
			}
		});
	}

	@Override
	public void sendMsg(String jid, String msg) {
		IMClient.getInstance().sendMsg(jid, msg);
	}
	
	private void onReceivedReplyMessage(String jid, String msg){
		Message replyMessage = MessageFactory.parseMessage(msg);
		if(replyMessage == null || replyMessage.getMsgType() != 
				MsgTypeConstant.ReplyNewAddrMsgType){
			return ;
		}
		
		String uid = jid.substring(0,jid.indexOf("@"));
		replyMessage.setUid(uid);
				
		// update the database
		QiuKnowUserFacade.updateAgreeQiuknowUser(jid, (ReplyMessage)replyMessage);
		
		// update the HotPoint
		AddrHotpointManager.showAddressListHotpoint();
		
		// notify the addrList
		for(IReplyMessage listener : mListener){
			listener.onReplyMessageChange();
		}
	}
	
}
