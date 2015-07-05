package main;

import gameLogic.Board;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import opening.Book;
import userInterface.BoardPanel;
import userInterface.ControlPanel;
import databases.Database;
import databases.ImageDatabase;


public class Main {
	
	
	public static void main (String [] args) {
		
		SwingUtilities.invokeLater (new Runnable () {
			
			public void run() {
				try {
					ImageDatabase.initImages();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Failed to load images.");
					e.printStackTrace();
				}
				Database.initDatabase();

				try {
					Book.initBook();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Failed to load opening book.");
					e.printStackTrace();
				}
				

				initGUI ();
				
				
			}

			private void initGUI() {
				JFrame frame = new JFrame ("Chess");
				frame.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints ();
				c.weightx = .8;
				c.gridx=0;c.gridy=0;
				Board test = Board.standardBoard();
				BoardPanel pane = new BoardPanel (test);
				ControlPanel control = new ControlPanel (pane);
				pane.setControlPanel(control);
				frame.add(pane,c);
				c.gridx++;
				c.weightx = .2;
				frame.add(control,c);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.repaint();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			
		});
		
		
	}
	
	
	

}
