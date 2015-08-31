package com.imrub.shoulder.base.app.path;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class BitmapStream {

	public static void saveBitmap(final Bitmap bitmap){
		final String dir = EnvirPath.getSplashIconDir();
		File d = new File(dir);
		if(!d.exists()){
			d.mkdir();
		}
		
		OutputStream output = null;
		try{
			output = new FileOutputStream(EnvirPath.getSplashIconFilePath());
			bitmap.compress(CompressFormat.JPEG, 100, output);
		}catch(Exception e){
		}finally{
			if(output != null){
				try{
					output.close();
				}catch(Exception e){
				}
			}
		}
	}

	public static void saveBitmapToPath(final Bitmap bitmap, final String path){
		
	}
	
}
