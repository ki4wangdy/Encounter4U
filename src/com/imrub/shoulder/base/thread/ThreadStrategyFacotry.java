package com.imrub.shoulder.base.thread;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadStrategyFacotry {
	
	public enum ThreadPoolStrategy{
		CachedThread,FixThread,SingleThread,WifiUpload,ImageLoader
	}
	
	private static int FIX_THREAD_COUNT = Runtime.getRuntime().availableProcessors() + 1;

	private HashMap<ThreadPoolStrategy, ExecutorService> threadPoolCachedFactory = new HashMap<ThreadPoolStrategy, ExecutorService>();
	
	private static ThreadStrategyFacotry mInstance = new ThreadStrategyFacotry();
	private ThreadStrategyFacotry(){
	}

	public static ExecutorService createService(ThreadPoolStrategy strateger){
		ExecutorService type = mInstance.threadPoolCachedFactory.get(strateger);
		if(type == null){
			switch (strateger) {
				case CachedThread:
					ExecutorService cachedService = Executors.newCachedThreadPool();
					mInstance.threadPoolCachedFactory.put(ThreadPoolStrategy.CachedThread, cachedService);
					type = cachedService;
					break;
				case FixThread:
					ExecutorService fixedService = Executors.newFixedThreadPool(FIX_THREAD_COUNT);
					mInstance.threadPoolCachedFactory.put(ThreadPoolStrategy.FixThread, fixedService);
					type = fixedService;
					break;
				case SingleThread:
					ExecutorService singleService = Executors.newSingleThreadExecutor();
					mInstance.threadPoolCachedFactory.put(ThreadPoolStrategy.SingleThread, singleService);
					type = singleService;
					break;
				case WifiUpload:
					ExecutorService wifiUpload = Executors.newSingleThreadExecutor();
					mInstance.threadPoolCachedFactory.put(ThreadPoolStrategy.WifiUpload, wifiUpload);
					type = wifiUpload;
					break;
				case ImageLoader:
					ExecutorService imageLoader = Executors.newSingleThreadExecutor();
					mInstance.threadPoolCachedFactory.put(ThreadPoolStrategy.ImageLoader, imageLoader);
					type = imageLoader;
					break;
			}
		}
		return type;
	}
	
	public static void destoryAllServices(){
		final HashMap<ThreadPoolStrategy, ExecutorService> pool = mInstance.threadPoolCachedFactory;
		Set<Entry<ThreadPoolStrategy, ExecutorService>> sets = pool.entrySet();
		for(Entry<ThreadPoolStrategy, ExecutorService> entry : sets){
			try{
				entry.getValue().shutdownNow();
			}catch(Exception e){
			}
		}
	}
	
}
