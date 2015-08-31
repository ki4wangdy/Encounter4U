package com.imrub.shoulder.module.hotpoint;

import android.view.ext.ISatelliteMenuAnimateListener;
import android.view.ext.SatelliteMenu;

import com.imrub.shoulder.base.db.facade.HotpointFacade;
import com.imrub.shoulder.base.db.facade.RoomFacade;
import com.imrub.shoulder.base.db.table.HotPoint;
import com.imrub.shoulder.base.db.table.HotPoint.HotPointConstant;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.main.SatelliteMenuProxy;

public class MainUIHotpointProxy implements ISatelliteMenuAnimateListener, IHotpointChange, IMsgCountListener{

	private boolean mIsStartRun = true;
	private SatelliteMenu mMenu;
	
	public MainUIHotpointProxy(SatelliteMenu proxy){
		this.mMenu = proxy;
		this.mMenu.addMenuListener(this);
		registerHotpointListener();
	}
	
	@Override
	public void onAnimateListener(int type, int id) {
		if(ISatelliteMenuAnimateListener.Type_End == type){
			if(id == SatelliteMenuProxy.Item_Cajian && mIsStartRun){
				mIsStartRun = false;
				onAppFirstRunFinished();
			}
		} else {
		}
	}

	private void onAppFirstRunFinished(){
		HotpointFacade.getInstance().onStartMainUI();
	}
	
	private void registerHotpointListener(){
		HotpointFacade.getInstance().regisetListener(this);
		RoomFacade.getInstance().setMsgUpdateListener(this);
	}
	
	public void onClickHotpoint(int id){
		if(!HotpointUtils.isShowMainHotpoint(HotpointFacade.
				getInstance().getHotpoint())){
			return ;
		}
		switch (id) {
			case SatelliteMenu.Item_Cajian:
				break;
			case SatelliteMenu.Item_Addrlist:
				break;
			case SatelliteMenu.Item_Chat:
				break;
			case SatelliteMenu.Item_Yuanfen:
				break;
			case SatelliteMenu.Item_Setting:
				SettingHotpointManager.firstClickSetting();
				break;
		}
	}
	
	@Override
	public void onChange(final int type, final HotPoint point) {
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onChangeOnMainThread(type, point);
			}
		});
	}

	private void onChangeOnMainThread(int type, final HotPoint point){
		if(type == IHotpointChange.Type_UI_Main){
			// item point
			itemhotpointChange(point);
			ThreadFacade.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// main point
					if(HotpointUtils.isShowMainHotpoint(point)){
						mMenu.addMainHotpoint();
					} else {
						mMenu.removeMainHotpoint();
					}
				}
			}, 100);
		}
	}
	
	private void itemhotpointChange(HotPoint point){
		// setting
		updateSettingHotpoint(point);
		
		// addressList
		updateAddressListHotpoint(point);
		
		// chat message count
		mMenu.updateChatHotpoint(RoomFacade.getInstance().getTotalMsgCount());
	}

	private void updateAddressListHotpoint(HotPoint point){
		int result = point.getField2() & HotPointConstant.F2_Status_Address;
		if(result != 0){
			mMenu.addAddresslistHotpoint();
		} else {
			mMenu.removeAddresslistHotpoint();
		}
	}
	
	private void updateSettingHotpoint(HotPoint point){
		int result = point.getField2() & HotPointConstant.F2_Status_Setting;
		if(result != 0){
			mMenu.addSettingHotpoint();
		} else {
			mMenu.removeSettingHotpoint();
		}
	}
	
	@Override
	public void onUpdateMsg(int totalCount) {
		ChatHotpointManager.updateChatMsgCount(totalCount);
	}
	
}
