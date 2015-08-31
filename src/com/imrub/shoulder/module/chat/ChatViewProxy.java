package com.imrub.shoulder.module.chat;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.net.NetworkManager;
import com.imrub.shoulder.base.db.facade.FriendFacade;
import com.imrub.shoulder.base.db.facade.RoomFacade;
import com.imrub.shoulder.base.db.table.chat.ChatInfoData;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.ITitleListener;
import com.imrub.shoulder.module.ProgressBarAnimationListener;
import com.imrub.shoulder.module.addrlist.IFriendLogo;
import com.imrub.shoulder.module.im.IConnectionListener;
import com.imrub.shoulder.module.im.client.IMClient;
import com.imrub.shoulder.module.main.MainUIActivity;
import com.imrub.shoulder.module.main.SatelliteViewManager;
import com.imrub.shoulder.widget.NotificationFactory;

public class ChatViewProxy extends Fragment implements IConnectionListener,IFriendLogo{
	
	private TextView mWarningView;
	private ListView mChatInfo;
	
	private ChatInfoAdapter mAdapter;
	private ITitleListener mTitleListener;
	
	private boolean isinited = false;
	
	private WeakReference<FragmentManager> mFragmentManager;
	
	public ChatViewProxy(){
	}
	
	public ChatViewProxy(ITitleListener titleListener){
		mTitleListener = titleListener;
		FriendFacade.getInstance().registerLogoChangeListener(this);
	}

	@Override
	public void onLogoChange(final String uid, final String newLogo) {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return LayoutInflater.from(AppContext.getAppContext().getApplicationContext()).
				inflate(R.layout.main_chatinfo, container, false);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(!isinited && isVisibleToUser){
			ThreadFacade.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateMainUI();
				}
			},MainUIActivity.LazyLoadTime);
			isinited = true;
		}
	}
	
	private void updateMainUI(){
		initView(getView());
		setInVisiableView();
	}
	
	private void setInVisiableView(){
		final View v = getView().findViewById(R.id.progress_bar);
		Animation animation = AnimationUtils.loadAnimation(AppContext.getAppContext(), 
				R.anim.default_progressbar_out);
		animation.setAnimationListener(new ProgressBarAnimationListener(v));
		v.startAnimation(animation);
	}
	
	private void initView(View view){
		mWarningView = (TextView)view.findViewById(R.id.main_ui_header_setting);
		
		boolean isConnected = NetworkManager.isConnectingToInternet(AppContext.getAppContext());
		mWarningView.setVisibility(isConnected ? View.GONE : View.VISIBLE);
		
		mChatInfo = (ListView)view.findViewById(R.id.chat_list);
		
		mAdapter = new ChatInfoAdapter();
		mChatInfo.setAdapter(mAdapter);		
		
		RoomFacade.getInstance().setChatInfoDataListener(mAdapter);
		IMClient.getInstance().registerConnectionListener(this);

		mChatInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				return true;
			}
		});
		
		mChatInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onClick(position);
			}
		});
		
		view.findViewById(R.id.chat_info_block).setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		String chatRoomId = getActivity().getIntent().getStringExtra(NotificationFactory.CHAT_ROOM_ID);
		String roomName = getActivity().getIntent().getStringExtra(NotificationFactory.CHAT_ROOM_NAME);
		if(chatRoomId != null){
			handleMsgIntent(chatRoomId,roomName);
			getActivity().getIntent().putExtra(NotificationFactory.CHAT_ROOM_ID, -1);
		}
	}
	
	public void handleMsgIntent(String chatRoomId, String roomName){
		onEnterChatRoom(chatRoomId,roomName);
	}
	
	private synchronized void onClick(int position){
		if(ChatViewFragmentManager.getInstance().getCurrentFragment() != null){
			return ;
		}
		ChatInfoData data = RoomFacade.getInstance().getChatRoomData(position);
		if(mTitleListener != null){
			mTitleListener.onEnterChatRoom(data.getChatTitle());
		}
		enterChatRoom(data.getId());
	}

	private synchronized void onEnterChatRoom(String roomId, String titleName){
		if(mTitleListener != null){
			mTitleListener.onEnterChatRoom(titleName);
		}
		enterChatRoom(roomId+"");
	}
	
	private void enterChatRoom(String id){
		
		// clear the notify bar
		NotificationFactory.cancel(id);
		
		if(mFragmentManager == null && getActivity() != null){
			FragmentActivity activity = getActivity();
			FragmentManager manager = activity.getSupportFragmentManager();
			mFragmentManager = new WeakReference<FragmentManager>(manager);
		}
		
		if(mFragmentManager != null){
			FragmentManager manager = mFragmentManager.get();
			if(manager != null){
				FragmentTransaction trans = manager.beginTransaction();
				trans.setCustomAnimations(R.anim.default_anim_in, R.anim.default_anim_out);
				Fragment fragment = new ChatRoomActivity(id);
				trans.add(R.id.root_layout, fragment).commit();
				
				// hide the satellite menu
				SatelliteViewManager.getInstance().showOrHide(false);
				ChatViewFragmentManager.getInstance().setCurrentFragment(fragment);
			}
		}
	}
	
	@Override
	public void onConnected() {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mWarningView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onDisconnected() {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mWarningView.setVisibility(View.VISIBLE);
			}
		});
	}
}
