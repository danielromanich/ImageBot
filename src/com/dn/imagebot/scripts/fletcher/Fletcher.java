package com.dn.imagebot.scripts.fletcher;

import java.awt.Point;
import com.dn.imagebot.api.methods.Time;
import com.dn.imagebot.api.wrappers.Item;
import com.dn.imagebot.main.Bot;
import com.dn.imagebot.script.LoopScript;
import com.dn.imagebot.util.Condition;
import com.dn.imagebot.util.Random;

public class Fletcher extends LoopScript {

	public Fletcher(Bot bot) {
		super(bot);
	}
	
	public int logsCut = 1;
	public FletcherUI ui;

	@Override
	public boolean onStart() {
		context.player.setIdle();
		ui = new FletcherUI();
		return true;
	}

	@Override
	public int onLoop() {
		if (ui.isRunning)
			return 100;
		Item logs = context.inventory.getItem(ui.selected.getLogs());
		if (logs == null) {
			if (context.bank.isOpen()) {
				if (context.inventory.isEmpty(1)) {
					Item bankLogs = context.bank.getItem(ui.selected.getLogs());
					if (bankLogs != null) {
						if (bankLogs.interact("Withdraw-All")) {
							Time.sleep(new Condition() {
	
								@Override
								public boolean validate() {
									return context.inventory.getItem(ui.selected.getLogs()) == null;
								}
								
							}, 750, 1000);
						}
					}
				} else {
					context.bank.depositAllExcept("Knife");
				}
			} else {
				Point bank = ui.bank.getBank(context);
				if (bank != null) {
					bank = new Point(Random.nextInt(bank.x, bank.x + 10), Random.nextInt(bank.y - 10, bank.y + 10));
					if (context.mouse.moveMouse(bank, true)) {
						if (context.menu.selectMenuOption("Bank", "Bank-booth")) {
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
			if (isAnimating()) {
				sleep(500, 750);
			} else if (context.colors.isColorAt(-13667218, 484, 375, 1)) {
				if (!ui.selected.getRectangle().contains(context.mouse.getLocalPoint()))
					context.mouse.moveMouse(ui.selected.getRectangle(), true);
				//context.menu.selectMenuOption(3);
				if (Time.sleep(new Condition() {

					@Override
					public boolean validate() {
						return !context.interfaces.isAmountInterfaceOpen();
					}
					
				}, 750, 1250)) {
					context.keyboard.type("111");
					Time.sleep(new Condition() {

						@Override
						public boolean validate() {
							return !isAnimating();
						}
						
					}, 1750, 2500);
				}
			} else {
				if (context.bank.isOpen())
					if (!context.bank.close())
						return 100;
				Item knife = context.inventory.getItem("knife");
				if (knife != null) {
					knife.interact("Use");
					Time.sleep(250, 350);
					logs.interact("Use");
					Time.sleep(300, 500);
					context.mouse.moveMouse(ui.selected.getRectangle(), true);
					System.out.println("Started sleep");
					Time.sleep(new Condition() {

						@Override
						public boolean validate() {
							return !context.colors.isColorAt(-6779081, 274, 411, 1);
						}
						
					}, 1000, 1250);
					System.out.println("Exited sleep..");
				}
			}
		}
		return 100;
	}
	
	long lastAnim = 0;
	private Item[] items;
	
	public boolean isAnimating() {
		if (items == null)
			items = context.inventory.getItems(ui.selected.getLogs());
		if (items != null) {
			if (System.currentTimeMillis() - lastAnim <= 2000) {
				return true;
			}
			Item[] newItems = context.inventory.getItems(ui.selected.getLogs());
			if (context.bank.isOpen()) {
				items = newItems;
			}
			if (newItems.length != items.length) {
				lastAnim = System.currentTimeMillis();
				logsCut += Math.abs(items.length - newItems.length);
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
