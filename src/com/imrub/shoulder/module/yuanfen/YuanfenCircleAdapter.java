package com.imrub.shoulder.module.yuanfen;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;

public class YuanfenCircleAdapter extends BaseAdapter{

	private List<YuanfenCircleData> mData;
	private Action<View> mNoContentListener;
	
	public YuanfenCircleAdapter(List<YuanfenCircleData> data){
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
	public int getItemViewType(int position) {
		return mData.get(position).getMsgType();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setOnEmptyClickListener(Action<View> listener){
		mNoContentListener = listener;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final YuanfenCircleData data = mData.get(position);
		if(convertView == null){
			return initEmptyView(data);
		}
		return convertView;
	}

	private View initEmptyView(YuanfenCircleData data){
		View v = null;
		if(data.getMsgType() == YuanfenCircleType.Emtpy_Type){
			v = LayoutInflater.from(AppContext.getAppContext()).
					inflate(R.layout.yuanfencircle_content_empty, null);
			initNoContentView(v);
		} else {
			v = LayoutInflater.from(AppContext.getAppContext()).
					inflate(R.layout.yuanfencircle_content_type_text, null);
			ViewStub stub = (ViewStub)v.findViewById(R.id.yuanfencircle_block);
			stub.inflate();
		}
		return v;
	}
	
	private void initNoContentView(View view){
		view.findViewById(R.id.yuanfencircle_content_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mNoContentListener != null){
					mNoContentListener.onExecute(v);
				}
			}
		});
	}
	
}
