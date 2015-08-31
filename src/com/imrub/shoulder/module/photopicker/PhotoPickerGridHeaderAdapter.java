package com.imrub.shoulder.module.photopicker;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.photopicker.image.IImage;
import com.imrub.shoulder.module.photopicker.imageUtils.IImageLoader;
import com.imrub.shoulder.module.photopicker.imageZoom.PhotoAnimationProxy;

public class PhotoPickerGridHeaderAdapter extends BaseAdapter {

	private IImageLoader mImageLoader;
	private ITitleable mTitleListener;
	
	private Context mContext;
	private PhotoAnimationProxy mProxy;
	
	private Action<View> mPhotoTakeListener;
	
	private List<IImage> mImages;
	
	public PhotoPickerGridHeaderAdapter(IImageLoader imageLoader, Context context){
		this.mImageLoader = imageLoader;
		this.mContext = context;
		this.mProxy = new PhotoAnimationProxy();
	}
	
	public void setImages(List<IImage> images){
		this.mImages = images;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(mImages == null){
			return 0;
		}
		int size = mImages.size();
		return size > 12 ? 12 : size;
	}
	
	public Object getItem(int position) {
		if(position == 0 || mImages == null){
			return null;
		}
		return mImages.get(position);
	};
	
	public long getItemId(int position) {
		return position;
	};
	
	public void setTitleChangeListener(ITitleable listener){
		mTitleListener = listener;
	}
	
	public void setOnPhotoTakeListener(Action<View> action){
		mPhotoTakeListener = action;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		
		if(position == 0){
			View view = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.photopicker_gridview_item, null);
			ImageView image = (ImageView)view.findViewById(R.id.photopicker_image_view);
			image.setImageResource(R.drawable.photopicker_photo_bg);
			image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mPhotoTakeListener.onExecute(v);
				}
			});
			return view;
		}
		
		final IImage image = mImages.get(position-1);
		
		ImageView imageView = null;
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.photopicker_gridview_item, null);
		} else {
			imageView = (ImageView)convertView.findViewById(R.id.photopicker_image_view);
			String tag = (String)imageView.getTag();
			if(image.getDataPath().equalsIgnoreCase(tag)){
				return convertView;
			}
		}
		
		imageView = (ImageView)convertView.findViewById(R.id.photopicker_image_view);
		imageView.setImageResource(R.drawable.photopicker_default_item_bg);
		imageView.setTag(image.getDataPath());
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Bitmap bitmap = mImageLoader.getFullBitmap(image);
				if(bitmap != null){
					mTitleListener.onImageClick(position-1);
					mProxy.zoomImageFromThumb(bitmap, v, position, mContext, (GridView)parent,mTitleListener, (View)parent.getParent().getParent());
				}
			}
		});
		
		mImageLoader.onImageLoade(new WeakReference<ImageView>(imageView), image, position);
		return convertView;
	}
	
}
