package com.dn.imagebot.api.methods;

import java.awt.Point;

import com.dn.imagebot.main.Bot;

public class Calculations {
	
	private Bot bot;
	public Calculations(Bot bot) {
		this.bot = bot;
	}
	
	public Point translate(Point p) {
		return new Point(bot.clientRect.x + p.x, bot.clientRect.y + p.y);
	}

}
