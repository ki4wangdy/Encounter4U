package com.imrub.shoulder.module.im.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.imrub.shoulder.module.im.IMBridge;
import com.imrub.shoulder.module.im.IMCallBack;

public class IMServer extends Service{

	static{
		System.loadLibrary("imCore");
	}
	
	private final IMBridge.Stub imBridge = new IMBridge.Stub() {
		@Override
		public void sendMsg(String jid, String msg) throws RemoteException {
			IMService.getInstance().sendMsg(jid, msg);
		}
		
		@Override
		public void setImCallBack(IMCallBack callBack) throws RemoteException {
			IMService.getInstance().setIMClientConnectionListener(callBack);
		}
		
		@Override
		public void login(String uid, String password, String server)
				throws RemoteException {
			IMService.getInstance().startServer(uid, password, server);
		}
		
		@Override
		public void register(String uid, String password, String server)
				throws RemoteException {
			IMService.getInstance().registerUser(uid, password, server);
		}
		
		@Override
		public void sendPing(String jid) throws RemoteException {
			IMService.getInstance().sendPing(jid);
		}
		
		@Override
		public boolean isToFriend(String jid) throws RemoteException {
			return IMService.getInstance().isToFriend(jid);
		}
		
	};

	
	@Override
	public IBinder onBind(Intent intent) {
		return imBridge.asBinder();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		IMService.getInstance().setIMClientConnectionListener(null);
		return super.onUnbind(intent);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, START_STICKY, startId);
	}
	
	@Override
	public void onDestroy() {
		AlartUtil.alartBroadcastTask(this, ServiceStopBroadcast.StopAction, 1000 * 3);
		super.onDestroy();
	}
	
}
