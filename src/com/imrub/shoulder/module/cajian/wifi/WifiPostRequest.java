package com.imrub.shoulder.module.cajian.wifi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.db.facade.WifiFacade;
import com.imrub.shoulder.base.db.table.cajian.WifiUserData;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.module.model.cajian.CJUserInfoContainer;

public class WifiPostRequest {

	public static final String Tag = "WifiPostRequest";
	
	private static WifiPostRequest mInstance;
	private WifiPostRequest(){
	}
	
	public static WifiPostRequest getInstance(){
		if(mInstance == null){
			mInstance = new WifiPostRequest();
		}
		return mInstance;
	}
	
	public synchronized boolean postRequest(String url, String postData){
		if(postData == null || postData.isEmpty()){
			return false;
		}
		HttpPost httpPost = new HttpPost(url); 
        HttpResponse httpResponse = null; 
        try { 
        	httpPost.setEntity(new StringEntity(postData, "UTF-8"));
            httpResponse = new DefaultHttpClient().execute(httpPost); 
            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
                String value = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                Logger.print(Tag, "after upload data, the result value is :"+value);
                return updateDB(value);
            } 
        } catch (ClientProtocolException e) { 
        } catch (IOException e) { 
        } 
        return false;
	}
	
	
	private boolean updateDB(String value){
		JSONObject obj = JSONObject.parseObject(value);
		int code = obj.getInteger("code");
		if(code != 0){
			return false;
		}
		
		JSONArray array = obj.getJSONArray("meet_info");
		if(array != null){
			List<WifiUserData> wifiData = JSONObject.parseArray(array.toJSONString(), WifiUserData.class);
			updateWifiDataToDatabase(wifiData);
		}
		return true;
	}

	private void updateWifiDataToDatabase(List<WifiUserData> data){
		
		// 1.查询数据库是否空
		boolean isEmpty = WifiFacade.isWifiDataEmpty();
		if(isEmpty){
			WifiFacade.insertAllWifiUser(data);
			// 通知其他界面
			CJUserInfoContainer.getInstance().fire(data);
		} else {
			// 2.遍历data，然后更新
			updateDBBaseOnNewData(data);
		}
		
	}
	
	private void updateDBBaseOnNewData(List<WifiUserData> data){
		ArrayList<WifiUserData> updateData = new ArrayList<WifiUserData>();
		ArrayList<WifiUserData> newData = new ArrayList<WifiUserData>();
		
		WifiUserData temp = new WifiUserData();
		for(WifiUserData item : data){
			temp.setMeet_uid(item.getMeet_uid());
			boolean isExist = WifiFacade.queryWifiUserIsExist(temp);
			// 存在该item，更新值
			if(isExist){
				updateData.add(item);
			} else {
				newData.add(item);
			}
		}
		
		// 1. 开始事务，批量更新
		if(!updateData.isEmpty()){
			WifiFacade.updateAllWifiUser(updateData);
		}

		// 2. 开始事务，批量插入
		if(!newData.isEmpty()){
			WifiFacade.insertAllWifiUser(newData);
		}
		
		// 3. 通知其他界面
		updateData.addAll(newData);
		CJUserInfoContainer.getInstance().fire(updateData);
	}
	
}
