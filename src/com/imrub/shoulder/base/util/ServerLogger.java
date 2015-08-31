package com.imrub.shoulder.base.util;

import com.imrub.shoulder.base.util.log.NullLogger;

public class ServerLogger {

private ILogger mProxy;
	
	private static ServerLogger mInstance;
	private ServerLogger(){
		mProxy = new NullLogger();
	}

	public static ServerLogger getInstance(){
		if(mInstance == null){
			mInstance = new ServerLogger();
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
