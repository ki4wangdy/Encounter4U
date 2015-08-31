package com.imrub.shoulder.base.util.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerUtils {

	public static String getCurrentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public static String getPerformanceString(String detail, long userTime){
		StringBuilder builder = new StringBuilder();
		builder.append(getCurrentTime()).append(" ");
		builder.append("Performance Test,").append(detail).append(userTime);
		return builder.toString();
	}
	
}
