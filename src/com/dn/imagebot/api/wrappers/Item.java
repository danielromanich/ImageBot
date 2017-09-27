package com.dn.imagebot.api.wrappers;

import java.awt.Rectangle;

import com.dn.imagebot.main.Bot;

public class Item {
	
	public static final int BANK_ITEM = 1, INVENTORY_ITEM = 0;
	
	private int slot, type;
	private String name;
	private Bot bot;
	public Item(Bot bot, String name, int slot, int type) {
		this.bot = bot;
		this.name = name;
		this.slot = slot;
		this.type = type;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final int getSlot() {
		return this.slot;
	}
	
	public final int getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public final boolean interact(String option) {
		Rectangle rect = type == BANK_ITEM ? bot.bank.getRectForSlot(slot) : bot.inventory.getRectForSlot(slot);
		rect = new Rectangle(rect.x + 5, rect.y + 5, rect.width - 5, rect.height - 5);
		if (bot.mouse.moveMouse(rect, true)) {
			if (bot.menu.selectMenuOption(option, getName())) {
				return true;
			}
		}
		return false;
	}

}
