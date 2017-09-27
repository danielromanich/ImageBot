package com.dn.imagebot.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.dn.imagebot.api.methods.Camera.Direction;
import com.dn.imagebot.api.methods.Walking;
import com.dn.imagebot.api.wrappers.MenuOption;
import com.dn.imagebot.data.Constants;
import com.dn.imagebot.main.Bot;

public class PaintUI extends JFrame implements MouseMotionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y;
	private Bot bot;
	
	private Rectangle[] options;
	private MenuOption[] option;
	
	
	public PaintUI(final int x, final int y, final Bot bot) {
		this.x = x;
		this.y = y;
		this.bot = bot;
		File f = new File("./Test.png");
		try {
			ImageIO.write(bot.getImage(Walking.MINIMAP_RECT), "PNG", f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		init();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isVisible()) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (bot.clientRect.contains(MouseInfo.getPointerInfo().getLocation())) {
						menu = bot.menu.getMenu();
						options = bot.menu.getMenuOptionRectangles();
						option = bot.menu.getMenuOptions();
						mouseX = MouseInfo.getPointerInfo().getLocation().x - x;
						mouseY = MouseInfo.getPointerInfo().getLocation().y - y;
						upText = bot.menu.getUpText();
						color = bot.colors.getColorAt(new Point(mouseX, mouseY));
						//camDir = bot.camera.getDirection();
						//isAnimating = bot.player.isAnimating();
						//p = bot.colors.getClosest(bot.colors.findColorObjectArray(0.01D, -9415116, 2, 10));
					}
					repaint();
				}
			}
			
		}).start();
	}
	
	//
	/**
	 * Blue banker
	 */
	//bot.colors.getClosest(bot.colors.findColorObjectArray(1, -12636331, 7, 15)); option 1
	
	/**
	 * Falador bank (both) booths
	 */
	//bot.colors.getClosest(bot.colors.findColorObjectArray(0.01D, -14936567, 2, 10)); option 0
	
	/**
	 * Bank booth pest control
	 */
	//bot.colors.getClosest(bot.colors.findColorObjectArray(-8424361, 5, 15)); option 0
	
	/**
	 * Bank booth edgeville
	 */
	//bot.colors.getClosest(bot.colors.findColorObjectArray(0.01D, -10993143, 2, 10)); option 0
	
	
	/**
	 * Emerald benedict - rogues den
	 */
	//bot.colors.getClosest(bot.colors.findColorObjectArray(0.01D, -10923478, 1, 20)); option 1
	
	/**
	 * Catherby booth
	 */
	//
	
	/**
	 * Al-kharid
	 */
	
	
	/**
	 * Bank chest - not functional
	 */
	//bot.colors.getClosest(bot.colors.findColorObjectArray(0.00001d, -10199716, 0.001, -11529976, 0, 2, 20));
	
	/**
	 * Range - catherby
	 */
	//bot.colors.getClosest(bot.colors.findColorObjectArray(0.001D, -5889515, 0, 10));
	
	
	private int mouseX, mouseY, color = -1;
	private String upText;
	private Direction camDir;
	private Point p;
	private Rectangle menu;
	public boolean drawClient = true, isAnimating = false, isFull, isEmpty;
	
	private void init() {
		this.setSize(Constants.RS_CLIENT_WIDTH, Constants.RS_CLIENT_HEIGHT);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setLocation(new Point(x, y));
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		
		final JPanel painter = new JPanel() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				if (!bot.scriptHandler.isRunning) {
					if (drawClient) {
						g.setColor(new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 100));
						g.fillRect(0, 0, Constants.RS_CLIENT_WIDTH, Constants.RS_CLIENT_HEIGHT);
					}
					
					g.setColor(Color.white);
					g.drawString("X: "+mouseX+" Y: "+mouseY, 10, 45);
					g.drawString("Color: "+color+" Luminosity: "+bot.colors.getLuminance(color), 10, 60);
					g.drawString("RGB: "+((color >> 16) & 0x000000FF)+", "+((color >> 8) & 0x000000FF)+", "+(color & 0x000000FF), 10, 75);
					g.drawString("Camera: "+camDir, 10, 90);
					g.drawString("isAnimating: "+isAnimating, 10, 105);
					if (option != null) {
						g.drawString("Menu options: ", 10, 120);
						for (int i = 0; i < option.length; i++) {
							final MenuOption o = option[i];
							g.drawString(o.getAction(), 10, 135 + i * 15);
						}
					} else 
						g.drawString("Uptext: "+(upText != null ? upText : ""), 10, 120);
					if (menu != null) {
						g.setColor(Color.green);
						g.drawRect(menu.x - 1, menu.y - 1, menu.width + 1, menu.height + 1);
					}
					if (options != null)
						for (Rectangle r : options) {
							g.drawRect(r.x, r.y, r.width, r.height);
						}
					
					
					g.setColor(new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 100));
					
					if (color != 0) {
						g.setColor(new Color(color));
						g.fillRect(300, 40, 20, 20);
					}
					g.setColor(Color.RED);
					if (p != null)
						g.drawRect(p.x - 2, p.y - 2, 4, 4);
					
					if (bot.ocr.current != null)
						g.drawImage(bot.ocr.current, 200, 200, null);
					int[] pixels = bot.colors.getPixels(bot.getImage());
					for (int i = 0; i < pixels.length; i++) {
						if (bot.colors.getSimilarity(pixels[i], -9415116) <= 0.01) {
							Point p = getPoint(i);
							if (Math.abs(p.x - mouseX) > 2 || Math.abs(p.y - mouseY) > 2)
								g.fillRect(p.x, p.y, 1, 1);
						}
					}
				}
			}
			
		};
		painter.setBounds(0, 0, Constants.RS_CLIENT_WIDTH, Constants.RS_CLIENT_HEIGHT);
		this.setGlassPane(painter);
		painter.setVisible(true);
		painter.setOpaque(true);
		//add(painter);
		
		this.setBackground(new Color(0, 0, 0, 0));
		this.setVisible(true);
	}
	
	Point getPoint(int value) {
		return new Point(value % bot.clientRect.width, (int) Math.floor(value/bot.clientRect.width));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getPoint().x;
		mouseY = e.getPoint().y;
	}

}
