package com.imrub.shoulder.module.hotpoint;

import com.imrub.shoulder.base.db.facade.HotpointFacade;
import com.imrub.shoulder.base.db.table.HotPoint;
import com.imrub.shoulder.base.db.table.HotPoint.HotPointConstant;

public class AddrHotpointManager {

	/**
	 * 更新通讯录
	 */
	public static void showAddressListHotpoint(){
		final HotPoint hotpoint = HotpointFacade.getInstance().getHotpoint();
		hotpoint.setField2(HotPointConstant.F2_Status_Address);
		HotpointFacade.getInstance().updateMainHotpoint();
	}

	public static void removeAddressListHotpoint(){
		final HotPoint hotpoint = HotpointFacade.getInstance().getHotpoint();
		hotpoint.removeField2(HotPointConstant.F2_Status_Address);
		HotpointFacade.getInstance().updateMainHotpoint();
	}
	

}
