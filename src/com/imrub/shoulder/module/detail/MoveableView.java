package com.imrub.shoulder.module.detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;

public class MoveableView extends View implements IViewPagerChangeListener{

	private Bitmap mBitmap;
	
	private int mWidth;
	private int mOffsetPixel;
	private int mPage;
	private int mIconWidth;
	
	private Action<Integer> mPagerListener;
	
	public MoveableView(Context context) {
		super(context);
		initBitmap();
	}

	public MoveableView(Context context, AttributeSet attrs){
		super(context, attrs);
		initBitmap();
	}
	
	private void initBitmap(){
		mBitmap = BitmapFactory.decodeResource(AppContext.getResource(), R.drawable.detail_moveable_icon);
		mWidth = AppContext.getScreenWidth();
		mPage = 1;
		mIconWidth = AppContext.getResource().getDimensionPixelSize(R.dimen.detail_sanjiao_width) / 2;
	}
	
	public void setonPagerListener(Action<Integer> action){
		mPagerListener = action;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mOffsetPixel != 0){
			int left = mOffsetPixel / 2 + (mWidth / 4);
			canvas.drawBitmap(mBitmap, left-mIconWidth, 0, null);
			return ;
		}
		canvas.drawBitmap(mBitmap, mPage == 1 ? ( mWidth * 3 ) / 4 - mIconWidth : ( mWidth * 1 / 4 ) - mIconWidth, 0, null);
	}
	
	@Override
	public void onPageScrolled(int arg0, float scrollPercent, int scrollPixel) {
		mOffsetPixel = scrollPixel;
		invalidate();
	}
	
	@Override
	public void onPageSelect(int page) {
		mPage = page;
		if(mPagerListener != null){
			mPagerListener.onExecute(page);
		}
	}
	
}
