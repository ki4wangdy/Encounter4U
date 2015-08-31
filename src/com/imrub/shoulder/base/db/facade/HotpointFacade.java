package com.imrub.shoulder.base.db.facade;

import java.util.ArrayList;
import java.util.List;

import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.UserSqliteManager;
import com.imrub.shoulder.base.db.table.HotPoint;
import com.imrub.shoulder.base.db.table.HotPoint.HotPointConstant;
import com.imrub.shoulder.base.db.table.SqliteUtil;
import com.imrub.shoulder.module.hotpoint.IHotpointChange;
import com.imrub.shoulder.module.hotpoint.SettingHotpointManager;

public class HotpointFacade {

	private HotPoint mHotpoint;
	private List<IHotpointChange> mListener;
	
	private static HotpointFacade mInstance;
	private HotpointFacade(){
		if(mHotpoint == null){
			mHotpoint = HotpointFacade.initHotpointSystem();
			mListener = new ArrayList<IHotpointChange>();
		}
	}

	public static HotpointFacade getInstance(){
		if(mInstance == null){
			mInstance = new HotpointFacade();
		}
		return mInstance;
	}
	
	public HotPoint getHotpoint(){
		return mHotpoint;
	}
	
	public void regisetListener(IHotpointChange listener){
		mListener.add(listener);
	}

	public void unRegisterListener(IHotpointChange listener){
		mListener.remove(listener);
	}

	public void onStartMainUI(){
		for(IHotpointChange l : mListener){
			l.onChange(IHotpointChange.Type_UI_Main, mHotpoint);
		}
	}
	
	/**
	 * 基于Fiedl2, 更新主按钮
	 */
	public void updateMainHotpoint(){
		int value = mHotpoint.getField2();
		if(value == 0){
			mHotpoint.removeField1(HotPointConstant.F1_Status_MainHotpiont);
		} else {
			mHotpoint.setField1(HotPointConstant.F1_Status_MainHotpiont);
		}
		mHotpoint.updateLocalDb();
		updateMainUIHotpoint();
	}
	
	private void updateMainUIHotpoint(){
		for(IHotpointChange l : mListener){
			l.onChange(IHotpointChange.Type_UI_Main, mHotpoint);
		}
	}
	
	public static HotPoint initHotpointSystem(){
		String userId = UserInfo.getInstance().getUid();
		if("".equalsIgnoreCase(userId)){
			return null;
		}
		HotPoint mHotpoint = new HotPoint();
		mHotpoint.setUserid(Integer.parseInt(userId));
		List<HotPoint> points = SqliteUtil.queryAllItem(UserSqliteManager.getInstance(), mHotpoint);
		if(points == null || points.isEmpty()){
			SettingHotpointManager.firstInitlizeSetting(mHotpoint);
			mHotpoint.insertLocalDb();
		}
		return mHotpoint;
	}
	
}
