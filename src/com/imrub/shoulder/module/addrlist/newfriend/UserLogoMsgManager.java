package com.imrub.shoulder.module.addrlist.newfriend;

import com.imrub.shoulder.base.db.facade.FriendFacade;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.base.db.table.im.FriendLogoMessage;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.im.IMsgControllerListener;
import com.imrub.shoulder.module.im.client.IMDataManager;
import com.imrub.shoulder.module.im.msg.MessageFactory;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;

public class UserLogoMsgManager implements IMsgControllerListener{

	private static UserLogoMsgManager mInstance;
	private UserLogoMsgManager(){
	}

	public static UserLogoMsgManager getInstance(){
		if(mInstance == null){
			mInstance = new UserLogoMsgManager();
		}
		return mInstance;
	}
	
	
	@Override
	public void onRecevicedMsg(int msgRoomType, final String jid,final String msg) {
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				onReceivedUserLogoChange(jid, msg);
			}
		});
	}
	
	@Override
	public void onDelayedReceivedMsg(int msgRoomType, String stmp, final String jid,
			final String msg) {
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				onReceivedUserLogoChange(jid, msg);
			}
		});
	}

	@Override
	public void sendMsg(String jid, String msg) {
	}
	
	public void notifyAllUsers(String newLogo){
		FriendLogoMessage msg = new FriendLogoMessage();
		msg.setNewLogo(newLogo);
		for(UserFriend f : FriendFacade.getInstance().getFriends()){
			String uid = f.getUid();
			msg.setUid(uid);
			IMDataManager.getInstance().sendMsg(f.getUid(), msg.getMessageJsonString());
		}
	}
	
	private void onReceivedUserLogoChange(String jid, String msg){
		Message userLogoMessage = MessageFactory.parseMessage(msg);
		if(userLogoMessage == null || userLogoMessage.getMsgType() != 
				MsgTypeConstant.UserLogoMsgType){
			return ;
		}
		FriendLogoMessage logoMsg = (FriendLogoMessage)userLogoMessage;
		String uid = logoMsg.getUserId();
		String logo = logoMsg.getNewLogo();
		FriendFacade.getInstance().onLogoChangeFire(uid, logo);
	}
	
}
