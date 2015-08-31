package com.imrub.shoulder.module.im;

import com.imrub.shoulder.module.addrlist.newfriend.IMAgreeFriendMsgManager;
import com.imrub.shoulder.module.addrlist.newfriend.IMReplyMsgManager;
import com.imrub.shoulder.module.addrlist.newfriend.UserLogoMsgManager;
import com.imrub.shoulder.module.im.client.IMClient;
import com.imrub.shoulder.module.im.client.IMDataManager;

public class IMModule {

	public static void initialize(){
		IMClient.getInstance().registerServerConnection();
		IMClient.getInstance().registerMessageBroadcast();
		
		IMClient.getInstance().registerMsgControllerListener(IMDataManager.getInstance());
		IMClient.getInstance().registerMsgControllerListener(IMReplyMsgManager.getInstance());
		IMClient.getInstance().registerMsgControllerListener(IMAgreeFriendMsgManager.getInstance());
		IMClient.getInstance().registerMsgControllerListener(UserLogoMsgManager.getInstance());
		IMClient.getInstance().startServer();
		
	}
	
}
