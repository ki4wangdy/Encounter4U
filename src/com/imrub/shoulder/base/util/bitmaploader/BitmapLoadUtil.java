package com.imrub.shoulder.base.util.bitmaploader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.imrub.shoulder.base.util.cache.DiskLruCache;
import com.imrub.shoulder.base.util.cache.Utils;

public class BitmapLoadUtil {

	public static final String HTTP_CACHE_DIR = ".cache";
    public static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
	
	public static BitmapLoadTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	 public static int calculateInSampleSize(BitmapFactory.Options options,
	            int reqWidth, int reqHeight) {
	        // Raw height and width of image
	        final int height = options.outHeight;
	        final int width = options.outWidth;
	        int inSampleSize = 1;

	        if (height > reqHeight || width > reqWidth) {
	            if (width > height) {
	                inSampleSize = Math.round((float) height / (float) reqHeight);
	            } else {
	                inSampleSize = Math.round((float) width / (float) reqWidth);
	            }

	            // This offers some additional logic in case the image has a strange
	            // aspect ratio. For example, a panorama may have a much larger
	            // width than height. In these cases the total pixels might still
	            // end up being too large to fit comfortably in memory, so we should
	            // be more aggressive with sample down the image (=larger
	            // inSampleSize).

	            final float totalPixels = width * height;

	            // Anything more than 2x the requested pixels we'll sample down
	            // further.
	            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

	            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
	                inSampleSize++;
	            }
	        }
	        return inSampleSize;
	 }
	
	 public static File downloadBitmap(String urlString) {
		 final File cacheDir = DiskLruCache.getDiskCacheDir();

		 final DiskLruCache cache = DiskLruCache.openCache(cacheDir, HTTP_CACHE_SIZE);
		 final File cacheFile = new File(cache.createFilePath(urlString));

		 if (cache != null && cache.containsKey(urlString)) {
			 return cacheFile;
		 }

		 Utils.disableConnectionReuseIfNecessary();
		 HttpURLConnection urlConnection = null;
		 BufferedOutputStream out = null;

		 try {
			 final URL url = new URL(urlString);
			 urlConnection = (HttpURLConnection) url.openConnection();
			 final InputStream in = new BufferedInputStream(urlConnection.getInputStream(), Utils.IO_BUFFER_SIZE);
			 out = new BufferedOutputStream(new FileOutputStream(cacheFile), Utils.IO_BUFFER_SIZE);

			 int b;
			 while ((b = in.read()) != -1) {
				 out.write(b);
			 }
			 return cacheFile;
	        } catch (final Exception e) {
	        } finally {
	            if (urlConnection != null) {
	                urlConnection.disconnect();
	            }
	            if (out != null) {
	                try {
	                    out.close();
	                } catch (final Exception e) {
	                }
	            }
	        }
		 return null;
	 }
	 
}
