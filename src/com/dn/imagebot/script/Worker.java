package com.dn.imagebot.script;

import com.dn.imagebot.main.Bot;

public abstract class Worker {
	
	public Bot context;
	private Worker(Bot bot) {
		this.context = bot;
	}

	public abstract boolean validate();
	public abstract int execute();
	
}
