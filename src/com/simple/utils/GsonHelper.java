package com.simple.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
	  public static Gson getGson(){
	  Gson gson = new GsonBuilder()
	          .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
	          .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式    
	          .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.  
	          //.setPrettyPrinting() //对json结果格式化.  
	          .create();  
	  return gson;
	}
}// ~
