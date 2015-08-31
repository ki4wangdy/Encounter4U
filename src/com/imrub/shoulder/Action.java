package com.imrub.shoulder;

public interface Action<T> {
	public void onExecute(final T arg);
}
