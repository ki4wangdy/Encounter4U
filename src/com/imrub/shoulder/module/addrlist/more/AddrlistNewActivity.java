package com.imrub.shoulder.module.addrlist.more;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.net.NetworkManager;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.FriendFacade;
import com.imrub.shoulder.base.db.facade.MessageFacade;
import com.imrub.shoulder.base.db.facade.QiuKnowUserFacade;
import com.imrub.shoulder.base.db.table.addr.AddrlistNewData;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.base.db.table.im.FriendMessage;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.addrlist.newfriend.AddrNewFriendActivity;
import com.imrub.shoulder.module.addrlist.newfriend.IMAgreeFriendMsgManager;
import com.imrub.shoulder.module.addrlist.newfriend.IStateChange;
import com.imrub.shoulder.module.addrlist.newfriend.StateChangeManager;
import com.imrub.shoulder.module.detail.UserDetailInfoActivity;
import com.imrub.shoulder.module.im.MessageIdUtils;
import com.imrub.shoulder.module.im.msg.MsgTypeConstant;
import com.imrub.shoulder.module.request.addrlist.CreateKnowFacade;
import com.imrub.shoulder.module.request.addrlist.ICreateKnow;
import com.imrub.shoulder.widget.NotificationFactory;
import com.imrub.shoulder.widget.ToastFactory;

public class AddrlistNewActivity extends BaseActivity implements
		Action<AddrlistNewData>, OnClickListener, ICreateKnow,
		IStateChange {

	private ListView mListView;

	private AddrlistNewAdapter mAdapter;
	private List<AddrlistNewData> mData;

	private String mJid;
	private AddrlistNewData mCurrentData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addrlist_new_view);
		initView();
		registerListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		CreateKnowFacade.getInstance().registerListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		CreateKnowFacade.getInstance().unRegisterListener(this);
	}
	
	private void registerListener() {
		StateChangeManager.getInstance().registerListener(this);
	}

	private void initView() {
		View titleContainer = findViewById(R.id.addrlist_new_title);
		TextView title = (TextView) titleContainer
				.findViewById(R.id.title_content);
		title.setText(R.string.addrlist_new_title);

		View v = titleContainer.findViewById(R.id.title_bg_image);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		View stub = (View) titleContainer.findViewById(R.id.title_addnewfriend);
		stub.setVisibility(View.VISIBLE);
		stub.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickAddNewFriend();
			}
		});

		mListView = (ListView) findViewById(R.id.addrlist_new_listview);
		mData = QiuKnowUserFacade.queryAllQiuknowUserInfo();
		if (mData.isEmpty()) {
			initEmptyView(true);
			return;
		}

		mAdapter = new AddrlistNewAdapter(mData);
		mAdapter.setOnAgreeListener(this);
		mAdapter.setOnClickListener(this);
		mListView.setAdapter(mAdapter);

	}

	private void initEmptyView(boolean isVisiable) {
		View view = (View) findViewById(R.id.addrlist_empty_new_data);
		view.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
		mListView.setVisibility(isVisiable ? View.GONE : View.VISIBLE);
	}

	private void onClickAddNewFriend() {
		startActivity(new Intent(this, AddrNewFriendActivity.class));
		updateLocalDB();
	}

	@Override
	public void onStateChange() {
		final List<AddrlistNewData> data = QiuKnowUserFacade.queryAllQiuknowUserInfo();
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mAdapter == null) {
					mAdapter = new AddrlistNewAdapter(data);
				} else {
					mAdapter.resetData(data);
				}
				mAdapter.notifyDataSetChanged();
				initEmptyView(false);
			}
		});
	}

	@Override
	public void onExecute(final AddrlistNewData data) {
		// 判断是否有网络
		if(!NetworkManager.isConnectingToInternet(this)){
			ToastFactory.showMsg(AppContext.getAppContext(), R.string.register_network_off);
			return ;
		}
				
		// loading view to agree
		showLoadingView(R.string.addrlist_new_agree);
		
		mJid = data.getUid();
		mCurrentData = data;
		CreateKnowFacade.getInstance().agreeKnowRequest(Integer.parseInt(data.getUid()));
		
	}

	@Override
	public void onError() {
		if(isLoadingViewShow()){
			dismissLoadingView();
		}
		ToastFactory.showMsg(this, R.string.detail_reply_error);
	}

	@Override
	public void onSuccess() {
		if(isLoadingViewShow()){
			dismissLoadingView();
		}

		// 更新好友界面
		updateFriendUI(mJid,mCurrentData.getName(),mCurrentData.getIconUrl(),"0");
		
		// 1. update the local database
		notifyTheSuccess(mJid);

		// 2. send the agree message
		sendAgreeMsg();
		
		// 3. notify the successfully message
		ToastFactory.showMsg(AppContext.getAppContext(),R.string.addrlist_new_agreed);
		
		// 4. send the message the current user
		// 1) insert one message
		String systemMsg = getString(R.string.msg_system_text, mCurrentData.getName());
		
		String roomId = MessageIdUtils.createRoomId(mJid);
		MessageFacade.getInstance().putChatRoomMessage(roomId, systemMsg, MsgTypeConstant.SystemMsgType);
		
		// 2) notify the bar
		NotificationFactory.notify(roomId, mCurrentData.getName(),systemMsg);

	}

	private void updateFriendUI(String uid, String name, String icon, String count){
		UserFriend friend = new UserFriend(uid,name,icon,count);
		FriendFacade.getInstance().addFriend(friend);
	}
	
	private void sendAgreeMsg(){
		String userName = UserInfo.getInstance().getUserName();
		String userIcon = UserInfo.getInstance().getUserLogo();
		String userSignature = "";
		String uid = mCurrentData.getUid();
		
		FriendMessage replyMsg = new FriendMessage(userSignature,userName,userIcon);
		IMAgreeFriendMsgManager.getInstance().sendMsg(uid+"@cajian.cc", replyMsg.getMessageJsonString());
	}
	
	private void notifyTheSuccess(String jid) {
		// notify the database
		if (mCurrentData != null) {
			mCurrentData.setStatus(AddrlistNewConstant.StatusType.Status_HasAgree);
			mCurrentData.setIsNewRequest(0);
			mCurrentData.setIsnew(0);
			QiuKnowUserFacade.updateQiukonwUserInfo(mCurrentData);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		AddrlistNewData data = (AddrlistNewData) v.getTag();
		onClickToDetail(data);
		updateLocalDB();
	}

	private void onClickToDetail(AddrlistNewData data) {
		Intent intent = new Intent(this, UserDetailInfoActivity.class);
		intent.putExtra("uid", data.getUid());
		startActivity(intent);
		// update the isNew state
		updateTheColor(data);
	}

	private void updateTheColor(final AddrlistNewData data) {
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				data.setIsnew(0);
				QiuKnowUserFacade.updateQiukonwUserInfo(data);
				updateUI();
			}
		});
	}

	private void updateUI() {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected void onDestroy() {
		updateLocalDB();
		unRegisterListener();
		super.onDestroy();
	}

	private void unRegisterListener() {
		StateChangeManager.getInstance().unRegisterListener(this);
	}

	private void updateLocalDB() {
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				for (AddrlistNewData data : mData) {
					if (data.getIsNewRequest() == 1) {
						data.setIsNewRequest(0);
						QiuKnowUserFacade.updateQiukonwUserInfo(data);
					}
				}
			}
		});
	}

}
