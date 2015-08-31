package com.imrub.shoulder.base.app.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
	
    public static boolean isNetworkConnected(Context context) {  
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                    .getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
            if (mNetworkInfo != null) {  
                return mNetworkInfo.isConnected();  
            }  
        }  
        return false;  
    }
  
    public static boolean isConnectingToInternet(Context context){
    	return isNetworkConnected(context);
    }
    
    public static boolean isWifiConnected(Context context) {  
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                    .getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi != null && mWifi.isConnected()) {  
            	return true;
            }  
        }  
        return false;  
    }
    
    public static boolean isMobileConnected(Context context) {  
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                    .getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mMobileNetworkInfo = mConnectivityManager  
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
            if (mMobileNetworkInfo != null && mMobileNetworkInfo.isConnected()) {  
            	return true;
            }  
        }  
        return false;  
    }
}
