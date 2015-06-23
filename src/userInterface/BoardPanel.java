package userInterface;

import gameLogic.AI;
import gameLogic.Board;
import gameLogic.Move;
import gameLogic.Position;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import tools.CommandLine;
import databases.ImageDatabase;

/**
 * Renders a board in a JPanel. 
 * @author kevinshao
 *
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
	
	/**
	 * The board to be rendered. 
	 */
	private Board board;
	public Board getBoard () { return board; }
	
	
	private boolean whiteAuto = true;
	private boolean blackAuto = true;
	
	/**
	 * The size of each square, in pixels. 
	 */
	public static final int SQUARE_SIZE = 75;
	
	/**
	 * Padding from edge of the panel. 
	 */
	public static final int PADDING = 15;
	
	/**
	 * Padding between each piece and the edge of their square. 
	 */
	public static final int PIECE_PADDING = 8;
	
	/**
	 * A position object representing the currently selected square. 
	 * Null if no square is selected. 
	 */
	private Position selectedSquare = null;
	
	public BoardPanel (final Board board) {
		this.board = board;
		this.setFocusable(true);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar()=='t') {
					new CommandLine(BoardPanel.this);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				new Thread (new Runnable () {
					@Override
					public void run () {
						int mouseX = e.getX();
						int mouseY = e.getY();
						
						mouseX -= PADDING;
						mouseY -= PADDING;
						
						Position p = new Position (8-mouseY/SQUARE_SIZE, mouseX/SQUARE_SIZE+1);

						if (selectedSquare == null) {
							byte piece = board.getData()[Board.indexFromPosition(p)];
							if (piece!=-1 && (board.isWhite(piece)==board.isWhiteMove())) {
								selectedSquare = p;
							}
						} else {
							Move m = new Move (
									Board.indexFromPosition(selectedSquare), 
									Board.indexFromPosition(p), 
									board.getData()[Board.indexFromPosition(selectedSquare)]);
							if (board.getLegalMoves().contains(m)) {
								board.performMove(m);
								BoardPanel.this.repaint();
								BoardPanel.this.checkAuto();
							}
							selectedSquare = null;
						}
						BoardPanel.this.repaint();
					}
				}).start();
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		this.repaint();
	}
	
	/**
	 * Checks whether an automatic move is called for. 
	 */
	public void checkAuto () {
		boolean turn = board.isWhiteMove();
		if ((whiteAuto&&turn)||(blackAuto&&!turn)) {
			board.performMove(AI.getBestMove(board));
			this.repaint();
			checkAuto ();
		}
		
	}
	
	@Override
	public void paint (Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		this.drawSquares (g);
		this.drawBoardBorder (g);
		this.drawPieces (g);
	}

	private void drawPieces(Graphics2D g) {
		boolean draw = true;
		byte [][] arr2d = this.board.to2dArray();
		for (int i = 0 ; i < arr2d.length; i++) {
			for (int j = 0 ; j < arr2d[i].length; j++) {
				draw = true;
				BufferedImage img = null;
				switch (arr2d[i][j]) {
				case -1: draw = false;
					break;
				case 0: img = ImageDatabase.pieces.get("wk");
					break;
				case 1: img = ImageDatabase.pieces.get("wq");
					break;
				case 2: img = ImageDatabase.pieces.get("wr");
					break;
				case 3: img = ImageDatabase.pieces.get("wb");
					break;
				case 4: img = ImageDatabase.pieces.get("wn");
					break;
				case 5: img = ImageDatabase.pieces.get("wp");
					break;
				case 6: img = ImageDatabase.pieces.get("bk");
					break;
				case 7: img = ImageDatabase.pieces.get("bq");
					break;
				case 8: img = ImageDatabase.pieces.get("br");
					break;
				case 9: img = ImageDatabase.pieces.get("bb");
					break;
				case 10: img = ImageDatabase.pieces.get("bn");
					break;
				case 11: img = ImageDatabase.pieces.get("bp");
					break;
				}
				if (draw) {
					g.drawImage(img, j*SQUARE_SIZE+PADDING+PIECE_PADDING, 
									(7-i)*SQUARE_SIZE+PADDING+PIECE_PADDING, 
									SQUARE_SIZE-(2*PIECE_PADDING), 
									SQUARE_SIZE-(2*PIECE_PADDING), 
									this);
				}
				
			}
		}
	}

	private void drawBoardBorder(Graphics2D g) {
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.BLACK);
		g.drawRect(PADDING, PADDING, SQUARE_SIZE*8, SQUARE_SIZE*8);
	}

	private void drawSquares(Graphics2D g) {
		byte [][] arr2d = this.board.to2dArray();
		for (int i = 0 ; i < arr2d.length; i++) {
			for (int j = 0 ; j < arr2d[i].length; j++) {
				BufferedImage img = ((i+j)%2==0)?ImageDatabase.blackSquare:ImageDatabase.whiteSquare;
				g.drawImage(img, j*SQUARE_SIZE+PADDING, (7-i)*SQUARE_SIZE+PADDING, SQUARE_SIZE, SQUARE_SIZE, this);
			}
		}
		if (selectedSquare!=null) {
			int row = selectedSquare.getRow();
			int column = selectedSquare.getColumn();
			g.setColor(Color.yellow);
			g.fillRect((column-1)*SQUARE_SIZE+PADDING, (8-row)*SQUARE_SIZE+PADDING, SQUARE_SIZE, SQUARE_SIZE);
		}

	}

	public Position getSelectedSquare() {
		return selectedSquare;
	}

	public void setSelectedSquare(Position selectedSquare) {
		this.selectedSquare = selectedSquare;
	}

	public boolean isWhiteAuto() {
		return whiteAuto;
	}

	public void setWhiteAuto(boolean whiteAuto) {
		this.whiteAuto = whiteAuto;
	}

	public boolean isBlackAuto() {
		return blackAuto;
	}

	public void setBlackAuto(boolean blackAuto) {
		this.blackAuto = blackAuto;
	}
	

}
