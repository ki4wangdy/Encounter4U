package com.imrub.shoulder.module.im.client;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.module.im.IConnectionListener;
import com.imrub.shoulder.module.im.IMBridge;
import com.imrub.shoulder.module.im.IMCallBack;
import com.imrub.shoulder.module.im.IMsgControllerListener;
import com.imrub.shoulder.module.im.server.IMServer;

public class IMClient {

	public static final String Tag = "IMClient";
	
	private List<IConnectionListener> mConnectionLisener = new ArrayList<IConnectionListener>();
	private List<IMsgControllerListener> mMsgControllerListener = new ArrayList<IMsgControllerListener>();
	
	private static final int Flag_Register 	= 0x02;
	private static final int Flag_Login		= 0x04;
	
	private int mflag ;
	private IMBridge imBridge;
	
	private static IMClient mInstance;
	private IMClient(){
	}

	public static IMClient getInstance(){
		if(mInstance == null){
			mInstance = new IMClient();
		}
		return mInstance;
	}
	
	private IMCallBack mIMcallBack = new IMCallBack.Stub() {
		
		@Override
		public synchronized void onReceivedMsg(int msgRoomType, String jid, String msg) throws RemoteException {
			onMsgReceivedInner(msgRoomType, jid, msg);
			Logger.print(Tag, msg +" received from the user:"+jid);
		}
		
		@Override
		public synchronized void onDelayReceivedMsg(int msgRoomType, String stmp,
				String jid, String msg) throws RemoteException {
			onDelayMsgReceivedInner(msgRoomType, stmp, jid, msg);
		}
		
	};
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			imBridge = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			imBridge = IMBridge.Stub.asInterface(service);
			try {
				imBridge.setImCallBack(mIMcallBack);
				
				String uid = UserInfo.getInstance().getUid();
				String server = UserInfo.getInstance().getImServer();

				if("".equalsIgnoreCase(uid) || "".equalsIgnoreCase(server)){
					return ;
				}
				
				switch (mflag) {
					case Flag_Login:
						imBridge.login(uid, uid, server);
						break;
					case Flag_Register:
						imBridge.register(uid, uid, server);
						break;
				}
				mflag = 0;
			} catch (RemoteException e) {
			}
		}
	};
	
	public synchronized void onDelayMsgReceived(int msgRoomType, String stmp, String jid,
			String msg){
		onDelayMsgReceivedInner(msgRoomType, stmp, jid, msg);
	}
	
	public synchronized void onMsgReceived(int msgRoomType, String jid, String msg){
		onMsgReceivedInner(msgRoomType, jid, msg);
	}
	
	private void onDelayMsgReceivedInner(int msgRoomType, String stmp, String jid,
			String msg){
		for(IMsgControllerListener callback : mMsgControllerListener){
			callback.onDelayedReceivedMsg(msgRoomType, stmp, jid, msg);
		}
		
		if(imBridge == null){
			bindService();
		}
		
	}
	
	private void onMsgReceivedInner(int msgRoomType, String jid, String msg){
		for(IMsgControllerListener callback : mMsgControllerListener){
			callback.onRecevicedMsg(msgRoomType, jid, msg);
		}
		
		if(imBridge == null){
			bindService();
		}
		
	}
	
	public synchronized void onConnected(){
		for(IConnectionListener callback : mConnectionLisener){
			callback.onConnected();
		}
		if(imBridge == null){
			bindService();
		}
//		Log.i("wangdy_tag", "IMClient receive onConnected");
	}
	
	public synchronized void onDisconnected(){
		for(IConnectionListener callback : mConnectionLisener){
			callback.onDisconnected();
		}
		if(imBridge == null){
			bindService();
		}
//		Log.i("wangdy_tag", "IMClient receive onDisconnected");
	}
	
	public ServiceConnection getServiceConnection(){
		return mServiceConnection;
	}
	
	public void login(){
		bindServiceAndLogin();
	}
	
	public void registerUser(){
		if(imBridge == null){
			mflag = Flag_Register;
			Intent intent = new Intent(AppContext.getAppContext(), IMServer.class);
			AppContext.getAppContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
			return ;
		}
		
		String uid = UserInfo.getInstance().getUid();
		String server = UserInfo.getInstance().getImServer();
		
		if("".equalsIgnoreCase(uid) || "".equalsIgnoreCase(server)){
			return ;
		}
		
		try {
			imBridge.register(uid, uid, server);
		} catch (RemoteException e) {
		}
	}
	
	public void bindServiceAndLogin(){
		if(imBridge == null){
			mflag = Flag_Login;
			Intent intent = new Intent(AppContext.getAppContext(), IMServer.class);
			AppContext.getAppContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
			return ;
		}
		
		String uid = UserInfo.getInstance().getUid();
		String server = UserInfo.getInstance().getImServer();
		
		if("".equalsIgnoreCase(uid) || "".equalsIgnoreCase(server)){
			return ;
		}
		
		try {
			imBridge.login(uid, uid, server);
		} catch (RemoteException e) {
		}
	}

	public void bindService(){
		Intent intent = new Intent(AppContext.getAppContext(), IMServer.class);
		AppContext.getAppContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	public void startServer(){
		Intent intent = new Intent(AppContext.getAppContext(), IMServer.class);
		AppContext.getAppContext().startService(intent);
	}
	
	public void registerServerConnection(){
		ConnectedBroadcast broadcast = new ConnectedBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectedBroadcast.Action);
		AppContext.getAppContext().registerReceiver(broadcast, filter);
	}
	
	public void registerMessageBroadcast(){
		MessageBroadcast broadcast = new MessageBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MessageBroadcast.Action);
		AppContext.getAppContext().registerReceiver(broadcast, filter);
	}
	
	public void unBindServer(){
		if(imBridge == null){
			return ;
		}
		AppContext.getAppContext().unbindService(mServiceConnection);
	}
	
	public void registerConnectionListener(IConnectionListener listener){
		mConnectionLisener.add(listener);
	}
	
	public void unRegisterConnectionListener(IConnectionListener listener){
		mConnectionLisener.remove(listener);
	}
	
	public void registerMsgControllerListener(IMsgControllerListener listener){
		mMsgControllerListener.add(listener);
	}
	
	public void unRegisterMsgControllerListener(IMsgControllerListener listener){
		mMsgControllerListener.remove(listener);
	}
	
	public boolean isToFriend(String jid){
		if(imBridge != null){
			try{
				return imBridge.isToFriend(jid);
			}catch(RemoteException e){
			}
		}
		return false;
	}
	
	public void sendMsg(String jid, String msg){
		if(imBridge != null){
			try {
				imBridge.sendMsg(jid, msg);
			} catch (RemoteException e) {
			}
		}
	}
	
	public void sendReplyMsg(String jid, String replyMessage){
		if(imBridge != null){
			try {
				imBridge.sendMsg(jid, replyMessage);
			} catch (RemoteException e) {
			}
		}
	}
	
	public void sendQiuKnow(String jid, String message){
		if(imBridge != null){
			try{
				imBridge.sendMsg(jid, message);
			}catch(RemoteException e){
			}
		}
	}
	
	public void sendPing(String jid){
		if(imBridge != null){
			try{
				imBridge.sendPing(jid);
			}catch(RemoteException e){
			}
		}
	}

}
