package com.dn.imagebot.api.wrappers;

import java.awt.Point;
import java.awt.image.BufferedImage;
import com.dn.imagebot.main.Bot;

public class DTM {
	
	private DTMNode[] nodes;
	public DTMNode center;
	private Bot bot;
	public DTM(Bot bot, DTMNode center, DTMNode ... nodes) {
		this.nodes = nodes;
		this.bot = bot;
		this.center = center;
	}
	
	public final DTMNode[] getNodes() {
		return this.nodes;
	}
	
	public final boolean isColorValid(int color, double tolerance) {
		return bot.colors.getSimilarity(center.getColor(), color) <= tolerance;
	}
	
	/**
	 * 
	 * @param image The image to search for the DTM 
	 * @param point The point
	 * @param failRate Represents the percentage it tolerates to fail between 0 - 100 (Respective 0% to 100%)
	 * @return 
	 */
	public final boolean validate(BufferedImage image, Point point, double tolerance) {
		int totalValid = 0;
		int[] pixels = bot.colors.getPixels(image);
		for (DTMNode node : nodes) {
			final Point pos = new Point(point.x + node.getPoint().x, point.y + node.getPoint().y);
			if (pos.x > 0 && pos.y > 0 && pos.x < image.getWidth() && pos.y < image.getHeight()) {
				if (bot.colors.getSimilarity(bot.colors.getColorAt(pos.x, pos.y, image.getWidth(), pixels), node.getColor()) <= tolerance) {
					totalValid++;
				}
			}
		}
		return nodes.length == totalValid;
	}

}
