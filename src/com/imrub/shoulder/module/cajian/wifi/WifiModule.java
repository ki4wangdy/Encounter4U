package com.imrub.shoulder.module.cajian.wifi;

import android.content.Intent;

import com.imrub.shoulder.base.app.AppContext;

public class WifiModule {

	public static void startWifiService(){
		Intent intent = new Intent(AppContext.getAppContext(), WifiService.class);
		intent.putExtra("initwifimacaddress", true);
		AppContext.getAppContext().startService(intent);
	}
	
}
