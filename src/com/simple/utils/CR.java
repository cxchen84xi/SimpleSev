package com.simple.utils;

import java.util.HashMap;

import com.prolib.entity.AppEntity;
import com.prolib.err.UserNotExistError;

public class CR {
	public CR(String objId,String language,String appId,String requesttype){
		this.setObjId(objId);
		this.setLanguage(language);
		this.setAppId(appId);
		this.setClientRequestType(requesttype);
	}
	
	private String objId;
	
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}

	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getClientRequestType() {
		return clientRequestType;
	}
	public void setClientRequestType(String clientRequestType) {
		this.clientRequestType = clientRequestType;
	}
	private String language;
	private String appId;
	private String clientRequestType;
	
	
	public boolean isMobile(){
		if(this.getClientRequestType()==null)return false;
		
		String type = this.getClientRequestType().toLowerCase();
		if("ios".equals(type)||"android".equals(type)){
			return true;
		}
		return false;
	}
	
	
}
