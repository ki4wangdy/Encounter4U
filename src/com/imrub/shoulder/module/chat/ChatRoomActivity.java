package com.imrub.shoulder.module.chat;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.db.facade.MessageFacade;
import com.imrub.shoulder.base.db.facade.RoomFacade;
import com.imrub.shoulder.base.db.table.im.Message;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.im.MessageIdUtils;
import com.imrub.shoulder.widget.SoftInputUtil;

public class ChatRoomActivity extends Fragment {

	private ListView mListView;
	private ChatRoomAdapter mAdapter;
	
	private EditText mEditText;
	private View mheaderView;
	
	private String mId;
	private boolean mIsLoading;
	private boolean hasNoData = false;
	
	public ChatRoomActivity(){
	}
	
	public ChatRoomActivity(String id){
		mId = id;
		RoomFacade.getInstance().clearMsgCount(mId);
		RoomFacade.getInstance().setCurrentChatRoomId(mId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		RoomFacade.getInstance().setCurrentChatRoomId(null);
		RoomFacade.getInstance().clearCacheData(mId);	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.main_chatroom_view, container, false);
		mListView = (ListView)view.findViewById(R.id.chat_room_list);
		mAdapter = new ChatRoomAdapter(mId);
		RoomFacade.getInstance().setChatRoomListener(mAdapter);
		
		int size = MessageFacade.getInstance().getChatRoomMessageCount(mId);
		if(size < RoomFacade.BUFFER_SIZE){
			hasNoData = true;
		}
		
		mheaderView = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.chatroom_header, null);
		if(!hasNoData){
			mListView.addHeaderView(mheaderView);
			mheaderView.setVisibility(View.GONE);
		}
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				SoftInputUtil.hideSoftInput(mListView.getWindowToken());
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {		
				if(firstVisibleItem == 0 && !mIsLoading && visibleItemCount != 0 && !hasNoData){
					mIsLoading = true;
					mheaderView.setVisibility(View.VISIBLE);
					loadMoreData();
				}
			}
		});
		
		mEditText = (EditText)view.findViewById(R.id.edit_text);

		TextView button = (TextView)view.findViewById(R.id.chat_room_send_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = mEditText.getText().toString();
				if(text.length() > 0){
					mAdapter.addData(text);
					mEditText.setText("");
				}
			}
		});
		
		mListView.setSelection(MessageFacade.getInstance().getChatRoomMessageCount(mId) - 1);
		return view;
	}

	private void loadMoreData(){
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				sleepSomeTime(500);
				Message data = MessageFacade.getInstance().getChatRoomMessage(mId, 0);
				int size = 0;
				if(data != null){
					String roomId = MessageIdUtils.createRoomId(mId);
					List<Message> datas = MessageFacade.queryMessageCountBaseonTime(roomId, data, 10);
					if(datas != null){
						size = datas.size();
						if(size < 10){
							hasNoData = true;
						}
						MessageFacade.getInstance().putChatRoomMessage(mId, datas);
					} else {
						hasNoData = true;
						onLoadMoreDataComplished(true,size);
						return ;
					}
				}
				onLoadMoreDataComplished(false,size);
			}
		});
	}

	private static void sleepSomeTime(long time){
		try{
			Thread.sleep(time);
		}catch(Exception e){
		}
	}
	
	private void onLoadMoreDataComplished(final boolean isRemove, final int size){
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(isRemove){
					mListView.removeHeaderView(mheaderView);
					mListView.setAdapter(mAdapter);
					mListView.setSelection(size);
					mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					mIsLoading = false;
					return ;
				}
				mheaderView.setVisibility(View.INVISIBLE);
				mListView.setAdapter(mAdapter);
				mIsLoading = false;
				if(size != -1){
					mListView.setSelectionFromTop(size + 1, mheaderView.getHeight());
				}
				mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			}
		});
	}
}
