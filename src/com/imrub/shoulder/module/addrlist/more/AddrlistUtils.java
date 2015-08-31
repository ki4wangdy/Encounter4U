package com.imrub.shoulder.module.addrlist.more;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imrub.shoulder.base.db.table.addr.AddrlistNewData;

public class AddrlistUtils {

	private AddrlistUtils(){
	}
	
	
	public static AddrlistNewData parseToObject(String jid, String msg){
		
		JSONObject body = (JSONObject)JSON.parse(msg);
		String name = body.getString("n");
		String iconUrl = body.getString("c");
		String signature = body.getString("s");
		String time = body.getString("t");
		return new AddrlistNewData(jid, name, iconUrl, signature, AddrlistNewConstant.StatusType.Status_ToAgree, time, 1, 1);
	}
	
}
