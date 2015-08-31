package com.imrub.shoulder.module.cajian.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.imrub.shoulder.base.thread.ThreadFacade;

public class WifiConnectReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(final Context context, final Intent intent) {
		ThreadFacade.runOnWifiUploadThread(new Runnable() {
			@Override
			public void run() {
				subThreadOnReceive(context, intent);
			}
		});
		
	}
	
	private void subThreadOnReceive(final Context context, Intent intent){
		String action = intent.getAction();
		if(action != null && action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)){
			ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if(info != null && (info.isAvailable() || info.isConnected())){
				WifiConnectUtils.startScanWifi(context);
			}
		} else if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equalsIgnoreCase(action)){
			WifiConnectUtils.printAllNetworksInfo(context);
		} else if(WifiManager.WIFI_STATE_CHANGED_ACTION.equalsIgnoreCase(action)){
			int wifiState = intent.getExtras().getInt(WifiManager.EXTRA_WIFI_STATE);
			if(wifiState == WifiManager.WIFI_STATE_DISABLED
					|| wifiState == WifiManager.WIFI_STATE_DISABLED){
				WifiConnectUtils.startScanWifi(context);
			}
		} else {
			WifiConnectUtils.startScanWifi(context);
		}
	}
	
}
