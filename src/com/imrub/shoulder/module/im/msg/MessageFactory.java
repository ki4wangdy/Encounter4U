package com.imrub.shoulder.module.im.msg;

import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.table.im.FriendLogoMessage;
import com.imrub.shoulder.base.db.table.im.FriendMessage;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.base.db.table.im.ReplyMessage;
import com.imrub.shoulder.base.db.table.im.SystemMessage;
import com.imrub.shoulder.base.db.table.im.TextMessage;
import com.imrub.shoulder.module.im.MessageIdUtils;


public class MessageFactory {
	
	public static Message createMessge(int type, String content){
		switch (type) {
			case MsgTypeConstant.TextMsgType:
				return new TextMessage(UserInfo.getInstance().getUid(),
						System.currentTimeMillis()+"", 
						MessageIdUtils.getMessageId(), content);
			case MsgTypeConstant.SoundMsgType:
				break;
			case MsgTypeConstant.ImageMsgType:
				break;
			case MsgTypeConstant.SystemMsgType:
				return new SystemMessage(System.currentTimeMillis()+"",
						MessageIdUtils.getMessageId(), content);
		}
		return null;
	}

	
	public static Message parseMessage(String msg){
		JSONObject obj = JSONObject.parseObject(msg);
		int type = obj.getInteger("p");
		switch (type) {
			case MsgTypeConstant.TextMsgType:
				return new TextMessage().getMessageTypeObject(msg);
			case MsgTypeConstant.ReplyNewAddrMsgType:
				return new ReplyMessage().getMessageTypeObject(msg);
			case MsgTypeConstant.AddFriendMsgType:
				return new FriendMessage().getMessageTypeObject(msg);
			case MsgTypeConstant.UserLogoMsgType:
				return new FriendLogoMessage().getMessageTypeObject(msg);
		}
		return null;
	}
	
	
	public static String getMessageContent(Message msg){
		int type = msg.getMsgType();
		switch (type) {
			case MsgTypeConstant.TextMsgType:
			case MsgTypeConstant.SystemMsgType:
				return msg.getMsg();
		}
		return "";
	}
	
	
}
