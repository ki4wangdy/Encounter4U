package com.imrub.shoulder.module.im.server;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.ping.PingService;

public class AlartUtil {

	// 2 minute
	public static final long Time_Ping_Internal = 2 * 60 * 1000;
	
	private static PendingIntent mPendingIntent = null;
	
	public static void alartBroadcastTask(final Context context, String action, long sleepTime){
		Intent intent = new Intent();
		intent.setAction(action);
		
		if (android.os.Build.VERSION.SDK_INT >= 12) {
		    intent.setFlags(32);
		}
		
		AlarmManager timeManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		timeManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime() + sleepTime, 
				PendingIntent.getBroadcast(context, 0, intent, 32));
	}
	
	public static void stopPingAlarm() {
		if(mPendingIntent != null){
			AlarmManager alarmManager = (AlarmManager) AppContext.getAppContext().getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(mPendingIntent);
			mPendingIntent = null;
		}
	}

	public static void startPingAlarm() {
		AlarmManager am = (AlarmManager) AppContext.getAppContext().getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(AppContext.getAppContext(), PingService.class);
		mPendingIntent = PendingIntent.getBroadcast(AppContext.getAppContext(), 0, intent, 0);
		am.setRepeating(AlarmManager.RTC, java.lang.System.currentTimeMillis() + Time_Ping_Internal , Time_Ping_Internal, mPendingIntent);
	}	
}
