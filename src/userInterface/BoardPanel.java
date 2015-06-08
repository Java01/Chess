package userInterface;

import gameLogic.Board;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Renders a board in a JPanel. 
 * @author kevinshao
 *
 */
public class BoardPanel extends JPanel {
	
	/**
	 * The board to be rendered. 
	 */
	private Board board;
	
	/**
	 * The size of each square, in pixels. 
	 */
	public static final int SQUARE_SIZE = 75;
	
	public static final int PADDING = 15;
	
	public BoardPanel (Board board) {
		this.board = board;
	}
	
	@Override
	public void paint (Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		this.drawSquares (g);
		this.drawBoardBorder (g);
		this.drawPieces (g);
	}

	private void drawPieces(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	private void drawBoardBorder(Graphics2D g) {
		g.setStroke(new BasicStroke(3));
		g.drawRect(PADDING, PADDING, SQUARE_SIZE*8, SQUARE_SIZE*8);
	}

	private void drawSquares(Graphics2D g) {
		byte [][] arr2d = this.board.to2dArray();
		for (int i = 0 ; i < arr2d.length; i++) {
			for (int j = 0 ; j < arr2d[i].length; j++) {
				BufferedImage img = null;
				if ((i+j)%2==0) {
					img = ImageDatabase.blackSquare;
				} else {
					img = ImageDatabase.whiteSquare;
				}
				g.drawImage(img, j*SQUARE_SIZE+PADDING, (7-i)*SQUARE_SIZE+PADDING, SQUARE_SIZE, SQUARE_SIZE, this);
				
			}
		}
	}
	
}
