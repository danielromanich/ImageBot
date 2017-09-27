package com.dn.imagebot.util.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.dn.imagebot.main.Bot;

public class ObjectImages {
	
	protected Bot bot;
	public ObjectImages(Bot bot) {
		this.bot = bot;
		this.loadImages();
	}
	
	public static final String IMAGE_DIR = "./data/img/";
	public final HashMap<String, BufferedImage> images = new HashMap<>();
	
	public BufferedImage getImage(String name) {
		return images.get(name.toLowerCase());
	}
	
	private void loadImages() {
		File dir = new File(IMAGE_DIR);
		for (File file : dir.listFiles()) {
			if (file != null) {
				if (file.getName().endsWith(".png")) {
					String name = file.getName().replace(".png", "").toLowerCase();//[0];
				    BufferedImage img = null;
					try {
						img = ImageIO.read( file );
					} catch (IOException e) {
						e.printStackTrace();
					}
				    BufferedImage convertedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
				    convertedImg.getGraphics().drawImage(img, 0, 0, null);
				    images.put(name, convertedImg);
				}
			}
		}
	}

}
