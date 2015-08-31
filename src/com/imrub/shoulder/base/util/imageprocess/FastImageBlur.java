package com.imrub.shoulder.base.util.imageprocess;

import android.graphics.Bitmap;

public class FastImageBlur {

    static {
        System.loadLibrary("imgBlur");
    }

    public static void fast2blur(Bitmap bitmap, int blurLevel){
    	if(blurLevel <= 0 || blurLevel >= 100){
    		return ;
    	}
    	buildBlur(bitmap, blurLevel);
    }
    
    private static native void buildBlur(Bitmap bitmap, int blurLevel);
	
}
