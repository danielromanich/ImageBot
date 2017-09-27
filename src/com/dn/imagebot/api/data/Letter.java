package com.dn.imagebot.api.data;

import java.awt.Point;
import java.util.ArrayList;

public class Letter {
	
	private Point main;
	private ArrayList<Point> relatives;
	private ArrayList<Integer> relativeColors;
	private String letter;
	private int width, height;
	public Letter(String letter, Point main, int width, int height, ArrayList<Point> relatives, ArrayList<Integer> relativeColors) {
		this.letter = letter;
		this.main = main;
		this.relatives = relatives;
		this.relativeColors = relativeColors;
		this.width = width;
		this.height = height;
	}
	
	public String getLetter() {
		return this.letter;
	}
	
	public final Point getMain() {
		return this.main;
	}
	
	public final int getWidth() {
		return this.width;
	}
	
	public boolean compare(int x, int y, int width, int[] pixels) {
		for (int i = 0; i < relatives.size(); i++) {
			Point p = relatives.get(i);
			int color = relativeColors.get(i);
			int dX = x + p.x;
			int dY = y + p.y;
			if (dX + dY * width < 0 || dX + dY * width >= pixels.length || pixels[dX + dY * width] != color)
				return false;
		}
		return true;
	}

}
