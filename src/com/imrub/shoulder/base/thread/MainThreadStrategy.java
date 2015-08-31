package com.imrub.shoulder.base.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MainThreadStrategy {
	
	private static final int DEFAULT_TYPE = 0x1;
	private static final int IMPROVE_ONE_TYPE = 0x2;
	
	private final Handler mHandler = new Handler(Looper.getMainLooper()){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			int type = msg.what;
			switch (type) {
				case DEFAULT_TYPE:
					final Runnable task = (Runnable)msg.obj;
					task.run();
					break;
				case IMPROVE_ONE_TYPE:
					final ActionWrapper<Object> action = (ActionWrapper<Object>)msg.obj;
					action.mAction.onExecute(action.mArg);
					break;
			}
		};
	};

	private static final MainThreadStrategy mInstance = new MainThreadStrategy();
	
	public static void postTask(final Runnable task){
		Message msg = mInstance.mHandler.obtainMessage(DEFAULT_TYPE,task);
		msg.sendToTarget();
	}

	public static void postTaskOnDelay(final Runnable task, final long delayMillis){
		mInstance.mHandler.postDelayed(task, delayMillis);
	}

	public static <T> void postTask(final IAction<T> action, final T arg){
		Message msg = mInstance.mHandler.obtainMessage(IMPROVE_ONE_TYPE,new ActionWrapper<T>(action,arg));
		msg.sendToTarget();
	}
	
}
