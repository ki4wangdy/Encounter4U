package com.imrub.shoulder.module.main;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ext.SatelliteMenu;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.base.util.log.LoggerUtils;
import com.imrub.shoulder.module.ITitleListener;
import com.imrub.shoulder.module.cajian.wifi.WifiModule;
import com.imrub.shoulder.module.chat.ChatViewFragmentManager;
import com.imrub.shoulder.module.chat.ChatViewProxy;
import com.imrub.shoulder.module.yuanfen.YuanfenViewProxy;
import com.imrub.shoulder.widget.NotificationFactory;
import com.imrub.shoulder.widget.SoftInputUtil;

public class MainUIActivity extends BaseActivity implements ITitleListener, 
		IFragmentListener, ISatelliteViewListener, Action<Integer>{

	public static final String Tag = "MainUIActivity";
	
	public static final int LazyLoadTime = 500;
	
	private SatelliteMenuProxy mSatelliteMenuProxy;
	private MaskingView mMaskingView;
	
	private MainUIProxy mMainUIProxy;
	
	private FrameLayout mTitleContainer;
	private TextView mTitle;
	
	private View mSatelliteView;
	private View mChatRoomTitleView;
	private View mYuanfenCircleTitle;
	
	private Fragment mCurrentFragment;
	private ArrayList<Fragment> mAllFragments;
	
	private ViewPager mViewPager;
	private boolean mIsFromNotify;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_face_view);
		long start = System.currentTimeMillis();
		WifiModule.startWifiService();
		initView();
		long end = System.currentTimeMillis() - start;
		Logger.print(Tag, LoggerUtils.getPerformanceString("mainUIActivity onCreate use time:", end));
		loadOnDelay();
	}

	private void loadOnDelay(){
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mSatelliteMenuProxy.startAnimate();
			}
		}, 300);
	}
	
	private void initView(){
		mTitleContainer = (FrameLayout)findViewById(R.id.main_ui_header_title);
		mTitle = (TextView)mTitleContainer.findViewById(R.id.title_text);
		initYuanfenCircleTitle(mTitleContainer);
		
		SatelliteMenu menu = (android.view.ext.SatelliteMenu)findViewById(R.id.satellite_menu);
		SatelliteViewManager.getInstance().setSatelliteView(this);
		
		mSatelliteView = findViewById(R.id.satellite_view);
		mSatelliteMenuProxy = new SatelliteMenuProxy(menu);
		mSatelliteMenuProxy.setOnMainItemClickListener(new Action<Integer>() {
			@Override
			public void onExecute(Integer type) {
				onMainItemClick(type);
			}
		});
		
		mSatelliteMenuProxy.setOnItemClickListener(new Action<Integer>() {
			@Override
			public void onExecute(Integer item) {
				onItemClick(item);
			}
		});
		
		mMaskingView = (MaskingView)findViewById(R.id.maskingview);
		mMaskingView.setOnClickListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				mSatelliteMenuProxy.close();
			}
		});
		
		mMainUIProxy = new MainUIProxy(this,this);
		addDefaultFragment();
		
	}

	private void initYuanfenCircleTitle(FrameLayout viewgroup){
		mYuanfenCircleTitle = (View)mTitleContainer.findViewById(R.id.yuanfencircle_title);
		mYuanfenCircleTitle.findViewById(R.id.title_notify).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				YuanfenViewProxy.onClickYuanfenCircleNotify();
			}
		});

		mYuanfenCircleTitle.findViewById(R.id.title_bianji).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				YuanfenViewProxy.onClickYuanfenCircleTitleBianji();
			}
		});
	}
	
	/**
	 * 此处是通知栏点击会键入到这里
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		if(intent == null){
			return ;
		}
		mIsFromNotify = true;
		super.onNewIntent(intent);
		setIntent(intent);
		handleTheIntent(intent);
	}
	
	private void handleTheIntent(Intent intent){
		// handle the chat message
		String chatRoomId = intent.getStringExtra(NotificationFactory.CHAT_ROOM_ID);
		String roomName = intent.getStringExtra(NotificationFactory.CHAT_ROOM_NAME);
		if(chatRoomId != null){
			handleMsgIntent(chatRoomId,roomName);
		}
	}

	private void handleMsgIntent(String chatRoomId, String roomName){
//		mMaskingView.animateDispearMaskView();
		getIntent().putExtra(NotificationFactory.CHAT_ROOM_ID, chatRoomId);
		mSatelliteMenuProxy.doClick();
		
		// whether or not cache has exist
		if(mCurrentFragment != null && mCurrentFragment instanceof ChatViewProxy){
			((ChatViewProxy)mCurrentFragment).handleMsgIntent(chatRoomId,roomName);
		} else {
			mMainUIProxy.onAnimator(2);
		}
	}
	
	@Override
	public void onTitleChange(int resId) {
		mTitle.setText(resId);
		mYuanfenCircleTitle.setVisibility(R.string.main_ui_title_yuanfen == resId 
				? View.VISIBLE : View.INVISIBLE);
	}
	
	@Override
	public void onEnterChatRoom(String title) {
		if(mChatRoomTitleView == null){
			mChatRoomTitleView = createChatRoomTitleView();
			View view = mChatRoomTitleView.findViewById(R.id.title_bg_image);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SoftInputUtil.hideSoftInput(mChatRoomTitleView.getWindowToken());
					onExitChatRoom();
					onChatRoomBack();
				}
			});
		}
		TextView text = (TextView)mChatRoomTitleView.findViewById(R.id.title_content);
		text.setText(title);

		mTitleContainer.addView(mChatRoomTitleView);
	}

	private void onChatRoomBack(){
		onBackPressed();
	}
	
	private static View createChatRoomTitleView(){
		return LayoutInflater.from(AppContext.getAppContext()).inflate(R.layout.title_view, null);
	}
	
	@Override
	public void onExitChatRoom() {
		mTitleContainer.removeView(mChatRoomTitleView);
	}
	
	private void onMainItemClick(int type){
		switch (type) {
			case SatelliteMenuProxy.Item_Main_Open:
				mMaskingView.animateDispearMaskView();
				break;
			case SatelliteMenuProxy.Item_Main_Close:
				mMaskingView.animateShowMaskView();
				break;
		}
	}
	
	private void onItemClick(int item){
		mMaskingView.animateDispearMaskView();
		mMainUIProxy.onAnimator(item);
	}
	
	private void addDefaultFragment() {
//		Fragment fragment = mMainUIProxy.getViewByViewType(SatelliteMenuProxy.Item_Cajian);
//		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//		trans.add(R.id.viewanimator, fragment).commit();
		
		Fragment cajian = mMainUIProxy.getViewByViewType(SatelliteMenuProxy.Item_Cajian);
		Fragment addr = mMainUIProxy.getViewByViewType(SatelliteMenuProxy.Item_Addrlist);
		Fragment chat = mMainUIProxy.getViewByViewType(SatelliteMenuProxy.Item_Chat);
		Fragment yuanfen = mMainUIProxy.getViewByViewType(SatelliteMenuProxy.Item_Yuanfen);
		Fragment setting = mMainUIProxy.getViewByViewType(SatelliteMenuProxy.Item_Setting);

		mAllFragments = new ArrayList<Fragment>();
		mAllFragments.add(cajian);
		mAllFragments.add(addr);
		mAllFragments.add(chat);
		mAllFragments.add(yuanfen);
		mAllFragments.add(setting);
		
		FragmentPagerAdapterImp imp = new FragmentPagerAdapterImp(getSupportFragmentManager(), mAllFragments);
		
		mViewPager = (ViewPager)findViewById(R.id.viewanimator);
		mViewPager.setAdapter(imp);
		mViewPager.setOnPageChangeListener(new FragmentPagerListenerImp(SatelliteViewAnimateManager.getInstance()));
		
		SatelliteViewAnimateManager.getInstance().setSatelliteView(mSatelliteMenuProxy);
		SatelliteViewAnimateManager.getInstance().setScrollIndexListener(this);
		
		mViewPager.setOffscreenPageLimit(5);
		mViewPager.setCurrentItem(0);
		
		updateTitle(0);
		mCurrentFragment = cajian;
		
	}

	@Override
	public void onExecute(Integer index) {
		mCurrentFragment = mAllFragments.get(index);
		updateTitle(index);
	}
	
	private void updateTitle(int index){
		int resId = MainUIProxy.getResId(index);
		mTitle.setText(resId);
		mYuanfenCircleTitle.setVisibility(R.string.main_ui_title_yuanfen == resId 
				? View.VISIBLE : View.INVISIBLE);
	}
	
	@Override
	public void onFragmentChange(int index) {
		mCurrentFragment = mAllFragments.get(index);
		mViewPager.setCurrentItem(index, false);
		if(mCurrentFragment instanceof ChatViewProxy && mIsFromNotify){
			String chatId = getIntent().getStringExtra(NotificationFactory.CHAT_ROOM_ID);
			String roomName = getIntent().getStringExtra(NotificationFactory.CHAT_ROOM_NAME);
			ChatViewProxy proxy = (ChatViewProxy)mCurrentFragment;
			proxy.handleMsgIntent(chatId, roomName);
			mIsFromNotify = false;
		}
	}
	
	@Override
	public void showOrHide(boolean isShow) {
		mSatelliteView.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}
	
	@Override
	public void onBackPressed() {
		Fragment fragment = ChatViewFragmentManager.getInstance().getCurrentFragment();
		if(fragment == null){
			moveTaskToBack(false);
			return ;
		}
		
		// restore the satellite menu
		SatelliteViewManager.getInstance().showOrHide(true);
		
		// remove the chatRoom fragment
		FragmentTransaction tran = fragment.getFragmentManager().beginTransaction();
		tran.setCustomAnimations(R.anim.default_anim_in, R.anim.default_anim_out);
		tran.remove(fragment).commit();
		ChatViewFragmentManager.getInstance().setCurrentFragment(null);
		
		// remove the title
		onExitChatRoom();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
