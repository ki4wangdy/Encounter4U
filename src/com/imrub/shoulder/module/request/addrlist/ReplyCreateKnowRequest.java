package com.imrub.shoulder.module.request.addrlist;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.IAction;
import com.imrub.shoulder.module.request.HttpUrlConstant;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.RequestBase;
import com.imrub.shoulder.module.request.RequestUtils;

public class ReplyCreateKnowRequest {

	public static void replyCreateKnowRequest(final IRequestResult<Integer> result, int fromUid, String comment){
		
		final String postData = AddrlistRequestFactory.replyCreateKnowRequest(fromUid, comment);
		RequestUtils.doASyncRequest(HttpUrlConstant.HttpUrlReplyKnow, postData, new IAction<String>() {
			@Override
			public void onExecute(String arg) {
				int code = -1;
				String msg = AppContext.getString(R.string.error_json_parse);
				try {
					JSONObject obj = (JSONObject)JSON.parse(arg);
					code = obj.getIntValue(RequestBase.Code);
					if(code == RequestBase.CodeOk){
						doSuccessResultOnUIThread(result, RequestBase.CodeOk);
						return ;
					}
				} catch (Exception e) {
				}
				doErrorResultOnUIThread(result, code, msg);
			}
		});
		
	}
	
	private static void doSuccessResultOnUIThread(final IRequestResult<Integer> result, final int r){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onSuccess(r);
			}
		});
	}
	
	private static void doErrorResultOnUIThread(final IRequestResult<Integer> result, final int code, final String msg){
		RequestUtils.doUiHandle(new Runnable() {
			@Override
			public void run() {
				result.onError(code, msg);
			}
		});
	}	
	
}
