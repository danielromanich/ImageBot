package com.dn.imagebot.api.methods;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.dn.imagebot.api.wrappers.DTM;
import com.dn.imagebot.main.Bot;

public class DTMHandler {
	
	private Bot bot;
	public DTMHandler(Bot bot) {
		this.bot = bot;
	}
	
	public Point getPointForDtm(String name) {
		DTM dtm = bot.dtmGenerator.dtms.get(name.toLowerCase());
		return getPointForDtm(dtm, 0.5, bot.clientRect);
	}
	
	public Point getPointForDtm(String name, double tolerance) {
		DTM dtm = bot.dtmGenerator.dtms.get(name.toLowerCase());
		return getPointForDtm(dtm, tolerance, bot.clientRect);
	}
	
	public Point getPointForDtm(String name, double tolerance, BufferedImage image) {
		DTM dtm = bot.dtmGenerator.dtms.get(name.toLowerCase());
		return getPointForDtm(dtm, tolerance, image);
	}
	
	
	public Point getPointForDtm(String name, double tolerance, Rectangle rect) {
		DTM dtm = bot.dtmGenerator.dtms.get(name.toLowerCase());
		return getPointForDtm(dtm, tolerance, rect);
	}
	
	private Point getPointForDtm(DTM dtm, double tolerance, Rectangle rect) {
		return getPointForDtm(dtm, tolerance, bot.getImage(rect));
	}
	
	private Point getPointForDtm(DTM dtm, double tolerance, BufferedImage image) {
		final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (dtm.isColorValid(bot.colors.getColorAt(x, y, image.getWidth(), pixels), tolerance)) {
					if (dtm.validate(image, new Point(x, y), tolerance)) {
						return new Point(x, y);
					}
				}

			}
		}
		return null;
	}
	
}
