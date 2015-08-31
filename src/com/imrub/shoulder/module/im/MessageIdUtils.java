package com.imrub.shoulder.module.im;

import java.util.Random;

public class MessageIdUtils {

	private static long id = 0;
	
	private static String prefix = randomString(5) + "-";
	private static Random randGen;
	
    private static char[] numbersAndLetters;
	
	public static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		
		if(numbersAndLetters == null){
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
		            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		
		if(randGen == null){
			randGen = new Random();
		}
		
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	public static String getMessageId() {
		return nextID();
	}
	
	private static synchronized String nextID() {
		return prefix + Long.toString(id++);
	}
	
	public static String createRoomId(String jid){
		if(jid.contains("room")){
			return jid;
		} else {
			return "room"+jid;
		}
	}
	
	public static String getToJid(String roomId){
		if(roomId.contains("room")){
			int startLen = "room".length();
			return roomId.substring(startLen);
		} else {
			return roomId;
		}
	}
	
}
