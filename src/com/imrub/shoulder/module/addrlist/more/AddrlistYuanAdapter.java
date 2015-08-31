package com.imrub.shoulder.module.addrlist.more;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.util.ImageSizeUtils;
import com.imrub.shoulder.base.util.bitmaploader.BitmapLoaderManager;
import com.imrub.shoulder.module.model.AddrlistYuanData;

public class AddrlistYuanAdapter extends BaseAdapter{

	private List<AddrlistYuanData> mData;
	
	public AddrlistYuanAdapter(List<AddrlistYuanData> data){
		this.mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AddrlistYuanData data = mData.get(position);
		ViewTag tag = null;
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.addrlist_yuan_item, null);
			ImageView icon = (ImageView)convertView.findViewById(R.id.addrlist_yuan_icon);
			TextView name = (TextView)convertView.findViewById(R.id.addrlist_yuan_name);
			TextView signature = (TextView)convertView.findViewById(R.id.addrlist_yuan_signature);
			TextView time = (TextView)convertView.findViewById(R.id.addrlist_yuan_encountertime);
			tag = new ViewTag(icon, name, signature, time);
			convertView.setTag(tag);
		} else {
			tag = (ViewTag)convertView.getTag();
		}
		tag.mName.setText(data.getName());
		tag.mSignature.setText(data.getSignature());
		tag.mEncounter.setText(data.getEncounterTime());
		updateImageView(tag.micon, "");
		return convertView;
	}

	private void updateImageView(ImageView imageView, String iconUrl){
		int width = AppContext.getDimensionPixelSize(R.dimen.addrlist_yuan_icon_width_height);
		BitmapLoaderManager.getInstance().loadBitmap(
				ImageSizeUtils.get96x96(iconUrl), 
				imageView, 
				width, width, 
				AppContext.getBitmap(R.drawable.icon_96x96));

	}
	
	private static class ViewTag{
		public ImageView micon;
		public TextView mName;
		public TextView mSignature;
		public TextView mEncounter;
		public ViewTag(ImageView icon, TextView name, 
				TextView signature, TextView encounter){
			this.micon = icon;
			this.mName = name;
			this.mSignature = signature;
			this.mEncounter = encounter;
		}
		
	}
	
}
