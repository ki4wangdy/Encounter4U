package com.imrub.shoulder.module.ping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.imrub.shoulder.module.im.server.IMService;

public class PingService extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		IMService.getInstance().sendPing("cajian.cc");
	}
}
