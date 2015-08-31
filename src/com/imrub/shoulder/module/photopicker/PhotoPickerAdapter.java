
package com.imrub.shoulder.module.photopicker;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.photopicker.imageUtils.IImageLoader;

public class PhotoPickerAdapter extends BaseAdapter{

	private ArrayList<PhotoPickerItem> mItems;
	private IImageLoader mImageLoader;
	
	public PhotoPickerAdapter(IImageLoader imageLoader){
		this.mItems = new ArrayList<PhotoPickerItem>();
		this.mImageLoader = imageLoader;
	}
	
	public void addPhotoPickerItem(PhotoPickerItem item){
		mItems.add(item);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mItems.size();
	}
	
	public Object getItem(int position) {
		return mItems.get(position);
	};
	
	public long getItemId(int position) {
		return position;
	};
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final PhotoPickerItem item = mItems.get(position);

		AdapterItemHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.photopicker_listitem_view, null);
			TextView title = (TextView)convertView.findViewById(R.id.photopicker_title);
			TextView number = (TextView)convertView.findViewById(R.id.photopicker_number);
			ImageView image = (ImageView)convertView.findViewById(R.id.photopicker_image_view);
			holder = new AdapterItemHolder();
			holder.mNumber = number;
			holder.mTitle = title;
			holder.mImageView = image;
			convertView.setTag(holder);
		} else {
			holder = (AdapterItemHolder)convertView.getTag();
		}

		holder.mTitle.setText(item.mName);
		holder.mNumber.setText(AppContext.getResource().getString(R.string.photopicker_image_number, item.mCount + ""));
		
		if(item.mCount != 0){
			mImageLoader.onImageLoade(new WeakReference<ImageView>(holder.mImageView), item.mImageList.get(0), position);
		}
		return convertView;
	}

	public static class AdapterItemHolder{
		public TextView mTitle;
		public TextView mNumber;
		public ImageView mImageView;
	}

}
