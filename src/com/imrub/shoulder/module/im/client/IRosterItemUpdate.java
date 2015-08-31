package com.imrub.shoulder.module.im.client;

public interface IRosterItemUpdate {
//	#define ROSTER_ITEM_UPDATE_ADDED	1
//	#define ROSTER_ITEM_UPDATE_REMOVE	2
//	#define ROSTER_ITEM_UPDATE_SUBSCRI	3
//	#define ROSTER_ITEM_UPDATE_UNSUB	4
	public static final int RosterSub = 3;
	public static final int RosterAdd = 1;
	public void onRosterItemUpdate(String jid, int type);
}
