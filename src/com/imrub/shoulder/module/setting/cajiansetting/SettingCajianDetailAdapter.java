package com.imrub.shoulder.module.setting.cajiansetting;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;

public class SettingCajianDetailAdapter extends BaseAdapter{

	private List<AuthorityUserInfo> mData;
	
	public SettingCajianDetailAdapter(List<AuthorityUserInfo> data){
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
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final AuthorityUserInfo user = mData.get(position);
		ViewTag tag = null;
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.setting_user_cajian_item, null);
			ImageView icon = (ImageView)convertView.findViewById(R.id.user_data_image);
			TextView name = (TextView)convertView.findViewById(R.id.user_data_name);
			TextView button = (TextView)convertView.findViewById(R.id.user_data_button);
			tag = new ViewTag(icon, name, button);
			convertView.setTag(tag);
		} else {
			tag = (ViewTag)convertView.getTag();
			if(user.getHeader_logo().equalsIgnoreCase((String)tag.mIcon.getTag())){
				return convertView;
			}
		}
		tag.mName.setText(user.getNick_name());
		return convertView;
	};
	
	private static class ViewTag{
		private ImageView mIcon;
		private TextView mName;
		@SuppressWarnings("unused")
		private TextView mButton;
		public ViewTag(ImageView icon, TextView name, TextView button){
			this.mIcon = icon;
			this.mName = name;
			this.mButton = button;
		}
	}
	
}
