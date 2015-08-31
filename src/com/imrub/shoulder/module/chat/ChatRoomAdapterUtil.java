package com.imrub.shoulder.module.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.FriendFacade;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.base.util.ImageSizeUtils;
import com.imrub.shoulder.base.util.TimeUtil;
import com.imrub.shoulder.base.util.bitmaploader.BitmapLoaderManager;
import com.imrub.shoulder.module.chat.ChatRoomAdapter.ViewTag;
import com.imrub.shoulder.module.im.msg.MessageFactory;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;

public class ChatRoomAdapterUtil {

	public View createView(Message item, String mCurrentUid){
		View converView = null;
		int msgType = item.getMsgType();
		switch (msgType) {
			case MsgTypeConstant.TextMsgType:
				converView = createTextView(item, mCurrentUid);
				break;
			case MsgTypeConstant.SystemMsgType:
				converView = createSystemMsgView(item);
				break;
		}
		return converView;
	}
	
	public void initView(Message item, View view){
		int msgtype = item.getMsgType();
		switch (msgtype) {
			case MsgTypeConstant.TextMsgType:
				initTextView(item, view);
				break;
			case MsgTypeConstant.SystemMsgType:
				initSystemMessage(item, view);
				break;
		}
		initLogo(item,view);
	}

	private void initLogo(Message item, View view){
		ViewTag tag = (ViewTag)view.getTag();
		String uid = item.getUid();
		String currentUid = UserInfo.getInstance().getUid();
		int width = AppContext.getDimensionPixelSize(R.dimen.msg_time_size);
		if(currentUid.equalsIgnoreCase(uid)){
			BitmapLoaderManager.getInstance().loadBitmap(
					ImageSizeUtils.get96x96(UserInfo.getInstance().getUserLogo()), 
					tag.icon, 
					width, 
					width, 
					AppContext.getBitmap(R.drawable.icon_96x96));
		} else {
			BitmapLoaderManager.getInstance().loadBitmap(
					ImageSizeUtils.get96x96(FriendFacade.getInstance().getFriendLogo(uid)), 
					tag.icon, 
					width, 
					width, 
					AppContext.getBitmap(R.drawable.icon_96x96));
		}
	}
	
	private View createTextView(Message item, String mCurrentUid){
		ViewTag tag;
		View convertView ;
		if (!mCurrentUid.equalsIgnoreCase(item.getUid())) {
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(
					R.layout.chat_room_type_left, null);
		} else {
			convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(
					R.layout.chat_room_type_right, null);
		}
		ImageView icon = (ImageView)convertView.findViewById(R.id.chat_room_icon);
		TextView time = (TextView) convertView.findViewById(R.id.chat_room_time);
		TextView content = (TextView) convertView.findViewById(R.id.content);
		tag = new ViewTag(content, time, icon);
		convertView.setTag(tag);
		return convertView;
	}
	
	private void initTextView(Message item, View view){
		ViewTag tag = (ViewTag)view.getTag();
		tag.content.setText(MessageFactory.getMessageContent(item));
		if(item.getShowTime() == 1){
			tag.time.setVisibility(View.VISIBLE);
			tag.time.setText(TimeUtil.getTime(Long.parseLong(item.getMessageTime())));
		} else {
			tag.time.setVisibility(View.GONE);
		}		
	}

	private View createSystemMsgView(Message item){
		View convertView = LayoutInflater.from(AppContext.getAppContext()).inflate(
				R.layout.chat_room_type_system_msg, null);
		ImageView icon = (ImageView)convertView.findViewById(R.id.chat_room_icon);
		TextView time = (TextView)convertView.findViewById(R.id.chat_room_time);
		TextView content = (TextView)convertView.findViewById(R.id.chat_room_system_msg);
		ViewTag tag = new ViewTag(content, time, icon);
		convertView.setTag(tag);
		return convertView;
	}
	
	private void initSystemMessage(Message item, View view){
		ViewTag tag = (ViewTag)view.getTag();
		tag.content.setText(MessageFactory.getMessageContent(item));
		tag.time.setVisibility(View.VISIBLE);
		tag.time.setText(TimeUtil.getTime(Long.parseLong(item.getMessageTime())));
	}
	
}
