package com.dn.imagebot.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Bezier {
	
	public Point2D[] getMousePath(Point start, Point end) {
		int riseValue = (end.y - start.y) / (end.x - start.x);
		ArrayList<Point> points = new ArrayList<Point>();
		Point currentPoint = start;
		while (currentPoint.getX() != end.getX() || currentPoint.getY() != end.getY()) {
			//points.add(e)
		}
		return null;
		
	}

}
