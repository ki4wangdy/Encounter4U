
package com.imrub.shoulder.module.photopicker.image;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.util.LruCache;

import com.imrub.shoulder.module.photopicker.imageUtils.ImageManager;
import com.imrub.shoulder.module.photopicker.imageUtils.Util;

public abstract class BaseImageList implements IImageList {
	
    private static final int CACHE_CAPACITY = 512;
    private final LruCache<Integer, BaseImage> mCache = 
    		new LruCache<Integer, BaseImage>(CACHE_CAPACITY);

    protected ContentResolver mContentResolver;
    protected int mSort;

    protected Uri mBaseUri;
    protected Cursor mCursor;
    
    protected String mBucketId;

    public BaseImageList(ContentResolver resolver, Uri uri, int sort,
            String bucketId) {
        mSort = sort;
        mBaseUri = uri;
        mBucketId = bucketId;
        mContentResolver = resolver;
        mCursor = createCursor();

        // TODO: We need to clear the cache because we may "reopen" the image
        // list. After we implement the image list state, we can remove this
        // kind of usage.
        mCache.evictAll();
    }

    public void close() {
        try {
//            invalidateCursor();
        } catch (IllegalStateException e) {
        }
        mContentResolver = null;
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }

    public Uri contentUri(long id) {
        try {
            long existingId = ContentUris.parseId(mBaseUri);
            if (existingId != id){
            	throw new AssertionError("existingId != id ");
            }
            return mBaseUri;
        } catch (NumberFormatException ex) {
            return ContentUris.withAppendedId(mBaseUri, id);
        }
    }

    public int getCount() {
        Cursor cursor = getCursor();
        if (cursor == null) return 0;
        synchronized (this) {
            return cursor.getCount();
        }
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

	private Cursor getCursor() {
        synchronized (this) {
            if (mCursor == null) return null;
            return mCursor;
        }
    }

    public IImage getImageAt(int i) {
        BaseImage result = mCache.get(i);
        if (result == null) {
            Cursor cursor = getCursor();
            if (cursor == null) return null;
            synchronized (this) {
                result = cursor.moveToPosition(i)
                        ? loadImageFromCursor(cursor)
                        : null;
                mCache.put(i, result);
            }
        }
        return result;
    }

    public boolean removeImage(IImage image) {
        if (mContentResolver.delete(image.fullSizeImageUri(), null, null) > 0) {
            ((BaseImage) image).onRemove();
            invalidateCache();
            return true;
        } else {
            return false;
        }
    }

    public boolean removeImageAt(int i) {
        return removeImage(getImageAt(i));
    }

    protected abstract Cursor createCursor();
    protected abstract BaseImage loadImageFromCursor(Cursor cursor);

    protected abstract long getImageId(Cursor cursor);

	protected void invalidateCursor() {
    }

    protected void invalidateCache() {
    	mCache.evictAll();
    }

    private static final Pattern sPathWithId = Pattern.compile("(.*)/\\d+");

    private static String getPathWithoutId(Uri uri) {
        String path = uri.getPath();
        Matcher matcher = sPathWithId.matcher(path);
        return matcher.matches() ? matcher.group(1) : path;
    }

    private boolean isChildImageUri(Uri uri) {
        Uri base = mBaseUri;
        return Util.equals(base.getScheme(), uri.getScheme())
                && Util.equals(base.getHost(), uri.getHost())
                && Util.equals(base.getAuthority(), uri.getAuthority())
                && Util.equals(base.getPath(), getPathWithoutId(uri));
    }

    public IImage getImageForUri(Uri uri) {
        if (!isChildImageUri(uri)) return null;
        long matchId;
        try {
            matchId = ContentUris.parseId(uri);
        } catch (NumberFormatException ex) {
            return null;
        }
        
        Cursor cursor = getCursor();
        if (cursor == null) return null;
        
        synchronized (this) {
            cursor.moveToPosition(-1); // before first
            for (int i = 0; cursor.moveToNext(); ++i) {
                if (getImageId(cursor) == matchId) {
                    BaseImage image = mCache.get(i);
                    if (image == null) {
                        image = loadImageFromCursor(cursor);
                        mCache.put(i, image);
                    }
                    return image;
                }
            }
            return null;
        }
    }

    public int getImageIndex(IImage image) {
        return ((BaseImage) image).mIndex;
    }

    // This provides a default sorting order string for subclasses.
    // The list is first sorted by date, then by id. The order can be ascending
    // or descending, depending on the mSort variable.
    // The date is obtained from the "datetaken" column. But if it is null,
    // the "date_modified" column is used instead.
    protected String sortOrder() {
        String ascending = (mSort == ImageManager.SORT_ASCENDING) ? " ASC" : " DESC";

        // Use DATE_TAKEN if it's non-null, otherwise use DATE_MODIFIED.
        // DATE_TAKEN is in milliseconds, but DATE_MODIFIED is in seconds.
        String dateExpr =
                "case ifnull(datetaken,0)" +
                " when 0 then date_modified*1000" +
                " else datetaken" +
                " end";

        // Add id to the end so that we don't ever get random sorting
        // which could happen, I suppose, if the date values are the same.
        return dateExpr + ascending + ", _id" + ascending;
    }
    
    @Override
    protected void finalize() throws Throwable {
    	close();
    	super.finalize();
    }
    
}
