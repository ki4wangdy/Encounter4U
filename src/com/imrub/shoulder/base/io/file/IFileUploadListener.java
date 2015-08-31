package com.imrub.shoulder.base.io.file;

public interface IFileUploadListener {
	public void transferStarted(String filePath, long num);
	public void transferred(long num);
	public void transferFinished(int code, String url, String localPath, String fileId, String expires);
	public void transferFailed(int code, String localPath);
}
