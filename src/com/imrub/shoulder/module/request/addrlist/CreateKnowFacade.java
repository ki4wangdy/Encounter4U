package com.imrub.shoulder.module.request.addrlist;

import java.util.ArrayList;

import com.imrub.shoulder.module.request.IRequestResult;

public class CreateKnowFacade implements IRequestResult<Integer>{

	private ArrayList<ICreateKnow> mListener = new ArrayList<ICreateKnow>();
	
	private static CreateKnowFacade mInstance;
	private CreateKnowFacade(){
	}
	
	public static CreateKnowFacade getInstance(){
		if(mInstance == null){
			mInstance = new CreateKnowFacade();
		}
		return mInstance;
	}
	
	public void registerListener(ICreateKnow listener){
		mListener.add(listener);
	}

	public void unRegisterListener(ICreateKnow listener){
		mListener.remove(listener);
	}
	
	@Override
	public void onError(int code, String msg) {
		for(ICreateKnow know : mListener){
			know.onError();
		}
	}

	public void onSuccess(Integer t) {
		for(ICreateKnow know : mListener){
			know.onSuccess();
		}
	};

	public void replyCreateKnowRequest(int toUid, String comment){
		ReplyCreateKnowRequest.replyCreateKnowRequest(this, toUid, comment);
	}
	
	public void createKnowRequest(int toUid, String comment){
		CreateKnowRequest.createKnowRequest(this, toUid, comment);
	}
	
	public void agreeKnowRequest(int fromUid){
		AgreeCreateKnowRequest.agreeCreateKnowRequest(this, fromUid);
	}
}
