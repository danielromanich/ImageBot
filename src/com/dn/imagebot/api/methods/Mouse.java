package com.dn.imagebot.api.methods;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.geom.CubicCurve2D;

import com.dn.imagebot.main.Bot;
import com.dn.imagebot.util.Random;

public class Mouse {
	
	private Bot bot;
	public Mouse(Bot bot) {
		this.bot = bot;
	}
	
	public int speed = 3;
	
	public boolean moveMouse(Point p, boolean translate) {
		if (p == null)
			return false;
		return moveMouse(p.x, p.y, translate);
	}
	
	public boolean moveMouse(Rectangle rect, boolean translate) {
		if (rect == null)
			return false;
		return moveMouse(Random.nextInt(rect.x, rect.x + rect.width), Random.nextInt(rect.y, rect.y + rect.height), translate);
	}
	
	public boolean click(boolean left) {
		if (left) {
			bot.robot.mousePress(InputEvent.BUTTON1_MASK);
			bot.robot.mouseRelease(InputEvent.BUTTON1_MASK);
		} else {
			bot.robot.mousePress(InputEvent.BUTTON3_MASK);
			bot.robot.mouseRelease(InputEvent.BUTTON3_MASK);
		}
		return true;
	}
	
	public boolean click(Point p, boolean left) {
		return click(p.x, p.y, left);
	}
	
	public boolean click(Rectangle rect, boolean left) {
		if (moveMouse(rect, true)) {
			Time.sleep(100, 150);
			return click(left);
		}
		return false;
	}
	
	public boolean click(int x, int y, boolean left) {
		if (moveMouse(x, y, true)) {
			Time.sleep(100, 150);
			return click(left);
		}
		return false;
	}
	
	public final Point getPoint() {
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	public final Point getLocalPoint() {
		return new Point(MouseInfo.getPointerInfo().getLocation().x - bot.clientRect.x, MouseInfo.getPointerInfo().getLocation().y - bot.clientRect.y);
	}
	
	public boolean moveMouse(Rectangle rect) {
		return moveMouse(rect, true);
	}
	
	public boolean moveMouse(int x, int y) {
		return moveMouse(x, y, true);
	}
	
	public boolean moveMouse(int x, int y, boolean translate) {
		long s = System.currentTimeMillis();
		if (!withinClient()) {
			long start = System.currentTimeMillis();
			while (!moveToClient() && System.currentTimeMillis() - start < 2000) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (!withinClient())
				return false;
		}
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		Point[] line = getLine(mouse, translate ? bot.calculations.translate(new Point(x, y)) : new Point(x, y));
		if (line != null) {
			for (Point p : line) {
				bot.robot.mouseMove(p.x, p.y);
				Time.sleep(5);
			}
		}
		if (getLength(new Point(bot.mouse.getPoint().x - bot.clientRect.x - x, bot.mouse.getPoint().y - bot.clientRect.y - y)) <= 15)
			return true;
		return false;
	}
	
	private boolean withinClient() {
		return bot.clientRect.contains(MouseInfo.getPointerInfo().getLocation());
	}
	
	public boolean moveToClient() {
		if (Random.nextInt(0, 1) == 1)
			bot.robot.mouseMove(bot.clientRect.x, bot.clientRect.y + Random.nextInt(0, bot.clientRect.height));
		else
			bot.robot.mouseMove(bot.clientRect.x + Random.nextInt(0, bot.clientRect.width), bot.clientRect.y);
		return withinClient();
	}
	
	private Point[] getLine(Point start, Point end) {
		Point a = start;
		Point b = end;
        //DEF VARS
        int steps = (int) (Math.sqrt(a.distance(b)) * 3);

        int multiplier = 1;

        double xOffset = (b.x - a.x) / (Math.sqrt(a.distance(b)) * 3);
        double yOffset = (b.y - a.y) / (Math.sqrt(a.distance(b)) * 3);

        double x = Math.toRadians((180 / ((double) steps)));
        double y = Math.toRadians((180 / ((double) steps)));

        Point[] path = new Point[steps + 2];

        //RANDOMISE PATH VARS
        double waviness = 1;
        if (a.distance(b) < 120) { // less than 120px
            waviness = 0.4;
        }
        if ((Math.random()) >= 0.5) {
            x *= (int) Math.floor((Math.random() * waviness) + 1);
        }
        if ((Math.random()) >= 0.5) {
            y *= (int) Math.floor(Math.random() * waviness + 1);
        }
        if (Math.random() >= 0.5) {
            multiplier *= -1;
        }

        double offset = (Math.random() * (1.6 + Math.sqrt(steps)) + 6) + 2;

        // GEN PATH
        for (int i = 0; i < steps + 1; i++) {
            path[i] = new Point((a.x + ((int) (xOffset * i) + (multiplier * (int) (offset * Math.sin(x * i))))),
                    (a.y + ((int) (yOffset * i) + (multiplier * (int) (offset * Math.sin(y * i))))));
        }

        // RETURN PATH
        path[0] = a;
        path[path.length - 1] = b;

        return path;
	}
	
	private final double getLength(Point p) {
		return Math.sqrt(p.x * p.x + p.y * p.y);
	}

}
