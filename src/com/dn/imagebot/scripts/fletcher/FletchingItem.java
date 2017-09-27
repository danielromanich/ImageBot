package com.dn.imagebot.scripts.fletcher;

import java.awt.Rectangle;

public enum FletchingItem {
	
	SHORTBOW("Logs", new Rectangle(173, 392, 57, 28), 5d),
	LONGBOW("Logs", new Rectangle(295, 392, 57, 28), 10d),
	OAK_SHORTBOW("Oak logs", new Rectangle(84, 385, 58, 39), 16.5d),
	OAK_LONGBOW("Oak logs", new Rectangle(219, 388, 99, 20), 25d),
	WILLOW_SHORTBOW("Willow logs", new Rectangle(84, 385, 58, 39), 33.3d),
	WILLOW_LONGBOW("Willow logs", new Rectangle(219, 388, 99, 20), 41.5d),
	MAPLE_SHORTBOW("Maple logs", new Rectangle(84, 385, 58, 39), 50d),
	MAPLE_LONGBOW("Maple logs", new Rectangle(219, 388, 99, 20), 58.3d),
	YEW_SHORTBOW("Yew logs", new Rectangle(84, 385, 58, 39), 67.5d),
	YEW_LONGBOW("Yew logs", new Rectangle(219, 388, 99, 20), 75d),
	MAGIC_SHORTBOW("Magic logs", new Rectangle(84, 385, 58, 39), 83.3d),
	MAGIC_LONGBOW("Magic logs", new Rectangle(219, 388, 99, 20), 91.5d);
	
	private String logName;
	private Rectangle rect;
	private double exp;
	FletchingItem(String logName, Rectangle rect, double exp) {
		this.logName = logName;
		this.rect = rect;
		this.exp = exp;
	}
	
	@Override
	public final String toString() {
		final String name = super.name();
		return name.charAt(0) + name.substring(1).toLowerCase().replace("_", " ");
	}
	
	public final String getLogs() {
		return this.logName;
	}
	
	public final Rectangle getRectangle() {
		return this.rect;
	}
	
	public final double getExp() {
		return this.exp;
	}

}
