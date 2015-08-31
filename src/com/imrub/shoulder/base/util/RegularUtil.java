package com.imrub.shoulder.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {

	public static boolean isMobileMatch(String mobiles){  
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		Matcher m = p.matcher(mobiles);  
		return m.matches();  
	}  
	
	public static boolean isMailMatch(String mailAddr){
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((" +
				"\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" +
				"(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(mailAddr);
		return matcher.matches();
	}
	
}
