package com.imrub.shoulder.module;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;

public class NewGuidFragment extends RelativeLayout {

	public Runnable onEnterAction;
	private ViewPager vpContaner;
	
	private ArrayList<ImageView> guides;
	private ArrayList<ImageView> pageIndicator;
	
	private ViewGroup areaPageNumberIndicator;
	
	private TextView btnEnter;
	private View btnLogin;
	
	public NewGuidFragment(Context context) {
		super(context);
		initialize();
	}

	private void initialize() {
		LayoutInflater.from(getContext()).inflate(R.layout.new_guid, this);
		btnEnter = (TextView) findViewById(R.id.btnEnter);
		findViewById(R.id.btnEnter).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onEnterAction.run();
			}
		});
		btnLogin = findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onEnterAction.run();
			}
		});
		areaPageNumberIndicator = (ViewGroup) findViewById(R.id.areaPageNumberIndicator);
		guides = generateGuideImageViews();
		pageIndicator = generatePageIndicatorView(guides.size());
		vpContaner = (ViewPager) findViewById(R.id.vpContaner);
		vpContaner.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				activePageIndicator(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		findViewById(R.id.areaNext).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vpContaner.setCurrentItem(vpContaner.getCurrentItem() + 1);
			}
		});
		vpContaner.setAdapter(new PagerAdapter() {
			@Override
			public int getCount() {
				return guides.size();
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public void startUpdate(ViewGroup container) {
				super.startUpdate(container);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = guides.get(position);
				ViewPager viewPager = (ViewPager) container;
				try {
					Object tag = view.getTag();
					if(view instanceof ImageView && tag != null) {
						((ImageView)view).setImageResource((Integer)tag);
					}
					viewPager.addView((View) view, 0);
				} catch (Exception ex) {

				}
				return view;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object destroyedItem) {
				if(destroyedItem instanceof ImageView) {
					((ImageView)destroyedItem).setImageDrawable(null);
				}
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return position + "";
			}
		});
		activePageIndicator(0);
	}

	protected void activePageIndicator(int position) {
		for(int i = 0; i < guides.size(); i+= 1	) {
			ImageView iv = (ImageView)areaPageNumberIndicator.getChildAt(i);
			if(i == position) {
				iv.setImageResource(R.drawable.ic_indicator_active);
				showBtnEnter();
			} else {
				iv.setImageResource(R.drawable.ic_indicator_deactive);
				hideBtnEnter();
			}
		}
	}

	private void hideBtnEnter() {
		btnEnter.setVisibility(View.GONE);
		btnLogin.setVisibility(View.GONE);
	}

	private void showBtnEnter() {
		btnEnter.setVisibility(View.VISIBLE);
		btnLogin.setVisibility(View.VISIBLE);
	}

	private ArrayList<ImageView> generatePageIndicatorView(int num) {
		int indicatorSize = AppContext.getDimensionPixelSize(R.dimen.newguid_newerindicator_size);
		pageIndicator = new ArrayList<ImageView>();
		for (int i = 0; i < num; i += 1) {
			ImageView view = generateGuideImageView(R.drawable.ic_indicator_deactive);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);
			lp.setMargins(3, 0, 3, 0);
			lp.weight = AppContext.getDimensionPixelSize(R.dimen.newguid_indicator_size);
			lp.height = AppContext.getDimensionPixelSize(R.dimen.newguid_indicator_size);
			areaPageNumberIndicator.addView(view, lp);
			pageIndicator.add(view);
		}
		return pageIndicator;
	}

	private ArrayList<ImageView> generateGuideImageViews() {
		guides = new ArrayList<ImageView>();
		guides.add(generateGuideImageViewSecurity(R.drawable.newer_gude1));
		guides.add(generateGuideImageViewSecurity(R.drawable.newer_gude2));
		guides.add(generateGuideImageViewSecurity(R.drawable.newer_gude3));
		return guides;
	}
	
	private ImageView generateGuideImageViewSecurity(int resId) {
		ImageView imageView = new ImageView(getContext());
		imageView.setTag(resId);
		imageView.setScaleType(ScaleType.FIT_XY);
		return imageView;
	}

	private ImageView generateGuideImageView(int resId) {
		ImageView imageView = new ImageView(getContext());
		imageView.setImageResource(resId);
		return imageView;
	}
}