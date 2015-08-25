package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import databases.Database;
import databases.ImageDatabase;
import opening.Book;
import userInterface.GameFrame;


public class Main {
	
	public static List<GameFrame> windows = new ArrayList<GameFrame> ();
	
	public static void main (String [] args) {
		//TODO Import/export pgn, disambiguate moves
		//TODO KeyEvent focus
		SwingUtilities.invokeLater (new Runnable () {
			
			public void run() {
				try {
					ImageDatabase.initImages();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Failed to load images.");
					e.printStackTrace();
				}
				Database.initDatabase();

				boolean database = true;
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
				} catch (Exception e) {
					database = false;
				}
				try {
					Book.initBook(database);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Failed to load opening book.");
					e.printStackTrace();
				}
				

				new GameFrame ("Chess");
				
				
			}


			
		});
		
		
	}
	
	
	

}
