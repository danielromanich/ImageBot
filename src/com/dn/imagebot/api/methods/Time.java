package com.dn.imagebot.api.methods;

import com.dn.imagebot.util.Condition;
import com.dn.imagebot.util.Random;

public class Time {
	
	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void sleep(int min, int max) {
		sleep(Random.nextInt(min, max));
	}

	public static boolean sleep(Condition con, int min, int max) {
		long start = System.currentTimeMillis();
		int rnd = Random.nextInt(min, max);
		while (System.currentTimeMillis() - start <= rnd) {
			if (!con.validate())
				return true;
			sleep(15);
		}
		return false;
	}
	
}
