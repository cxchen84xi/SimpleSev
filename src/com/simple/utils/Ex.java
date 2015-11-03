package com.simple.utils;

import com.prolib.G;
import com.prolib.entity.ResultMsg;


public class Ex {
	public static String execute(Result t){
		try{
			return t.returnResult();
		}catch(Exception ex){
			ResultMsg msg = new ResultMsg();
	        msg.setErrorMsg(ex.toString());
	        msg.setSuccess(false);
			return GsonHelper.getGson().toJson(msg);
		}
	}
}
