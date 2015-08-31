package com.imrub.shoulder.module.chat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.MessageFacade;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;

public class ChatRoomAdapter extends BaseAdapter implements INotifyChatRoomListener{

	private String mId;
	private String mCurrentUid;
	
	private ChatRoomAdapterUtil mUtil;
	
	public ChatRoomAdapter(String id) {
		this.mCurrentUid = UserInfo.getInstance().getUid();
		this.mId = id;
		this.mUtil = new ChatRoomAdapterUtil();
	}

	@Override
	public int getCount() {
		return MessageFacade.getInstance().getChatRoomMessageCount(mId);
	}

	public void addData(final String text) {
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				MessageFacade.getInstance().putChatRoomMessage(mId, text, MsgTypeConstant.TextMsgType);
			}
		});
	}

	@Override
	public Object getItem(int position) {
		return MessageFacade.getInstance().getChatRoomMessage(mId, position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}
	
	@Override
	public int getItemViewType(int position) {
		Message msg = MessageFacade.getInstance().getChatRoomMessage(mId, position);
		if(msg.getMsgType() == MsgTypeConstant.SystemMsgType){
			return 1;
		} 
		if(msg.getUid().equalsIgnoreCase(mCurrentUid)){
			return 2;
		}
		return 3;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message item = MessageFacade.getInstance().getChatRoomMessage(mId, position);
		if (convertView == null) {
			convertView = mUtil.createView(item,mCurrentUid);
		}
		mUtil.initView(item, convertView);
		return convertView;
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
	
	public static class ViewTag {
		public TextView content;
		public TextView time;
		public ImageView icon;
		public ViewTag(TextView content, TextView time, ImageView icon) {
			this.content = content;
			this.time = time;
			this.icon = icon;
		}
	}

}
