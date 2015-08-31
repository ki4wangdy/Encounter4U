package com.imrub.shoulder.module.addrlist;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

public class IndexSectionListView extends ListView  {

	int i = 1;
	private IndexScroller mScroller = null;

	public IndexSectionListView(Context context) {
		super(context);
		initScrollerIndex(context);
	}

	public IndexSectionListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initScrollerIndex(context);
	}

	private void initScrollerIndex(Context context) {
		mScroller = new IndexScroller(context, this);
	}
	
	public void showIndexBar() {
		if(mScroller != null) {
			mScroller.show();
		}
	}
	
	public void hideIndexBar() {
		if(mScroller != null) {
			mScroller.hide();
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (mScroller != null && mScroller.onTouchEvent(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if (mScroller != null) {
			if (adapter.isEmpty()) {
				mScroller.hide();
			} else {
				mScroller.show();
			}
			mScroller.setAdapter(adapter);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mScroller != null)
			mScroller.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public void draw(Canvas canvas) {
		try {
			super.draw(canvas);
			if (mScroller != null)
				mScroller.draw(canvas);
		} catch (Exception e) {
		}
	}
}