package com.imrub.shoulder.module.addrlist;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.imrub.shoulder.base.db.facade.FriendFacade;
import com.imrub.shoulder.base.db.facade.QiuKnowUserFacade;
import com.imrub.shoulder.base.db.table.addr.AddrlistNewData;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.ProgressBarAnimationListener;
import com.imrub.shoulder.module.addrlist.SectionAdapter.ViewTag;
import com.imrub.shoulder.module.addrlist.more.AddrlistNewActivity;
import com.imrub.shoulder.module.addrlist.more.AddrlistYuanActivity;
import com.imrub.shoulder.module.addrlist.newfriend.IMReplyMsgManager;
import com.imrub.shoulder.module.addrlist.newfriend.IReplyMessage;
import com.imrub.shoulder.module.detail.UserDetailInfoActivity;
import com.imrub.shoulder.module.hotpoint.AddrHotpointManager;
import com.imrub.shoulder.module.im.INewSubscriber;
import com.imrub.shoulder.module.main.MainUIActivity;

public class AddrlistViewProxy extends Fragment implements INewSubscriber, IReplyMessage, IFriendChange, IFriendLogo{

	private TextView mNewFriend;
	private TextView mCount;
	
	private SectionAdapter mSectionAdpater;
	
	private boolean isinited = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FriendFacade.getInstance().addListener(this);
		FriendFacade.getInstance().registerLogoChangeListener(this);
	}
	
	@Override
	public void onFriendChange(UserFriend userFriend) {
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				notifyDataOnUIThread();
			}
		});
	}

	@Override
	public void onLogoChange(String uid, String newLogo) {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mSectionAdpater != null){
					mSectionAdpater.notifyDataSetChanged();
				}
			}
		});
	}
	
	private void notifyDataOnUIThread(){
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mSectionAdpater.rebuildCache();
				mSectionAdpater.notifyDataSetChanged();
				mCount.setText(getString(R.string.addrlist_foot_text, 
						FriendFacade.getInstance().getFriends().size()));
			}
		});
	}
	
	@Override
	public void onHandleSubscribeRequest(String jid, int type, String msg) {
		updateNewFrindInSubThread();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.main_addrlist, container, false);
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
	
	private void initListView(){
		
		ListView listView = (ListView)getView().findViewById(R.id.addrlist_view);
		
		List<UserFriend> list = FriendFacade.getInstance().getFriends();
		mSectionAdpater = new SectionAdapter(list);
		
		View header = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.addrlist_header, null);
		listView.addHeaderView(header);
		
		header.findViewById(R.id.addrlist_header_block_1).setOnClickListener(
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickNewFriend();
				notifyNewFriendUI();
			}
		});
		
		header.findViewById(R.id.addrlist_header_block_2).setOnClickListener(
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickNewYuanfen();
			}
		});

		mNewFriend = (TextView)header.findViewById(R.id.addrlist_header_block_1).findViewById(R.id.addrlist_header_icon_update);
		
		View foot = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.addrlist_foot, null);
		mCount = (TextView)foot.findViewById(R.id.addrlist_foot_text);
		mCount.setText(getString(R.string.addrlist_foot_text, list.size()));
		listView.addFooterView(foot);
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				onLongClickViewItem(view);
				return true;
			}
		});
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onClickListViewItem(view);
			}
		});		
		listView.setAdapter(mSectionAdpater);
		listView.setVisibility(View.VISIBLE);
		
	}
	
	private void updateMainUI(){
		initListView();
		updateNewFrindInSubThread();
		registerAddrlistListener();
		setInVisiableView();
	}

	private void setInVisiableView(){
		final View v = getView().findViewById(R.id.progress_bar);
		Animation animation = AnimationUtils.loadAnimation(AppContext.getAppContext(), 
				R.anim.default_progressbar_out);
		animation.setAnimationListener(new ProgressBarAnimationListener(v));
		v.startAnimation(animation);
	}
	
	@Override
	public void onReplyMessageChange() {
		updateNewFrindInSubThread();
	}
	
	private void updateNewFrindInSubThread(){
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				List<AddrlistNewData> newData = QiuKnowUserFacade.queryAllNewQiuknowRequest();
				if(newData != null){
					int count = newData.size();
					if(count > 0){
						updateNewFriendCount(count);
					}
				} else {
					notifyNewFriendOnUIThead();
				}
			}
		});
	}
	
	private void updateNewFriendCount(final int count){
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mNewFriend.setText(""+count);
				mNewFriend.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void notifyNewFriendOnUIThead(){
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyNewFriendUI();
			}
		});
	}
	
	private void onClickListViewItem(View v){
		ViewTag tag = (ViewTag)v.getTag();
		if(tag == null){
			return ;
		}
		
		int index = tag.mIndex;
		if(index < 0){
			return ;
		}
		
		UserFriend data = FriendFacade.getInstance().getFriends().get(index);
		
		Intent intent = new Intent(AppContext.getAppContext(), UserDetailInfoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("uid", data.getUid());
		AppContext.getAppContext().startActivity(intent);
	}
	
	private void onLongClickViewItem(View v){
	}
	
	private void notifyNewFriendUI(){
		mNewFriend.setText("0");
		mNewFriend.setVisibility(View.GONE);
		AddrHotpointManager.removeAddressListHotpoint();
	}
	
	private void onClickNewYuanfen(){
		startActivity(new Intent(getActivity(), AddrlistYuanActivity.class));
	}
	
	private void onClickNewFriend(){
		startActivity(new Intent(getActivity(), AddrlistNewActivity.class));
	}

	private void registerAddrlistListener(){
		IMReplyMsgManager.getInstance().registerListener(this);
	}
	
	private void unRegisterListener(){
		IMReplyMsgManager.getInstance().unRegisterListener(this);
		FriendFacade.getInstance().registerLogoChangeListener(this);
	}
	
	@Override
	public void onDestroy() {
		unRegisterListener();
		super.onDestroy();
	}
	
}
