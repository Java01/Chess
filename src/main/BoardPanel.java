package main;

import gameLogic.Board;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Renders a board in a JPanel. 
 * @author kevinshao
 *
 */
public class BoardPanel extends JPanel {
	
	/**
	 * The board to be rendered
	 */
	private Board board;
	
	public BoardPanel (Board board) {
		this.board = board;
	}
	
	@Override
	public void paint (Graphics g) {
		
	}
	
}
