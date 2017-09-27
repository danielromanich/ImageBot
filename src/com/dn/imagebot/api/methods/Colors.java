package com.dn.imagebot.api.methods;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import com.dn.imagebot.main.Bot;

public class Colors {
	
	private Bot bot;
	public Colors(Bot bot) {
		this.bot = bot;
	}
	
	public static final Point PLAYER_POINT = new Point(266, 174);
	
	public int getColorAt(int x, int y) {
		final BufferedImage image = bot.getImage();
		if (x >= 0 && y >= 0 && y <= image.getHeight() && x <= image.getWidth())
			return ((DataBufferInt)image.getRaster().getDataBuffer()).getData()[y * image.getWidth() + x];
		return -1;
	}
	
	public int[] getPixels(BufferedImage image) {
		return ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	}
	
	public int getColorAt(Point p) {
		return getColorAt(p.x, p.y);
	}
	
	public boolean isColorAt(int color, int x, int y, double tolerance) {
		return getSimilarity(color ,getColorAt(x, y)) <= tolerance;
	}
	
	public boolean isColorAt(int color, Point p, double tolerance) {
		return isColorAt(color, p.x, p.y, tolerance);
	}
	
	public int getColorAt(int x, int y, int width, int[] pixels) {
		return pixels[y * width + x];
	}
	/**
	 * 
	 * @param color1 the first color
	 * @param color2 the second color
	 * @return return the similarity in percentage between (0 and 100)
	 */
	public double getSimilarity(int color1, int color2) {
		return (Math.abs(getH(color1) - getH(color2)) * 100) / 255;
	}
	
	public double getSimilarity(int color1, int color2, int type) {
		if (type == 0) {
			return (Math.abs(((color1 >> 16) & 0x000000FF) - ((color2 >> 16) & 0x000000FF)) * 100) / 255;
		} else if (type == 1) {
			return (Math.abs(((color1 >> 8) & 0x000000FF) - ((color2 >> 8) & 0x000000FF)) * 100) / 255;
		} else if (type == 2) {
			return (Math.abs((color1 & 0x000000FF) - (color2 & 0x000000FF)) * 100) / 255;
		}
		return (Math.abs(getH(color1) - getH(color2)) * 100) / 255;
	}
	
	
	public double getLuminance(int rgb) {
		double r = (rgb >> 16) & 0x000000FF;
		double g = (rgb >> 8) & 0x000000FF;
		double b = rgb & 0x000000FF;
		return 0.2126*r + 0.7152*g + 0.0722*b;
	}
	
	public Color getColor(int rgb) {
		int r = (rgb >> 16) & 0x000000FF;
		int g = (rgb >> 8) & 0x000000FF;
		int b = rgb & 0x000000FF;
		return new Color(r, g, b);
	}
	
	public double getH(int rgb) {
		double r = (rgb >> 16) & 0x000000FF;
		double g = (rgb >> 8) & 0x000000FF;
		double b = rgb & 0x000000FF;
	    double h = -1;

	    double min, max, delta;

	    min = Math.min(Math.min(r, g), b);
	    max = Math.max(Math.max(r, g), b);

	     delta = max - min;

		// S
		if (max != 0) {
		} else {
			return h;
		}

	    // H
	     if( r == max )
	        h = ( g - b ) / delta; // between yellow & magenta
	     else if( g == max )
	        h = 2 + ( b - r ) / delta; // between cyan & yellow
	     else
	        h = 4 + ( r - g ) / delta; // between magenta & cyan

	     h *= 60;    // degrees

	    if( h < 0 )
	        h += 360;

	    return h;
		
	}
	
	public Point[] findColorObjectArray(double tolerance, int color, int min, int max) {
		ArrayList<Point> added = new ArrayList<>();
		final BufferedImage image = bot.getImage();
		final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (getSimilarity(getColorAt(x, y, image.getWidth(), pixels), color) <= tolerance) {
					int count = 0;
					for (int i = -max; i < max; i++) {
						if (x + i > 0 && x + i < image.getWidth() && y + i > 0 && y + i < image.getHeight()) {
							if (getSimilarity(getColorAt(x + i, y + i, image.getWidth(), pixels), color) <= tolerance)
								count++;
						}
					}
					if (count > min && count < max)
						added.add(new Point(x, y));
						//return new Point(x, y);
				}
			}
		}
		return added.toArray(new Point[added.size()]);
	}
	
	public Point[] findColorObjectArray(int color, int min, int max) {
		ArrayList<Point> added = new ArrayList<>();
		final BufferedImage image = bot.getImage();
		final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (getColorAt(x, y, image.getWidth(), pixels) ==  color) {
					int count = 0;
					for (int i = -max; i < max; i++) {
						if (x + i > 0 && x + i < image.getWidth() && y + i > 0 && y + i < image.getHeight()) {
							if (getColorAt(x + i, y + i, image.getWidth(), pixels) == color)
								count++;
						}
					}
					if (count > min && count < max)
						added.add(new Point(x, y));
						//return new Point(x, y);
				}
			}
		}
		return added.toArray(new Point[added.size()]);
	}
	
	public Point[] findColorObjectArray(double tolerance, int color, double ignoreTolerance, int ignoreColor, int min, int max) {
		ArrayList<Point> added = new ArrayList<>();
		final BufferedImage image = bot.getImage();
		final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (getSimilarity(getColorAt(x, y, image.getWidth(), pixels), color) <= tolerance) {
					int count = 0;
					loop: for (int i = -max; i < max; i++) {
						if (x + i > 0 && x + i < image.getWidth() && y + i > 0 && y + i < image.getHeight()) {
							if (getSimilarity(getColorAt(x + i, y + i, image.getWidth(), pixels), ignoreColor) <= ignoreTolerance) {
								count = 0;
								break loop;
							}
							if (getSimilarity(getColorAt(x + i, y + i, image.getWidth(), pixels), color) <= tolerance)
								count++;
						}
					}
					if (count > min && count < max)
						added.add(new Point(x, y));
						//return new Point(x, y);
				}
			}
		}
		return added.toArray(new Point[added.size()]);
	}
	
	public Point[] findColorObjectArray(double tolerance, int color, double ignoreTolerance, int colorSimilarity, int ignoreColor, int min, int max) {
		ArrayList<Point> added = new ArrayList<>();
		final BufferedImage image = bot.getImage();
		final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (getSimilarity(getColorAt(x, y, image.getWidth(), pixels), color) <= tolerance) {
					int count = 0;
					loop: for (int i = -max; i < max; i++) {
						if (x + i > 0 && x + i < image.getWidth() && y + i > 0 && y + i < image.getHeight()) {
							if (getSimilarity(getColorAt(x + i, y + i, image.getWidth(), pixels), ignoreColor, colorSimilarity) <= ignoreTolerance) {
								count = 0;
								break loop;
							}
							if (getSimilarity(getColorAt(x + i, y + i, image.getWidth(), pixels), color) <= tolerance)
								count++;
						}
					}
					if (count > min && count < max) {
						added.add(new Point(x, y));
					}
						//return new Point(x, y);
				}
			}
		}
		return added.toArray(new Point[added.size()]);
	}
	
	public Point findColorObject(double tolerance, int color, int min, int max) {
		final BufferedImage image = bot.getImage();
		final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (getSimilarity(getColorAt(x, y, image.getWidth(), pixels), color) <= tolerance) {
					int count = 0;
					for (int i = -max; i < max; i++) {
						if (x + i > 0 && x + i < image.getWidth() && y + i > 0 && y + i < image.getHeight()) {
							if (getSimilarity(getColorAt(x + i, y + i, image.getWidth(), pixels), color) <= tolerance)
								count++;
						}
					}
					if (count > min && count < max)
						return new Point(x, y);
				}
			}
		}
		return null;
	}
	
	public Point findColor(double tolerance, int ... colors) {
		final BufferedImage image = bot.getImage();
		final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				for (int color : colors)
					if (getSimilarity(getColorAt(x, y, image.getWidth(), pixels), color) <= tolerance) {
						return new Point(x, y);
				}
			}
		}
		return null;
	}
	
	public Point getClosest(List<Point> points) {
		Point current = null;
		int dist = Integer.MAX_VALUE;
		for (Point p : points) {
			int distance = getDistance(p, PLAYER_POINT);
			if (distance < dist) {
				current = p;
				dist = distance;
			}
		}
		return current;
	}
	
	public Point getClosest(Point[] points) {
		Point current = null;
		int dist = Integer.MAX_VALUE;
		for (Point p : points) {
			int distance = getDistance(p, PLAYER_POINT);
			if (distance < dist) {
				current = p;
				dist = distance;
			}
		}
		return current;
	}
	
	private final int getDistance(Point p, Point p1) {
		return (int) Math.sqrt((p.x - p1.x) * (p.x - p1.x) + (p.y - p1.y) * (p.y - p1.y));
	}	

}
