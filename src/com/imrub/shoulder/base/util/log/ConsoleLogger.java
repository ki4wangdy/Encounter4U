package com.imrub.shoulder.base.util.log;

import android.util.Log;

import com.imrub.shoulder.base.util.ILogger;

public class ConsoleLogger implements ILogger{

	public static final String Tag = "wangdy_ki";
	
	@Override
	public void print(String tag, String log) {
		Log.i(Tag, log);
	}
	
}
