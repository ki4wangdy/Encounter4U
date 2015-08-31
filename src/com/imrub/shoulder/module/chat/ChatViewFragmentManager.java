package com.imrub.shoulder.module.chat;

import android.support.v4.app.Fragment;

public class ChatViewFragmentManager {

	private Fragment mCurrentFragment;
	
	private static ChatViewFragmentManager minstance;
	private ChatViewFragmentManager(){
	}

	public static final ChatViewFragmentManager getInstance(){
		if(minstance == null){
			minstance = new ChatViewFragmentManager();
		}
		return minstance;
	}
	
	public void setCurrentFragment(Fragment fragment){
		mCurrentFragment = fragment;
	}
	
	public Fragment getCurrentFragment(){
		return mCurrentFragment;
	}
	
}
