package com.imrub.shoulder.module.addrlist;

import java.util.HashMap;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.base.util.ImageSizeUtils;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.base.util.bitmaploader.BitmapLoaderManager;

public class SectionAdapter extends BaseAdapter implements SectionIndexer{

	private static String[] mSections;
	private List<UserFriend> mdata;
	private int mSize;
	
	private HashMap<String, Integer> mCacheKey = new HashMap<String, Integer>();
	
	static {
		String sections = "*ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
		mSections = new String[28];
		for(int i=0 ;i<28; i++){
			mSections[i] = String.valueOf(sections.charAt(i));
		}
	}
	
	public SectionAdapter(List<UserFriend> data){
		mdata = data;
		printUser();
		rebuildCache();
	}
	
	private void printUser(){
		for(UserFriend f : mdata){
			Logger.print("", "uid:"+f.getUid() + " nick:"+f.getNick_name() + " pinyin:"+f.getPinyin()+"\n");
		}
	}
	
	public void rebuildCache(){
		mCacheKey.clear();
		int i = 0;
		for(UserFriend f : mdata){
			char c = f.getPinyin().toUpperCase().charAt(0);
			Integer index = mCacheKey.get(c+"");
			if(index == null){
				mCacheKey.put(c+"",i);
			}
			i++;
		}

		int m=0;
		for(int j=1; j<27; j++){
			String value = mSections[j];
			Integer index = mCacheKey.get(value);
			if(index == null){
				mCacheKey.put(value, m);
			} else {
				m = index;
			}
		}
		
		mSize = mdata.size();
		
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
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserFriend friend = mdata.get(position);
		int type = getItemViewType(position);
		ViewTag tag = null;
		if(convertView == null){
			View view = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.addrlist_item_0, null);
			TextView alph = (TextView)view.findViewById(R.id.addr_list_alph);
			ImageView icon = (ImageView)view.findViewById(R.id.addr_list_icon);
			TextView name = (TextView)view.findViewById(R.id.addr_list_name);
			TextView count = (TextView)view.findViewById(R.id.addr_list_encount_time);
			View line = view.findViewById(R.id.addr_list_line);
			convertView = view;
			tag = new ViewTag(alph, icon, name, count, line, position);
			convertView.setTag(tag);
		} else {
			tag = (ViewTag)convertView.getTag();
		}
		tag.mLine.setVisibility(isShow(position) ? View.VISIBLE : View.GONE);
		tag.mIndex = position;
		initData(tag, type, friend.getNick_name(),friend.getMeet_count(),friend.getPinyin());
		updateImage(tag.mIcon, friend.getHeader_logo());
		return convertView;
	}
	
	private boolean isShow(int currentPosition){
		if(currentPosition == mSize - 1){
			return false;
		}
		return mdata.get(currentPosition).getPinyin().charAt(0)
				== mdata.get(currentPosition+1).getPinyin().charAt(0) ? 
					 true : false;
	}
	
	private void initData(ViewTag tag, int type, String userName, String count,String pinyin){
		if(type != 0){
			tag.mAlph.setVisibility(View.GONE);
		} else {
			tag.mAlph.setText(pinyin.toUpperCase().charAt(0) + "");
			// 不同的话，隐藏之前的line
		}
		tag.mName.setText(userName);
		tag.mCount.setText(AppContext.getResource().getString(R.string.addrlist_encount_time__, count));
	}
	
	private void updateImage(ImageView imageView, String iconUrl){
		int width = AppContext.getDimensionPixelSize(R.dimen.addrlist_icon_width_heigth);
		BitmapLoaderManager.getInstance().loadBitmap(
				ImageSizeUtils.get96x96(iconUrl), 
				imageView, 
				width, width, 
				AppContext.getBitmap(R.drawable.icon_72x72));
	}
	
	@Override
	public Object[] getSections() {
		return mSections;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		 char c = mSections[sectionIndex].charAt(0);
		 Integer index = mCacheKey.get(c + "");
		 if(index == null){
			 if(c == "*".charAt(0)){
				 return 0;
			 } else {
				 return mdata.size()-1;
			 }
		 }
		 return index < 0 ? 1 : index+1;
	}

	@Override
	public int getSectionForPosition(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		if(position == 0){
			return 0;
		}
		String str = mdata.get(position - 1).getPinyin();
		String strs = mdata.get(position).getPinyin();
		if(str.charAt(0) == strs.charAt(0)){
			return 1;
		}
		return 0;
	}
	
	static class ViewTag{
		public TextView mAlph;
		public ImageView mIcon;
		public TextView mName;
		public TextView mCount;
		public View mLine;
		public int mIndex;
		public ViewTag(TextView alph, ImageView icon, TextView name, TextView count, View line, int index){
			this.mAlph = alph;
			this.mIcon = icon;
			this.mName = name;
			this.mCount = count;
			this.mLine = line;
			this.mIndex = index;
		}
	}
	
	
}
