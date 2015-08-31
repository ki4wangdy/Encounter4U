package com.imrub.shoulder.base.util;

import com.imrub.shoulder.base.thread.ThreadFacade;

public class Timer {

	static int mCount = 1;
	static ITimerListener mListener;
	
	static boolean isCancel;
	
	public static final Runnable mTask = new Runnable() {
		@Override
		public void run() {
			if(isCancel){
				return ;
			}
			
			if(mListener != null){
				mListener.onTime(mCount);
				if(mListener.isContinue(mCount)){
					ThreadFacade.postMsgByDelayTime(1000, mTask);
					mCount ++ ;
				}
			}
		}
	};
	
	public static void stop(){
		isCancel = true;
	}
	
	public static void start(final ITimerListener listener){
		mListener = listener;
		isCancel = false;
		mCount = 1;
		ThreadFacade.postMsgByDelayTime(1000, mTask);
	}
	
	public interface ITimerListener{
		public void onTime(final int time);
		public boolean isContinue(final int time);
	}
	
}
