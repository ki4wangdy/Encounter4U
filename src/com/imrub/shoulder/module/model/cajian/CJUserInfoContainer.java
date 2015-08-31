package com.imrub.shoulder.module.model.cajian;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.WifiFacade;
import com.imrub.shoulder.base.db.table.cajian.WifiUserData;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.module.cajian.wifi.IWifiUserData;

public class CJUserInfoContainer {

	public static final String Tag = "CJUserInfoContainer";
	
	private List<WifiUserData> mInfos = new ArrayList<WifiUserData>();
	private List<IWifiUserData> mListener = new ArrayList<IWifiUserData>();
	
	private static CJUserInfoContainer mInstance;
	private CJUserInfoContainer(){
	}

	public static CJUserInfoContainer getInstance(){
		if(mInstance == null){
			mInstance = new CJUserInfoContainer();
		}
		return mInstance;
	}

	public void registerListener(IWifiUserData listener){
		mListener.add(listener);
	}
	
	public void unRegisterListener(IWifiUserData listener){
		mListener.remove(listener);
	}
	
	public void fire(final List<WifiUserData> data){
		synchronized (CJUserInfoContainer.class) {
			CJUserInfoContainer.getInstance().onWifiServiceChangeAddData(data);
			for(IWifiUserData listener : mListener){
				listener.onWifiUserChange(data);
			}
		}
	}

	public void setData(List<WifiUserData> data){
		mInfos = data;
	}
	
	public List<WifiUserData> getCJUserInfo(){
		return mInfos;
	}
	
	public boolean isEmpty(){
		return mInfos.isEmpty();
	}
	
	private void onWifiServiceChangeAddData(List<WifiUserData> data){
		if(mInfos == null || mInfos.isEmpty()){
			mInfos = new ArrayList<WifiUserData>();
			mInfos.addAll(data);
			return ;
		}
		
		SparseArray<WifiUserData> infoMap = new SparseArray<WifiUserData>();
		
		int size = mInfos.size();
		for(int i=0; i<size; i++){
			WifiUserData d = mInfos.get(i);
			Logger.print(Tag, "infos's data i:"+i+" meet_uid:"+d.getMeet_uid());
			infoMap.put(d.getMeet_uid(), d);
		}
		
		size = data.size();
		for(int i=0; i<size; i++){
			WifiUserData newData = data.get(i);
			WifiUserData datas = infoMap.get(newData.getMeet_uid());
			if(datas != null){
				Logger.print(Tag, "datas.copyFromData(newData) i:"+i+" newData's meet_uid:"+newData.getMeet_uid() + " infoData's meet_uid:"+datas.getMeet_uid());
				datas.copyFromData(newData);
			} else {
				Logger.print(Tag, "mInfos.add(newData); i:"+i+" newData's meet_uid:"+newData.getMeet_uid());
				mInfos.add(newData);
			}
		}
		
		for(WifiUserData ds : mInfos){
			Logger.print(Tag, " onWifiServiceChangeAddData info'size is :" + ds.getMeet_uid());
		}
	}
	
	public void onSplashLoadDB(){

		if("".equalsIgnoreCase(UserInfo.getInstance().getUid())){
			return ;
		}

		List<WifiUserData> allData = WifiFacade.queryAllWifiUserData();
		if(allData == null || allData.isEmpty()){
			Logger.print(Tag, "onSplashLoadDB init data is empty!");
			return ;
		}
		
		mInfos.clear();
		for(WifiUserData d : allData){
			Logger.print(Tag, "onSplashLoadDB init data is:"+d.getMeet_uid());
			mInfos.add(d);
		}
		
		
	}
	
}
