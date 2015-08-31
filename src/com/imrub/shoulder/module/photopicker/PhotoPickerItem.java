package com.imrub.shoulder.module.photopicker;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images;

import com.imrub.shoulder.R;
import com.imrub.shoulder.module.photopicker.image.IImage;
import com.imrub.shoulder.module.photopicker.imageUtils.ImageManager;

public class PhotoPickerItem {

	@SuppressWarnings("unused")
	private static final ImageListData[] IMAGE_LIST_DATA = {
			// Camera Images
			new ImageListData(PhotoPickerItem.TYPE_CAMERA_IMAGES,
					ImageManager.INCLUDE_IMAGES,
					ImageManager.CAMERA_IMAGE_BUCKET_ID,
					R.string.photopicker_camera_image),

			// All Images
			new ImageListData(PhotoPickerItem.TYPE_ALL_IMAGES,
					ImageManager.INCLUDE_IMAGES, null,
					R.string.photopicker_all_image), 
	};

	@SuppressWarnings("unused")
	private static final class ImageListData {
		ImageListData(int type, int include, String bucketId, int stringId) {
			mType = type;
			mInclude = include;
			mBucketId = bucketId;
			mStringId = stringId;
		}
		int mType;
		int mInclude;
		int mStringId;
		String mBucketId;
	}
	
	public static final int TYPE_NONE = -1;
	public static final int TYPE_ALL_IMAGES = 0;
	public static final int TYPE_ALL_VIDEOS = 1;
	public static final int TYPE_CAMERA_IMAGES = 2;
	public static final int TYPE_CAMERA_VIDEOS = 3;
	public static final int TYPE_CAMERA_MEDIAS = 4;
	public static final int TYPE_NORMAL_FOLDERS = 5;

	public final int mType;
	public final String mBucketId;
	public final String mName;
	public final List<IImage> mImageList;
	public final int mCount;
	public final Uri mFirstImageUri; // could be null if the list is empty

	// The thumbnail bitmap is set by setThumbBitmap() later because we want
	// to let the user sees the folder icon as soon as possible (and
	// possibly
	// select them), then present more detailed information when we have it.
	public Bitmap mThumbBitmap; // the thumbnail bitmap for the image list

	public PhotoPickerItem(int type, String bucketId, String name,
			List<IImage> list) {
		mType = type;
		mBucketId = bucketId;
		mName = name;
		mImageList = list;
		mCount = list.size();
		if (mCount > 0) {
			mFirstImageUri = list.get(0).fullSizeImageUri();
		} else {
			mFirstImageUri = null;
		}
	}

	public void setThumbBitmap(Bitmap thumbBitmap) {
		mThumbBitmap = thumbBitmap;
	}

	public boolean needsBucketId() {
		return mType >= TYPE_CAMERA_IMAGES;
	}

	public void launch(Activity activity) {
		Uri uri = Images.Media.INTERNAL_CONTENT_URI;
		if (needsBucketId()) {
			uri = uri.buildUpon().appendQueryParameter("bucketId", mBucketId)
					.build();
		}
		Intent intent = new Intent(activity, ImageGallery.class);
		intent.putExtra("name", mName);
		activity.startActivity(intent);
	}

	public int getIncludeMediaTypes() {
		return convertItemTypeToIncludedMediaType(mType);
	}

	public static int convertItemTypeToIncludedMediaType(int itemType) {
		switch (itemType) {
			case TYPE_ALL_IMAGES:
			case TYPE_CAMERA_IMAGES:
				return ImageManager.INCLUDE_IMAGES;
			case TYPE_ALL_VIDEOS:
			case TYPE_CAMERA_VIDEOS:
				return ImageManager.INCLUDE_VIDEOS;
			case TYPE_NORMAL_FOLDERS:
			case TYPE_CAMERA_MEDIAS:
			default:
				return ImageManager.INCLUDE_IMAGES | ImageManager.INCLUDE_VIDEOS;
		}
	}

	public int getOverlay() {
		switch (mType) {
			case TYPE_ALL_IMAGES:
			case TYPE_CAMERA_IMAGES:
			case TYPE_ALL_VIDEOS:
			case TYPE_CAMERA_VIDEOS:
			case TYPE_CAMERA_MEDIAS:
			case TYPE_NORMAL_FOLDERS:
			default:
		}
		return 0;
	}
}
