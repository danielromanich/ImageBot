package com.dn.imagebot.api.methods.ocr;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.dn.imagebot.api.data.Letter;
import com.dn.imagebot.main.Bot;
import com.dn.imagebot.util.image.Letters;

public class OCR {
	
	public static final int[] colors = {-2655689, -256, -16711681, -2063815, -1998023};
	public static final int WHITE_COLOR = 16777215;
	
	private Letters letters;
	
	public Image current;
	
	private Bot bot;
	public OCR(Bot bot) {
		this.bot = bot;
		this.letters = new Letters(bot);
	}
	
	public Letter[] getLetters() {
		return this.letters.getLetters().toArray(new Letter[this.letters.getLetters().size()]);
	}
	
	public String getText(BufferedImage image, boolean menu) {
		current = image;
		int[] pixels = filter(image, menu);
		String text = "";
		int lastX = 0;
		Letter lastLetter = null;
		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++) {
				if (x + y * image.getWidth() < pixels.length) {
					int color = pixels[x + y * image.getWidth()];
					if (color == WHITE_COLOR) {
						for (Letter l : letters.getLetters())
							if (l.compare(x, y, image.getWidth(), pixels)) {
								if (lastLetter != null && x - (lastX + lastLetter.getWidth()) > 4) {
									text += " ";
								}
								lastLetter = l;
								lastX = x;
								text += l.getLetter();
								y = 0;
								x += l.getWidth();
							}
					}
				}
			}
		return text;
	}
	
	private int[] filter(BufferedImage image, boolean menu) {
		int[] pixels = bot.colors.getPixels(image);
		for (int i = 0; i < pixels.length; i++) {
			double l = bot.colors.getLuminance(pixels[i]);
			int r = ((pixels[i] >> 16) & 0x000000FF);
			int g = ((pixels[i] >> 8) & 0x000000FF);
			int b = (pixels[i] & 0x000000FF);
			if (menu) {
				if (l > 140)
					pixels[i] = WHITE_COLOR;
				else
					pixels[i] = 0;
			} else {
				if (l > 195 && l < 250 && getDifference(r, g, b) <= 0.8 || l > 150 && l < 205 && b > 200 || l > 80 && l < 190 && getDifference(r, g, b) <= 0.04 && getDifference(r, g, b) >= 0.025 && r > 160)
					pixels[i] = WHITE_COLOR;
				else
					pixels[i] = 0;
			}
		}
		return pixels;
	}
	
	private int getDifference(int r, int g, int b) {
		return (r / (g != 0 ? g : 1))/(b != 0 ? b : 1);
	}

}
