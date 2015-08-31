package com.imrub.shoulder.module;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.path.EnvirPath;
import com.imrub.shoulder.base.app.store.AppConfig;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.FriendFacade;
import com.imrub.shoulder.base.db.facade.RoomFacade;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.Logger;
import com.imrub.shoulder.base.util.TimeUtil;
import com.imrub.shoulder.base.util.log.LoggerUtils;
import com.imrub.shoulder.module.im.client.IMClient;
import com.imrub.shoulder.module.login.LoginActivity;
import com.imrub.shoulder.module.main.MainUIActivity;
import com.imrub.shoulder.module.model.cajian.CJUserInfoContainer;

public class SplashActivity extends BaseActivity {

	public static final String tag = "SplashActivity";
	
	private boolean hasInitlized;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter);

		hasInitlized = false;
		initilzeSplashIcon();
		doInitlizeJob();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				forwardActivity(getIntent());
			}
		}, 1800);
		
	}

	private void initilzeSplashIcon(){
		if(!TimeUtil.checkWhetherSplashIconOverTime()){
			ImageView imageView=  (ImageView)findViewById(R.id.splash_imageview);
			imageView.setImageURI(Uri.fromFile(new File(EnvirPath.getSplashIconFilePath())));
		}
		Splashfetcher.asyncFetcherSplashIcon(Splashfetcher.splashIconUrl);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void doInitlizeJob(){
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				doJobOnBackgroundThread();
			}
		});
	}

	private void doJobOnBackgroundThread(){
		try{
			
			long start = System.currentTimeMillis();
			
			// load user chatInfo and chatData database
			RoomFacade.getInstance().loadDB();
			
			// 1.load CJ User , it means WifiUserData
			CJUserInfoContainer.getInstance().onSplashLoadDB();

			// 2.load User Friend
			FriendFacade.getInstance().loadDBFriend();
			
			long end = System.currentTimeMillis() - start;
			Logger.print(tag, LoggerUtils.getPerformanceString("doJobOnBackgroundThread use time:",end));
			
			// just to sleep some time to simulate something to do in background thread
			Thread.sleep(3600);
		}catch(Exception ex){
			Logger.print(tag, CrashHandler.getExceptionMessage(ex));
		}
		forwardActivity(getIntent());
	}

	private synchronized void forwardActivity(final Intent intent){
		if(hasInitlized){
			return ;
		}
		hasInitlized = true;
		
		if(AppConfig.getInstance().isAppFirstRun()){
			startActivity(new Intent(this,NewGuidActivity.class),
					R.anim.splash_anim_fade_in, R.anim.splash_anim_fade_out);
		} else {
			if(UserInfo.getInstance().getToken().length() == 0){
				startActivity(new Intent(this, LoginActivity.class),
						R.anim.splash_anim_fade_in, R.anim.splash_anim_fade_out);
			} else {
				IMClient.getInstance().login();
				startActivity(new Intent(this, MainUIActivity.class),
						R.anim.splash_anim_fade_in, R.anim.splash_anim_fade_out);
			}
		}
		finish();
	}
}
