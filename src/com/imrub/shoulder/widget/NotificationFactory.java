package com.imrub.shoulder.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.im.MessageIdUtils;
import com.imrub.shoulder.module.main.MainUIActivity;

public class NotificationFactory {

	public static final String CHAT_ROOM_ID 	= "chat_room_id";
	public static final String CHAT_ROOM_NAME	= "chat_room_name";
	
	@SuppressWarnings("deprecation")
	public static void notify(String roomId, String chatRoomName, String msg){
		NotificationManager nm = (NotificationManager)AppContext.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);               
		Notification n = new Notification(R.drawable.ic_launcher, chatRoomName + ":" + msg, System.currentTimeMillis());             
		n.flags = Notification.FLAG_AUTO_CANCEL;                
		Intent i = new Intent(AppContext.getAppContext(), MainUIActivity.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		i.putExtra(CHAT_ROOM_ID, MessageIdUtils.createRoomId(roomId));
		i.putExtra(CHAT_ROOM_NAME, chatRoomName);
		
		//PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(
		        AppContext.getAppContext(), 
		        R.string.app_name, 
		        i, 
		        PendingIntent.FLAG_UPDATE_CURRENT);
		n.defaults = Notification.DEFAULT_SOUND;
		n.setLatestEventInfo(
				AppContext.getAppContext(),
		        chatRoomName, 
		        msg, 
		        contentIntent);
		
		String jid = MessageIdUtils.getToJid(roomId);
		nm.notify(Integer.parseInt(jid), n);
	}
	
	public static void cancel(String roomId){
		NotificationManager nm = (NotificationManager)AppContext.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
		String jid = MessageIdUtils.getToJid(roomId);
		nm.cancel(Integer.parseInt(jid));
	}
	
}
