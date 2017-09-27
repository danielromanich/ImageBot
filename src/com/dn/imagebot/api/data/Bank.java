package com.dn.imagebot.api.data;

import java.awt.Point;

import com.dn.imagebot.main.Bot;

public enum Bank {
	
	VARROCK_WEST,
	FALADOR,
	PEST_CONTROL,
	EDGEVILLE,
	GNOME_BANK,
	ROGUES_DEN;
	
	public final String toString() {
		final String name = super.name();
		return name.charAt(0) + name.substring(1).toLowerCase().replace("_", " ");
	}
	
	public final Point getBank(Bot bot) {
		switch (this) {
		case VARROCK_WEST:
			return bot.colors.getClosest(bot.colors.findColorObjectArray(1, -12636331, 7, 15));
		case FALADOR:
			return bot.colors.getClosest(bot.colors.findColorObjectArray(0.01D, -14936567, 2, 10));
		case PEST_CONTROL:
			return bot.colors.getClosest(bot.colors.findColorObjectArray(0.0001D, -8424361, 5, 15));
		case EDGEVILLE:
			return bot.colors.getClosest(bot.colors.findColorObjectArray(0.01D, -10993143, 2, 10));
		case ROGUES_DEN:
			return bot.colors.getClosest(bot.colors.findColorObjectArray(0.01D, -10923478, 1, 20));
		case GNOME_BANK:
			return bot.colors.getClosest(bot.colors.findColorObjectArray(0.01D, -15897843, 2, 5));
		}
		return null;
	}

}
