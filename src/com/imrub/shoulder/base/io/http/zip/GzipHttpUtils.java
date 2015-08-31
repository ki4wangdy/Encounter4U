package com.imrub.shoulder.base.io.http.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import com.imrub.shoulder.base.io.http.IDownloadListener;

public class GzipHttpUtils {

	private static final String CriticalUserAgent = "im-encounter";
	public static final int NetworkTransferBufSize = 40960 * 3;

	private static final int OPT_CONNECT_TIMEOUT = 15000;
	private static final int OPT_READ_TIMEOUT = 30000;

	public static byte[] getData(String url) throws Exception {
		HttpResponse response = generateHttpClientViaGet(url);
		InputStream is = null;
		try{
			is = response.getEntity().getContent();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[NetworkTransferBufSize];
			int len = 0;
			
			while ((len = is.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		}catch(Exception e){
		}finally{
			if(is != null){
				try{
					is.close();
				}catch(Exception e){
				}
			}
		}
		return null;
	}
	
	
	public static byte[] postData(String url, List<NameValuePair> params) throws Exception {
		HttpResponse response = generateHttpClientViaPost(url, params);
		
		InputStream is = null;
		try{
			is = response.getEntity().getContent();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[NetworkTransferBufSize];
			int len = 0;
			
			while ((len = is.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		}catch(Exception e){
		}finally{
			if(is != null){
				try{
					is.close();
				}catch(Exception e){
				}
			}
		}
		return null;
	}
	
	public static boolean downloadToFile(String url, String path) {
		try {
			HttpResponse httpResponse = generateHttpClientViaGet(url);
			FileOutputStream fos = null;
			InputStream is = null;
			try {
				int httpCode = httpResponse.getStatusLine().getStatusCode();
				if (httpCode >= 300 || httpCode < 200) {
					return false;
				}
				is = httpResponse.getEntity().getContent();
				fos = new FileOutputStream(path);
				byte[] buf = new byte[NetworkTransferBufSize];
				int nReaded = 0;
				while ((nReaded = is.read(buf)) > 0) {
					fos.write(buf, 0, nReaded);
				}
				return true;
			} catch (Exception ex) {
				File f = new File(path);
				if (f.exists()) {
					f.delete();
				}
				return false;
			} finally {
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			}
		} catch (Exception ex) {
			File f = new File(path);
			if (f.exists()) {
				f.delete();
			}
			return false;
		}
	}

	public static void downloadDataAsync(final String url, final String path, final IDownloadListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					downloadData(url, path, listener);
				} catch (Exception e) {
					listener.onInterrupted(e);
				}
			}
		}).start();
	}
	
	public static byte[] downloadData(String url, final String path, final IDownloadListener listener) throws IOException {
		HttpResponse httpResponse = generateHttpClientViaGet(url);
		InputStream is = null;
		try {
			is = httpResponse.getEntity().getContent();
			int httpCode = httpResponse.getStatusLine().getStatusCode();
			if (httpCode < 200 || httpCode >= 300) {
				throw new Exception("http code not right..." + httpCode + "@" + url);
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[NetworkTransferBufSize];
			int nReaded = 0;
			while ((nReaded = is.read(buf)) > 0) {
				bos.write(buf, 0, nReaded);
			}
			return bos.toByteArray();
		} catch (Exception ex) {
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	private static HttpResponse generateHttpClientViaGet(String url) throws IOException {
		HttpGet httpGet = new HttpGet(url);
		httpGet.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, OPT_CONNECT_TIMEOUT);
		httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, OPT_READ_TIMEOUT);
		httpGet.addHeader("User-agent", CriticalUserAgent);
		httpGet.addHeader("accept-encoding", "gzip");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse httpResp, HttpContext httpContext) throws HttpException, IOException {
				HttpEntity entity = httpResp.getEntity();
				Header ceheader = entity.getContentEncoding();
				if (ceheader != null) {
					for (HeaderElement element : ceheader.getElements()) {
						if (element.getName().equalsIgnoreCase("gzip")) {
							httpResp.setEntity(new GzipDecompressingEntity(httpResp.getEntity()));
						}
					}
				}
			}
		});
		return httpClient.execute(httpGet);
	}
	
	private static HttpResponse generateHttpClientViaPost(String url,List<NameValuePair> params) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, OPT_CONNECT_TIMEOUT);
		httpPost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, OPT_READ_TIMEOUT);
		httpPost.addHeader("User-agent", CriticalUserAgent);
		httpPost.addHeader("accept-encoding", "gzip");
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse httpResp, HttpContext httpContext) throws HttpException, IOException {
				HttpEntity entity = httpResp.getEntity();
				Header ceheader = entity.getContentEncoding();
				if (ceheader != null) {
					for (HeaderElement element : ceheader.getElements()) {
						if (element.getName().equalsIgnoreCase("gzip")) {
							httpResp.setEntity(new GzipDecompressingEntity(httpResp.getEntity()));
						}
					}
				}
			}
		});
		return httpClient.execute(httpPost);
	}
	
}
