package com.imrub.shoulder.module.addrlist.more;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.db.table.addr.AddrlistNewData;
import com.imrub.shoulder.base.util.ImageSizeUtils;
import com.imrub.shoulder.base.util.bitmaploader.BitmapLoaderManager;

public class AddrlistNewAdapter extends BaseAdapter{

	private List<AddrlistNewData> mdata;
	private Action<AddrlistNewData> mListener;
	private OnClickListener mClickListener;
	
	public AddrlistNewAdapter(List<AddrlistNewData> data){
		mdata = data;
	}
	
	public void resetData(List<AddrlistNewData> data){
		mdata = data;
	}
	
	@Override
	public int getCount() {
		return mdata.size();
	}

	@Override
	public Object getItem(int position) {
		return mdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setOnAgreeListener(Action<AddrlistNewData> listener){
		mListener = listener;
	}
	
	public void setOnClickListener(OnClickListener listener){
		mClickListener = listener;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AddrlistNewData data = mdata.get(position);
		ViewTag tag = null;
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.addrlist_new_item, null);
			ImageView icon = (ImageView)convertView.findViewById(R.id.addrlist_new_icon);
			TextView name = (TextView)convertView.findViewById(R.id.addrlist_new_name);
			TextView signature = (TextView)convertView.findViewById(R.id.addrlist_new_signature);
			TextView toagree = (TextView)convertView.findViewById(R.id.addrlist_new_button_toagree);
			TextView hasAgree = (TextView)convertView.findViewById(R.id.addrlist_new_hasagree);
			TextView pleasewait = (TextView)convertView.findViewById(R.id.addrlist_new_pleasewait);
			tag = new ViewTag(icon, name, signature, toagree, hasAgree, pleasewait,data);
			convertView.setTag(tag);
		} else {
			tag = (ViewTag)convertView.getTag();
		}
		tag.mData = data;
		
		String name = data.getName();
		tag.mName.setText(name != null ? name : "");
		String signature = data.getSignature();
		tag.mSignature.setText(signature != null ? signature : "");
		
		if(data.getIsnew() == 1){
			convertView.setBackgroundResource(R.drawable.addrlist_new_friend_needsee_bg);
		} else {
			convertView.setBackgroundResource(R.drawable.addrlist_new_friend_bg);
		}
		
		updateImage(tag.micon, tag.mData.getIconUrl());
		registerListener(tag,convertView, data);
		updateStatus(tag, data);
		return convertView;
	}

	private void updateImage(ImageView imageView, String iconUrl){
		int width = AppContext.getDimensionPixelSize(R.dimen.addrlist_yuan_icon_width_height);
		BitmapLoaderManager.getInstance().loadBitmap(
				ImageSizeUtils.get96x96(iconUrl), 
				imageView, 
				width, width, 
				AppContext.getBitmap(R.drawable.icon_96x96));
	}
	
	private void registerListener(final ViewTag tag, final View view, final AddrlistNewData data){
		tag.mtoAgree.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener != null){
					mListener.onExecute(tag.mData);
				}
			}
		});
		
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mClickListener != null){
					tag.mName.setTag(data);
					mClickListener.onClick(tag.mName);
				}
			}
		});
		
	}
	
	private void updateStatus(ViewTag tag, AddrlistNewData data){
		int type = data.getStatus();
		switch (type) {
			case AddrlistNewConstant.StatusType.Status_ToAgree:
				tag.mtoAgree.setVisibility(View.VISIBLE);
				tag.mHasAgree.setVisibility(View.GONE);
				tag.mPleaseWait.setVisibility(View.GONE);
				break;
			case AddrlistNewConstant.StatusType.Status_HasAgree:
				tag.mtoAgree.setVisibility(View.GONE);
				tag.mHasAgree.setVisibility(View.VISIBLE);
				tag.mPleaseWait.setVisibility(View.GONE);
				break;
			case AddrlistNewConstant.StatusType.Status_NeedCheck:
				tag.mtoAgree.setVisibility(View.GONE);
				tag.mHasAgree.setVisibility(View.GONE);
				tag.mPleaseWait.setVisibility(View.VISIBLE);
				break;
		}
	}
	
	private static class ViewTag{
		public ImageView micon;
		public TextView mName;
		public TextView mSignature;
		public TextView mtoAgree;
		public TextView mHasAgree;
		public TextView mPleaseWait;
		public AddrlistNewData mData;
		public ViewTag(ImageView icon, TextView name, 
				TextView signature, TextView toagree,
				TextView hasAgree, TextView pleasewait, AddrlistNewData data){
			this.micon = icon;
			this.mName = name;
			this.mSignature = signature;
			this.mtoAgree = toagree;
			this.mHasAgree = hasAgree;
			this.mPleaseWait = pleasewait;
			this.mData = data;
		}
	}
	
}
