
package com.imrub.shoulder.module.photopicker.image;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.net.Uri;

public interface IImage {
	
    static final int THUMBNAIL_TARGET_SIZE = 320;
    static final int MINI_THUMB_TARGET_SIZE = 96;
    
    static final int THUMBNAIL_MAX_NUM_PIXELS = 512 * 384;
    static final int MINI_THUMB_MAX_NUM_PIXELS = 128 * 128;
    
    static final int UNCONSTRAINED = -1;

    public static final boolean ROTATE_AS_NEEDED = true;
    public static final boolean NO_ROTATE = false;
    public static final boolean USE_NATIVE = true;
    public static final boolean NO_NATIVE = false;
    
    public abstract int getDegreesRotated();
    
    /** Get the image list which contains this image. */
    public abstract IImageList getContainer();

    /** Get the bitmap for the full size image. */
    public abstract Bitmap fullSizeBitmap(int minSideLength,int maxNumberOfPixels);
    
    public abstract Bitmap fullSizeBitmap(int minSideLength,
            int maxNumberOfPixels, boolean rotateAsNeeded, boolean useNative);

    /** Get the input stream associated with a given full size image. */
    public InputStream fullSizeImageData();
    public Uri fullSizeImageUri();

    /** Get the path of the (full size) image data. */
    public String getDataPath();

    // Get the title of the image
    public String getTitle();

    // Get metadata of the image
    public long getDateTaken();
    public String getMimeType();

    public int getWidth();
    public int getHeight();

    // Get property of the image
    public boolean isReadonly();
    public boolean isDrm();

    // Get the bitmap of the medium thumbnail
    public Bitmap thumbBitmap(boolean rotateAsNeeded);

    // Get the bitmap of the mini thumbnail.
    public Bitmap miniThumbBitmap();

    // Rotate the image
    public boolean rotateImageBy(int degrees);

}
