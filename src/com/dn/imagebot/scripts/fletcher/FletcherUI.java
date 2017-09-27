package com.dn.imagebot.scripts.fletcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.dn.imagebot.api.data.Bank;

public class FletcherUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8880283780430721454L;
	
	public boolean isRunning = true;
	public FletchingItem selected;
	public Bank bank;
	
	public FletcherUI() {
		init();
	}
	
	private void init() {
		setSize(190, 125);
		setTitle("AutoFletcher");
		setLayout(null);
		setResizable(false);
		
		final JComboBox<FletchingItem> itemBox = new JComboBox<FletchingItem>(FletchingItem.values()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
		};
		itemBox.setBounds(10, 10, itemBox.getPreferredSize().width, 25);
		add(itemBox);
		
		final JComboBox<Bank> bankBox = new JComboBox<Bank>(Bank.values()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
		};
		bankBox.setBounds(10, 40, itemBox.getPreferredSize().width, 25);
		add(bankBox);
		
		final JButton start = new JButton("Start");
		start.setBounds(10, 70, itemBox.getPreferredSize().width, 25);
		add(start);
		
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				selected = (FletchingItem) itemBox.getSelectedItem();
				bank = (Bank) bankBox.getSelectedItem();
				isRunning = false;
			}
			
		});
		
		setVisible(true);
	}

}
