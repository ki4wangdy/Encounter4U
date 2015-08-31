package com.imrub.shoulder.module.addrlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SectionIndexer;


public class IndexScroller {
	
	private float mIndexbarWidth;
	private float mIndexbarMargin;
	private float mPreviewPadding;
	private float mDensity;
	private float mScaledDensity;
	private float mAlphaRate;
	
	private int mState = STATE_HIDDEN;
	private int mListViewWidth;
	private int mListViewHeight;
	private int mCurrentSection = -1;
	
	private boolean mIsIndexing = false;
	
	private ListView mListView = null;
	private SectionIndexer mIndexer = null;
	private String[] mSections = null;
	
	private RectF mIndexbarRect;
	private boolean isShowBackgroud;
	
	private static final int STATE_HIDDEN = 0;
	private static final int STATE_SHOWING = 1;
	private static final int STATE_SHOWN = 2;
	private static final int STATE_HIDING = 3;
	
	public IndexScroller(Context context, ListView lv) {
		mDensity = context.getApplicationContext().getResources().getDisplayMetrics().density;
		mScaledDensity = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
		mListView = lv;
		setAdapter(mListView.getAdapter());
		
		mIndexbarWidth = 20 * mDensity;
		mIndexbarMargin = 5 * mDensity;
		mPreviewPadding = 5 * mDensity;
	}

	public void draw(Canvas canvas) {
		if (mState == STATE_HIDDEN)
			return;
		
		Paint indexbarPaint = new Paint();
		indexbarPaint.setColor(Color.BLACK);
		
		if (isShowBackgroud) {
			indexbarPaint.setAlpha((int) (64 * mAlphaRate));
		} else {
			indexbarPaint.setAlpha((int) (0 * mAlphaRate));
		}
		indexbarPaint.setAntiAlias(true);
		canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mDensity, indexbarPaint);
		
		if (mSections != null && mSections.length > 0) {
			if (mCurrentSection >= 0) {
				Paint previewPaint = new Paint();
				previewPaint.setColor(Color.BLACK);
				previewPaint.setAlpha(96);
				previewPaint.setAntiAlias(true);
				previewPaint.setShadowLayer(3, 0, 0, Color.argb(64, 0, 0, 0));
				
				Paint previewTextPaint = new Paint();
				previewTextPaint.setColor(Color.WHITE);
				previewTextPaint.setAntiAlias(true);
				previewTextPaint.setTextSize(50 * mScaledDensity);
				
				float previewTextWidth = previewTextPaint.measureText(mSections[mCurrentSection]);
				float previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent();
				RectF previewRect = new RectF((mListViewWidth - previewSize) / 2
						, (mListViewHeight - previewSize) / 2
						, (mListViewWidth - previewSize) / 2 + previewSize
						, (mListViewHeight - previewSize) / 2 + previewSize);
				
				canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity, previewPaint);
				canvas.drawText(mSections[mCurrentSection], previewRect.left + (previewSize - previewTextWidth) / 2 - 1
						, previewRect.top + mPreviewPadding - previewTextPaint.ascent() + 1, previewTextPaint);
			}
			
			Paint indexPaint = new Paint();
			indexPaint.setColor(Color.DKGRAY);
			indexPaint.setAlpha((int) (255 * mAlphaRate));
			indexPaint.setAntiAlias(true);
			indexPaint.setTextSize(12 * mScaledDensity);
			indexPaint.setFakeBoldText(true);
			
			float sectionHeight = (mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length;
			float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
			for (int i = 0; i < mSections.length; i++) {
				float paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections[i])) / 2;
				canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft
						, mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
			}
		}
	}
	
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mState != STATE_HIDDEN && contains(ev.getX(), ev.getY())) {
				isShowBackgroud = true;
				mIsIndexing = true;
				mCurrentSection = getSectionByPoint(ev.getY());
				if (mListView != null && mIndexer != null) {
					int index = mIndexer.getPositionForSection(mCurrentSection);
					if(index >= 0){
						mListView.setSelection(index);
					}
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mSections == null || mSections.length == 0) {
				hide();
			}
			if (mIsIndexing) {
				if (contains(ev.getX(), ev.getY())) {
					mCurrentSection = getSectionByPoint(ev.getY());
					if (mListView != null && mIndexer != null) {
						mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
					}
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			isShowBackgroud = false;
			if (mIsIndexing) {
				mIsIndexing = false;
				mCurrentSection = -1;
			}
			break;
		}
		return false;
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		mListViewWidth = w;
		mListViewHeight = h;
		mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth
				, mIndexbarMargin
				, w - mIndexbarMargin
				, h - mIndexbarMargin);
	}
	
	public void show() {
		setState(STATE_SHOWING);
	}
	
	public void hide() {
		setState(STATE_HIDDEN);
	}
	
	public void setAdapter(Adapter adapter) {
		if (adapter instanceof SectionIndexer) {
			mIndexer = (SectionIndexer) adapter;
			mSections = (String[]) mIndexer.getSections();
		}
	}
	
	private void setState(int state) {
		if (state < STATE_HIDDEN || state > STATE_HIDING)
			return;
		
		mState = state;
		switch (mState) {
		case STATE_HIDDEN:
			mHandler.removeMessages(0);
			break;
		case STATE_SHOWING:
			mAlphaRate = 0;
			fade(0);
			break;
		case STATE_SHOWN:
			break;
		case STATE_HIDING:
			break;
		}
	}
	
	private boolean contains(float x, float y) {
		return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top + mIndexbarRect.height());
	}
	
	private int getSectionByPoint(float y) {
		if (mSections == null || mSections.length == 0)
			return 0;
		if (y < mIndexbarRect.top + mIndexbarMargin)
			return 0;
		if (y >= mIndexbarRect.top + mIndexbarRect.height() - mIndexbarMargin)
			return mSections.length - 1;
		return (int) ((y - mIndexbarRect.top - mIndexbarMargin) / ((mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length));
	}
	
	private void fade(long delay) {
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageAtTime(0, SystemClock.uptimeMillis() + delay);
	}

	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (mState) {
				case STATE_SHOWING:
					mAlphaRate = 1;
					mListView.invalidate();
					break;
				case STATE_SHOWN:
					break;
				case STATE_HIDING:
					mAlphaRate -= mAlphaRate * 0.2;
					if (mAlphaRate < 0.1) {
						mAlphaRate = 0;
						setState(STATE_HIDDEN);
					}
					mListView.invalidate();
					break;
			}
		}
	};
}