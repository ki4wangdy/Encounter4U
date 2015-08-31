package com.imrub.shoulder.module.photopicker;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.path.EnvirPath;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.imageprocess.PhotoPickerImageUtil;
import com.imrub.shoulder.module.photopicker.image.IImage;
import com.imrub.shoulder.module.photopicker.image.IImageList;
import com.imrub.shoulder.module.photopicker.imageUtils.ImageManager;
import com.imrub.shoulder.module.photopicker.imageZoom.PhotoView;
import com.imrub.shoulder.widget.Title;

public class PhotoPickerNow extends BaseActivity{

	private Title mTitle;
	private PhotoView mPhotoView;
	
	private String mPath;
	private IImage mImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photopicker_now);
		initView();
	}
	
	private void initView(){
		initTitle();
		mPath = getIntent().getStringExtra("uri");
		mPhotoView = (PhotoView)findViewById(R.id.imagegallery_photoview);
		
		IImageList list = ImageManager.makeSingleImageList(getContentResolver(), Uri.fromFile(new File(mPath)));
		mImage = list.getImageAt(0);
		
		mPhotoView.setImageBitmap(mImage.fullSizeBitmap(IImage.UNCONSTRAINED, 
				AppContext.getScreenHeight() * AppContext.getScreenWidth()));
	}
	
	private void initTitle() {
		View v = findViewById(R.id.imagegallery_title);
		mTitle = new Title(v);
		mTitle.setContent("");
		
		mTitle.setPhotoPickerButtonName(R.string.photopicker_button_name);
		mTitle.setPhotoPickerButtonVisiable(true);
		mTitle.setPhotoPickerButtonListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onPhotoPickerClick();
			}
		});
	}
	
	private void onPhotoPickerClick(){
    	
    	showLoadingView(R.string.photopicker_handle);
    	
    	final Matrix matrix = mPhotoView.getImageMatrix();
    	final Bitmap bitmap = mImage.fullSizeBitmap(IImage.UNCONSTRAINED, 
				AppContext.getScreenHeight() * AppContext.getScreenWidth());
    	
    	ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				PhotoPickerImageUtil.capaturePhotoPicker2(bitmap, matrix, EnvirPath.getPhotoPickerCapturePath());
				complete();
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
	
}
