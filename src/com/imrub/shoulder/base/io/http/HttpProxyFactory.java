package com.imrub.shoulder.base.io.http;

import java.util.HashMap;

import com.imrub.shoulder.base.io.http.httpclient.HttpClientProxy;
import com.imrub.shoulder.base.io.http.zip.GZipHttpProxy;

public class HttpProxyFactory {

	private static final HashMap<Proxy, IHttpWrapper> proxyCache = new HashMap<HttpProxyFactory.Proxy, IHttpWrapper>();

	public static enum Proxy {
		httpclient, curl, ziphttpclient
	}

	public static IHttpWrapper createProxy(final Proxy proxyType) {
		IHttpWrapper proxy = proxyCache.get(proxyType);
		if(proxy != null){
			return proxy;
		}
		
		switch (proxyType) {
			case httpclient:
				proxy = HttpClientProxy.createHttpClientProxy();
				break;
			case ziphttpclient:
				proxy = GZipHttpProxy.createGzipHttpProxy();
				break;
			case curl:
				// be implemented in the future
				break;
		}
		
		if(proxy == null){
			proxy = HttpClientProxy.createHttpClientProxy();
		}
		proxyCache.put(proxyType, proxy);
		return proxy;
	}

	public static Proxy getCurrentDefaultProxy(){
		return Proxy.httpclient;
	}
	
}
