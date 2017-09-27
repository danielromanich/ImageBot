package com.dn.imagebot.api.methods;

import java.awt.Rectangle;
import java.awt.image.DataBufferInt;

import com.dn.imagebot.main.Bot;

public class Player {
	
	public Bot bot;
	public Player(Bot bot) {
		this.bot = bot;
	}
	
	public static final Rectangle PLAYER_RECT = new Rectangle(246, 155, 30, 36);
	
	private int[] idleAnim;
	public final boolean isAnimating() {
		if (idleAnim == null)
			setIdle();
		int[] endPixels = ((DataBufferInt)bot.getImage(PLAYER_RECT).getRaster().getDataBuffer()).getData();
		int count = 0;
		for (int i = 0; i < idleAnim.length; i++) {
			if (idleAnim[i] == endPixels[i])
				count++;
		}
		return (count * 100) / idleAnim.length < 90;
	}
	
	public void setIdle() {
		idleAnim = ((DataBufferInt)bot.getImage(PLAYER_RECT).getRaster().getDataBuffer()).getData();
	}

}
