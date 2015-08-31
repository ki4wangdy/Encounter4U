package com.imrub.shoulder.base.io.http.zip;

import com.imrub.shoulder.base.io.http.IDownloadListener;
import com.imrub.shoulder.base.io.http.IHttpWrapper;

public class GZipHttpProxy implements IHttpWrapper{

	@Override
	public void downloadData(String url, String path, IDownloadListener listener)
			throws Exception {
		GzipHttpUtils.downloadData(url, path, listener);
	}

	@Override
	public void downloadDataAsync(String url, String path,
			IDownloadListener listener) {
		GzipHttpUtils.downloadDataAsync(url, path, listener);
	}

	public byte[] getData(String url) throws Exception {
		return GzipHttpUtils.getData(url);
	};
	
	@Override
	public byte[] postData(String url, String postData)
			throws Exception {
//		return GzipHttpUtils.postData(url, postData);
		return null;
	}

	public static IHttpWrapper createGzipHttpProxy(){
		return new GZipHttpProxy();
	}

}
