package com.imrub.shoulder.base.db.table.im;

import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;

public class FriendMessage extends Message{

	private String signature;
	private String userName;
	private String userIcon;

	public FriendMessage(){
	}
	
	public FriendMessage(String signature, String userName, String userIcon){
		this.signature = signature;
		this.userName = userName;
		this.userIcon = userIcon;
	}
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	
	/**
	 * {"p":2,"t":"342423","s":"ÄãºÃ°¡","n":"luffy11","c":"http://logo.ki.com"}
	 */
	@Override
	public Message getMessageTypeObject(String msg) {
		JSONObject obj = JSONObject.parseObject(msg);
		
		int type = obj.getInteger("p");
		setMsgType(type);
		
		String time = obj.getString("t");
		setTime(time);

		signature = obj.getString("s");

		String name = obj.getString("n");
		setUserName(name);
		
		String urlImage = obj.getString("c");
		setUserIcon(urlImage);
		
		return this;
	}
	
	@Override
	public String getMessageJsonString() {
		JSONObject obj = new JSONObject();
		obj.put("p", MsgTypeConstant.AddFriendMsgType);
		String time = getMessageTime();
		if(time == null || "".equalsIgnoreCase(time)){
			obj.put("t", System.currentTimeMillis()+"");
		} else {
			obj.put("t", time);
		}
		obj.put("s", signature);
		obj.put("n", getUserName());
		obj.put("c", getUserIcon());
		return obj.toJSONString();
	}
	
}
