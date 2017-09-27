package com.dn.imagebot.scripts.superheater;

import java.awt.Point;
import java.awt.Rectangle;

import com.dn.imagebot.api.data.Bank;
import com.dn.imagebot.api.methods.Time;
import com.dn.imagebot.api.methods.Tabs.Tab;
import com.dn.imagebot.api.wrappers.Item;
import com.dn.imagebot.main.Bot;
import com.dn.imagebot.script.LoopScript;
import com.dn.imagebot.util.Condition;
import com.dn.imagebot.util.Random;

public class SuperHeater extends LoopScript {

	public SuperHeater(Bot bot) {
		super(bot);
	}
	

	@Override
	public boolean onStart() {
		return true;
	}
	
	public int state = 0;
	public int tries = 0;
	
	public int smelted = 0;

	@Override
	public int onLoop() {
		if (smelted > 2) {
			stop();
			System.out.println("Done!");
			return 100;
		}
		if (!hasOres()) { 
			if (context.bank.isOpen()) {
				if (context.inventory.isEmpty(1)) {
					Item ore = context.bank.getItem("gold ore");
					if (ore != null) {
						if (ore.interact("Withdraw-All")) {
							Time.sleep(new Condition() {
	
								@Override
								public boolean validate() {
									return context.inventory.getItem("gold ore") == null;
								}
								
							}, 750, 1000);
						}
					} else {
						context.log("Could not locate any ores, stopping!");
					}
				} else {
					context.bank.depositAllExcept("nature rune");
				}
			} else {
				Point bank = Bank.PEST_CONTROL.getBank(context);
				if (bank != null) {
					bank = new Point(Random.nextInt(bank.x, bank.x), Random.nextInt(bank.y, bank.y));
					if (context.mouse.moveMouse(bank, true)) {
						if (context.menu.interact("Bank", "Bank booth")) {
							Time.sleep(new Condition() {

								@Override
								public boolean validate() {
									return !context.bank.isOpen();
								}
								
							}, 1500, 2000);
						}
					}
				}
			}
		} else {
			if (context.bank.isOpen())
				if (!context.bank.close())
					return 100;
			if (!context.tabs.isOpen(Tab.MAGIC)) {
				if (!context.tabs.open(Tab.MAGIC))
					return 100;
			}
			Rectangle sHeat = new Rectangle(660, 299, 18, 12);
			if (sHeat != null) {
				if (context.mouse.click(sHeat, true)) {
					if (Time.sleep(new Condition() {

						@Override
						public boolean validate() {
							return !context.tabs.isOpen(Tab.INVENTORY);
						}
						
					}, 500, 750)) {
						hasOres();
						if (context.tabs.isOpen(Tab.INVENTORY)) {
							if (!context.inventory.contains("nature rune")) {
								context.log("Out of nature runes.");
								stop();
								return 0;
							}
						}
						Item[] ore = context.inventory.getItems("gold ore");
						if (ore != null && ore.length > 0) {
							if (ore.length == 1) {
								items = new Item[]{};
							}
							if (ore != null) {
								if (ore[ore.length - 1].interact("Cast Superheat Item")) {
									if (Time.sleep(new Condition() {
	
										@Override
										public boolean validate() {
											return !context.tabs.isOpen(Tab.MAGIC);
										}
										
									}, 1250, 1500)) {
										stop();
										smelted++;
										sleep(300, 500);
									}
								}
							}
						}
					}
				}
			}
		}
		stop();
		return 100;
	}
	
	long lastAnim = 0;
	private Item[] items;
	
	public boolean hasOres() {
		if (!context.tabs.isOpen(Tab.INVENTORY) && !context.bank.isOpen()) {
			if (items == null) {
				if (context.tabs.open(Tab.INVENTORY)) {
					items = context.inventory.getItems("gold ore");
				}
			} else {
				return items.length > 0;
			}
		}
		if (items == null && context.tabs.isOpen(Tab.INVENTORY))
			items = context.inventory.getItems("gold ore");
		if (items != null) {
			Item[] newItems = context.inventory.getItems("gold ore");
			if (newItems.length > 0) {
				items = newItems;
				return true;
			}
		}
		return false;
	}

	@Override
	public void onFinish() {

	}

}
