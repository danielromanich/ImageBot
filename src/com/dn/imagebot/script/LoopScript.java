package com.dn.imagebot.script;

import com.dn.imagebot.main.Bot;

public abstract class LoopScript extends Script {

	public LoopScript(Bot bot) {
		super(bot);
	}

	public abstract boolean onStart();
	public abstract int onLoop();
	public abstract void onFinish();

}
