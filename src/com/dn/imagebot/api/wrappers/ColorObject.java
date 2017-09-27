package com.dn.imagebot.api.wrappers;

public class ColorObject {
	
	private int[] colors;
	public ColorObject(int ... colors) {
		this.colors = colors;
	}
	
	public final int[] getColors() {
		return this.colors;
	}

}
