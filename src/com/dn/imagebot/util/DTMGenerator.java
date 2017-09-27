package com.dn.imagebot.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import com.dn.imagebot.api.wrappers.DTM;
import com.dn.imagebot.api.wrappers.DTMNode;
import com.dn.imagebot.main.Bot;

public class DTMGenerator {
	
	public HashMap<String, DTM> dtms = new HashMap<String, DTM>();
	
	private Bot bot;
	public DTMGenerator(Bot bot) {
		this.bot = bot;
	}
	
	public static final int IGNORE_COLOR = new Color(255, 10, 234).getRGB();
	
	public static final Point[] DTM_NODE_LOCATIONS = {new Point(-4, 0), new Point(4, 0), new Point(0, -4), new Point(0, 4),
		new Point(-4, -4), new Point(-4, 4), new Point(4, -4), new Point(4, 4)};
	
	public void addDtm(String name, BufferedImage image) {
		final DTM dtm = createDTM(image);
		if (dtm != null) {
			dtms.put(name, dtm);
		}
	}
	
	public void addDTMS(HashMap<String, BufferedImage> images) {
		for (String name : images.keySet()) {
			if (!addSpecial(name, images.get(name)))
				addDtm(name, images.get(name));
		}
	}
	
	private DTM createDTM(BufferedImage image) {
		final Point center = new Point(image.getWidth() / 2, image.getHeight() / 2);
		ArrayList<DTMNode> nodes = new ArrayList<DTMNode>();
		for (Point dtmPoint : DTM_NODE_LOCATIONS) {
			final Point point = dtmPoint;
			int color = image.getRGB(center.x + point.x, center.y + point.y);
			if (color != IGNORE_COLOR)
				nodes.add(new DTMNode(image.getRGB(center.x + point.x, center.y + point.y), point));
		}
		return new DTM(bot, new DTMNode(image.getRGB(center.x, center.y), center), nodes.toArray(new DTMNode[nodes.size()]));
	}
	
	public DTM createDTM(BufferedImage image, Point p) {
		final Point center = p;
		ArrayList<DTMNode> nodes = new ArrayList<DTMNode>();
		for (Point dtmPoint : DTM_NODE_LOCATIONS) {
			final Point point = dtmPoint;
			int color = image.getRGB(center.x + point.x, center.y + point.y);
			if (color != IGNORE_COLOR)
				nodes.add(new DTMNode(image.getRGB(center.x + point.x, center.y + point.y), point));
		}
		return new DTM(bot, new DTMNode(image.getRGB(center.x, center.y), center), nodes.toArray(new DTMNode[nodes.size()]));
	}
	
	private boolean addSpecial(String name, BufferedImage image) {
		switch (name) {
		case "minimap_flag":
			dtms.put(name, new DTM(bot, new DTMNode(image.getRGB(3, 4), new Point(3, 4)), new DTMNode[]{
				new DTMNode(image.getRGB(6, 0), new Point(3, -4)), new DTMNode(image.getRGB(6, 13), new Point(3, 9)),
				new DTMNode(image.getRGB(0, 5), new Point(-3, 1))
			}));
			return true;
		}
		return false;
	}

}
