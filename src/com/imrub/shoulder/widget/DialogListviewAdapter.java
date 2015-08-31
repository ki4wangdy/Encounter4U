package com.imrub.shoulder.widget;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;

public class DialogListviewAdapter extends BaseAdapter{

	private List<Integer> mData;
	
	public DialogListviewAdapter(List<Integer> data){
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewTag viewTag = null;
		if(convertView == null){
			View view = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.dialog_two_list_itemview, null);;
			convertView = view;
			TextView textView = (TextView)view.findViewById(R.id.item);
			viewTag = new ViewTag(textView);
			convertView.setTag(viewTag);
		} else {
			viewTag = (ViewTag)convertView.getTag();
		}
		
		Integer item = mData.get(position);
		viewTag.mText.setText(""+item);
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class ViewTag{
		public TextView mText;
		public ViewTag(TextView textView){
			this.mText = textView;
		}
	}
	
}
