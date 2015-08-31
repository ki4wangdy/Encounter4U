package com.imrub.shoulder.base.util.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;

import com.imrub.shoulder.base.Md5;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.path.EnvirPath;

public class DiskLruCache {
	
    private static final String CACHE_FILENAME_PREFIX 	= "cache_";
    
    private static final int COMPRESS_QUALITY			=	70;
    private static final int MAX_CACHE_BYTE_SIZE		= 1024 * 1024 * 5; // 5M
    
    private CompressFormat mCompressFormat = CompressFormat.PNG;
    private int mCompressQuality = COMPRESS_QUALITY;
    
    private int maxCacheByteSize = MAX_CACHE_BYTE_SIZE;
    private final LruCache<String, String> mFileCache = new LruCache<String, String>(maxCacheByteSize);
 
    private final File mCacheDir;
    
    private static final FilenameFilter cacheFileFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            return filename.startsWith(CACHE_FILENAME_PREFIX);
        }
    };
 
    public static DiskLruCache openCache(File cacheDir, int maxByteSize) {
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        if (cacheDir.isDirectory() && cacheDir.canWrite()
                && Utils.getUsableSpace(cacheDir) > maxByteSize) {
            return new DiskLruCache(cacheDir, maxByteSize);
        }
        return null;
    }

    public void put(String key, Bitmap data) {
        synchronized (mFileCache) {
            if (mFileCache.get(key) == null) {
                try {
                    final String file = createFilePath(mCacheDir, key);
                    if (writeBitmapToFile(data, file)) {
                        mFileCache.put(key, file);
                    }
                } catch (Exception e) {
                }
            }
        }
    }
    
    public Bitmap get(String key) {
        synchronized (mFileCache) {
            final String file = mFileCache.get(key);
            if (file != null) {
                return BitmapFactory.decodeFile(file);
            } else {
                final String existingFile = createFilePath(mCacheDir, key);
                if (new File(existingFile).exists()) {
                    mFileCache.put(key, existingFile);
                    return BitmapFactory.decodeFile(existingFile);
                }
            }
            return null;
        }
    }
    
    public boolean containsKey(String key) {
    	synchronized (mFileCache) {
    		if (mFileCache.get(key) != null) {
    			return true;
    		}
    		final String existingFile = createFilePath(mCacheDir, key);
    		if (new File(existingFile).exists()) {
    			mFileCache.put(key, existingFile);
    			return true;
    		}
    		return false;
		}
    }
    
    public void setCompressParams(CompressFormat compressFormat, int quality) {
        mCompressFormat = compressFormat;
        mCompressQuality = quality;
    }
    
    public void clearCache() {
    	if(mCacheDir == null || !mCacheDir.exists()){
    		return ;
    	}
        final File[] files = mCacheDir.listFiles(cacheFileFilter);
        for (int i=0; i<files.length; i++) {
            files[i].delete();
        }
    }
    
    public String createFilePath(String key) {
        return createFilePath(mCacheDir, key);
    }
    
    public static File getDiskCacheDir() {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
                        !Utils.isExternalStorageRemovable() ?
                        EnvirPath.getSdCardCacheDir() :
                        AppContext.getAppContext().getCacheDir().getPath();
        return new File(cachePath);
    }
    
    private boolean writeBitmapToFile(Bitmap bitmap, String file)
            throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file), Utils.IO_BUFFER_SIZE);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
    private DiskLruCache(File cacheDir, int maxByteSize) {
        mCacheDir = cacheDir;
        maxCacheByteSize = maxByteSize;
    }
 
    private static String createFilePath(File cacheDir, String key) {
    	return cacheDir.getAbsolutePath() + File.separator + Md5.getMD5FullStr(key);
    }
    
}
