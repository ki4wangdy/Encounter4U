package com.imrub.shoulder.base.util;

import com.imrub.shoulder.base.util.log.NullLogger;

public class Logger {

	private ILogger mProxy;
	
	private static Logger mInstance;
	private Logger(){
		mProxy = new NullLogger();
	}

	public static Logger getInstance(){
		if(mInstance == null){
			mInstance = new Logger();
		}
		return mInstance;
	}

	public static void setLoggerProxy(ILogger proxy){
		getInstance().mProxy = proxy;
	}
	
	public static void print(String tag, String log){
		getInstance().mProxy.print(tag, log);
	}
	
}
