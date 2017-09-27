package com.dn.imagebot.util.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import com.dn.imagebot.api.data.Letter;
import com.dn.imagebot.api.methods.ocr.OCR;
import com.dn.imagebot.main.Bot;

public class Letters {
	
	protected Bot bot;
	public Letters(Bot bot) {
		this.bot = bot;
		this.loadLetters();
	}
	
	public static final String IMAGE_DIR = "./data/uptext/";
	private final ArrayList<Letter> letters = new ArrayList<>();
	
	public ArrayList<Letter> getLetters() {
		return this.letters;
	}
	
	private void loadLetters() {
		File dir = new File(IMAGE_DIR);
		for (File file : dir.listFiles()) {
			if (file != null) {
				if (file.getName().endsWith(".png")) {
					String name = file.getName().replace(".png", "");//[0];
					if (name.contains("upper"))
						name = name.replace("upper_", "").toUpperCase();
					else if (name.equals("front_slash"))
						name = "/";
					else if (name.equals("right_parenth")) {
						name = ")";
					} else if (name.equals("left_parenth")) {
						name = "(";
					}
					else if (name.equals("dash"))
						name = "-";
				    BufferedImage img = null;
					try {
						img = ImageIO.read( file );
					} catch (IOException e) {
						e.printStackTrace();
					}
				    BufferedImage convertedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
				    convertedImg.getGraphics().drawImage(img, 0, 0, null);
				    letters.add(getLetterForImage(name, convertedImg));
				}
			}
		}
	}
	
	private Letter getLetterForImage(String name, BufferedImage img) {
		int[] pixels = bot.colors.getPixels(img);
		boolean first = false;
		Point start = null;
		final ArrayList<Point> relatives = new ArrayList<>();
		final ArrayList<Integer> colors = new ArrayList<>();
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int color = pixels[x + y * img.getWidth()];
				if (color != 0) {
					if (!first) {
						start = new Point(x, y);
						first = true;
					} else {
						relatives.add(new Point(x - start.x, y - start.y));
						colors.add(OCR.WHITE_COLOR);
					}
				} else if (first) {
					relatives.add(new Point(x - start.x, y - start.y));
					colors.add(0);
				}
			}
		}
		int y = -1;
		for (int i = 0; i < 2; i++) {
			for (int x = -1; x < img.getWidth() + 1; x++) {
				relatives.add(new Point(x - start.x, y - start.y));
				colors.add(0);
			}
			y = img.getHeight();
		}
		int x = -1;
		for (int i = 0; i < 2; i++) {
			for (y = -1; y < img.getHeight() + 1; y++) {
				relatives.add(new Point(x - start.x, y - start.y));
				colors.add(0);
			}
			x = img.getWidth();
		}
		return new Letter(name, start, img.getWidth(), img.getHeight(), relatives, colors);
	}

}
