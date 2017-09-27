package com.dn.imagebot.api.wrappers;

import java.awt.Point;

public class DTMNode {
	
	private Point point;
	private int color;
	public DTMNode(int color, Point point) {
		this.color = color;
		this.point = point;
	}
	
	public final int getColor() {
		return this.color;
	}
	
	public final Point getPoint() {
		return this.point;
	}

}
