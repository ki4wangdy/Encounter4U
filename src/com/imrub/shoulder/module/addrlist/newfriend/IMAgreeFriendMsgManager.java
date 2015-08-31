package com.imrub.shoulder.module.addrlist.newfriend;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.db.facade.FriendFacade;
import com.imrub.shoulder.base.db.facade.QiuKnowUserFacade;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.base.db.table.im.FriendMessage;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.module.im.IMsgControllerListener;
import com.imrub.shoulder.module.im.MessageIdUtils;
import com.imrub.shoulder.module.im.client.IMClient;
import com.imrub.shoulder.module.im.client.IMDataManager;
import com.imrub.shoulder.module.im.msg.MessageFactory;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;
import com.imrub.shoulder.widget.NotificationFactory;

public class IMAgreeFriendMsgManager implements IMsgControllerListener{

	private static IMAgreeFriendMsgManager mInstance;
	private IMAgreeFriendMsgManager(){
	}
	
	public static IMAgreeFriendMsgManager getInstance(){
		if(mInstance == null){
			mInstance = new IMAgreeFriendMsgManager();
		}
		return mInstance;
	}
	
	@Override
	public void onDelayedReceivedMsg(int msgRoomType, String stmp, String jid,
			String msg) {
		onReceivedAgreeFriendMessage(jid, msg);
	}

	@Override
	public void onRecevicedMsg(int msgRoomType, String jid, String msg) {
		onReceivedAgreeFriendMessage(jid, msg);
	}

	@Override
	public void sendMsg(String jid, String msg) {
		IMClient.getInstance().sendMsg(jid, msg);
	}
	
	private void onReceivedAgreeFriendMessage(String jid, String msg){
		Message friendMessage = MessageFactory.parseMessage(msg);
		if(friendMessage == null || friendMessage.getMsgType() != 
				MsgTypeConstant.AddFriendMsgType){
			return ;
		}

		jid = jid.substring(0, jid.indexOf("@cajian.cc"));
		friendMessage.setUid(jid);
		
		// 1.update the database
		FriendMessage friendMsg = (FriendMessage)friendMessage;
		updateDb(jid, friendMsg);
		
		// 2.insert into UserFriend
		UserFriend frined = new UserFriend(jid, friendMsg.getUserName(),
				friendMsg.getUserIcon(), "0");
		FriendFacade.getInstance().addFriend(frined);
		
		// 3.notify the message
		// 1) insert one message
		Message agreeMsg = MessageFactory.createMessge(MsgTypeConstant.TextMsgType, AppContext.getString(R.string.msg_agree_to_friend_text));
		IMDataManager.getInstance().onRecevicedMsg(0, jid+"@cajian.cc", agreeMsg.getMessageJsonString());
		
		// 2) notify the bar
		String roomId = MessageIdUtils.createRoomId(jid);
		NotificationFactory.notify(roomId, friendMsg.getUserName(), agreeMsg.getMsg());
		
	}
	
	private void updateDb(String jid, FriendMessage replyMsg){
		QiuKnowUserFacade.updateAgreeNotNewQiuknowUser(jid, replyMsg);
	}
		
}
