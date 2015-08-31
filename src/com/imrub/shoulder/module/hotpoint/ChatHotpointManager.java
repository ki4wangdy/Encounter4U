package com.imrub.shoulder.module.hotpoint;

import com.imrub.shoulder.base.db.facade.HotpointFacade;
import com.imrub.shoulder.base.db.table.HotPoint;
import com.imrub.shoulder.base.db.table.HotPoint.HotPointConstant;

public class ChatHotpointManager {

	/**
	 * 更新聊天个数
	 */
	public static void addChatMsgCount(){
		final HotPoint hotpoint = HotpointFacade.getInstance().getHotpoint();
		hotpoint.setField2(HotPointConstant.F2_Status_Chat);
		HotpointFacade.getInstance().updateMainHotpoint();
	}
	
	public static void updateChatMsgCount(int msgCount){
		final HotPoint hotpoint = HotpointFacade.getInstance().getHotpoint();
		if(msgCount > 0){
			addChatMsgCount();
			return ;
		}
		hotpoint.removeField2(HotPointConstant.F2_Status_Chat);
		HotpointFacade.getInstance().updateMainHotpoint();
	}
	
}
