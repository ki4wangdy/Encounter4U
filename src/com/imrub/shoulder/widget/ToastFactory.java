package com.imrub.shoulder.widget;

import android.content.Context;
import android.widget.Toast;

public class ToastFactory {

	public static void showMsg(final Context context, int msgId){
		Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show();
	}

	public static void showMsgForShortTime(final Context context, int msgId){
		Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show();
	}

	
}
