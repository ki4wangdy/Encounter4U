package com.imrub.shoulder.module.main;

import com.imrub.shoulder.Action;



public class SatelliteViewAnimateManager implements IFragmentPagerScrollListener{

	private Action<Integer> mAction;
	
	private SatelliteMenuProxy mSatelliteMenuProxy; 
	private int mCurrentIndex = 0;
	
	private static SatelliteViewAnimateManager mInstance;
	private SatelliteViewAnimateManager(){
	}
	
	public static final SatelliteViewAnimateManager getInstance(){
		if(mInstance == null){
			mInstance = new SatelliteViewAnimateManager();
		}
		return mInstance;
	}
	
	public void setSatelliteView(SatelliteMenuProxy menuProxy){
		this.mSatelliteMenuProxy = menuProxy;
	}
	
	public void setScrollIndexListener(Action<Integer> action){
		mAction = action;
	}
	
	@Override
	public void onPageScrolled(int currentPosition, float percent, int pixel) {
		
		// 0.0 - 0.25 - 0.5 - 0.75 - 1.0
		// 0.0 - 0.5  - 1.0 - 0.5  -0.0
		
		/**
		 * if(0.0 -> 0.5), then percent * 2
		 * if(0.5 -> 1.0), then (1-percent) * 2
		 */
		
		float per = 0.0f;
		if(percent > 0.0 && percent <= 0.5){
			per = percent * 2;
		} else if(percent > 0.5 && percent <= 1.0){
			per = (1 - percent) * 2;
		}

		int index = 0;
		if(mCurrentIndex == currentPosition){
			if(mCurrentIndex == 0){
				index = 1;
			} else {
				index = currentPosition + 1;
			}
		} else {
			int max = (mCurrentIndex > currentPosition ? mCurrentIndex : currentPosition);
			index = max - 1;
		}
		
		updateMenuAlha(index, per);
		
		if(Math.abs(per) < 0.0001 ){
			mCurrentIndex = currentPosition;
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int isScroll) {
	}

	@Override
	public void onPageSelected(int currentIndex) {
		mAction.onExecute(currentIndex);
	}
	
	private void updateMenuAlha(int index, float percent){
		mSatelliteMenuProxy.updateSatelliteMenu(index, percent);
	}
	
}
