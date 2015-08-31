package com.imrub.shoulder.base.db.table.im;

import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;

public class TextMessage extends Message {

	public TextMessage(){
	}
	
	public TextMessage(String uid, String time, String id, String content){
		setUid(uid);
		setTime(time);
		setId(id);
		setMsg(content);
		setMsgType(MsgTypeConstant.TextMsgType);
	}
	
	/**
	 * {"p":1,"t":"2343223","m":"hello world"}
	 */
	@Override
	public Message getMessageTypeObject(String msg) {
		JSONObject obj = JSONObject.parseObject(msg);
		
		int type = obj.getInteger("p");
		setMsgType(type);
		
		String time = obj.getString("t");
		setTime(time);
		
		String msgContent = obj.getString("m");
		setMsg(msgContent);
		
		return this;
	}
	
	/**
	 * {"p":1,"t":"2343223","m":"hello world"}
	 */
	@Override
	public String getMessageJsonString() {
		JSONObject obj = new JSONObject();
		obj.put("p", MsgTypeConstant.TextMsgType);
		obj.put("t", getTime());
		obj.put("m", getMsg());
		return obj.toJSONString();
	}
	
}
