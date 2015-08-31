package com.imrub.shoulder.module;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import com.imrub.shoulder.base.util.Logger;

public class CrashHandler implements UncaughtExceptionHandler{

	public static final String Tag = "CrashHandler";
	
	public static void initCrashHandler(){
		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
	}
	
	public static String getExceptionMessage(Throwable ex){
		try {
			StringWriter writer = new StringWriter();
			ex.printStackTrace(new PrintWriter(writer));
			String buf = writer.getBuffer().toString();
			writer.close();
			return buf;
		} catch (IOException e) {
		}
		return "";
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Logger.print(Tag, getExceptionMessage(ex));
		System.exit(0);
	}

	
}
