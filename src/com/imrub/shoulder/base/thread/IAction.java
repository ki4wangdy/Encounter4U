package com.imrub.shoulder.base.thread;

public interface IAction<T extends Object> {
	public void onExecute(final T arg);
}
