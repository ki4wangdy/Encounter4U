package com.imrub.shoulder.module.photopicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.path.EnvirPath;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.imageprocess.PhotoPickerImageUtil;
import com.imrub.shoulder.module.photopicker.PhotoPickerContentObserver.IPhotoPickerContentObserver;
import com.imrub.shoulder.module.photopicker.image.IImage;
import com.imrub.shoulder.module.photopicker.imageUtils.BitmapManager;
import com.imrub.shoulder.module.photopicker.imageUtils.ImageListManager;
import com.imrub.shoulder.module.photopicker.imageZoom.PhotoView;
import com.imrub.shoulder.widget.Title;

public class PhotoPicker extends BaseActivity implements IPhotoPickerContentObserver, IPhotoPickerItem{

	private ListView mListView;
	private PhotoView mPhotoView;
	
	private Title mTitle;

	private PhotoPickerAdapter mListAdapter;
	private PhotoPickerGridHeaderAdapter mHeaderApdater;
	
	private List<IImage> mAllImages;
	private IImage mImage;
	
	private ArrayList<PhotoPickerItem> allItems = new ArrayList<PhotoPickerItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photopicker_view);
		PhotoPickerContentObserver.onCreate(this);
		BitmapManager.instance().onCreate();
		BitmapManager.instance().startImageLoader();
		ImageListManager.getInstance().startWorker(this);
		initView();
	}

	private void loadAllImagesFinish(){
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mHeaderApdater.setImages(mAllImages);
			}
		});
	}
	
	private void initView() {
		initTitle();
		mListView = (ListView) findViewById(R.id.photopicker_listview);
		mPhotoView = (PhotoView)findViewById(R.id.imagegallery_photoview);

		// Add the header view
		GridView gridView = (GridView) findViewById(R.id.photopicker_gridview);

		mHeaderApdater = new PhotoPickerGridHeaderAdapter(
				 BitmapManager.instance(),this);
		mHeaderApdater.setTitleChangeListener(new ITitleable() {
			@Override
			public void onTitleStatusChange(boolean isVisiable) {
				mTitle.setPhotoPickerButtonVisiable(isVisiable);
			}
			
			@Override
			public void onImageClick(int position) {
				if(mAllImages != null){
					mImage = mAllImages.get(position);
				}
			}
		});
		
		mHeaderApdater.setOnPhotoTakeListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onPhotoTake();
			}
		});
		
		gridView.setAdapter(mHeaderApdater);

		// Add the listview item
		mListAdapter = new PhotoPickerAdapter(BitmapManager.instance());
		mListView.setAdapter(mListAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onClickListItem(position);
			}
		});
	}

	private void initTitle() {
		View v = findViewById(R.id.photopicker_title);
		mTitle = new Title(v);
		mTitle.setContent(getString(R.string.photopicker_main_title));
		
		mTitle.setPhotoPickerButtonName(R.string.photopicker_button_name);
		mTitle.setPhotoPickerButtonListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onPhotoPickerClick();
			}
		});
	}

	@Override
	public void onAddNewItem(final PhotoPickerItem item) {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				allItems.add(item);
				mListAdapter.addPhotoPickerItem(item);
				mListAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onAllImages(List<IImage> allImages) {
		mAllImages = allImages;
		loadAllImagesFinish();
	}
	
	@Override
	public void onRake(boolean unmounted, boolean scanning) {
		if(unmounted){
			finish();
		}
	}
	
	private void onPhotoTake(){
		// makesure the file's parent dir exist
		File file = new File(EnvirPath.getSdcardPhotoPickerCapturePath());
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, 102);
	}
	
	private void onClickListItem(int position) {
		launchImageGallery(position);
		ImageListManager.getInstance().setCurrentImageList(allItems.get(position).mImageList);
	}

	private void onPhotoPickerClick(){
    	if(mImage == null){
    		return ;
    	}
    	
    	showLoadingView(R.string.photopicker_handle);
    	
    	final Matrix matrix = mPhotoView.getImageMatrix();
    	final Bitmap bitmap = BitmapManager.instance().getFullBitmap(mImage);
    	
    	ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				if(bitmap != null){
					PhotoPickerImageUtil.capaturePhotoPicker2(bitmap, matrix, EnvirPath.getPhotoPickerCapturePath());
					complete();
				}
			}
		});
    }

	private void complete(){
    	ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dismissLoadingView();
				setResult(RESULT_OK);
				finish();
			}
		});
    }
	
	private void launchImageGallery(int position){
		PhotoPickerItem item = allItems.get(position);
		Uri uri = Images.Media.INTERNAL_CONTENT_URI;
		if (item.needsBucketId()) {
			uri = uri.buildUpon().appendQueryParameter("bucketId", item.mBucketId).build();
		}
		Intent intent = new Intent(this, ImageGallery.class);
		intent.putExtra("name", item.mName);
		startActivityForResult(intent, 101);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 101:
				if(resultCode == Activity.RESULT_OK){
					setResult(RESULT_OK);
					finish();
				}
				break;
			case 102:
				if(resultCode == Activity.RESULT_OK){
					Intent intent = new Intent(this,PhotoPickerNow.class);
					intent.putExtra("uri", EnvirPath.getSdcardPhotoPickerCapturePath());
					startActivityForResult(intent, 101);
				}
				break;
		}
	}
	
	@Override
	protected void onDestroy() {
		PhotoPickerContentObserver.onDestory();
		ImageListManager.getInstance().abortWorker();
		BitmapManager.instance().onDestory();
		super.onDestroy();
	}

}
