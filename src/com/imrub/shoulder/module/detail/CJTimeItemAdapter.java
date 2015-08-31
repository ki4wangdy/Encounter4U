package com.imrub.shoulder.module.detail;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.util.TimeUtil;


public class CJTimeItemAdapter extends BaseAdapter{

	private List<Integer> mData;
	
	public CJTimeItemAdapter(List<Integer> data) {
		mData = data;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewTag tag = null;
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.main_viewpager_left_item, null);
			TextView time = (TextView)convertView.findViewById(R.id.detail_viewpager_left_time);
			TextView count = (TextView)convertView.findViewById(R.id.detail_viewpager_left_count);
			tag = new ViewTag(time, count);
			convertView.setTag(tag);
		}
		
		if(tag == null){
			tag = (ViewTag)convertView.getTag();
		}
		
		updateView(tag,position);
		return convertView;
	}

	private void updateView(ViewTag tag, int position){
		tag.mTime.setText(TimeUtil.longTimeToString(mData.get(position)));
		tag.mCount.setText(AppContext.getAppContext().getResources().
				getString(R.string.detail_viewpager_left_count,position+1));
	}
	
	private class ViewTag{
		public TextView mTime;
		public TextView mCount;
		public ViewTag(TextView time, TextView count){
			mTime = time;
			mCount = count;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
}
