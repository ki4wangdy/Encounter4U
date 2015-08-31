package com.imrub.shoulder.module.hotpoint;

import com.imrub.shoulder.base.app.store.AppConfig;
import com.imrub.shoulder.base.db.facade.HotpointFacade;
import com.imrub.shoulder.base.db.table.HotPoint;
import com.imrub.shoulder.base.db.table.HotPoint.HotPointConstant;

public class SettingHotpointManager {

	public static void firstClickSetting(){
		final HotPoint hotpoint = HotpointFacade.getInstance().getHotpoint();
		if(AppConfig.getInstance().isUserFirstClickSetting()){
			AppConfig.getInstance().setUserFirstClickSetting(false);
			hotpoint.removeField2(HotPointConstant.F2_Status_Setting);
			HotpointFacade.getInstance().updateMainHotpoint();
		}
	}
	
	/**
	 * 在设置模块，第一次初始化红点
	 */
	public static void firstInitlizeSetting(HotPoint hotpoint){
		hotpoint.setField7(HotPointConstant.F7_Status_User_Icon);
		hotpoint.setField2(HotPointConstant.F2_Status_Setting);
		hotpoint.setField1(HotPointConstant.F1_Status_MainHotpiont);
	}
	
	public static boolean isUserIconShow(){
		final HotPoint hotpoint = HotpointFacade.getInstance().getHotpoint();
		int value = hotpoint.getField7();
		return (value & HotPointConstant.F7_Status_User_Icon) == 1 ? true : false;
	}
	
	public static void clearUserIconShow(){
		final HotPoint hotpoint = HotpointFacade.getInstance().getHotpoint();
		hotpoint.removeField7(HotPointConstant.F7_Status_User_Icon);
		if(hotpoint.getField7() == 0){
			SettingHotpointManager.removeSettingHotpoint();
		}
	}

	public static void showSettingHotpoint(){
		final HotPoint hotpoint = HotpointFacade.getInstance().getHotpoint();
		hotpoint.setField2(HotPointConstant.F2_Status_Setting);
		HotpointFacade.getInstance().updateMainHotpoint();
	}

	public static void removeSettingHotpoint(){
		final HotPoint hotpoint = HotpointFacade.getInstance().getHotpoint();
		hotpoint.removeField2(HotPointConstant.F2_Status_Setting);
		HotpointFacade.getInstance().updateMainHotpoint();
	}
	
}
