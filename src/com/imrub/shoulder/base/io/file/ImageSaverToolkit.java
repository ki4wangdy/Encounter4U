package com.imrub.shoulder.base.io.file;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;

public class ImageSaverToolkit {

	public static void saveBitmap(String path, Bitmap bitmap){
		
		File file = new File(path);
		if(!file.exists()){
			file.delete();
		}
		
		FileOutputStream output = null;
		try{
			output = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, output); 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(output != null){
				try{
					output.flush();
					output.close();
				}catch(Exception e){
				}
			}
		}			
	}
	
}
