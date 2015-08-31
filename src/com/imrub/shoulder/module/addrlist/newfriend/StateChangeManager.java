package com.imrub.shoulder.module.addrlist.newfriend;

import java.util.ArrayList;

public class StateChangeManager {

	private ArrayList<IStateChange> mListener = new ArrayList<IStateChange>();
	
	private static StateChangeManager mInstance;
	private StateChangeManager(){
	}

	public static StateChangeManager getInstance(){
		if(mInstance == null){
			mInstance = new StateChangeManager();
		}
		return mInstance;
	}
	
	public void registerListener(IStateChange listener){
		mListener.add(listener);
	}
	
	public void unRegisterListener(IStateChange listener){
		mListener.remove(listener);
	}
	
	public void fireNotify(){
		for(IStateChange listener : mListener){
			listener.onStateChange();
		}
	}
	
}
