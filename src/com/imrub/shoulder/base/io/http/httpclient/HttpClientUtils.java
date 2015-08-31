package com.imrub.shoulder.base.io.http.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.imrub.shoulder.base.io.http.IDownloadListener;

public class HttpClientUtils {

	private static final int BUFFER_LENGTH = 1024 * 10;
	private static final int OPT_CONNECT_TIMEOUT = 15000;
	private static final int OPT_READ_TIMEOUT = 30000;

	public static byte[] getData(String url) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		
		if(response.getStatusLine().getStatusCode() == 200){
			InputStream is = response.getEntity().getContent();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[BUFFER_LENGTH];
			int len = 0;
			
			while ((len = is.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		}
		return null;
	}

	public static byte[] postData(String url, String postData) throws Exception {

		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		
		request.setEntity(new StringEntity(postData, "UTF-8"));
		
		request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, OPT_CONNECT_TIMEOUT);
		request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, OPT_READ_TIMEOUT);
		HttpResponse response = client.execute(request);
		
		InputStream is = response.getEntity().getContent();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[BUFFER_LENGTH];
		int len = 0;

		while ((len = is.read(buffer)) > 0) {
			bos.write(buffer, 0, len);
		}
		
		is.close();
		return bos.toByteArray();
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

	public static void downloadData(String url, String path, IDownloadListener listener) throws Exception {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}

		File fileTemp = new File(path + ".temp");

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, OPT_CONNECT_TIMEOUT);
		request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, OPT_READ_TIMEOUT);

		long offset = fileTemp.length();
		request.addHeader("Range", "bytes=" + offset + "-");

		HttpResponse response = client.execute(request);
		long totalLength = response.getEntity().getContentLength() + offset;
		listener.onPrepared(totalLength);
		int httpCode = response.getStatusLine().getStatusCode();
		if (httpCode == 416 || (httpCode >= 400 && httpCode < 500 && httpCode != 404)) {
			listener.onCompleted();
			return;
		}

		if (totalLength == offset) {
			listener.onCompleted();
			return;
		}

		InputStream is = response.getEntity().getContent();
		FileOutputStream fos = new FileOutputStream(fileTemp, true);
		byte[] buffer = new byte[BUFFER_LENGTH];
		int len = 0;
		long downloadedSize = offset;
		while ((len = is.read(buffer)) > 0) {
			fos.write(buffer, 0, len);
			downloadedSize += len;
			listener.onDownloadProgressChanged(downloadedSize, totalLength);
		}
		fos.close();
		is.close();

		fileTemp.renameTo(file);
		listener.onCompleted();
	}
}
