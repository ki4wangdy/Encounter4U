package com.imrub.shoulder.module.request;

public interface IRequestResult<T> {
	public void onSuccess(T t);
	public void onError(int code, String msg);
}
