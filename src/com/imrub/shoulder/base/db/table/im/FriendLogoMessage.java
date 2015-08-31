package com.imrub.shoulder.base.db.table.im;

import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;

public class FriendLogoMessage extends Message{

	private String userId;
	private String newLogo;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNewLogo() {
		return newLogo;
	}

	public void setNewLogo(String newLogo) {
		this.newLogo = newLogo;
	}

	public FriendLogoMessage(){
	}
	
	public FriendLogoMessage(String uid, String logo){
		this.userId = uid;
		this.newLogo = logo;
	}

	/**
	 * {"p":4, "u":10563,"l":"http://www.logo.com/ki.png"}
	 */
	@Override
	public Message getMessageTypeObject(String msg) {
		JSONObject obj = JSONObject.parseObject(msg);
		
		int type = obj.getInteger("p");
		setMsgType(type);
		
		String uid = obj.getString("u");
		setUserId(uid);
		
		String logo = obj.getString("l");
		setNewLogo(logo);

		return this;
	}
	
	@Override
	public String getMessageJsonString() {
		JSONObject obj = new JSONObject();
		obj.put("p", MsgTypeConstant.UserLogoMsgType);
		obj.put("u", UserInfo.getInstance().getUid());
		obj.put("l", UserInfo.getInstance().getUserLogo());
		return obj.toJSONString();
	}
	
	
}
