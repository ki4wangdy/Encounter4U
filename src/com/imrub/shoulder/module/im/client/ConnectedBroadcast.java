package com.imrub.shoulder.module.im.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectedBroadcast extends BroadcastReceiver{

	public static final String Action = "com.imrub.shoulder.im.client.Connection";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent != null){
			boolean isConnected = intent.getBooleanExtra("isconnected", true);
			if(isConnected){
				IMClient.getInstance().onConnected();
			} else {
				IMClient.getInstance().onDisconnected();
			}
		}
	}
	
}
