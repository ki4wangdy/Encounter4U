
package com.imrub.shoulder.module.im;

interface IMCallBack{
	void onDelayReceivedMsg(int msgRoomType, String stmp, String jid,
			String msg);
	void onReceivedMsg(int msgRoomType, String jid, String msg);
}