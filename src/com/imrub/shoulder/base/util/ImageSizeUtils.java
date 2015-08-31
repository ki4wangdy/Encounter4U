package com.imrub.shoulder.base.util;

public class ImageSizeUtils {

	public static String get140x140(String url){
		if(url == null || "".equalsIgnoreCase(url)){
			return url;
		}
		String postfix = ".jpg";
		int index = url.indexOf(".jpg");
		if(index < 0){
			index = url.indexOf(".png");
			postfix = ".png";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(url.substring(0, index)).append("_140x140").append(postfix);
		return builder.toString();
	}
	
	public static String get96x96(String url){
		if(url == null || "".equalsIgnoreCase(url)){
			return url;
		}
		String postfix = ".jpg";
		int index = url.indexOf(".jpg");
		if(index < 0){
			index = url.indexOf(".png");
			postfix = ".png";
		}

		StringBuilder builder = new StringBuilder();
		builder.append(url.substring(0, index)).append("_96x96").append(postfix);
		return builder.toString();
	}
	
}
