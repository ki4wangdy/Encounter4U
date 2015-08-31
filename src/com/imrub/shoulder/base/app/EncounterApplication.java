package com.imrub.shoulder.base.app;

import android.app.Application;
import android.content.IntentFilter;

import com.imrub.shoulder.base.db.DBModule;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.base.util.log.SdcardLogger;
import com.imrub.shoulder.module.CrashHandler;
import com.imrub.shoulder.module.im.IMModule;
import com.imrub.shoulder.module.im.client.ConnectedBroadcast;
import com.imrub.shoulder.module.im.client.MessageBroadcast;
import com.imrub.shoulder.module.im.server.NetworkBroadcast;

public class EncounterApplication extends Application{

	public static final String Tag = "EncounterApplication";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		AppModule.initialize(getApplicationContext());
		registerBroadcast();
		
		DBModule.initialize();
		IMModule.initialize();
		CrashHandler.initCrashHandler();
		
		Logger.setLoggerProxy(new SdcardLogger());
		
	}

	private void registerBroadcast(){
		IntentFilter intent = new IntentFilter();
		intent.addAction(ConnectedBroadcast.Action);
		registerReceiver(new ConnectedBroadcast(), intent);
		
		intent = new IntentFilter();
		intent.addAction(NetworkBroadcast.ConAction);
		registerReceiver(new NetworkBroadcast(), intent);
		
		intent = new IntentFilter();
		intent.addAction(MessageBroadcast.Action);
		registerReceiver(new MessageBroadcast(), intent);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Logger.print(Tag, "Application will be kill, because of onLowMemory!");
	}
	
}
