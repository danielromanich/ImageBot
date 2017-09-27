package com.dn.imagebot.api.methods;

import java.awt.Point;
import java.awt.Rectangle;

import com.dn.imagebot.main.Bot;
import com.dn.imagebot.util.Condition;

public class Tabs {
	
	private Bot bot;
	public Tabs(Bot bot) {
		this.bot = bot;
	}
	
	public boolean isOpen(Tab tab) {
		return bot.colors.isColorAt(tab.color, tab.p, 0.001D);
	}
	
	public Tab getOpen() {
		for (Tab tab : Tab.values()) {
			if (bot.colors.isColorAt(tab.color, tab.p, 0.001D))
				return tab;
		}
		return null;
	}
	
	public boolean open(final Tab tab) {
		if (bot.mouse.moveMouse(tab.rect, true)) {
			if (bot.mouse.click(true)) {
				return Time.sleep(new Condition() {

					@Override
					public boolean validate() {
						return !isOpen(tab);
					}
					
				}, 350, 500);
			}
		}
		return false;
	}
	
	
	public enum Tab {
		COMBAT(new Point(534, 186), new Rectangle(532, 176, 21, 16), -10608616),
		SKILLS(new Point(589, 180), new Rectangle(556, 176, 21, 16), -13036786), 
		QUESTS(new Point(622, 174), new Rectangle(599, 176, 21, 16), -13036786),
		INVENTORY(new Point(656, 173), new Rectangle(631, 176, 21, 16), -13364978),
		EQUIPMENT(new Point(686, 172), new Rectangle(664, 176, 21, 16), -12314606),
		PRAYER(new Point(719, 172), new Rectangle(697, 176, 21, 16), -12314606),
		MAGIC(new Point(731, 173), new Rectangle(731, 176, 21, 16), -9755621),
		CLAN(new Point(534, 473), new Rectangle(528, 472, 21, 16), -9755621),
		FRIENDS(new Point(579, 471), new Rectangle(566, 472, 21, 16), -10871272),
		IGNORE(new Point(621, 474), new Rectangle(599, 472, 21, 16), -13364978),
		LOGOUT(new Point(643, 470), new Rectangle(632, 472, 21, 16), -10214887),
		SETTINGS(new Point(667, 471), new Rectangle(667, 472, 21, 16), -9361891),
		EMOTE(new Point(697, 480), new Rectangle(700, 472, 21, 16), -9755621),
		MUSIC(new Point(752, 470), new Rectangle(731, 472, 21, 16), -11396074);
		
		private Point p;
		private int color;
		private Rectangle rect;
		Tab(Point p, Rectangle rect, int color) {
			this.p = p;
			this.rect = rect;
			this.color = color;
		}
		
		public int getColor() {
			return this.color;
		}
		
		public Point getPoint() {
			return this.p;
		}
		
		public Rectangle getBounds() {
			return this.rect;
		}
		
	}

}
