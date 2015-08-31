/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imrub.shoulder.module.photopicker.image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.Images;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.photopicker.imageUtils.BitmapManager;
import com.imrub.shoulder.module.photopicker.imageUtils.Util;

public abstract class BaseImage implements IImage {
	
    private static final int UNKNOWN_LENGTH = -1;
    
    private int mWidth = UNKNOWN_LENGTH;
    private int mHeight = UNKNOWN_LENGTH;

    private static final Options mOptions = new Options();
    static{
    	mOptions.outWidth = AppContext.getDimensionPixelSize(R.dimen.photo_picker_list_item_image_width_height);
    	mOptions.outHeight = mOptions.outWidth;
    }
    
    protected ContentResolver mContentResolver;
    
    protected Uri mUri;
    protected long mId;
    private final long mDateTaken;
    protected final int mIndex;
    
    protected String mDataPath;
    protected String mMimeType;
    
    private String mTitle;
    protected BaseImageList mContainer;

    protected BaseImage(BaseImageList container, ContentResolver cr,
            long id, int index, Uri uri, String dataPath, String mimeType,
            long dateTaken, String title) {
        mContainer = container;
        mContentResolver = cr;
        mId = id;
        mIndex = index;
        mUri = uri;
        mDataPath = dataPath;
        mMimeType = mimeType;
        mDateTaken = dateTaken;
        mTitle = title;
    }

    public String getDataPath() {
        return mDataPath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Image)) return false;
        return mUri.equals(((Image) other).mUri);
    }

    @Override
    public int hashCode() {
        return mUri.hashCode();
    }

    public Bitmap fullSizeBitmap(int minSideLength, int maxNumberOfPixels) {
        return fullSizeBitmap(minSideLength, maxNumberOfPixels,
                IImage.ROTATE_AS_NEEDED, IImage.NO_NATIVE);
    }

    public Bitmap fullSizeBitmap(int minSideLength, int maxNumberOfPixels,
            boolean rotateAsNeeded, boolean useNative) {
        Uri url = mContainer.contentUri(mId);
        if (url == null) return null;

        Bitmap b = Util.makeBitmap(minSideLength, maxNumberOfPixels,
                url, mContentResolver, useNative);

        if (b != null && rotateAsNeeded) {
//            b = Util.rotate(b, getDegreesRotated());
        }

        return b;
    }

    public InputStream fullSizeImageData() {
        try {
            InputStream input = mContentResolver.openInputStream(mUri);
            return input;
        } catch (IOException ex) {
            return null;
        }
    }

    public Uri fullSizeImageUri() {
        return mUri;
    }

    public IImageList getContainer() {
        return mContainer;
    }

    public long getDateTaken() {
        return mDateTaken;
    }

    public int getDegreesRotated() {
        return 0;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public String getTitle() {
        return mTitle;
    }

    private void setupDimension() {
        ParcelFileDescriptor input = null;
        try {
            input = mContentResolver.openFileDescriptor(mUri, "r");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapManager.instance().decodeFileDescriptor(
                    input.getFileDescriptor(), options);
            mWidth = options.outWidth;
            mHeight = options.outHeight;
        } catch (FileNotFoundException ex) {
            mWidth = 0;
            mHeight = 0;
        } finally {
            Util.closeSilently(input);
        }
    }

    public int getWidth() {
        if (mWidth == UNKNOWN_LENGTH) setupDimension();
        return mWidth;
    }

    public int getHeight() {
        if (mHeight == UNKNOWN_LENGTH) setupDimension();
        return mHeight;
    }

    public Bitmap miniThumbBitmap() {
        Bitmap b = null;
        try {
            long id = mId;
            b = BitmapManager.instance().getThumbnail(mContentResolver, id,
                    Images.Thumbnails.MINI_KIND, mOptions, false);
        } catch (Throwable ex) {
            return null;
        }

        if(b == null){
        	return null;
        }
        
        Bitmap bb = computeBitmap(b, mOptions);
        if(bb != null){
        	return bb;
        }
        return b;
    }

    private Bitmap computeBitmap(Bitmap bitmap, Options option){
    	int width = option.outWidth > option.outHeight ? option.outHeight : option.outWidth;
    	return width < 0 ? null : Bitmap.createBitmap(bitmap, 0, 0, width, width);
    }
    
    protected void onRemove() {
    }

    @Override
    public String toString() {
        return mUri.toString();
    }
}
