package com.imrub.shoulder.base.app.path;

import java.io.File;

import android.os.Environment;

import com.imrub.shoulder.base.app.AppContext;

public class EnvirPath {

	public static String getSdcardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * sdcard/.Encounter
	 * @return
	 */
	public static String getAppDir(){
		String path = getSdcardPath()+"/.Encounter";
		createDirectoryIfNotExist(path);
		return path;
	}
	
	/**
	 * sdcard/.Encounter/log
	 * @return
	 */
	public static String getAppLogDir(){
		String appRootDir = getAppDir();
		String loggerPath = "/log";
		createDirectoryIfNotExist(appRootDir+loggerPath);
		return appRootDir + loggerPath;
	}

	/**
	 * sdcard/.Encounter/cache
	 * @return
	 */
	public static String getSdCardCacheDir(){
		String path = getAppDir()+"/cache";
		createDirectoryIfNotExist(path);
		return path;
	}

	/**
	 * com.imrub.shoulder/cache
	 * @return
	 */
	public static String getAppCache(){
		return AppContext.getAppContext().getCacheDir().getAbsolutePath();
	}
	
	/**
	 * com.imrub.shoulder/cache/image
	 * @return
	 */
	public static String getCacheImageDir(){
		String fileStr = getAppCache()+"/image";
		File file = new File(fileStr);
		if(!file.exists()){
			file.mkdir();
		}
		return fileStr;
	}
	
	/**
	 * com.imrub.shoulder/cache/image/logo.png
	 * @return
	 */
	public static String getappcacheuserimagelogo(){
		return getCacheImageDir()+"/logo.png";
	}
	
	/**
	 * sdcard/.Encounter/temp
	 * @return
	 */
	public static String getAppTempdir() {
		String path = getAppDir() + "/temp";
		createDirectoryIfNotExist(path);
		return path;
	}

	/**
	 *  com.imrub.sholder/cache/image/logo.png
	 * @return
	 */
	public static String getPhotoPickerCapturePath(){
		return getappcacheuserimagelogo();
	}
	
	/**
	 *  com.imrub.shoulder/cache/image/logo.png
	 * @return
	 */
	public static String getSdcardPhotoPickerCapturePath(){
		return getappcacheuserimagelogo();
	}
	
	/**
	 *  com.imrub.shoulder/file/splash
	 * @return
	 */
	public static String getSplashIconDir(){
		return AppContext.getAppContext().getFilesDir().getAbsolutePath() + "/splash";
	}
	
	/**
	 * com.imrub.shoulder/file/splash/splashicon.png
	 * @return
	 */
	public static String getSplashIconFilePath(){
		return AppContext.getAppContext().getFilesDir().getAbsolutePath() + "/splash/splashicon.png";
	}

	private static void createDirectoryIfNotExist(String dir) {
		File dirF = new File(dir);
		if (!dirF.exists() || !dirF.isDirectory()) {
			dirF.mkdirs();
		}
	}
	
}
