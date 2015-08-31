package com.imrub.shoulder.base.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;

public class AppContext {

	private Context mAppContext;
	private static AppContext mInstance;
	
	private AppContext(final Context appContext){
		this.mAppContext = appContext;
	}

	public static final AppContext getInstance(){
		if(mInstance == null){
			throw new RuntimeException("Failed to initialize EncounterAppContext!");
		}
		return mInstance;
	}
	
	public static void initialize(final Context appContext){
		if(mInstance == null){
			mInstance = new AppContext(appContext);
		}
	}
	
	public static Context getAppContext(){
		return getInstance().mAppContext;
	}
	
	public static String getString(final int resId){
		return getInstance().mAppContext.getString(resId);
	}
	
	public static int getDimensionPixelSize(int dimenId){
		return getInstance().mAppContext.getResources().getDimensionPixelSize(dimenId);
	}
	
	public static Bitmap getBitmap(int resc){
		return BitmapFactory.decodeResource(AppContext.getAppContext().getResources(), resc);
	}
	
	public static Resources getResource(){
		return AppContext.getAppContext().getResources();
	}
	
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(){
		WindowManager wm = (WindowManager) AppContext.getAppContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}
	
	@SuppressWarnings("deprecation")
	public static int getScreenHeight(){
		WindowManager wm = (WindowManager) AppContext.getAppContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}
	
}
