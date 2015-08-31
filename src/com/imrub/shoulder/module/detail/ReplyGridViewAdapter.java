package com.imrub.shoulder.module.detail;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.model.detail.RequestInfo;


public class ReplyGridViewAdapter extends BaseAdapter{

	private List<RequestInfo> mData;
	private String mUserName;
	
	public ReplyGridViewAdapter(List<RequestInfo> data, String userName){
		this.mData = data;
		this.mUserName = userName;
	}
	
	public void addRequestInfo(RequestInfo request){
		int count = mData.size();
		if(count == 3){
			mData.remove(0);
		}
		mData.add(request);
		notifyDataSetChanged();
	}
	
	public void addRequestInfos(List<RequestInfo> infos){
		mData.clear();
		mData.addAll(infos);
		notifyDataSetChanged();
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
		
		ViewTag tag = null;
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.detail_reply_view, null);
			TextView content = (TextView)convertView.findViewById(R.id.content);
			tag = new ViewTag(content);
			convertView.setTag(tag);
		} else {
			tag = (ViewTag)convertView.getTag();
		}
		
		RequestInfo info = mData.get(position);
		if(info.getType() == RequestInfo.TypeReply){
			tag.mTextView.setText(AppContext.getAppContext().getString(R.string.detail_reply_text_content1, info.getContent()));
		} else {
			tag.mTextView.setText(mUserName + "£º"+info.getContent());
		}
		
		return convertView;
	}
	
	private static class ViewTag{
		public TextView mTextView;
		public ViewTag(TextView textView){
			this.mTextView = textView;
		}
	}
	
}
