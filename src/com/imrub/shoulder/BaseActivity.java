package com.imrub.shoulder;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.imrub.shoulder.base.app.net.NetworkManager;
import com.imrub.shoulder.module.request.IRequestUI;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.ToastFactory;

public class BaseActivity extends FragmentActivity implements IRequestUI{

	protected Dialog mLoadingDialog;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ActivityController.getInstance().setCurrentActivity(this);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActivityController.getInstance().setCurrentActivity(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		ActivityController.getInstance().setCurrentActivity(this);
		super.onNewIntent(intent);
	}

	@Override
	protected void onStart() {
		ActivityController.getInstance().setCurrentActivity(this);
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		ActivityController.getInstance().setCurrentActivity(this);
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		ActivityController.getInstance().setCurrentActivity(this);
		super.onResume();
	}
	
	public void showLoadingView(String msg){
		mLoadingDialog = DialogFactory.showLoadingDialog(msg);
	}
	
	public void dismissLoadingView(){
		if(mLoadingDialog != null){
			mLoadingDialog.cancel();
			mLoadingDialog = null;
		}
	}
	
	public boolean isLoadingViewShow(){
		return (mLoadingDialog != null && mLoadingDialog.isShowing()) ? true : false;
	}
	
	public void showLoadingView(int msgId){
		showLoadingView(getString(msgId));
	}

	public boolean isNetworkConnected(){
		if(!NetworkManager.isNetworkConnected(this)){
			ToastFactory.showMsg(this, R.string.register_network_off);
			return false;
		}
		return true;
	}
	
	public void startActivity(Intent intent,int enterAnim, int exitAnim) {
		super.startActivity(intent);
		onStartActivityForOverridePendingTransition(enterAnim, exitAnim);
	}
	
	protected void onStartActivityForOverridePendingTransition(int enterAnim, int exitAnim){
		super.overridePendingTransition(enterAnim, exitAnim);
	}
	
	public void finish(int enterAnim, int exitAnim) {
		super.finish();
		onFinishActivityForOverridePendingTransition(enterAnim, exitAnim);
	}
	
	protected void onFinishActivityForOverridePendingTransition(int enterAnim, int exitAnim){
		super.overridePendingTransition(enterAnim, exitAnim);
	}
	
}
