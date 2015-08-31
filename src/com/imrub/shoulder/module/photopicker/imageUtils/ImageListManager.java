package com.imrub.shoulder.module.photopicker.imageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.photopicker.IPhotoPickerItem;
import com.imrub.shoulder.module.photopicker.PhotoPickerItem;
import com.imrub.shoulder.module.photopicker.image.IImage;
import com.imrub.shoulder.module.photopicker.image.IImageList;

public class ImageListManager {

	private Thread mWorkerThread;
	// onDestory we need to close the cursor
	private ArrayList<IImageList> mAllLists = new ArrayList<IImageList>();

	// This is used to stop the worker thread.
	private volatile boolean mAbort = false;

	private List<IImage> mCurrentImageList;
	
	private static ImageListManager mInstance;
	private ImageListManager() {
	}

	public static ImageListManager getInstance() {
		if (mInstance == null) {
			mInstance = new ImageListManager();
		}
		return mInstance;
	}

	public void startWorker(final IPhotoPickerItem callback) {
		mAbort = false;
		mWorkerThread = new Thread("GalleryPicker Worker") {
			@Override
			public void run() {
				workerRun(callback);
			}
		};
		mWorkerThread.start();
	}

	private void workerRun(final IPhotoPickerItem callback) {
		checkAllImages(callback);
		checkScanning();
		checkBucketIds(callback);
	}

	private void checkAllImages(IPhotoPickerItem callback){
		IImageList allImages = ImageManager.makeImageList(AppContext.getAppContext().getContentResolver(),
				ImageManager.DataLocation.ALL, ImageManager.INCLUDE_IMAGES,
				ImageManager.SORT_DESCENDING, null);
		ArrayList<IImage> mAllImages = new ArrayList<IImage>();
		int size = allImages.getCount();
		int maxSize = size > 12 ? 12 : size;
		for(int i=0; i<maxSize; i++){
			mAllImages.add(allImages.getImageAt(i));
		}
		allImages.close();
		callback.onAllImages(mAllImages);
	}
	
	// This is run in the worker thread.
	private void checkScanning() {
		ContentResolver cr = AppContext.getAppContext().getContentResolver();
		final boolean scanning = ImageManager.isMediaScannerScanning(cr);
		ThreadFacade.runOnUiThread(new Runnable() {
			public void run() {
				updateScanningDialog(scanning);
			}
		});
	}

	// Display a dialog if the storage is being scanned now.
	public void updateScanningDialog(boolean scanning) {
		// boolean prevScanning = (mMediaScanningDialog != null);
		// if (prevScanning == scanning)
		// return;
		// // Now we are certain the state is changed.
		// if (prevScanning) {
		// mMediaScanningDialog.cancel();
		// mMediaScanningDialog = null;
		// } else if (scanning) {
		// mMediaScanningDialog = ProgressDialog.show(this, null,
		// getResources().getString(R.string.wait), true, true);
		// }
	}

	// This is run in the worker thread.
	private void checkBucketIds(final IPhotoPickerItem callback) {
		final IImageList allImages = ImageManager.makeImageList(AppContext.getAppContext().getContentResolver(),
				ImageManager.DataLocation.ALL, ImageManager.INCLUDE_IMAGES,
				ImageManager.SORT_DESCENDING, null);

		if (mAbort) {
			allImages.close();
			return;
		}

		HashMap<String, String> hashMap = allImages.getBucketIds();
		allImages.close();
		if (mAbort) return;

		for (Map.Entry<String, String> entry : hashMap.entrySet()) {
			String key = entry.getKey();
			if (key == null) {
				continue;
			}

			// Bug , we just ignore the screenshots and WeiXin
//			if(entry.getValue().equalsIgnoreCase("Screenshots") || entry.getValue().equalsIgnoreCase("WeiXin")){
//				continue;
//			}
			
			IImageList list = createImageList(ImageManager.INCLUDE_IMAGES, key,
					AppContext.getAppContext().getContentResolver());
			
			ArrayList<IImage> images = new ArrayList<IImage>();
			int size = list.getCount();
			for(int i=0; i<size; i++){
				images.add(list.getImageAt(i));
			}
			
			if(list != null){
				list.close();
			}
			
			if (mAbort)
				return;
			
			if (images.isEmpty()) {
				continue;
			}
			
			PhotoPickerItem item = new PhotoPickerItem(PhotoPickerItem.TYPE_NORMAL_FOLDERS, key,
					entry.getValue(), images);
			callback.onAddNewItem(item);
		}
	}

	private IImageList createImageList(int mediaTypes, String bucketId,
			ContentResolver cr) {
		IImageList list = ImageManager.makeImageList(cr,
				ImageManager.DataLocation.EXTERNAL, mediaTypes,
				ImageManager.SORT_DESCENDING, bucketId);
		mAllLists.add(list);
		return list;
	}
	
	public void abortWorker() {
		if (mWorkerThread != null) {
			mAbort = true;
			try {
				mWorkerThread.join();
			} catch (InterruptedException ex) {
			}
			mWorkerThread = null;
			clearImageLists();
		}
	}

	private void clearImageLists() {
		for (IImageList list : mAllLists) {
			list.close();
		}
		mAllLists.clear();
	}

	public void setCurrentImageList(List<IImage> imageList){
		mCurrentImageList = imageList;
	}
	
	public List<IImage> getCurrentImageList(){
		return mCurrentImageList;
	}
	
}
