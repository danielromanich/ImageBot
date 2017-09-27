package com.dn.imagebot.api.methods;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import com.dn.imagebot.api.wrappers.Item;
import com.dn.imagebot.main.Bot;

public class Inventory {
	
	public static final int START_X = 562, START_Y = 210, ITEM_X = 34, ITEM_Y = 35, PADDING_X = 8, PADDING_Y = 1;
	final HashMap<Integer, Rectangle> itemRects = new HashMap<Integer, Rectangle>();
	public static final Rectangle INVENTORY_RECT = new Rectangle(START_X, START_Y, 159, 249);
	public int[][] cachedSlots = new int[28][];
	
	private Bot bot;
	public Inventory(Bot bot) {
		this.bot = bot;
		for (int i = 0; i < 28; i++) {
			int row = i % 4;
			int column = (int) Math.floor(i/4);
			int startX = START_X + row * PADDING_X + row * ITEM_X;
			int startY = START_Y + column * PADDING_Y + column * ITEM_Y;
			itemRects.put(i, new Rectangle(startX, startY, ITEM_X, ITEM_Y));
		}
	}
	
	public Rectangle getRectForSlot(int slot) {
		return itemRects.get(slot);
	}
	
	public Item[] getItems(String name) {
		BufferedImage[] images = getSlotImages();
		ArrayList<Item> items = new ArrayList<>();
		for (int slot = 27; slot >= 0; slot--) {
			Point p = bot.dtmHandler.getPointForDtm(name, 0.5D, images[slot]);
			if (p != null) {
				items.add(new Item(bot, name, slot, Item.INVENTORY_ITEM));
			}
		}
		return items.toArray(new Item[items.size()]);
	}
	
	public static long start = 0;
	
	public Item getItem(String name) {
		BufferedImage[] images = getSlotImages();
		for (int slot = 0; slot < 28; slot++) {
			Point p = bot.dtmHandler.getPointForDtm(name, 0.5D, images[slot]);
			if (p != null) {
				return new Item(bot, name, slot, Item.INVENTORY_ITEM);
			}
		}
		return null;
	}
	
	public boolean contains(String name) {
		return getItem(name) != null;
	}
	
	public final boolean isEmpty(int slot) {
		return isEmpty(slot, getSlotImages()[slot]);
	}
	
	public final boolean isEmpty(int slot, BufferedImage image) {
		for (int pixel : bot.colors.getPixels(image)) {
			if (pixel == -16777215)
				return false;
		}
		return true;
	}
	
	public boolean isFull() {
		BufferedImage[] images = getSlotImages();
		for (int slot = 0; slot < 28; slot++) {
			if (isEmpty(slot, images[slot])) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isEmpty() {
		BufferedImage[] images = getSlotImages();
		for (int slot = 0; slot < 28; slot++) {
			if (!isEmpty(slot, images[slot])) {
				return false;
			}
		}
		return true;
	}
	
	public int getEmptySlots() {
		int empty = 0;
		BufferedImage[] images = getSlotImages();
		for (int slot = 0; slot < 28; slot++) {
			if (getItem("empty_inv"+slot, slot, images[slot]) != null) {
				empty++;
			}
		}
		return empty;
	}
	
	public int getFullSlots() {
		return 28 - getEmptySlots();
	}
	
	
	public Item getItem(String name, int slot, BufferedImage image) {
		Point p = bot.dtmHandler.getPointForDtm(name, 0.5D, image);
		if (p != null) {
			return new Item(bot, name, slot, Item.INVENTORY_ITEM);
		}
		return null;
	}
	
	public BufferedImage[] getSlotImages() {
		BufferedImage[] images = new BufferedImage[28];
		BufferedImage image = bot.getImage();
		for (int slot = 0; slot < 28; slot++) {
			BufferedImage img = new BufferedImage(itemRects.get(slot).width, itemRects.get(slot).height, image.getType());
			Graphics2D gr = img.createGraphics();  
            gr.drawImage(image, 0, 0, itemRects.get(slot).width, itemRects.get(slot).height, itemRects.get(slot).x, itemRects.get(slot).y, itemRects.get(slot).x + itemRects.get(slot).width, itemRects.get(slot).y + itemRects.get(slot).height, null);  
            gr.dispose();  
            images[slot] = img;
		}
		return images;
	}

}
