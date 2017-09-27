package com.dn.imagebot.api.methods;

import java.awt.Point;
import com.dn.imagebot.main.Bot;

public class Camera {
	
	private Bot bot;
	public Camera(Bot bot) {
		this.bot = bot;
	}
	
	public final Direction getDirection() {
		Point p = bot.colors.findColor(-15921029);
		if (p != null) {
			if (Direction.NORTH.getPoint().x >= p.x && p.x <= Direction.NORTH.getPoint().x + 3 && p.y >= Direction.NORTH.getPoint().y && p.y <= Direction.NORTH.getPoint().y + 3) {
				return Direction.NORTH;
			} else if (p.y <= Direction.SOUTH.getPoint().y && p.x >= Direction.SOUTH.getPoint().x && p.x <= Direction.SOUTH.getPoint().x + 3) {
				return Direction.SOUTH;
			} else if (p.x >= Direction.EAST.getPoint().x && p.x <= Direction.EAST.getPoint().x + 3 && p.y <= Direction.EAST.getPoint().y + 3 && p.y >= Direction.EAST.getPoint().y - 3) {
				return Direction.EAST;
			} else {
				return Direction.WEST;
			}
		}
		return null;
	}
	
	public final boolean setDirection(Direction dir) {
		long timeOut = System.currentTimeMillis();
		//boolean left = Random.nextInt(0, 1) == 0;
		////.keyboard.typeKey(left ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT);
		while (!dir.equals(getDirection()) && System.currentTimeMillis() - timeOut <= 5000) {
			Time.sleep(10, 20);
		}
		//bot.keyboard.releaseKey(left ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT);
		return dir.equals(getDirection());
	}
	
	public enum Direction {
		NORTH(new Point(561, 23)),
		EAST(new Point(557, 21)),
		SOUTH(new Point(559, 19)),
		WEST(new Point(563, 17));
		
		private Point p;
		Direction(Point p) {
			this.p = p;
		}
		
		@Override
		public final String toString() {
			String name = super.name();
			return name.charAt(0) + name.substring(1).toLowerCase();
		}
		
		public final Point getPoint() {
			return this.p;
		}
	}

}
