package com.imrub.shoulder.base.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.imrub.shoulder.base.app.store.SplashJpeg;

public class TimeUtil {

	public static boolean checkSplashTimeForOnce(){
		
		final long t = SplashJpeg.getInstance().getSplashOnceTime();
		if(t == 0){
			return true;
		}
		
		Calendar oldDate = new GregorianCalendar();
		oldDate.setTimeInMillis(t);
		Calendar nowDate = new GregorianCalendar();
		if(oldDate.get(Calendar.DAY_OF_YEAR) != nowDate.get(Calendar.DAY_OF_YEAR)){
			return true;
		} else if(oldDate.get(Calendar.MONTH) != nowDate.get(Calendar.MONTH)){
			return true;
		}
		return false;
	}
	
	public static boolean checkWhetherSplashIconOverTime(){
		long t = System.currentTimeMillis() / 1000 ;
		final long startTime = SplashJpeg.getInstance().getStartTime();
		final long endTime = SplashJpeg.getInstance().getEndTime();
		
		if(startTime == 0 || endTime == 0){
			return true;
		}
		return t < endTime && t > startTime ? false : true;
	}

	public static String longTimeToString(long time){
		SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
		return formate.format(new Date(time));
	}
	
	public static String getTime(long time) {
		Date currentTime = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
	
}
