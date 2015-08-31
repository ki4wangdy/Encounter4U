package com.imrub.shoulder.module.hotpoint;

import com.imrub.shoulder.base.db.table.HotPoint;

public interface IHotpointChange {
	// this is for Type
	public static final int Type_UI_Main  =  1<<0;
	
	public void onChange(int type, HotPoint point);
}
