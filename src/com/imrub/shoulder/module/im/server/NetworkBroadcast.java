package com.imrub.shoulder.module.im.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.imrub.shoulder.module.im.client.IMClient;

public class NetworkBroadcast extends BroadcastReceiver{

	public static final String ConAction = "android.net.conn.CONNECTIVITY_CHANGE";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
		if(ConAction.equals(action)){
			ConnectivityManager connectivityManager = (ConnectivityManager) context.
					getSystemService(Context.CONNECTIVITY_SERVICE);   
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();   
            if(activeNetInfo != null &&activeNetInfo.isAvailable() && activeNetInfo.isConnected()){
    			Intent imServerIntent = new Intent(context, IMServer.class);
    			context.startService(imServerIntent);
    			// IMClient to login
    			IMClient.getInstance().login();
            }
		}
	}
	
//	���������ɣ�
//	1.��װӦ�ú�����Ҫ����һ�Ρ�
//	2.���ǩ���󣬲�������eclipse��װapk�ļ����ֶ���װ�ú�ҲҪ����һ�Ρ�
//	3.������£�
//	 <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
//	 <uses-permission android:name="android.permission.RESTART_PACKAGES" />
//	4.������£�
//	<receiver android:name=".BootBroadcastReceiver" >
//	            <intent-filter>
//	                <action android:name="android.intent.action.BOOT_COMPLETED" />
//
//	                <category android:name="android.intent.category.HOME" />
//	            </intent-filter>
//	            <intent-filter>
//	                <action android:name="android.intent.action.PACKAGE_ADDED" />
//	                <action android:name="android.intent.action.PACKAGE_REMOVED" />
//	                <action android:name="android.intent.action.PACKAGE_REPLACED" />
//
//	                <data android:scheme="package" />
//	            </intent-filter>
//	        </receiver>
//
//	5.���벿�֣�
//	public class BootBroadcastReceiver extends BroadcastReceiver
//	{
//	@Override
//	public void onReceive(Context context, Intent intent)
//	{
//	//���չ㲥��ϵͳ������ɺ����г���
//	if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
//	{
//	Intent ootStartIntent = new Intent(context, Login_Activity.class);
//	ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	context.startActivity(ootStartIntent);
//	}
//	//���չ㲥����װ���º��Զ������Լ���      
//	if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) || intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED))
//	{
//	Intent ootStartIntent = new Intent(context, Login_Activity.class);
//	ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	context.startActivity(ootStartIntent);
//	}
//	}
//	}
	
}
