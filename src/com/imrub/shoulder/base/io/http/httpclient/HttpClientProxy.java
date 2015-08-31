package com.imrub.shoulder.base.io.http.httpclient;

import com.imrub.shoulder.base.io.http.IDownloadListener;
import com.imrub.shoulder.base.io.http.IHttpWrapper;

public class HttpClientProxy implements IHttpWrapper{

	@Override
	public void downloadData(String url, String path, IDownloadListener listener)
			throws Exception {
		HttpClientUtils.downloadData(url, path, listener);
	}

	@Override
	public void downloadDataAsync(String url, String path,
			IDownloadListener listener) {
		HttpClientUtils.downloadDataAsync(url, path, listener);
	}

	public byte[] getData(String url) throws Exception {
		return HttpClientUtils.getData(url);
	};
	
	@Override
	public byte[] postData(String url, String postData)
			throws Exception {
		return HttpClientUtils.postData(url, postData);
	}

	public static IHttpWrapper createHttpClientProxy(){
		return new HttpClientProxy();
	}
	
}
