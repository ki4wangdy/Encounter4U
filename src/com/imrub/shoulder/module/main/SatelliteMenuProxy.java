package com.imrub.shoulder.module.main;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ext.SatelliteMenu;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.module.hotpoint.MainUIHotpointProxy;

public class SatelliteMenuProxy {

	public static final int Item_Cajian 		= 	SatelliteMenu.Item_Cajian;
	public static final int Item_Addrlist 		= 	SatelliteMenu.Item_Addrlist;
	
	public static final int Item_Chat			= 	SatelliteMenu.Item_Chat;
	public static final int Item_Yuanfen		= 	SatelliteMenu.Item_Yuanfen;
	public static final int Item_Setting		= 	SatelliteMenu.Item_Setting;
	
	public static final int Item_Main_Open		= 1;
	public static final int Item_Main_Close		= 2;
	
	private android.view.ext.SatelliteMenu mMenu;
	private Action<Integer> mAction;
	
	private MainUIHotpointProxy mHotpointProxy;
	
	private AnimationListener mListener = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			mMenu.setVisibility(View.VISIBLE);
			expand();
		}
	};
	
	public SatelliteMenuProxy(SatelliteMenu menu){
		this.mMenu = menu;
		this.mHotpointProxy = new MainUIHotpointProxy(mMenu); 
		initView();
	}
	
	private void initView(){
		mMenu.setMainImage(R.drawable.satellite_center_bg);
		
		List<android.view.ext.SatelliteMenuItem> items = new ArrayList<android.view.ext.SatelliteMenuItem>();
        items.add(new android.view.ext.SatelliteMenuItem(4, R.drawable.satellite_setting_bg));
        items.add(new android.view.ext.SatelliteMenuItem(3, R.drawable.satellite_yuanfen_bg));
        items.add(new android.view.ext.SatelliteMenuItem(2, R.drawable.satellite_chat_bg));
        items.add(new android.view.ext.SatelliteMenuItem(1, R.drawable.satellite_addrlist_bg));
        items.add(new android.view.ext.SatelliteMenuItem(0, R.drawable.satellite_cajian_bg));
        
        mMenu.addItems(items);        
        mMenu.setOnItemClickedListener(new android.view.ext.SatelliteMenu.SateliteClickedListener() {
			@Override
			public void eventOccured(int id) {
				mHotpointProxy.onClickHotpoint(id);
				mAction.onExecute(id);
			}
		});
	}
	
	public void setOnMainItemClickListener(final Action<Integer> listener){
		mMenu.setOnMainItemClickedListener(new android.view.ext.SatelliteMenu.SateliteMainItemClickedListener() {
			@Override
			public void onClick(boolean isOpen) {
				listener.onExecute(isOpen ? Item_Main_Open : Item_Main_Close);
			}
		});
	}
	
	public void setOnItemClickListener(Action<Integer> action){
		mAction = action;
	}

	public void startAnimate(){
		Animation animate = AnimationUtils.loadAnimation(AppContext.getAppContext(), R.anim.splash_anim_fade_in);
		animate.setDuration(1000);
		animate.setAnimationListener(mListener);
		mMenu.startAnimation(animate);
	}
	
	public void updateSatelliteMenu(int index, float percent){
		mMenu.updateSatelliteMenu(index, percent);
	}
	
	public void expand(){
		mMenu.expand();
	}
	
	public void close(){
		mMenu.close();
	}

	public void doClick(){
		mMenu.doClick();
	}
	
}
