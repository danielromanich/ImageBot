package com.dn.imagebot.api.methods;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import com.dn.imagebot.api.wrappers.Item;
import com.dn.imagebot.main.Bot;
import com.dn.imagebot.util.Condition;

public class Bank {
	
	public static final Rectangle BANK_RECT = new Rectangle(72, 82, 386, 213);
	public static final int START_X = 72, START_Y = 82, ITEM_X = 34, ITEM_Y = 35, PADDING_X = 14, PADDING_Y = 1;
	final HashMap<Integer, Rectangle> itemRects = new HashMap<Integer, Rectangle>();
	
	public static final Rectangle CLOSE_RECT = new Rectangle(479, 15, 16, 17);
	
	public int slots = 48;
	
	private Bot bot;
	public Bank(Bot bot) {
		this.bot = bot;
		for (int i = 0; i < 48; i++) {
			int row = i % 8;
			int column = (int) Math.floor(i/8);
			int startX = START_X + row * PADDING_X + row * ITEM_X;
			int startY = START_Y + column * PADDING_Y + column * ITEM_Y;
			itemRects.put(i, new Rectangle(startX, startY, ITEM_X, ITEM_Y));
		}
	}
	
	public final Rectangle getRectForSlot(int slot) {
		return this.itemRects.get(slot);
	}
	
	public Item[] getItems(String name) {
		ArrayList<Item> items = new ArrayList<>();
		BufferedImage[] images = getSlotImages();
		for (int slot : itemRects.keySet()) {
			Point p = bot.dtmHandler.getPointForDtm(name, 0.5D, images[slot]);
			if (p != null) {
				items.add(new Item(bot, name, slot, Item.BANK_ITEM));
			}
		}
		return items.toArray(new Item[items.size()]);
	}
	
	
	public Item getItem(String name) {
		BufferedImage[] images = getSlotImages();
		for (int slot : itemRects.keySet()) {
			Point p = bot.dtmHandler.getPointForDtm(name, 0.5D, images[slot]);
			if (p != null) {
				return new Item(bot, name, slot, Item.BANK_ITEM);
			}
		}
		return null;
	}
	
	public final boolean isOpen() {
		return bot.colors.isColorAt(-26593, 62, 303, 0.01);
	}
	
	public final boolean close() {
		if (bot.mouse.moveMouse(CLOSE_RECT, true)) {
			Time.sleep(250, 400);
			if (bot.mouse.click(true)) {
				return Time.sleep(new Condition() {

					@Override
					public boolean validate() {
						return isOpen();
					}
					
				}, 500, 750);
			}
		}
		return false;
	}
	
	public final boolean depositAllExcept(String ... names) {
		BufferedImage[] images = bot.inventory.getSlotImages();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 28; i++) {
			final int slot = i;
			if (!isOpen())
				return false;
			if (System.currentTimeMillis() - start > 4000)
				break;
			boolean empty = !bot.inventory.isEmpty(slot, images[slot]);
			loop: for (String name : names)
				if (bot.dtmHandler.getPointForDtm(name, 1, images[slot]) != null) {
					empty = false;
					break loop;
				}
			if (empty) {
				if (new Item(bot, "", slot, Item.INVENTORY_ITEM).interact("Deposit-all")) {
					if (Time.sleep(new Condition() {
						
						@Override
						public boolean validate() {
							return !bot.inventory.isEmpty(slot);
						}
						
					}, 1500, 2000)) {
						images = bot.inventory.getSlotImages();
					}
				}
			}
		}
		return false;
	}
	
	public BufferedImage[] getSlotImages() {
		BufferedImage[] images = new BufferedImage[48];
		BufferedImage image = bot.getImage();
		for (int slot = 0; slot < 48; slot++) {
			BufferedImage img = new BufferedImage(itemRects.get(slot).width, itemRects.get(slot).height, image.getType());
			Graphics2D gr = img.createGraphics();  
            gr.drawImage(image, 0, 0, itemRects.get(slot).width, itemRects.get(slot).height, itemRects.get(slot).x, itemRects.get(slot).y, itemRects.get(slot).x + itemRects.get(slot).width, itemRects.get(slot).y + itemRects.get(slot).height, null);  
            gr.dispose();  
            images[slot] = img;
		}
		return images;
	}
	
	public boolean open() {
		return false;
	}

}
