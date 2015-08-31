package com.imrub.shoulder.module.im.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageBroadcast extends BroadcastReceiver{

	public static final String Action = "com.imrub.shoulder.im.client.Message";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent != null){
			String stmp = intent.getStringExtra("stmp");
			int msgRoomType = intent.getIntExtra("msgRoomType", 0);
			String jid = intent.getStringExtra("jid");
			String msg = intent.getStringExtra("msg");
			if(stmp != null && !"".equalsIgnoreCase(stmp)){
				IMClient.getInstance().onDelayMsgReceived(msgRoomType, stmp, jid, msg);
				return ;
			}
			IMClient.getInstance().onMsgReceived(msgRoomType,jid, msg);
		}
	}
	
}
