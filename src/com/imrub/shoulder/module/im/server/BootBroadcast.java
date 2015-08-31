package com.imrub.shoulder.module.im.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.imrub.shoulder.base.app.store.UserInfo;

public class BootBroadcast extends BroadcastReceiver{

	public static final String Start_Boot = "android.intent.action.BOOT_COMPLETED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		final String actionType = intent.getAction();
		if(Start_Boot.equalsIgnoreCase(actionType)){
			// start the server and login if the network is available
			Intent imServerIntent = new Intent(context, IMServer.class);
			
			String uid = UserInfo.getInstance().getUid();
			String server = UserInfo.getInstance().getImServer();
			
			if("".equalsIgnoreCase(uid) || "".equalsIgnoreCase(server)){
				return ;
			}

			intent.putExtra("uid", uid);
			intent.putExtra("server", server);
			
			context.startService(imServerIntent);
		}
	}
	
}
