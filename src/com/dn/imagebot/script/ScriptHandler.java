package com.dn.imagebot.script;

import com.dn.imagebot.main.Bot;

public class ScriptHandler {
	
	protected Bot bot;
	public ScriptHandler(Bot bot) {
		this.bot = bot;
	}
	
	public boolean isRunning = false;
	
	public void startScript(final Script script) {
		this.isRunning = true;
		if (!script.onStart()) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isRunning) {
					script.onLoop();
				}
				script.onFinish();
			}
			
		}).start();
	}
	
	public void stopCurrent() {
		isRunning = false;
	}

}
