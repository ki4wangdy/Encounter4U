package com.imrub.shoulder.base.thread;

public class ActionWrapper<T> {
	public IAction<T> mAction;
	public T mArg;
	public ActionWrapper(IAction<T> action, T arg){
		this.mAction = action;
		this.mArg = arg;
	}
}
