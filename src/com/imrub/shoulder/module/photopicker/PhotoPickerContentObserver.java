package com.imrub.shoulder.module.photopicker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore.Images;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.photopicker.imageUtils.ImageManager;

public class PhotoPickerContentObserver {

	private static BroadcastReceiver mReceiver;
	private static ContentObserver mDbObserver;
	
	private static Handler mHandler;
	
	public static void onCreate(final IPhotoPickerContentObserver callback){
		
		mHandler = new Handler(Looper.getMainLooper());
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				onReceiveMediaBroadcast(callback, intent);
			}
		};

		mDbObserver = new ContentObserver(mHandler) {
			@Override
			public void onChange(boolean selfChange) {
				callback.onRake(false,ImageManager.isMediaScannerScanning(AppContext.getAppContext().getContentResolver()));
			}
		};

		AppContext.getAppContext().getContentResolver().registerContentObserver( 
				Images.Media.EXTERNAL_CONTENT_URI, true, mDbObserver);

		// install an intent filter to receive SD card related events.
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		intentFilter.addDataScheme("file");

		AppContext.getAppContext().registerReceiver(mReceiver, intentFilter);
	}
	
	// This is called when we receive media-related broadcast.
	private static void onReceiveMediaBroadcast(final IPhotoPickerContentObserver callback, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
			// SD card available
			// TODO put up a "please wait" message
		} else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
			// SD card unavailable
			callback.onRake(true, false);
		} else if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
			callback.onRake(false, true);
		} else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
			callback.onRake(false, false);
		} else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
			callback.onRake(true, false);
		}
	}
	
	public static void onDestory(){
		AppContext.getAppContext().unregisterReceiver(mReceiver);
		AppContext.getAppContext().getContentResolver().unregisterContentObserver(mDbObserver);
		mReceiver = null;
		mDbObserver = null;
		mHandler = null;
	}
	
	public interface IPhotoPickerContentObserver {
		public void onRake(boolean unmounted, boolean scanning);
	}
	
}
