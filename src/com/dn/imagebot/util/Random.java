package com.dn.imagebot.util;

public class Random {
	
	public static final int nextInt(int min, int max) {
		return min + (int)(Math.random()* (max - min)); 
	}

}
