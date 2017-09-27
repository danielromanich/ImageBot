package com.dn.imagebot.main;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import com.dn.imagebot.api.methods.Bank;
import com.dn.imagebot.api.methods.Calculations;
import com.dn.imagebot.api.methods.Camera;
import com.dn.imagebot.api.methods.Colors;
import com.dn.imagebot.api.methods.DTMHandler;
import com.dn.imagebot.api.methods.Interfaces;
import com.dn.imagebot.api.methods.Inventory;
import com.dn.imagebot.api.methods.Keyboard;
import com.dn.imagebot.api.methods.Menu;
import com.dn.imagebot.api.methods.Mouse;
import com.dn.imagebot.api.methods.Player;
import com.dn.imagebot.api.methods.Tabs;
import com.dn.imagebot.api.methods.Walking;
import com.dn.imagebot.api.methods.ocr.OCR;
import com.dn.imagebot.script.ScriptHandler;
import com.dn.imagebot.ui.UI;
import com.dn.imagebot.util.DTMGenerator;
import com.dn.imagebot.util.image.ImageComparator;
import com.dn.imagebot.util.image.ObjectImages;

public class Bot {
	
	public UI ui;
	public Robot robot;
	public ImageComparator imageComparator = new ImageComparator(this);
	public Colors colors = new Colors(this);
	public Calculations calculations = new Calculations(this);
	public Mouse mouse = new Mouse(this);
	public Keyboard keyboard = new Keyboard(this);
	public ObjectImages objectImages = new ObjectImages(this);
	public DTMGenerator dtmGenerator = new DTMGenerator(this);
	public DTMHandler dtmHandler = new DTMHandler(this);
	public Menu menu = new Menu(this);
	public ScriptHandler scriptHandler = new ScriptHandler(this);
	public Player player = new Player(this);
	public Camera camera = new Camera(this);
	public Inventory inventory = new Inventory(this);
	public Bank bank = new Bank(this);
	public Interfaces interfaces = new Interfaces(this);
	public Walking walking = new Walking(this);
	public Tabs tabs = new Tabs(this);
	public OCR ocr = new OCR(this);
	
	public Rectangle clientRect = null;
	
	public Bot() {
		super();
		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		this.dtmGenerator.addDTMS(objectImages.images);
		this.ui = new UI(this);
	}
	
	public final BufferedImage getImage() {
		final Rectangle screenRect = new Rectangle(clientRect);
		return robot.createScreenCapture(screenRect);
	}
	
	public final BufferedImage getImage(Rectangle rect) {
		final Rectangle screenRect = new Rectangle(clientRect.x + rect.x, clientRect.y + rect.y, rect.width, rect.height);
		return robot.createScreenCapture(screenRect);
	}
	
	public void log(String log) {
		ui.log(log);
	}

}
