package com.imrub.shoulder.module.hotpoint;

import com.imrub.shoulder.base.db.table.HotPoint;

public class HotpointUtils {

	public static boolean isShowMainHotpoint(HotPoint point){
		return point.getField1() == HotPoint.HotPointConstant.F1_Status_MainHotpiont ? true : false;
	}
	
}
