package com.imrub.shoulder.base.thread;

import java.util.concurrent.ExecutorService;

import com.imrub.shoulder.base.thread.ThreadStrategyFacotry.ThreadPoolStrategy;

public class ThreadFacade {
	
	public static void runOnUiThread(final Runnable task){
		MainThreadStrategy.postTask(task);
	}

	public static void runOnUiThread(final Runnable task, final long delayMillis){
		MainThreadStrategy.postTaskOnDelay(task, delayMillis);
	}
	
	public static void runOnCacheThread(final Runnable task){
		ExecutorService service = ThreadStrategyFacotry.createService(ThreadPoolStrategy.CachedThread);
		if(!service.isShutdown()){
			service.execute(task);
		}
	}

	public static void runOnFixThread(final Runnable task){
		ExecutorService service = ThreadStrategyFacotry.createService(ThreadPoolStrategy.FixThread);
		if(!service.isShutdown()){
			service.execute(task);
		}
	}

	public static void runOnSingleThread(final Runnable task){
		ExecutorService service = ThreadStrategyFacotry.createService(ThreadPoolStrategy.SingleThread);
		if(!service.isShutdown()){
			service.execute(task);
		}
	}

	public static void runOnWifiUploadThread(final Runnable task){
		ExecutorService service = ThreadStrategyFacotry.createService(ThreadPoolStrategy.WifiUpload);
		if(!service.isShutdown()){
			service.execute(task);
		}
	}
	
	public static void runOnImageLoaderThread(final Runnable task){
		ExecutorService service = ThreadStrategyFacotry.createService(ThreadPoolStrategy.ImageLoader);
		if(!service.isShutdown()){
			service.execute(task);
		}
	}
	
	public static <T> void runOnThreadArgAction(final IAction<T> action, final T arg){
		MainThreadStrategy.postTask(action, arg);
	}
	
	public static void postMsgByDelayTime(final int delayTime, final Runnable task){
		MainThreadStrategy.postTaskOnDelay(task, delayTime);
	}
	
}
