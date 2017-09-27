package com.dn.imagebot.script;

import com.dn.imagebot.main.Bot;
import com.dn.imagebot.util.Random;

public abstract class Script {
	
	protected Bot context;
	public Script(Bot bot) {
		this.context = bot;
	}
	
	public abstract boolean onStart();
	public abstract int onLoop();
	public abstract void onFinish();
	
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void sleep(int min, int max) {
		sleep(Random.nextInt(min, max));
	}
	
	public final void stop() {
		context.ui.startButton.doClick();
	}
	
	@Override
	public final String toString() {
		return this.getClass().getName();
	}

}
