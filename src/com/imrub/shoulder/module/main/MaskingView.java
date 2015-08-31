package com.imrub.shoulder.module.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.R;

public class MaskingView extends View{

	private Action<View> mAction;
	
	private volatile boolean mIsStartToShowing = false;
	private volatile boolean mIsStartToDispearing = false;
	
	private AnimationListener mStartToShowListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			mIsStartToDispearing = true;
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			mIsStartToDispearing = false;
			setVisibility(View.VISIBLE);
		}
	};
	
	
	private AnimationListener mStartToDispearListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			mIsStartToShowing = true;
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			mIsStartToShowing = false;
			setVisibility(View.GONE);
		}
	};
	
	public MaskingView(Context context) {
		super(context);
	}
	
	public MaskingView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public void setOnClickListener(Action<View> action){
		mAction = action;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(mIsStartToDispearing || mIsStartToShowing){
			return true;
		}
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(mAction != null){
				mAction.onExecute(null);
			}
			animateDispearMaskView();
			return true;
		}
		
		return super.onTouchEvent(event);
	}
	
	public void animateDispearMaskView(){
		clearAnimation();
		Animation animate = AnimationUtils.loadAnimation(getContext(), R.anim.splash_anim_fade_out);
		animate.setAnimationListener(mStartToDispearListener);
		startAnimation(animate);
	}
	
	public void animateShowMaskView(){
		clearAnimation();
		Animation animate = AnimationUtils.loadAnimation(getContext(), R.anim.splash_anim_fade_in);
		animate.setAnimationListener(mStartToShowListener);
		startAnimation(animate);
	}
	
}
