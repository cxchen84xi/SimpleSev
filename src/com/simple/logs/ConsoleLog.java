package com.simple.logs;

public class ConsoleLog implements NLog{

	@Override
	public NLog debug(String msg) {
		System.out.println(msg);
		return this;
	}

	@Override
	public NLog info(String msg) {
		System.out.println(msg);
		return this;
	}

	@Override
	public NLog warm(String msg) {
		System.out.println(msg);
		return this;
	}

	@Override
	public NLog error(String msg) {
		System.out.println(msg);
		return this;
	}

}
