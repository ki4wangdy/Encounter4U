
package com.imrub.shoulder.module.photopicker;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.path.EnvirPath;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.imageprocess.PhotoPickerImageUtil;
import com.imrub.shoulder.module.photopicker.image.IImage;
import com.imrub.shoulder.module.photopicker.imageUtils.BitmapManager;
import com.imrub.shoulder.module.photopicker.imageUtils.ImageListManager;
import com.imrub.shoulder.module.photopicker.imageZoom.PhotoView;
import com.imrub.shoulder.widget.Title;

public class ImageGallery extends BaseActivity {
    
    private List<IImage> mAllImages;
    private IImage mImage;
    
    private GridView mGridView;
    private Title mTitle;
    
    private PhotoView mPhotoView;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.photopicker_imagegallery);

        mAllImages = ImageListManager.getInstance().getCurrentImageList();
        initView();
        
        BitmapManager.instance().startImageLoader();
    }

    private void initView(){
    	initTitle();
    	mGridView = (GridView)findViewById(R.id.grid_view);
    	mPhotoView = (PhotoView)findViewById(R.id.imagegallery_photoview);
    	
        ImageGalleryAdapter adapter = new ImageGalleryAdapter(mAllImages,this);
        adapter.setTitleChangeListener(new ITitleable() {
			@Override
			public void onTitleStatusChange(boolean isVisiable) {
				mTitle.setPhotoPickerButtonVisiable(isVisiable);
			}
			
			@Override
			public void onImageClick(int position) {
				mImage = mAllImages.get(position);
			}
			
		});
        mGridView.setAdapter(adapter);
    }

    private void initTitle(){
		View v = findViewById(R.id.imagegallery_title);
		mTitle = new Title(v);
		
		String name = getIntent().getStringExtra("name");
		mTitle.setContent(name);
		
		mTitle.setPhotoPickerButtonName(R.string.photopicker_button_name);
		mTitle.setPhotoPickerButtonListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				onPhotoPickerClick();
			}
		});
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
    
    @Override
    protected void onDestroy() {
    	BitmapManager.instance().stopImageLoader();
    	super.onDestroy();
    }
    
    // Returns the image list parameter which contains the subset of image/video
    // we want.
//    private ImageManager.ImageListParam allImages(boolean storageAvailable) {
//        if (!storageAvailable) {
//            return ImageManager.getEmptyImageListParam();
//        } else {
//            String uri = (String)getIntent().getExtras().getString("uri");
//            return ImageManager.getImageListParam(
//                    ImageManager.DataLocation.EXTERNAL,
//                    ImageManager.INCLUDE_IMAGES,
//                    ImageManager.SORT_DESCENDING,
//                    (uri != null)
//                    ? Uri.parse(uri).getQueryParameter("bucketId")
//                    : null);
//        }
//    }
    
}
