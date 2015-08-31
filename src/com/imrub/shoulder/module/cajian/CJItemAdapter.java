package com.imrub.shoulder.module.cajian;

import java.util.List;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.db.table.cajian.WifiUserData;
import com.imrub.shoulder.base.util.ImageSizeUtils;
import com.imrub.shoulder.base.util.TimeUtil;
import com.imrub.shoulder.base.util.bitmaploader.BitmapLoaderManager;
import com.imrub.shoulder.module.model.cajian.CJUserInfoContainer;

public class CJItemAdapter extends BaseAdapter{

	public CJItemAdapter(){
	}
	
	@Override
	public int getCount() {
		return CJUserInfoContainer.getInstance().getCJUserInfo().size();
	}
	
	@Override
	public Object getItem(int position) {
		return CJUserInfoContainer.getInstance().getCJUserInfo().get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewTag tag = null;
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).
					inflate(R.layout.main_adapter_view,null);
			ImageView logo = (ImageView)convertView.findViewById(R.id.user_logo);
			TextView name = (TextView)convertView.findViewById(R.id.user_name);
			TextView encounterTime = (TextView)convertView.findViewById(R.id.user_encounter_time);
			TextView encounterCount = (TextView)convertView.findViewById(R.id.user_encounter);
			TextView signature = (TextView)convertView.findViewById(R.id.user_signature);
			tag = new ViewTag(logo, name, encounterTime, encounterCount, signature);
			convertView.setTag(tag);
		}
		
		if(tag == null){
			tag = (ViewTag)convertView.getTag();
		}
		
		updateView(tag,position);
		return convertView;
	}

	private void updateView(ViewTag tag, int position){
		tag.index = position;
		final List<WifiUserData> data = CJUserInfoContainer.getInstance().getCJUserInfo();
		WifiUserData info = data.get(position);
		if(tag.logo.getTag() == null || ((String)tag.logo.getTag()).equals(info.getHeader_logo())){
			// set image resource from network
			tag.logo.setTag(info.getHeader_logo());
		}
		
		tag.name.setText(info.getNick_name());
		tag.encounterCount.setText(AppContext.getAppContext().
				getString(R.string.main_ui_encounter,info.getMeet_count()));
		
		tag.signature.setText(info.getDescription());
		tag.encounterTime.setText(AppContext.getAppContext().getString(R.string.
				main_ui_encounter_time, TimeUtil.longTimeToString(info.getMeet_time())));
		
		int width = AppContext.getDimensionPixelSize(R.dimen.image_width);
		BitmapLoaderManager.getInstance().loadBitmap(ImageSizeUtils.get140x140(info.getHeader_logo()), tag.logo, 
				width, width,
				BitmapFactory.decodeResource(AppContext.getResource(), R.drawable.icon_66x66));
		
	}
	
	public static class ViewTag{
		public int index;
		public ImageView logo;
		public TextView name;
		public TextView encounterTime;
		public TextView encounterCount;
		public TextView signature;
		public ViewTag(ImageView logo, TextView name, TextView encounterTime, 
				TextView encounterCount, TextView signature){
			this.logo = logo;
			this.name = name;
			this.encounterCount = encounterCount;
			this.encounterTime = encounterTime;
			this.signature = signature;
		}
	}
	
}
