package com.imrub.shoulder.module;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class ProgressBarAnimationListener implements AnimationListener{

	private View mTarget;
	
	public ProgressBarAnimationListener(View target){
		mTarget = target;
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		if(mTarget != null){
			mTarget.setVisibility(View.GONE);
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

}
