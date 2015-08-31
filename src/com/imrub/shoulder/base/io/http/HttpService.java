package com.imrub.shoulder.base.io.http;


public class HttpService implements IHttpWrapper{

	private static HttpService mInstance ;
	private IHttpWrapper mProxy;
	
	private HttpService(){
		mProxy = HttpProxyFactory.createProxy(HttpProxyFactory.getCurrentDefaultProxy());
	}

	public static HttpService getInstance(){
		if(mInstance == null){
			synchronized (HttpService.class) {
				if(mInstance == null){
					mInstance = new HttpService();
				}
			}
		}
		return mInstance;
	}

	@Override
	public void downloadData(String url, String path, IDownloadListener listener)
			throws Exception {
		mProxy.downloadData(url, path, listener);
	}
	
	@Override
	public void downloadDataAsync(String url, String path,
			IDownloadListener listener) {
		mProxy.downloadDataAsync(url, path, listener);
	}

	@Override
	public byte[] getData(String url) throws Exception {
		return mProxy.getData(url);
	}

	@Override
	public byte[] postData(String url, String postData)
			throws Exception {
		return mProxy.postData(url, postData);
	}
	
}
