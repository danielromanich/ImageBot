package com.dn.imagebot.util.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import com.dn.imagebot.main.Bot;

public class ImageComparator {

	private Bot bot;

	public ImageComparator(Bot bot) {
		this.bot = bot;
	}

	/**
	 * Finds the a region in one image that best matches another, smaller,
	 * image.
	 */
	public Point findImage(BufferedImage target){
			final BufferedImage screen = bot.getImage();
		   int w1 = screen.getWidth(); int h1 = target.getHeight();
		   int w2 = target.getWidth(); int h2 = target.getHeight();
		   assert(w2 <= w1 && h2 <= h1);
		   // will keep track of best position found
		   int bestX = 0; int bestY = 0; double lowestDiff = Double.POSITIVE_INFINITY;
		   // brute-force search through whole image (slow...)
		   for(int x = 0;x < w1-w2;x++){
		     for(int y = 0;y < h1-h2;y++){
		       double comp = compareImages(screen.getSubimage(x,y,w2,h2),target);
		       if(comp < lowestDiff){
		         bestX = x; bestY = y; lowestDiff = comp;
		       }
		     }
		   }
		   // output similarity measure from 0 to 1, with 0 being identical
		   // return best location
		   return new Point(bestX, bestY);
	 }

	/**
	 * Determines how different two identically sized regions are.
	 */
	public double compareImages(BufferedImage im1, BufferedImage im2) {
		assert (im1.getHeight() == im2.getHeight() && im1.getWidth() == im2
				.getWidth());
		double variation = 0.0;
		for (int x = 0; x < im1.getWidth(); x++) {
			for (int y = 0; y < im1.getHeight(); y++) {
				variation += compareARGB(im1.getRGB(x, y), im2.getRGB(x, y))
						/ Math.sqrt(3);
			}
		}
		return variation / (im1.getWidth() * im1.getHeight());
	}
	
	

	/**
	 * Calculates the difference between two ARGB colours
	 * (BufferedImage.TYPE_INT_ARGB).
	 */
	public double compareARGB(int rgb1, int rgb2) {
		double r1 = ((rgb1 >> 16) & 0xFF) / 255.0;
		double r2 = ((rgb2 >> 16) & 0xFF) / 255.0;
		double g1 = ((rgb1 >> 8) & 0xFF) / 255.0;
		double g2 = ((rgb2 >> 8) & 0xFF) / 255.0;
		double b1 = (rgb1 & 0xFF) / 255.0;
		double b2 = (rgb2 & 0xFF) / 255.0;
		double a1 = ((rgb1 >> 24) & 0xFF) / 255.0;
		double a2 = ((rgb2 >> 24) & 0xFF) / 255.0;
		// if there is transparency, the alpha values will make difference
		// smaller
		return a1
				* a2
				* Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2)
						+ (b1 - b2) * (b1 - b2));
	}
	
	public Point getPoint(String name) {
		final BufferedImage source = bot.getImage();
		final BufferedImage image = bot.objectImages.getImage(name);
		ArrayList<Integer> loadedPixels = loadPixels(image);
		final int[] pixels = ((DataBufferInt)source.getRaster().getDataBuffer()).getData();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (loadedPixels.contains(bot.colors.getColorAt(x, y, image.getWidth(), pixels))) {
					int count = 0;
					for (int nX = x; nX < x + image.getWidth(); nX++) {
						for (int nY = y; nY < y + image.getHeight(); nY++) {
							if (nX > 0 && nX < image.getWidth() && nY > 0 && nY < image.getHeight() && loadedPixels.contains(bot.colors.getColorAt(nX, nY, image.getWidth(), pixels))) {
								count++;
							}
						}
					}
					if (count >= (image.getWidth() * image.getHeight()) * 0.75 && count <= (image.getWidth() * image.getHeight())) {
						return new Point(x, y);
					}
				}
			}
		}
		return null;
	}
	
	private ArrayList<Integer> loadPixels(BufferedImage image) {
		ArrayList<Integer> loaded = new ArrayList<Integer>();
		final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for (int p : pixels) {
			if (!loaded.contains(p))
				loaded.add(p);
		}
		return loaded;
	}

}
