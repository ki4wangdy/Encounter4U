package com.imrub.shoulder.module.main;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentPagerAdapterImp extends FragmentPagerAdapter{

	private ArrayList<Fragment> mFragments;
	
	public FragmentPagerAdapterImp(FragmentManager fm, ArrayList<Fragment> fragments ) {
		super(fm);
		mFragments = fragments;
	}

	public int getCount() {
		return mFragments.size();
	};
	
	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}
	
}
