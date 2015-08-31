package com.imrub.shoulder.module.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.db.facade.RoomFacade;
import com.imrub.shoulder.base.db.table.chat.ChatInfoData;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.ImageSizeUtils;
import com.imrub.shoulder.base.util.TimeUtil;
import com.imrub.shoulder.base.util.bitmaploader.BitmapLoaderManager;

public class ChatInfoAdapter extends BaseAdapter implements INotifyListener{

	public ChatInfoAdapter() {
	}

	@Override
	public int getCount() {
		return RoomFacade.getInstance().getChatRoomCount();
	}

	@Override
	public Object getItem(int position) {
		return RoomFacade.getInstance().getChatRoomData(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ChatInfoData data = RoomFacade.getInstance().getChatRoomData(position);
		ViewTag tag = null; 
		if(convertView == null){
			convertView = LayoutInflater.from(AppContext.getAppContext()).
					inflate(R.layout.main_chatinfo_view, null);
			ImageView icon = (ImageView)convertView.findViewById(R.id.chat_image_icon);
			TextView msgCount = (TextView)convertView.findViewById(R.id.chat_msg_count);
			TextView title = (TextView)convertView.findViewById(R.id.chat_title);
			TextView content = (TextView)convertView.findViewById(R.id.chat_content);
			TextView time = (TextView)convertView.findViewById(R.id.chat_time);
			ImageView nosound = (ImageView)convertView.findViewById(R.id.chat_nosound_icon);
			tag = new ViewTag(icon, nosound, msgCount, title, content, time);
			convertView.setTag(tag);
		} else {
			tag = (ViewTag)convertView.getTag();
		}
		
		updateView(tag, data);
		return convertView;
	}

	private void updateView(ViewTag tag, ChatInfoData data){
		int count = data.getNewMsgCount();
		if(count > 0){
			tag.msgCount.setText(count + ""); 
		}
		
		tag.msgCount.setVisibility(count == 0 ? View.INVISIBLE : View.VISIBLE);
		
		tag.title.setText(data.getChatTitle());
		tag.content.setText(data.getChatContent());
		tag.time.setText(TimeUtil.longTimeToString(data.getChatTime()));
		
		int width = AppContext.getDimensionPixelSize(R.dimen.main_ui_chat_view_height);
		BitmapLoaderManager.getInstance().loadBitmap(
				ImageSizeUtils.get96x96(data.getIconUrl()), 
				tag.icon, 
				width, 
				width, 
				AppContext.getBitmap(R.drawable.icon_96x96));
		
	}
	
	@Override
	public void notifyChange() {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
	
	public static class ViewTag{
		public ImageView icon;
		public ImageView nosound;
		public TextView msgCount;
		public TextView title;
		public TextView content;
		public TextView time;
		public ViewTag(ImageView icon, ImageView nosound, TextView msgCount, 
				TextView title, TextView content, TextView time){
			this.icon = icon;
			this.nosound = nosound;
			this.msgCount = msgCount;
			this.title = title;
			this.content = content;
			this.time = time;
		}
	}
	
	
}
