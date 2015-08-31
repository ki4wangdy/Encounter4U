package com.imrub.shoulder.module.cajian;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.net.NetworkManager;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.WifiFacade;
import com.imrub.shoulder.base.db.table.cajian.WifiUserData;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.module.cajian.wifi.IWifiUserData;
import com.imrub.shoulder.module.cajian.wifi.WifiConnectUtils;
import com.imrub.shoulder.module.cajian.wifi.WifiPostRequest;
import com.imrub.shoulder.module.im.IConnectionListener;
import com.imrub.shoulder.module.im.client.IMClient;
import com.imrub.shoulder.module.model.cajian.CJUserInfoContainer;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.widget.ToastFactory;

public class CajianViewProxy extends Fragment implements IConnectionListener, IWifiUserData{

	public static final String Tag = "CajianViewProxy";
	
	private TextView mWarningView;
	private CajianListViewProxy mListviewProxy;
	private PullToRefreshScrollView mScrollView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(AppContext.getAppContext()).
				inflate(R.layout.main_cajian,container,false);
		IMClient.getInstance().registerConnectionListener(this);
		
		initTopView(view);
		if(CJUserInfoContainer.getInstance().isEmpty()){
			registerListener();
			initEmptyView(view);
		} else {
			initListView(view);
		}
		return view;
	}
	
	private void initEmptyView(View view){
		initRefreshScrollView(view);
		final View suijiText = view.findViewById(R.id.main_ui_center_button);
		suijiText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});	
	}
	
	private void initRefreshScrollView(View view){
		mScrollView = (PullToRefreshScrollView)view.findViewById(R.id.main_ui_notlist_block);
		mScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {  
		    @Override  
		    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {  
		    	onRefreshInner(refreshView);
		    }  
		});  
	}
	
	private void initListView(View view){
		mListviewProxy = new CajianListViewProxy(view);
		mListviewProxy.updateData();
	}
	
	private void initTopView(View view){
		mWarningView = (TextView)view.findViewById(R.id.main_ui_header_setting);
		
		// 1.是否开启了网络，没有那么显示网络没有开启
		boolean isConnected = NetworkManager.isConnectingToInternet(AppContext.getAppContext());
		if(!isConnected){
			
			mWarningView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.main_ui_header_nolan, 0, 0, 0);
			mWarningView.setText(R.string.main_ui_header_content);
			return ;
		}
		
		// 2.如果开启了网络，是否是WIFI连接的
		boolean isWifi = NetworkManager.isWifiConnected(AppContext.getAppContext());
		if(!isWifi){
			mWarningView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.main_ui_header_nowlan, 0, 0, 0);
			mWarningView.setText(R.string.main_ui_cajian_notwifi_text);
			return ;
		}
		
		mWarningView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.main_ui_header_nologo, 0, 0, 0);
		mWarningView.setText(R.string.main_ui_cajian_notlogo_text);
		
	}
	
	private void onRefreshInner(PullToRefreshBase<ScrollView> refreshView){
		
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
		if(macCollection == null || "".equalsIgnoreCase(macCollection)){
			finishRefresh();
			return ;
		}
		
		// 删除无用的MAC collection
		WifiFacade.deleteAllUploadedWifiData();
		
		String uid = UserInfo.getInstance().getUid();
		String token = UserInfo.getInstance().getToken();
		
		String post = WifiConnectUtils.getOnlineJson(uid, token, macCollection);
		if(post != null){
			Logger.print(Tag, "In emptyView user:"+ uid + " will post data, the data is:"+post);
			WifiPostRequest.getInstance().postRequest(HttpUrlConstant.HttpMacUrl, post);
			Logger.print(Tag, "In emptyView "+ uid + " will finishRefresh.");
		}
		finishRefresh();
	}
	
	private void finishRefresh(){
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mScrollView.onRefreshComplete();
			}
		});
	}
	
	@Override
	public void onWifiUserChange(final List<WifiUserData> data) {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mScrollView != null){
					mScrollView.onRefreshComplete();
				}
				unRegiserListener();
				initListView(getView());
			}
		});
	}
	
	@Override
	public void onConnected() {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				initTopView(getView());
			}
		});
	}

	@Override
	public void onDisconnected() {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				initTopView(getView());
			}
		});
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		CJUserInfoContainer.getInstance().unRegisterListener(this);
	}
	
	private void registerListener(){
		CJUserInfoContainer.getInstance().registerListener(this);
	}
	
	private void unRegiserListener(){
		CJUserInfoContainer.getInstance().unRegisterListener(this);
	}
	
}
