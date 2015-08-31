package com.imrub.shoulder.module.main;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

import com.imrub.shoulder.R;
import com.imrub.shoulder.module.IAnimatorViewListener;
import com.imrub.shoulder.module.ITitleListener;
import com.imrub.shoulder.module.addrlist.AddrlistViewProxy;
import com.imrub.shoulder.module.cajian.CajianViewProxy;
import com.imrub.shoulder.module.chat.ChatViewProxy;
import com.imrub.shoulder.module.setting.SettingViewProxy;
import com.imrub.shoulder.module.yuanfen.YuanfenViewProxy;

public class MainUIProxy implements IAnimatorViewListener{

	private ArrayList<Fragment> mViewCache = new ArrayList<Fragment>(7);
	
	private ITitleListener mTitleChangeListener;
	private IFragmentListener mFragmentListener;
	
	public MainUIProxy(ITitleListener listener, IFragmentListener fragmentListener){
		initViewCache();
		mTitleChangeListener = listener;
		mFragmentListener = fragmentListener;
	}

	private void initViewCache(){
		for(int i=0; i<5; i++){
			mViewCache.add(null);
		}
	}
	
	public static int getResId(int viewType){
		switch (viewType) {
			case SatelliteMenuProxy.Item_Cajian:
				return R.string.main_ui_title_cajian;
			case SatelliteMenuProxy.Item_Chat:
				return R.string.main_ui_title_chat;
			case SatelliteMenuProxy.Item_Setting:
				return R.string.main_ui_title_setting;
			case SatelliteMenuProxy.Item_Yuanfen:
				return R.string.main_ui_title_yuanfen;
			case SatelliteMenuProxy.Item_Addrlist:
				return R.string.main_ui_title_tongxunlu;
		}
		return 0;
	}
	
	public Fragment getViewByViewType(int viewType){
		mTitleChangeListener.onTitleChange(getResId(viewType));
		Fragment fragment = mViewCache.get(viewType);
		if(fragment == null){
			fragment = createFragmentByViewType(viewType);
			mViewCache.set(viewType, fragment);
		}
		return fragment;
	}
	
	public boolean hasExist(int viewType){
		return mViewCache.get(viewType) == null ? false : true;
	}
	
	public Fragment getFragmentByViewType(int viewType){
		return mViewCache.get(viewType);
	}
	
	private Fragment createFragmentByViewType(int viewType){
		switch (viewType) {
			case SatelliteMenuProxy.Item_Addrlist:
				return new AddrlistViewProxy(); 
			case SatelliteMenuProxy.Item_Cajian:
				return new CajianViewProxy();
			case SatelliteMenuProxy.Item_Chat:
				return new ChatViewProxy(mTitleChangeListener);
			case SatelliteMenuProxy.Item_Setting:
				return new SettingViewProxy();
			case SatelliteMenuProxy.Item_Yuanfen:
				return new YuanfenViewProxy();
			}
		return null;
	}
	
	@Override
	public void onAnimator(int viewType) {
		mTitleChangeListener.onTitleChange(getResId(viewType));
		mFragmentListener.onFragmentChange(viewType);
	}
	
}
