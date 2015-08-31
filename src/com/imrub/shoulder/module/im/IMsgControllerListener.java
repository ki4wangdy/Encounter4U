package com.imrub.shoulder.module.im;

public interface IMsgControllerListener {
	public void sendMsg(String jid, String msg);
	public void onRecevicedMsg(int msgRoomType, String jid, String msg);
	public void onDelayedReceivedMsg(int msgRoomType, String stmp, String jid, String msg);
}
