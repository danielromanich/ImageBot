package com.dn.imagebot.api.methods;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.dn.imagebot.api.wrappers.MenuOption;
import com.dn.imagebot.main.Bot;
import com.dn.imagebot.util.Random;

public class Menu {
	
	private Bot bot;
	public Menu(Bot bot) {
		this.bot = bot;
	}
	
	private static final int MENU_OUTLINE_COLOR = -10660793;
	private static final int FIRST_OPTION_X = 7, FIRST_OPTION_Y = 7;
	private static final int TOP_HEIGHT = 19, OPTION_HEIGHT = 15;
	
	public String getUpText() {
		String action = this.bot.ocr.getText(bot.getImage(new Rectangle(FIRST_OPTION_X, FIRST_OPTION_Y, 250, 20)), false);
		if (action.contains("/"))
			action = action.split(" /")[0];
		return action;
	}
	
	public boolean isOpen() {
		Point p = bot.mouse.getLocalPoint();
		BufferedImage image = bot.getImage();
		int[] pixels = bot.colors.getPixels(image);
		for (int x = -10; x < 10; x++) {
			for (int y = -10; y < 10; y++) {
				int dX = p.x + x;
				int dY = p.y + y;
				if (dX + dY * image.getWidth() < pixels.length && dX + dY * image.getWidth() >= 0) {
					if (pixels[dX + dY * image.getWidth()] == MENU_OUTLINE_COLOR) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public MenuOption[] getMenuOptions() {
		Rectangle[] menus = getMenuOptionRectangles();
		if (menus != null) {
			MenuOption[] m = new MenuOption[menus.length];
			for (int i = 0; i < menus.length; i++) {
				String action = bot.ocr.getText(bot.getImage(menus[i]), true);
				m[i] = new MenuOption(action, menus[i]);
			}
			return m;
		}
		return new MenuOption[]{new MenuOption(bot.menu.getUpText(), null)};
	}
	
	public Rectangle[] getMenuOptionRectangles() {
		Rectangle menu = getMenu();
		if (menu != null) {
			int size = (menu.height - TOP_HEIGHT)/OPTION_HEIGHT;
			if (size >= 0) {
				Rectangle[] rectangles = new Rectangle[size];
				for (int i = 0; i < size; i++) {
					rectangles[i] = new Rectangle(menu.x + 2, menu.y + TOP_HEIGHT + OPTION_HEIGHT * i, menu.width - 4, OPTION_HEIGHT + 3);
				}
				return rectangles;
			}
		}
		return null;
	}
	
	public Rectangle getMenu() {
		Point p = bot.mouse.getLocalPoint();
		boolean contains = false;
		Point start = null;
		BufferedImage image = bot.getImage();
		int[] pixels = bot.colors.getPixels(image);
		loop: for (int x = -10; x < 10; x++) {
			for (int y = -10; y < 10; y++) {
				int dX = p.x + x;
				int dY = p.y + y;
				if (dX + dY * image.getWidth() < pixels.length && dX + dY * image.getWidth() >= 0) {
					if (pixels[dX + dY * image.getWidth()] == MENU_OUTLINE_COLOR) {
						contains = true;
						start = new Point(dX, dY);
						break loop;
					}
				}
			}
		}
		if (contains) {
			boolean cornerFound = false;
			int x = start.x;
			int bestX = 0, lastX = 0;
			while (!cornerFound) {
				lastX = bestX;
				for (int i = 0; i < 15; i++) {
					if (x - i >= 0 && pixels[(x - i) + start.y * image.getWidth()] == MENU_OUTLINE_COLOR)
						bestX = x - i;
				}
				x = bestX;
				if (lastX == bestX)
					cornerFound = true;
			}
			int y = start.y;
			int bestY = 0, lastY = 0;
			cornerFound = false;
			while (!cornerFound) {
				lastY = bestY;
				for (int i = 0; i < 15; i++) {
					if (y - i >= 0 && pixels[x + (y - i) * image.getWidth()] == MENU_OUTLINE_COLOR)
						bestY = y - i;
				}
				y = bestY;
				if (lastY == bestY)
					cornerFound = true;
			}
			int nX = x, nY = y;
			for (int x1 = 0; x1 < 300; x1++) {
				if (pixels[nX + nY * image.getWidth()] == MENU_OUTLINE_COLOR)
					nX = x1 + x;
				else
					break;
			}
			for (int y1 = 0; y1 < 300; y1++) {
				if (pixels[(nX - 1) + nY * image.getWidth()] == MENU_OUTLINE_COLOR)
					nY = y1 + y;
				else
					break;
			}
			return new Rectangle(x, y, nX - x, nY - y);
		}
		return null;
	}
	
	public boolean interact(String action, String name) {
		return selectMenuOption(action, name);
	}
	
	public boolean selectMenuOption(String action, String name) {
		String upText = this.getUpText();
		if (!isOpen() && upText.contains(action) && upText.contains(name))
			return bot.mouse.click(true);
		else if (!isOpen()) {
			if (!bot.mouse.click(false))
				return false;
		}
		if (isOpen()) {
			MenuOption[] options = this.getMenuOptions();
			for (MenuOption o : options) {
				if (o.getAction().contains(action) && o.getAction().contains(name)) {
					if (bot.mouse.moveMouse(getFixedRectangle(o.getRectangle()))) {
						Time.sleep(150, 200);
						if (bot.mouse.click(true)) {
							Time.sleep(250, 400);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private Rectangle getFixedRectangle(Rectangle r) {
		return new Rectangle(r.x, r.y, r.width, r.height - 3);
	}
	
}
