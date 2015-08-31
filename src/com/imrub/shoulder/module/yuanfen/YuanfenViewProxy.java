package com.imrub.shoulder.module.yuanfen;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.ProgressBarAnimationListener;
import com.imrub.shoulder.module.main.MainUIActivity;

public class YuanfenViewProxy extends Fragment implements Action<View>{

	private YuanfenCircleAdapter mYuanfenCircleDapter;
	
	private boolean isinited = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.main_yuanfen, container, false);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(!isinited && isVisibleToUser){
			ThreadFacade.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateMainUI();
				}
			},MainUIActivity.LazyLoadTime);
			isinited = true;
		}
	}
	
	private void updateMainUI(){
		initListView(getView());
		setInVisiableView();
	}
	
	private void setInVisiableView(){
		final View v = getView().findViewById(R.id.progress_bar);
		Animation animation = AnimationUtils.loadAnimation(AppContext.getAppContext(), 
				R.anim.default_progressbar_out);
		animation.setAnimationListener(new ProgressBarAnimationListener(v));
		v.startAnimation(animation);
	}
	
	private void initListView(View view){
		ListView listView = (ListView)view.findViewById(R.id.yuanfencircle_listview);
		initListview(listView);
		listView.setVisibility(View.VISIBLE);
	}
	
	private void initListview(ListView listview){
		View view = LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.yuanfencircle_header, null);
		listview.addHeaderView(view);
		
		List<YuanfenCircleData> data = new ArrayList<YuanfenCircleData>();
		YuanfenCircleData emptyData1 = new YuanfenCircleData();
		YuanfenCircleData emptyData2 = new YuanfenCircleData();
		YuanfenCircleData emptyData3 = new YuanfenCircleData();
		YuanfenCircleData emptyData4 = new YuanfenCircleData();

		data.add(emptyData1);
		data.add(emptyData2);
		data.add(emptyData3);
		data.add(emptyData4);
		
		mYuanfenCircleDapter = new YuanfenCircleAdapter(data);
		mYuanfenCircleDapter.setOnEmptyClickListener(this);
		listview.setAdapter(mYuanfenCircleDapter);
	}
	
	public static void onClickYuanfenCircleTitleBianji(){
	}
	
	public static void onClickYuanfenCircleNotify(){
	}

	@Override
	public void onExecute(View arg) {
		onClickYuanfenCircleTitleBianji();
	}
	
}
