package com.imrub.shoulder.module.main;

import android.support.v4.view.ViewPager.OnPageChangeListener;


public class FragmentPagerListenerImp implements OnPageChangeListener{

	private IFragmentPagerScrollListener mListener;
	
	public FragmentPagerListenerImp(IFragmentPagerScrollListener listener){
		mListener = listener;
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if(mListener != null){
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if(mListener != null){
			mListener.onPageScrollStateChanged(arg0);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		if(mListener != null){
			mListener.onPageSelected(arg0);
		}
	}

}
