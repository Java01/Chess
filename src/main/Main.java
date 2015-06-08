package main;

import gameLogic.Board;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import userInterface.BoardPanel;
import userInterface.ImageDatabase;


public class Main {
	
	
	public static void main (String [] args) {
		try {
			ImageDatabase.initImages();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Failed to load images.");
		}
		
		
		
		JFrame frame = new JFrame ("Test");
		Board test = Board.standardBoard();
		BoardPanel pane = new BoardPanel (test);
		frame.add(pane);
		frame.setSize(1280, 720);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	

}
