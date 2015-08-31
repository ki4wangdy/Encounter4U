package com.imrub.shoulder.base.util.cache;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.path.EnvirPath;

public class Utils {
	
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    
    public static final int LRUCAHCE_MEDIUM_SIZE		= 6 ;
    
    public static final int APPLICATION_MEMORY_MEDIUM	= 	16;
    public static final int APPLICATION_MEMORY_LARGE	=	24;
    
    private Utils() {
    };

    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (hasHttpConnectionBug()) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    @SuppressLint("NewApi")
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    @SuppressLint("NewApi")
    public static File getExternalCacheDir() {
        if (hasExternalCacheDir()) {
            return new File(EnvirPath.getAppDir());
        }
        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + AppContext.getAppContext().getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
    public static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    public static int getMemoryClass() {
        return ((ActivityManager) AppContext.getAppContext().getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    public static int getLruCacheSize(){
    	int memorySize = Utils.getMemoryClass();
    	if(memorySize <= APPLICATION_MEMORY_MEDIUM){
    		return APPLICATION_MEMORY_MEDIUM / 4 ;
    	} else if(memorySize >= APPLICATION_MEMORY_LARGE){
    		return APPLICATION_MEMORY_LARGE / 3;
    	}
    	return LRUCAHCE_MEDIUM_SIZE;
    }
    
    public static boolean hasHttpConnectionBug() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO;
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasActionBar() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
}
