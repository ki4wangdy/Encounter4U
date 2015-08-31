package com.imrub.shoulder.module.detail;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;

public class ViewPagerProxy {

	private ViewPager mPager;
	private IViewPagerChangeListener mListener;

	private List<Integer> mMeetInfos;
	
	public ViewPagerProxy(ViewPager pager){
		this.mPager = pager;
		initViewPager();
	}
	
	public void setData(List<Integer> meetInfo){
		mMeetInfos = meetInfo;
	}
	
	private void initViewPager(){
		mPager.setAdapter(new ViewPagerAdapterImp());
		mPager.setOnPageChangeListener(new ViewPagerChangeListener());
		mPager.setCurrentItem(1);
	}

	public void setOnPageChangeListener(IViewPagerChangeListener listener){
		mListener = listener;
	}
	
	private class ViewPagerAdapterImp extends PagerAdapter{
		
		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public int getItemPosition(Object object) {
			if(object instanceof View){
				View v = (View)object;
				Integer value = (Integer)v.getTag();
				return value;
			}
			return -1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = null;
			switch (position) {
				case 0:
					view = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.detail_viewpager_left, null);
					ListView list = (ListView)view.findViewById(R.id.user_left_listview);
					if(mMeetInfos != null){
						list.setAdapter(new CJTimeItemAdapter(mMeetInfos));
					} else {
						view = LayoutInflater.from(AppContext.getAppContext()).
								inflate(R.layout.detail_meetfon_empty_view, null);
					}
					view.setTag(0);
					break;
				case 1:
					view = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.detail_viewpager_right, null);
					view.setTag(1);
					break;
			}
			mPager.addView(view);
			return view;
		}
	}

	private class ViewPagerChangeListener implements OnPageChangeListener{
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if(mListener != null){
				mListener.onPageScrolled(arg0, arg1, arg2);
			}
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		
		@Override
		public void onPageSelected(int index) {
			if(mListener != null){
				mListener.onPageSelect(index);
			}
		}
	}
	
}
