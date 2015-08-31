package com.imrub.shoulder.base.util.imageprocess;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.io.file.ImageSaverToolkit;

public class PhotoPickerImageUtil {

	public static void capaturePhotoPicker(Bitmap bitmap, Matrix matrix, String filePath){
		int screenWidth = AppContext.getScreenWidth();
    	int screenHeight = AppContext.getScreenHeight() - AppContext.getResource().
    			getDimensionPixelSize(R.dimen.photo_picker_layer_title_height);
    	
    	Bitmap firstBitmap = Bitmap.createBitmap(screenWidth,screenHeight,Config.ARGB_8888);
    	Canvas c = new Canvas(firstBitmap);
    	
    	c.drawBitmap(bitmap, matrix, null);

    	Bitmap copy = Bitmap.createBitmap(firstBitmap);
    	
    	c = new Canvas(firstBitmap);
    	Bitmap bs = BitmapFactory.decodeResource(AppContext.getResource(), R.drawable.photopicker_layer);
    	
    	Paint paint = new Paint();
		PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);  
		paint.setXfermode(xfermode);
		
		c.drawBitmap(bs, 0, 0, paint);
    	
		for(int i=0;i<firstBitmap.getWidth();i++){
			for(int j=0;j<firstBitmap.getHeight();j++){
				int mm = firstBitmap.getPixel(i, j);
				if(mm != 0){
					copy.setPixel(i, j, 0);
				}
			}
		}

		int xStart = 0;
		int yStart = 0;
		
		int y = screenHeight / 2;
		
		for(int i=0; i<copy.getWidth(); i++){
			if(copy.getPixel(i, y) != 0){
				xStart = i;
				break;
			}
		}

		int x = screenWidth / 2;
		for(int j=0; j<copy.getHeight(); j++){
			if(copy.getPixel(x, j) != 0){
				yStart = j;
				break;
			}
		}
		
		int width = screenWidth - 2*(xStart + 1);
		int height = screenHeight - 2*(yStart + 1);
		
		Bitmap finalBitmap = Bitmap.createBitmap(copy, xStart, yStart, width, height);
    	ImageSaverToolkit.saveBitmap(filePath, finalBitmap);
    	
    	copy.recycle();
    	firstBitmap.recycle();
    	finalBitmap.recycle();
	}
	
	public static void capaturePhotoPicker2(Bitmap bitmap, Matrix matrix, String filePath){
		int screenWidth = AppContext.getScreenWidth();
    	int screenHeight = AppContext.getScreenHeight() - AppContext.getResource().
    			getDimensionPixelSize(R.dimen.photo_picker_layer_title_height);
    	
    	//Bitmap firstBitmap = Bitmap.createBitmap(screenWidth,screenHeight,Config.ARGB_8888);
    	
    	Bitmap firstBitmap = Bitmap.createBitmap(screenWidth,screenHeight,Config.ARGB_8888);
    	Canvas c = new Canvas(firstBitmap);
    	
    	c.drawBitmap(bitmap, matrix, null);
//    	Canvas c = new Canvas(firstBitmap);
//    	
//    	c.drawBitmap(bitmap, matrix, null);
//
//    	Bitmap copy = Bitmap.createBitmap(firstBitmap);
    	
//    	c = new Canvas(firstBitmap);
    	Bitmap bs = BitmapFactory.decodeResource(AppContext.getResource(), R.drawable.photopicker_layer);
    	
//    	Paint paint = new Paint();
//		PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);  
//		paint.setXfermode(xfermode);
//		
//		c.drawBitmap(bs, 0, 0, paint);
//    	
//		for(int i=0;i<firstBitmap.getWidth();i++){
//			for(int j=0;j<firstBitmap.getHeight();j++){
//				int mm = firstBitmap.getPixel(i, j);
//				if(mm != 0){
//					copy.setPixel(i, j, 0);
//				}
//			}
//		}

		int xStart = 0;
		int yStart = 0;
		
		int y = screenHeight / 2;
		
		for(int i=0; i<bs.getWidth(); i++){
			if(bs.getPixel(i, y) == 0){
				xStart = i;
				break;
			}
		}

		int x = screenWidth / 2;
		for(int j=0; j<bs.getHeight(); j++){
			if(bs.getPixel(x, j) == 0){
				yStart = j;
				break;
			}
		}
		
		int width = screenWidth - 2*(xStart + 1);
//		int height = screenHeight - 2*(yStart + 1);
		
		Bitmap finalBitmap = Bitmap.createBitmap(firstBitmap, xStart, yStart, width, width);
    	ImageSaverToolkit.saveBitmap(filePath, finalBitmap);
    	
//    	copy.recycle();
    	firstBitmap.recycle();
    	finalBitmap.recycle();
	}
	
}
