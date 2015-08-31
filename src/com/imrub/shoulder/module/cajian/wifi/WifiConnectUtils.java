package com.imrub.shoulder.module.cajian.wifi;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.Md5;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.net.NetworkManager;
import com.imrub.shoulder.base.db.facade.WifiFacade;
import com.imrub.shoulder.base.db.table.cajian.WifiCollection;

public class WifiConnectUtils {

	public static long preTime;
	
	public static void startScanWifi(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiManager.startScan();
	}
	
	public static void printAllNetworksInfo(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		ArrayList<ScanResult> list = (ArrayList<ScanResult>) wifiManager.getScanResults();
		StringBuilder builder = new StringBuilder();
		for(ScanResult scan : list){
			builder.append(scan.BSSID);
			builder.append(",");
		}

		String macCollection = builder.toString();
		if(updateDBMacCache(macCollection)){
			return ;
		}

		// 删除无用的MAC collection
		WifiFacade.deleteAllUploadedWifiData();
		
		// 如果当前没有连接上网络，该MAC地址集合为离线，下次再上传
		if(!NetworkManager.isConnectingToInternet(AppContext.getAppContext())){
			return ;
		}
		
		final Intent intent = new Intent(context,WifiService.class);
		intent.putExtra("wifimacaddr_time", preTime+"");
		context.startService(intent);
	}

	public static String getWifiNetworks(){
		WifiManager wifiManager = (WifiManager) AppContext.getAppContext().getSystemService(Context.WIFI_SERVICE);
		ArrayList<ScanResult> list = (ArrayList<ScanResult>) wifiManager.getScanResults();
		if(list == null){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for(ScanResult scan : list){
			builder.append(scan.BSSID);
			builder.append(",");
		}
		return builder.toString();
	}
	
	public static String getAllBssid(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		ArrayList<ScanResult> list = (ArrayList<ScanResult>) wifiManager.getScanResults();
		StringBuilder builder = new StringBuilder();
		for(ScanResult scan : list){
			builder.append(scan.BSSID);
			builder.append(",");
		}
		return builder.toString();
	}

	public static boolean updateDBMacCache(String value){
		
		long now = System.currentTimeMillis() / 1000;
		if(preTime != 0){
			if( now - preTime < 5){
				preTime = now ;
				return true;
			}
		}
		
		// 先查询数据库里是否有数据
		WifiCollection item = new WifiCollection();
		int count = WifiFacade.queryAllWifiCollectionCount();
		if(count == 0){
			//直接插入
			long time = now;
			item.setField1(time+"");
			item.setField2(value);
			item.setField4(NetworkManager.isConnectingToInternet(AppContext.getAppContext()) ? 0 : 1);
			item.setField5(Md5.getMD5FullStr(value));
			WifiFacade.insertWifiData(item);
			
		} else {
			// 和最近的第一条数据进行比较，通过md5比较
			String md5NewData = Md5.getMD5FullStr(value);
			item.setField1(now+"");
			WifiCollection oldData = WifiFacade.queryWifiCollection(item);
			if(oldData != null){
				String macCollectionMd5 = oldData.getField5();
				if(macCollectionMd5.equalsIgnoreCase(md5NewData)){
					// 相同的MD5，返回并更新时间
					preTime = now;
					return true;
				}
			}
			
			item.setField1(now+"");
			item.setField2(value);
			item.setField4(NetworkManager.isConnectingToInternet(AppContext.getAppContext()) ? 0 : 1);
			item.setField5(md5NewData);
			WifiFacade.insertWifiData(item);
		}
		
		preTime = now;
		return false;
	}
	
	public static boolean isWifiOpen(Context context){
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		return wifi == State.CONNECTED ? true : false;
	}

	
	public static String toMacAddress(String uid, String token, List<WifiCollection> data, String time){
		JSONObject obj = new JSONObject();
		obj.put("pv", "0.1");
		obj.put("uid", uid);
		obj.put("token", token);
		
		WifiCollection timeCollect = null;
		
		JSONArray offLine = new JSONArray();
		for(WifiCollection item : data){
			
			if(item.getField1().equalsIgnoreCase(time)){
				timeCollect = item;
				continue;
			}
			
			// 1.
			JSONObject itemObj = new JSONObject();
			itemObj.put("timestamp", Integer.parseInt(item.getField1()));
			
			// 2.
			String[] macAddressList = item.getField2().split(",");
			JSONArray array = new JSONArray();
			for(String mac : macAddressList){
				array.add(mac);
			}
			if(array.isEmpty()){
				return null;
			}
			itemObj.put("mac", array);
			
			// 3.
			offLine.add(itemObj);
		}

		obj.put("offline", offLine);
		
		// 添加Online
		if(timeCollect != null){
			String[] macAddressList = timeCollect.getField2().split(",");
			JSONArray array = new JSONArray();
			for(String mac : macAddressList){
				array.add(mac);
			}
			if(array.isEmpty()){
				return null;
			}
			obj.put("online", array);
		}
		
		return obj.toJSONString();
	}

	public static String getOnlineJson(String uid, String token, String macCollection){
		JSONObject obj = new JSONObject();
		obj.put("pv", "0.1");
		obj.put("uid", uid);
		obj.put("token", token);
		
		// 添加Online
		String[] macAddressList = macCollection.split(",");
		JSONArray array = new JSONArray();
		for(String mac : macAddressList){
			array.add(mac);
		}
		if(array.isEmpty()){
			return null;
		}
		obj.put("online", array);
		return obj.toJSONString();
	}

	public static String toAllMacAddress(String uid, String token, List<WifiCollection> data){
		JSONObject obj = new JSONObject();
		obj.put("pv", "0.1");
		obj.put("uid", uid);
		obj.put("token", token);
		
		JSONArray offLine = new JSONArray();
		for(WifiCollection item : data){
			
			// 1.
			JSONObject itemObj = new JSONObject();
			itemObj.put("timestamp", Integer.parseInt(item.getField1()));
			
			// 2.
			String[] macAddressList = item.getField2().split(",");
			JSONArray array = new JSONArray();
			for(String mac : macAddressList){
				array.add(mac);
			}
			if(array.isEmpty()){
				return null;
			}
			itemObj.put("mac", array);
			
			// 3.
			offLine.add(itemObj);
		}

		obj.put("offline", offLine);
		return obj.toJSONString();
	}
	
}
