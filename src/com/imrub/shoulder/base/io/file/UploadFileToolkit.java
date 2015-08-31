package com.imrub.shoulder.base.io.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.app.store.UserInfo;

public class UploadFileToolkit {

	private static final int connect_time_out = 45 * 1000;
	private static final int socket_time_out = 60 * 1000;

	public static int uploadFile(final String hostUri, final String filepath, final IFileUploadListener listener) {

		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, connect_time_out);
		HttpConnectionParams.setSoTimeout(params, socket_time_out);

		HttpClient client = new DefaultHttpClient(params);
		final File file = new File(filepath);
		final HttpPost post = new HttpPost(hostUri);
		post.addHeader("Connection", "close");

		try {

			CustomMultiPartEntity entity = new CustomMultiPartEntity(
					new CustomMultiPartEntity.ProgressListener() {

						@Override
						public void transferred(long numBytes) {
							long num = numBytes;
							if (num > file.length()) {
								num = file.length();
							}
							if (null != listener) {
								listener.transferred(num);
							}

						}
					});

			FileBody fb = new FileBody(file);
			entity.addPart("file", fb);
			post.setEntity(entity);

			// upload retry time = 3
			DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(3, true);
			((AbstractHttpClient) client).setHttpRequestRetryHandler(retryHandler);

			HttpResponse response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			
			if(HttpURLConnection.HTTP_OK == code){
				HttpEntity responseEntity = response.getEntity();
				InputStream is = responseEntity.getContent();
				String responseBody = inputStreamToString(is);
				//{"masterFileId":"http://121.42.10.208:8888/group1/M00/00/00/eSoK0FXVXlySNN3YAABH5FHmezc948.jpg","code":0}
				JSONObject obj = JSONObject.parseObject(responseBody);
				String logo = obj.getString("masterFileId");
				if(logo != null){
					UserInfo.getInstance().setUserLogo(logo);
				}
			}
			return code;
		} catch (Exception e) {
			// Use this rather than e.printStackTrace
			if (null != listener) {
				listener.transferFailed(-1, file.getAbsolutePath());
			}
		} finally {
			// client.getConnectionManager().shutdown();
		}
		return -1;
	}

	private static String inputStreamToString(InputStream is)
			throws IOException {
		try {
			if (null != is) {
				Writer writer = new StringWriter();
				char[] buffer = new char[1024];
				Reader reader = new BufferedReader(new InputStreamReader(is));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				return writer.toString();
			}
		} finally {
			is.close();
		}
		return null;
	}

}
