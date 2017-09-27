package com.dn.imagebot.api.wrappers;

import java.awt.Rectangle;

public class MenuOption {
	
	private String action;
	private Rectangle rect;
	public MenuOption(String action, Rectangle rect) {
		this.action = action;
		this.rect = rect;
	}
	
	public String getAction() {
		return this.action;
	}
	
	public Rectangle getRectangle() {
		return this.rect;
	}

}
