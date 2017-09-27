package com.dn.imagebot.ui;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.dn.imagebot.api.data.Bank;
import com.dn.imagebot.api.methods.Time;
import com.dn.imagebot.data.Constants;
import com.dn.imagebot.main.Bot;
import com.dn.imagebot.script.Script;
import com.dn.imagebot.scripts.fletcher.Fletcher;
import com.dn.imagebot.scripts.pestcontrol.PestControl;
import com.dn.imagebot.scripts.superheater.SuperHeater;
import com.dn.imagebot.util.Condition;

public class UI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2810348463716872098L;
	
	private Bot bot;
	
	public PaintUI paint;
	
	public UI(Bot bot) {
		this.bot = bot;
		init();
	}
	
	public BufferedImage current;
	public JButton startButton;
	
	public boolean painting = true;
	
	private JTextArea logArea;
	
	private void init() {
		setSize(600, 600);
		setTitle("Welcome Mrsdefnerd to DNBot");
		setResizable(false);
		setLayout(null);
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		final JComboBox<Script> scriptBox = new JComboBox<Script>();
		scriptBox.addItem(new PestControl(bot));
		scriptBox.addItem(new Fletcher(bot));
		scriptBox.addItem(new SuperHeater(bot));
		scriptBox.setBounds(40, 50, scriptBox.getPreferredSize().width, 25);
		add(scriptBox);
		
		startButton = new JButton("Start script");
		startButton.setBounds(40 + scriptBox.getPreferredSize().width + 10, 50, startButton.getPreferredSize().width, 25);
		add(startButton);
		
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (startButton.getText().startsWith("Start")) {
					if (setClient()) {
						Script currentScript = (Script) scriptBox.getSelectedItem();
						bot.scriptHandler.startScript(currentScript);
						startButton.setText("Stop script");
					}
				} else {
					startButton.setText("Start script");
					bot.scriptHandler.stopCurrent();
				}
			}
			
		});
		
		final JButton clickContinue = new JButton("Continue");
		clickContinue.setBounds(480 - 115, 375, 110, 20);
		add(clickContinue);
		
		clickContinue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (paint != null) {
					if (painting) {
						paint.setVisible(false);
					} else {
						paint.dispose();
						paint = null;
					}
				} else if (painting) {
					if (setClient()) {
						paint = new PaintUI(bot.clientRect.x, bot.clientRect.y, bot);
						paint.drawClient = false;
					}
				}
			}
			
		});
		
		final JButton test = new JButton("Test");
		test.setBounds(10, 375, 110, 20);;
		add(test);
		
		test.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						bot.mouse.moveMouse(Bank.GNOME_BANK.getBank(bot), true);
						Time.sleep(200);
						bot.menu.interact("Bank", "Gnome banker");
						Time.sleep(new Condition() {

							@Override
							public boolean validate() {
								return bot.bank.isOpen();
							}
							
						}, 500, 3000);
						bot.bank.getItem("Willow logs").interact("Withdraw-All");
					}
					
				}).start();
			}
			
		});
		
		logArea = new JTextArea();
		log("Welcome to DNBot. The bot has attempted to locate your client. If your client are not glowing make sure to have the full client visible" +
				"on the screen, then hit the 'detect client' button!");
		logArea.setLineWrap(true);
		logArea.setEditable(false);
		logArea.setMargin(new Insets(2, 5, 5, 5));
		final JScrollPane scrollLog = new JScrollPane(logArea);
		scrollLog.setBounds(0, 400, 595, 195);
		add(scrollLog);
		
		setClient();
		setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
		    public void windowClosing(WindowEvent e) {
				if (paint != null) {
					paint.setVisible(false);
					paint.dispose();
					bot.scriptHandler.stopCurrent();
				}
		    }
		    
		});
		
	}
	
	private boolean setClient() {
		final Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage image = bot.robot.createScreenCapture(screenRect);
		Point[] points = findClient(image);
		if (points != null) {
			bot.clientRect = new Rectangle(points[0].x, points[0].y, points[1].x - points[0].x, points[1].y - points[0].y);
			if (paint != null) {
				paint.setVisible(false);
				paint.dispose();
			}
			//paint = new PaintUI(bot.clientRect.x, bot.clientRect.y, bot);
			log("Successfully loaded the client");//, if the client is perfectly highlighted in red, please click on the continue button!");
			return true;
		} else {
			log("Failed to load the client, please close all interfaces then try again!");
			return false;
		}
	}
	
	private Point[] findClient(BufferedImage image) {
		final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (validateCoords(image, x, y, pixels)) {
					if (bot.colors.getColorAt(x, y, image.getWidth(), pixels) == -256) {
						x += 456;
						y += 262;
					}
					Point p = new Point(x - Constants.RS_CLIENT_WIDTH, y - Constants.RS_CLIENT_HEIGHT);
					Point p1 = new Point(x + 1, y + 1);
					return new Point[]{p, p1};
				}
			}
		}
		return null;
	}
	
	private final boolean validateCoords(BufferedImage image, int x1, int y1, int[] pixels) {
		if (bot.colors.getColorAt(x1, y1, image.getWidth(), pixels) == -15592180) {
			int points = 0;//741 477 - 764 502
			if (x1 > 21) {
				if (bot.colors.getColorAt(x1 - 23, y1 - 25, image.getWidth(), pixels) == -728818) {
					points++;
				}
			}
			if (y1 > 3) {
				int start = points;
				for (int y = -3; y < 0; y++) {
					if (bot.colors.getColorAt(x1, y1 + y, image.getWidth(), pixels) == -15330032) {
						points++;
						if (points - start >= 3) {
						}
					}
				}
			}
			if (points == 4)
				return true;//309, 241
		} else if (bot.colors.getColorAt(x1, y1, image.getWidth(), pixels) == -256) {
			int points = 0;
			if (x1 + 50 < image.getWidth() && y1 + 50 < image.getHeight()) {
				if (bot.colors.getColorAt(x1 + 1, y1, image.getWidth(), pixels) == -256) {
					points++;
				}
				if (bot.colors.getColorAt(x1 + 6, y1, image.getWidth(), pixels) == -256) {
					points++;
				}
				if (x1 >= 1 && bot.colors.getColorAt(x1 - 1, y1, image.getWidth(), pixels) == -11776423) {
					points++;
				}
				if (bot.colors.getColorAt(x1, y1 + 7, image.getWidth(), pixels) == -256) {
					points++;
				}
				return points == 4;
			}
			return false;
		}
		return false;
	}
	
	public void log(String log) {
		logArea.append("["+Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE)+":"+Calendar.getInstance().get(Calendar.SECOND)+"] - "+log+"\n");
	}
}
