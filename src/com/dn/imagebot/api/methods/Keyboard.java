package com.dn.imagebot.api.methods;

import java.awt.event.KeyEvent;

import com.dn.imagebot.main.Bot;
import com.dn.imagebot.util.Random;

public class Keyboard {
	
	private Bot bot;
	public Keyboard(Bot bot) {
		this.bot = bot;
	}
	
	public final void type(String message) {
		sendKeys(message, true);
		bot.robot.keyPress(KeyEvent.VK_ENTER);
		bot.robot.keyRelease(KeyEvent.VK_ENTER);
	}
	
    private void sendKeys(String s, boolean pressEnter) {
        byte[] bytes = s.getBytes();
        for (byte b : bytes) {
			int code = b;
			if (code > 96 && code < 123)
				code = code - 32;
			bot.robot.keyPress(code);
			bot.robot.delay(Random.nextInt(50, 80));
			bot.robot.keyRelease(code);
			bot.robot.delay(Random.nextInt(30, 50));
        }
    }


}
