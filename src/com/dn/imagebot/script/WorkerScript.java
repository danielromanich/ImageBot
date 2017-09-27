package com.dn.imagebot.script;

import java.util.ArrayList;
import java.util.Collections;

import com.dn.imagebot.main.Bot;

public abstract class WorkerScript extends Script {
	
	public Bot context;
	private ArrayList<Worker> workers = new ArrayList<Worker>();	
	
	private WorkerScript(Bot bot) {
		super(bot);
	}
	
	@Override
	public final int onLoop() {
		for (Worker worker : workers) {
			if (worker.validate()) {
				return worker.execute();
			}
		}
		return 100;
	}
	
	public abstract boolean onStart();
	public abstract void onFinish();
	
	
	public void provide(Worker ... workers) {
		Collections.addAll(this.workers, workers);
	}

}
