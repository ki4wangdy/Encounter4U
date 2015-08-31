package com.imrub.shoulder.base.io.http;

public interface IDownloadListener {
	void onPrepared(long totalLength);
	void onDownloadProgressChanged(long downloadedSize, long totalLength);
	void onInterrupted(Exception ex);
	void onCompleted();
}
