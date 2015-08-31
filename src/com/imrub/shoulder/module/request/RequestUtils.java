package com.imrub.shoulder.module.request;

import com.imrub.shoulder.base.io.http.HttpService;
import com.imrub.shoulder.base.thread.IAction;
import com.imrub.shoulder.base.thread.ThreadFacade;

public class RequestUtils {
	
	public static final int JSON_TYPE_CAJIANLOGIN		=  0x0000000a;
	public static final int JSON_TYPE_FORGETPW			=  0x0000000b;
	public static final int JSON_TYPE_RESETPW			=  0x0000000c;
	
	public static void doASyncRequest(final String httpUrl, final String postData, final IAction<String> action){
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				try {
					byte[] result = HttpService.getInstance().postData(httpUrl, postData);
					action.onExecute(new String(result));
				} catch (Exception e) {
				}
			}
		});
	}
	
	public static void doUiHandle(final Runnable task){
		ThreadFacade.runOnUiThread(task);
	}
	
	public static void doSuccessResultOnUIThread(final IRequestResult<String> result, final String str){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onSuccess(str);
			}
		});
	}
	
	public static void doErrorResultOnUIThread(final IRequestResult<String> result, final int code, final String msg){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onError(code, msg);
			}
		});
	}
}
