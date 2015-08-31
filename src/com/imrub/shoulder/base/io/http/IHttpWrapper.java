package com.imrub.shoulder.base.io.http;


public interface IHttpWrapper {

	public byte[] getData(String url) throws Exception ;
	public byte[] postData(String url, String postData) throws Exception ;

	public void downloadDataAsync(final String url, final String path, final IDownloadListener listener) ;
	public void downloadData(String url, String path, IDownloadListener listener) throws Exception ;
}
