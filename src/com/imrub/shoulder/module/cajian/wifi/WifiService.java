package com.imrub.shoulder.module.cajian.wifi;

import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.net.NetworkManager;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.WifiFacade;
import com.imrub.shoulder.base.db.table.cajian.WifiCollection;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.module.request.HttpUrlConstant;

public class WifiService extends Service{

	public static final String Tag = "WifiService";
	
	public static final int MsgWhatType_Init 	= 1;
	public static final int MsgWhatType_Upload 	= 2;
	
	private BroadcastReceiver receiver = new WifiConnectReceiver();
	
	private HandlerThread mHandlerThread ;
	private Handler updateMacAddrToServiceHandle;
	
	private boolean mHasRegister = false;
	
	private final Callback mTask = new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
				case MsgWhatType_Upload:
					uploadMacAddressFromTime((String)msg.obj);
					break;
				case MsgWhatType_Init:
					uploadAllMacAddress();
					break;
			}
			return true;
		}
	};

	private void uploadMacAddressFromTime(String time){
		// 查询出所有的数据，flag是0
		
		List<WifiCollection> data = WifiFacade.queryAllNotUploadWifiCollection();
		if(data == null || data.isEmpty()){
			return ;
		}
		
		boolean isOk = uploadAllMacAddressToServer(data,time);
		if(isOk){
			WifiFacade.setAllWifiCollectionUploaded(data);
		}
	}
	
	private void uploadAllMacAddress(){
		// 查询出所有的数据，flag是0
		List<WifiCollection> data = WifiFacade.queryAllNotUploadWifiCollection();
		if(data == null){
			return ;
		}
		
		boolean isOk = uploadAllMacAddressToServer(data,"");
		if(isOk){
			WifiFacade.setAllWifiCollectionUploaded(data);
		}
	}
	
	private boolean uploadAllMacAddressToServer(List<WifiCollection> data, String time){
		final String uid = UserInfo.getInstance().getUid();
		final String token = UserInfo.getInstance().getToken();
		
		if(uid.isEmpty() || token.isEmpty()){
			return false ;
		}
		
		// 如果是WIFI，那么继续上传
		boolean isWifi = NetworkManager.isWifiConnected(AppContext.getAppContext());
		if(!isWifi){
			return false;
		}
		
		// 按照离线的要求，上传所有的数据
		String postData = "";
		if("".equalsIgnoreCase(time)){
			postData = WifiConnectUtils.toAllMacAddress(uid, token, data);
		}
		// 时间为time的为online，其他的都为OFFICE
		else {
			postData = WifiConnectUtils.toMacAddress(uid, token, data, time);
		}
		
		// 判断online或者OFFLINE数据为null
		if(postData == null){
			return false;
		}

		Logger.print(Tag, "In backgroundThread WifiService , user:"+ uid + " will post data, the data is:"+postData);
		return WifiPostRequest.getInstance().postRequest(HttpUrlConstant.HttpMacUrl, postData);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		init(intent);
		pushMacAddress(intent);
		return super.onStartCommand(intent, START_STICKY, startId);
	}

	private void pushMacAddress(Intent intent){
		if(intent == null || intent.getExtras() == null){
			return ;
		}
		
		// 初始化时，检测MAC是否打开，然后上传
		boolean isValue = intent.getExtras().getBoolean("initwifimacaddress", false);
		if(isValue && WifiConnectUtils.isWifiOpen(this)){
			Message msg = updateMacAddrToServiceHandle.obtainMessage();
			msg.what = MsgWhatType_Init ;
			msg.sendToTarget();
			return ;
		}
		
		// MAC Address 变化时，会上传
		String wifimacaddrTime = intent.getExtras().getString("wifimacaddr_time", "");
		if(updateMacAddrToServiceHandle != null && !wifimacaddrTime.isEmpty()){
			Message msg = updateMacAddrToServiceHandle.obtainMessage();
			msg.obj = wifimacaddrTime;
			msg.what = MsgWhatType_Upload ;
			msg.sendToTarget();
		}
	}
	
	private void init(Intent intent){
		registerIntent(intent);
		initThread();
		initHandler();
	}
	
	private void registerIntent(Intent intent){
		if(mHasRegister){
			return ;
		}
		mHasRegister = true;
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		registerReceiver(receiver, filter);
	}
	
	private void initHandler(){
		if(updateMacAddrToServiceHandle == null){
			updateMacAddrToServiceHandle = new Handler(mHandlerThread.getLooper(),mTask);
		}
	}
	
	private void initThread(){
		if(mHandlerThread == null){
			mHandlerThread = new HandlerThread("wifi_upload_mac");
			mHandlerThread.start();
		}
	}
	
	private void quitThread(){
		if(mHandlerThread != null){
			mHandlerThread.quitSafely();
		}
	}

	private void unRegisterIntent(){
		unregisterReceiver(receiver);
	}
	
	@Override
	public void onDestroy() {
		quitThread();
		unRegisterIntent();
		super.onDestroy();
	}

}
