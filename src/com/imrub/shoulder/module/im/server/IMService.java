package com.imrub.shoulder.module.im.server;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.RemoteException;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.util.ServerLogger;
import com.imrub.shoulder.base.util.log.SdcardLogger;
import com.imrub.shoulder.module.im.IConnectionListener;
import com.imrub.shoulder.module.im.IMCallBack;
import com.imrub.shoulder.module.im.IMsgControllerListener;
import com.imrub.shoulder.module.im.client.ConnectedBroadcast;

public class IMService implements IConnectionListener, IMsgControllerListener{

	public static final String Tag = "IMService";
	
	private List<IConnectionListener> mConnectionListener = new ArrayList<IConnectionListener>();
	private List<IMsgControllerListener> mMsgControllerListener = new ArrayList<IMsgControllerListener>();
	
	private volatile boolean mhasLogin = false;
	
	private volatile IMCallBack mCallBack;
	
	private static IMService mInstance;
	private IMService(){
		initImserver();
	}

	public static IMService getInstance(){
		if(mInstance == null){
			mInstance = new IMService();
		}
		return mInstance;
	}

	public synchronized void registerConnectionListener(IConnectionListener listener){
		mConnectionListener.add(listener);
	}
	
	public synchronized void unRegisterConnectionListener(IConnectionListener listener){
		mConnectionListener.remove(listener);
	}
	
	public synchronized void registerMsgControllerListener(IMsgControllerListener listener){
		mMsgControllerListener.add(listener);
	}
	
	public synchronized void unRegisterMsgControllerListener(IMsgControllerListener listener){
		mMsgControllerListener.remove(listener);
	}
	
	public void startServer(final String userName, final String password, final String server){
		if(mhasLogin){
			return ;
		}
		mhasLogin = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				login(userName, password, server);
			}
		}).start();
	}
	
	public void registerUser(final String userName, final String password, final String server){
		new Thread(new Runnable() {
			@Override
			public void run() {
				registerUserNative(userName, password, server);
			}
		}).start();
	}
	
	@Override
	public synchronized void onConnected() {
		for(IConnectionListener call : mConnectionListener){
			call.onConnected();
		}
		
		Intent intent = new Intent();
		intent.setAction(ConnectedBroadcast.Action);
		intent.putExtra("isconnected", true);
		AppContext.getAppContext().sendBroadcast(intent);
		
		// start to send ping packet
		AlartUtil.startPingAlarm();

		// logger 
		ServerLogger.setLoggerProxy(new SdcardLogger());
		
	}

	@Override
	public synchronized void onDisconnected() {
		for(IConnectionListener call : mConnectionListener){
			call.onDisconnected();
		}
		
		Intent intent = new Intent();
		intent.setAction(ConnectedBroadcast.Action);
		intent.putExtra("isconnected", false);
		AppContext.getAppContext().sendBroadcast(intent);
		
		mhasLogin = false;
		
		// stop to send ping packet
		AlartUtil.stopPingAlarm();
	}

	public synchronized void setIMClientConnectionListener(IMCallBack callBack){
		mCallBack = callBack;
	}
	
	@Override
	public synchronized void onDelayedReceivedMsg(int msgRoomType, String stmp, String jid,
			String msg) {
		if(mCallBack != null){
			try {
				mCallBack.onDelayReceivedMsg(msgRoomType, stmp, jid, msg);
			} catch (RemoteException e) {
			}
		}
	}
	
	@Override
	public synchronized void onRecevicedMsg(int msgRoomType, String jid, String msg) {
		if(mCallBack != null){
			try {
				mCallBack.onReceivedMsg(msgRoomType, jid, msg);
				return ;
			} catch (RemoteException e) {
			}
		}
	}

	public synchronized void onPingRecevied(int type){
	}
	
	public synchronized void onRosterReceived(String rosters){
	}
	
	public synchronized boolean isBinderAlive(){
		return mCallBack == null ? false : true;
	}
	
	@Override
	public void sendMsg(String jid, String msg) {
		sendMsgNative(jid, msg);
	}

	public native void subscribe(String jid, String name, String msg);
	public native void unsubscribe(String jid, String msg);
	public native void remove(String jid);
	public native void sendPing(String jid);
	
	public native boolean isToFriend(String jid);
	public native int getJidType(String jid);
	
	private native void cancel(String jid, String msg);
	private native void ackSubscriptionRequest(String jid, int ack);
	
	private native void initImserver();
	private native void sendMsgNative(String jid, String msg);
	private native void registerUserNative(String userName, String password, String server);
	private native void login(String userName, String password, String server);
	
	
}
