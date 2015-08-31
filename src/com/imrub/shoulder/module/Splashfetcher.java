package com.imrub.shoulder.module;

import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.app.path.BitmapStream;
import com.imrub.shoulder.base.app.store.SplashJpeg;
import com.imrub.shoulder.base.io.http.HttpService;
import com.imrub.shoulder.base.thread.ThreadFacade;

public class Splashfetcher {
	
	public static final String splashIconUrl = "http://121.42.10.208:8080/splash.json";
	
	/**
	 * the splash.json example
	 * {
     *	"start_time": 0,
     *	"end_time": 0,
     *	"url": "http://121.42.10.208/splash/splashicon.png"
	 * }
	 * @param url
	 * @param listener
	 */
	
	public static void asyncFetcher(final String url,final IFetcherListener listener){
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				try{
					byte[] buf = HttpService.getInstance().getData(url);
					onFetcherResult(buf, listener);
				}catch(Exception e){
				}
			}
		});
	}

	private static void onFetcherResult(final byte[] buf, final IFetcherListener listener){
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(listener != null){
					listener.onFetecher(buf);
				}
			}
		});
	}
	
	public static void asyncFetcherSplashIcon(String url){
//		boolean isOk = TimeUtil.checkSplashTimeForOnce();
//		if(!isOk){
//			return ;
//		}
		
		SplashJpeg.getInstance().setSplashOnceTime(new Date().getTime());
		asyncFetcher(url, new IFetcherListener() {
			@Override
			public void onFetecher(byte[] str) {
				if(str == null){
					SplashJpeg.getInstance().setStartTime(0);
					SplashJpeg.getInstance().setEndTime(0);
					return ;
				}
				JSONObject obj = JSON.parseObject(new String(str));
				String id = SplashJpeg.getInstance().getSplashIconId();
				if(obj.getString("id").equals(id)){
					return ;
				}
				SplashJpeg.getInstance().setSplashIconId(obj.getString("id"));
				SplashJpeg.getInstance().setStartTime(obj.getLong("start_time"));
				SplashJpeg.getInstance().setEndTime(obj.getLong("end_time"));
				asyncDownloadIcon(obj.getString("url"));
			}
		});
	}
	
	private static void asyncDownloadIcon(final String iconUrl){
		asyncFetcher(iconUrl, new IFetcherListener() {
			@Override
			public void onFetecher(byte[] str) {
//				BitmapHelper.verifyBitmap(str);
				Bitmap bitmap = BitmapFactory.decodeByteArray(str, 0, str.length);
				BitmapStream.saveBitmap(bitmap);
				bitmap.recycle();
			}
		});
	}
	
	public interface IFetcherListener{
		public void onFetecher(final byte[] str);
	}
	
}
