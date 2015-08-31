package com.imrub.shoulder.module.photopicker;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.photopicker.image.IImage;
import com.imrub.shoulder.module.photopicker.imageUtils.BitmapManager;
import com.imrub.shoulder.module.photopicker.imageUtils.IImageLoader;
import com.imrub.shoulder.module.photopicker.imageUtils.ImageLoader;
import com.imrub.shoulder.module.photopicker.imageUtils.ImageLoader.LoadedCallback;
import com.imrub.shoulder.module.photopicker.imageZoom.PhotoAnimationProxy;

public class ImageGalleryAdapter extends BaseAdapter implements IImageLoader{

	private List<IImage> mImageList;
	
	private PhotoAnimationProxy mProxy;
	private Context mContext;
	
	private ITitleable mTitleListener;
	
	public ImageGalleryAdapter(List<IImage> imageList, Context context){
		this.mImageList = imageList;
		this.mProxy = new PhotoAnimationProxy();
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		return mImageList.size();
	}
	
	public Object getItem(int position) {
		return mImageList.get(position);
	};
	
	public long getItemId(int position) {
		return position;
	};
	
	public void setTitleChangeListener(ITitleable listener){
		mTitleListener = listener;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		
		final IImage image = mImageList.get(position);
		
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.photopicker_gridview_item, null);
			ImageView imageView = (ImageView)convertView.findViewById(R.id.photopicker_image_view);
			holder = new ViewHolder();
			holder.mImage = imageView;
		} else {
			holder = (ViewHolder)convertView.getTag();
			String tag = (String)holder.mImage.getTag();
			if(image.getDataPath().equalsIgnoreCase(tag)){
				return convertView;
			}
		}
		
		convertView.setTag(holder);
		
		ImageView imageView = (ImageView)convertView.findViewById(R.id.photopicker_image_view);
		imageView.setImageResource(R.drawable.photopicker_default_item_bg);
		imageView.setTag(image.getDataPath());
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Bitmap bitmap = getFullBitmap(image);
				if(bitmap != null){
					mTitleListener.onImageClick(position);
					mProxy.zoomImageFromThumb(bitmap, v, position, mContext, (GridView)parent,mTitleListener,parent);
				}
			}
		});
		
		onImageLoade(new WeakReference<ImageView>(imageView), image, position);
		return convertView;
	}
	
	@Override
    public void onImageLoade(final WeakReference<ImageView> imageView, final IImage image, final int position) {
    	
		final LruCache<String, Bitmap> cache = BitmapManager.instance().getCache();
		final ImageLoader loader = BitmapManager.instance().getImageLoader();
		
    	Bitmap bitmap = cache.get(image.getDataPath());
    	if(bitmap != null){
    		ImageView imageTarget = imageView.get();
    		if(imageTarget != null){
    			imageTarget.setImageBitmap(bitmap);
    			return ;
    		}
    	}

    	loader.getBitmap(image, new LoadedCallback() {
			@Override
			public void run(final Bitmap result) {
				if(result == null){
					return ;
				}
				String datapath = image.getDataPath();
				if(datapath != null && result != null){
					cache.put(datapath, result);
					final ImageView imageViewTarget = imageView.get();
					if(imageViewTarget != null){
						String datapaths = (String)imageViewTarget.getTag();
						if(datapath.equalsIgnoreCase(datapaths)){
							ThreadFacade.runOnUiThread(new Runnable() {
								public void run() {
									imageViewTarget.setImageBitmap(result);
								}
							});
						}
					}
				}
			}
		}, position);
    }
	
	@Override
	public Bitmap getFullBitmap(IImage image) {
		final LruCache<String, Bitmap> cache = BitmapManager.instance().getCache();
		String path = image.getDataPath() + "full";
		final Bitmap b = cache.get(path);
		if(b == null){
			Bitmap bitmap = image.fullSizeBitmap(IImage.UNCONSTRAINED, AppContext.getScreenWidth()*AppContext.getScreenHeight());
			if(bitmap != null){
				cache.put(path, bitmap);
				return bitmap;
			}
		}
		return b;
	}
	
	public static class ViewHolder{
		public ImageView mImage;
	}
	
}
