package com.imrub.shoulder.module.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.net.NetworkManager;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.AddrFacade;
import com.imrub.shoulder.base.db.table.im.ReplyMessage;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.addrlist.newfriend.StateChangeManager;
import com.imrub.shoulder.module.im.client.IMClient;
import com.imrub.shoulder.module.request.addrlist.CreateKnowFacade;
import com.imrub.shoulder.module.request.addrlist.ICreateKnow;
import com.imrub.shoulder.widget.TextWatcherImp;
import com.imrub.shoulder.widget.Title;
import com.imrub.shoulder.widget.ToastFactory;

public class QiuKnowMessageActivity extends BaseActivity implements ICreateKnow{

	private Title mTitle;
	private EditText mEdittext;

	private String mUid;
	private String userName;
	private String targetName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_qiuknow_view);
		registerListener();
		initView();
		initData();
	}
	
	private void registerListener(){
		CreateKnowFacade.getInstance().registerListener(this);
	}
	
	private void initTile(){
		View titleContainer = findViewById(R.id.qiuknow_title);
		mTitle = new Title(titleContainer);
		mTitle.setContent(getString(R.string.addrlist_new_title));
		
		mTitle.setOnBackListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				finish();
			}
		});
		
		TextView sendbutton = (TextView)titleContainer.findViewById(R.id.qiuknow_button);
		sendbutton.setVisibility(View.VISIBLE);
		sendbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickSend();
			}
		});
	}
	
	private void initView(){
		initTile();
		mEdittext = (EditText)findViewById(R.id.qiuknow_edittext);
		TextView numberLimit = (TextView)findViewById(R.id.qiuknow_numberlimit);
		mEdittext.addTextChangedListener(new TextWatcherImp(mEdittext, numberLimit, TextWatcherImp.MaxNumber));
	}

	private void initData(){
		mUid = (String)getIntent().getStringExtra("uid");
		userName = getIntent().getStringExtra("name");
		targetName = getIntent().getStringExtra("targetName");
	}
	
	private boolean CheckNull(){
		String msg = mEdittext.getText().toString();
		if("".equalsIgnoreCase(msg.trim())){
			return true;
		}
		return false;
	}
	
	private void onClickSend(){
		// ≈–∂œ «∑Ò”–Õ¯¬Á
		if(!NetworkManager.isConnectingToInternet(this)){
			ToastFactory.showMsg(AppContext.getAppContext(), R.string.register_network_off);
			return ;
		}
		
		if(CheckNull()){
			ToastFactory.showMsg(this, R.string.detail_qiuknow_null);
			return ;
		}
		
		showLoadingView(R.string.detail_qiuknow_send_msg);
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				handleSendRequestInSubThread();
			}
		});
	}
	
	private String getToServerMsg(){
		return mEdittext.getText().toString();
	}
	
	private void handleSendRequestInSubThread(){
		// 1. send the message to server
		CreateKnowFacade.getInstance().createKnowRequest(Integer.parseInt(mUid), getToServerMsg());
	}
	
	@Override
	public void onError() {
		if(isLoadingViewShow()){
			dismissLoadingView();
		}
		ToastFactory.showMsgForShortTime(this, R.string.detail_qiuknow_send_error);
	}
	
	@Override
	public void onSuccess() {
		
		// 2°£send the message to UID
		String currentUserLogo = UserInfo.getInstance().getUserLogo();
		ReplyMessage replyMsg = new ReplyMessage(getToServerMsg(), userName, currentUserLogo);
		IMClient.getInstance().sendQiuKnow(mUid+"@cajian.cc", replyMsg.getMessageJsonString());
		
		// 3.update the local DB
		AddrFacade.insertOrUpdateQiuknowFriend(mUid, getToServerMsg(), targetName);
		
		// 4.notify other activity
		StateChangeManager.getInstance().fireNotify();

		// 5. dismiss the loading view
		if(isLoadingViewShow()){
			dismissLoadingView();
		}
		
		// 6. update the userDetailInfo UI, when we success to comment
		ToastFactory.showMsgForShortTime(this, R.string.detail_qiuknow_send_success);
		
		Intent intent = getIntent();
		intent.putExtra("msg", getToServerMsg());
		setResult(RESULT_OK, intent);
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		}, 100);
	}

	private void unRegisterListener(){
		CreateKnowFacade.getInstance().unRegisterListener(this);
	}
	
	@Override
	protected void onDestroy() {
		unRegisterListener();
		super.onDestroy();
	}
	
}
