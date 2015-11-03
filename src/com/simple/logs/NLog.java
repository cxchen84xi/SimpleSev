package com.simple.logs;

public interface NLog {
	public NLog debug(String msg);
	public NLog info(String msg);
	public NLog warm(String msg);
	public NLog error(String msg);
	 
}// 
