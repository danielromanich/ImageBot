package com.dn.imagebot.api.methods;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.dn.imagebot.api.wrappers.DTM;
import com.dn.imagebot.main.Bot;
import com.dn.imagebot.util.Condition;

public class Walking {
	
	public static final Rectangle MINIMAP_RECT = new Rectangle(570, 11, 144, 150);
	public static final Point PLAYER_DOT = new Point(73, 73);
	
	private String MAP_DIR = "./data/maps/";
	
	private BufferedImage currentMap;
	//private DTM[] mapDTMs;
	private int threshHold = 25;
	
	private Bot bot;
	public Walking(Bot bot) {
		this.bot = bot;
	}
	
	public boolean walkTo(String name) {
		Point p = bot.dtmHandler.getPointForDtm(name, 0.0001d, bot.getImage(MINIMAP_RECT));
		if (p != null) { //&& !isMoving()) {
			p = new Point(MINIMAP_RECT.x + p.x, MINIMAP_RECT.y + p.y);
			if (bot.mouse.click(p, true)) {
				return Time.sleep(new Condition() {

					@Override
					public boolean validate() {
						return false;//!isMoving();
					}
					
				}, 1500, 2000);
			}
		}
		return false;
	}
	
	public boolean isMoving() {
		Point p = bot.dtmHandler.getPointForDtm("minimap_flag", 0.0001d, bot.getImage(MINIMAP_RECT));
		return p != null && getDistance(p) > threshHold;
	}
	
	public int getDistance(String name) {
		Point p = bot.dtmHandler.getPointForDtm(name, 0.00001d, bot.getImage(MINIMAP_RECT));
		if (p != null) {
			return (int) Math.sqrt((PLAYER_DOT.x - p.x) * (PLAYER_DOT.x - p.x) + (PLAYER_DOT.y - p.y) * (PLAYER_DOT.y - p.y));
		}
		return -1;
	}
	
	public int getDistance(Point p) {
		if (p != null) {
			return (int) Math.sqrt((PLAYER_DOT.x - p.x) * (PLAYER_DOT.x - p.x) + (PLAYER_DOT.y - p.y) * (PLAYER_DOT.y - p.y));
		}
		return -1;
	}
	
	public void setPath(String mapName, Point[] points, int treshHold) {
		File dir = new File(MAP_DIR);
		for (File file : dir.listFiles()) {
			if (file != null) {
				if (file.getName().toLowerCase().startsWith(mapName.toLowerCase())) {
				    BufferedImage img = null;
					try {
						img = ImageIO.read( file );
					} catch (IOException e) {
						e.printStackTrace();
					}
				    BufferedImage convertedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
				    convertedImg.getGraphics().drawImage(img, 0, 0, null);
				    currentMap = convertedImg;
				    ArrayList<DTM> dtms = new ArrayList<DTM>();
				    for (Point p : points) {
				    	if (p.x > 4 && p.y > 4 && p.x + 4 < currentMap.getWidth() && p.y + 4 < currentMap.getHeight()) {
				    		dtms.add(bot.dtmGenerator.createDTM(currentMap, p));
				    	}
				    }
					break;
				}
			}
		}
	}

}
