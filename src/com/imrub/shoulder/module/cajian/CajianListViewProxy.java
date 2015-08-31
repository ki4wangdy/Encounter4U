package com.imrub.shoulder.module.cajian;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.net.NetworkManager;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.WifiFacade;
import com.imrub.shoulder.base.db.table.cajian.WifiUserData;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.module.cajian.CJItemAdapter.ViewTag;
import com.imrub.shoulder.module.cajian.wifi.IWifiUserData;
import com.imrub.shoulder.module.cajian.wifi.WifiConnectUtils;
import com.imrub.shoulder.module.cajian.wifi.WifiPostRequest;
import com.imrub.shoulder.module.detail.UserDetailInfoActivity;
import com.imrub.shoulder.module.model.cajian.CJUserInfoContainer;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.widget.ToastFactory;

public class CajianListViewProxy implements IWifiUserData{

	public static final String Tag = "CajianListViewProxy";
	
	private View mView;
	private View mEmptyViewBlock;

	private PullToRefreshListView mListView;
	private CJItemAdapter mAdapter;
	
	private boolean mHasInit;
	
	public CajianListViewProxy(View view){
		mView = view;
		mEmptyViewBlock = view.findViewById(R.id.main_ui_notlist_block);
	}
	
	private void initListView(){
		regiserListener();
		
		ViewStub stub = (ViewStub)mView.findViewById(R.id.main_ui_listview);
		stub.inflate();
		
		mListView = (PullToRefreshListView)mView.findViewById(R.id.main_ui_cajian_listview);
		
		final ListView listView = mListView.getRefreshableView();
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				onCJItemLongClick(view);
				return true;
			}
		});
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onCJItemClick(view);
			}
		});

		final View foot = LayoutInflater.from(AppContext.getAppContext()).
				inflate(R.layout.main_cajian_center,null,false);
		View button = (View)foot.findViewById(R.id.main_ui_center_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickSuijiChat();
			}
		});
		
		listView.addFooterView(foot);
		
		mListView.setMode(Mode.PULL_FROM_START);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {  
		    @Override  
		    public void onRefresh(PullToRefreshBase<ListView> refreshView) {  
		    	onRefreshInner(refreshView);
		    }  
		});  
		
		mAdapter = new CJItemAdapter();
		mListView.setAdapter(mAdapter);
		mHasInit = true;
	}
	
	private void regiserListener(){
		CJUserInfoContainer.getInstance().registerListener(this);
	}
	
	public void updateData(){
		if(!mHasInit){
			initListView();
			hideEmtpyViewBlock();
			return ;
		}
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onWifiUserChange(final List<WifiUserData> data) {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mListView != null){
					mListView.onRefreshComplete();
				}
				updateData();
			}
		});
	}
	
	private void onRefreshInner(PullToRefreshBase<ListView> refreshView){
		
		// 判断是否有网络
		if(!NetworkManager.isConnectingToInternet(AppContext.getAppContext())){
			ToastFactory.showMsg(AppContext.getAppContext(), R.string.register_network_off);
			finishRefresh();
			return ;
		}
		
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				onRefreshOnSubThread();
			}
		});
	}
	
	private void onRefreshOnSubThread(){
		String macCollection = WifiConnectUtils.getWifiNetworks();
		if("".equalsIgnoreCase(macCollection)){
			finishRefresh();
			return ;
		}
		
		// 删除无用的MAC collection
		WifiFacade.deleteAllUploadedWifiData();
		
		String uid = UserInfo.getInstance().getUid();
		String token = UserInfo.getInstance().getToken();
		
		String postData = WifiConnectUtils.getOnlineJson(uid, token, macCollection);
		if(postData != null){
			Logger.print(Tag, "In CajianListView "+ uid +" will post data, the data is:"+postData);
			WifiPostRequest.getInstance().postRequest(HttpUrlConstant.HttpMacUrl, postData);
			Logger.print(Tag, "In CajianListView "+ uid +" will finishRefresh.");
		}
		finishRefresh();
	}
	
	private void finishRefresh(){
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mListView.onRefreshComplete();
			}
		});
	}
	
	private void hideEmtpyViewBlock(){
		mEmptyViewBlock.setVisibility(View.GONE);
	}
	
	private void onClickSuijiChat(){
	}
	
	private void onCJItemLongClick(View view){
	}

	private void onCJItemClick(View view){
		ViewTag tag = (ViewTag)view.getTag();
		int index = tag.index;
		if(index < 0){
			return ;
		}
		
		WifiUserData data = (WifiUserData)mAdapter.getItem(index);
		int uid = data.getMeet_uid();
		
		Intent intent = new Intent(AppContext.getAppContext(), UserDetailInfoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("uid", uid+"");
		AppContext.getAppContext().startActivity(intent);
		
	}
	
}
