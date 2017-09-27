package com.dn.imagebot.api.methods;

import java.awt.Point;

import com.dn.imagebot.main.Bot;

public class Interfaces {
	
	private Bot bot;
	public Interfaces(Bot bot) {
		this.bot = bot;
	}
	
	public final boolean isAmountInterfaceOpen() {
		return bot.colors.isColorAt(-16777088, new Point(263, 429), 1);
	}

}
