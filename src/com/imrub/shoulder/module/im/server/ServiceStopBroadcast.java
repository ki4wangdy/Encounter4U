package com.imrub.shoulder.module.im.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceStopBroadcast extends BroadcastReceiver{

	public static final String StopAction = "com.imrub.shoulder.ServerStop";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		final String actionType = intent.getAction();
		if(actionType.equalsIgnoreCase(StopAction)){
			Intent imServerIntent = new Intent(context, IMServer.class);
			context.startService(imServerIntent);
		}
	}
	
}
