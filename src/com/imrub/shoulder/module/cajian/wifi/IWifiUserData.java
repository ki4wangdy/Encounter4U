package com.imrub.shoulder.module.cajian.wifi;

import java.util.List;

import com.imrub.shoulder.base.db.table.cajian.WifiUserData;

public interface IWifiUserData {
	public void onWifiUserChange(List<WifiUserData> data);
}
