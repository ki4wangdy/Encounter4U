package com.imrub.shoulder.module.model;

public class AddrlistYuanData {

	private String name;
	private String signature;
	
	private String uid;
	private String encounterTime;
	
	public AddrlistYuanData(String name, String signature, String uid, String encounterTime){
		this.name = name;
		this.signature = signature;
		this.encounterTime = encounterTime;
		this.uid = uid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getEncounterTime() {
		return encounterTime;
	}
	
	public void setEncounterTime(String encounterTime) {
		this.encounterTime = encounterTime;
	}
	
}
