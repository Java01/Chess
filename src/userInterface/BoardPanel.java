package userInterface;

import gameLogic.AI;
import gameLogic.Board;
import gameLogic.IllegalMoveException;
import gameLogic.Move;
import gameLogic.Position;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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
	public void setBoard (Board board) {this.board = board;}
	public Board getBoard () { return board; }
	
	private boolean whiteAuto = false;
	private boolean blackAuto = true;
	
	private boolean compThinking = false;
	
	private ControlPanel ctrl;
	public void setControlPanel (ControlPanel ctrl) {
		this.ctrl = ctrl;
		this.refreshControlPanel();
	}
	public ControlPanel getControlPanel () {return ctrl;}
	
	/**
	 * This field will contain information regarding the piece, if any, that is 
	 * currently sliding on this BoardPanel. 
	 */
	private Transit transit = null;
	
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
				if (transit==null && !compThinking) {
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
								int from = Board.indexFromPosition(selectedSquare);
								int to = Board.indexFromPosition(p);
								Move m = new Move (
										Board.indexFromPosition(selectedSquare), 
										Board.indexFromPosition(p), 
										board.getData()[Board.indexFromPosition(selectedSquare)]);
								if (board.getLegalMoves(true).contains(m)) {
									transit = new Transit (from, to, board.getData()[from], board.getData()[to]);
									boolean moveExecuted = true;
									try {
										board.performMove(m, true);
									} catch (IllegalMoveException e) {
										moveExecuted = false;
									}
									if (moveExecuted) {
										for (int i = 0; i < transit.getTotal(); i++) {
											transit.next ();
											BoardPanel.this.repaint ();
											try {
												Thread.sleep(transit.getSleepTime());
											} catch (InterruptedException e1) {
												e1.printStackTrace();
											}
										}
									} else {
										transit = null;
									}
									transit = null;
									BoardPanel.this.repaint();
									BoardPanel.this.checkAuto();
								}
								selectedSquare = null;
							}
							BoardPanel.this.repaint();
						}
					}).start();
				}
				
				
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
		this.setVisible(true);
		this.validate();
		int dimension = PADDING*2 + SQUARE_SIZE*8;
		this.setPreferredSize(new Dimension (dimension, dimension));
	}
	
	/**
	 * Checks whether an automatic move is called for. 
	 */
	public void checkAuto () {
		boolean turn = board.isWhiteMove();
		if ((whiteAuto&&turn)||(blackAuto&&!turn)) {
			compThinking = true;
			Move m = AI.getBestMove(board);
			int from = m.getFrom().toIndex();
			int to = m.getTo().toIndex();
			transit = new Transit (from, to, board.getData()[from], board.getData()[to]);
			boolean moveExecuted = true;
			try {
				board.performMove(m, true);
			} catch (IllegalMoveException e) {
				moveExecuted = false;
			}
			if (moveExecuted) {
				for (int i = 0; i < transit.getTotal(); i++) {
					transit.next ();
					BoardPanel.this.repaint ();
					try {
						Thread.sleep (transit.getSleepTime());
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} else {
				transit = null;
				System.out.println("Computer decided on an illegal move. ");
			}
			transit = null;
			this.repaint();
			compThinking = false;
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
				byte val = arr2d[i][j];
				BufferedImage img = null;
				if (transit!=null && transit.getFrom() == (Board.indexFromPosition(i+1, j+1))) {
					draw = false;
				}
				if (transit != null && transit.getTo() == Board.indexFromPosition(i+1, j+1)) {
					draw = true;
					val = (byte) transit.getPieceTo();
				}
				
				if (val==-1) {
					draw = false;
				} else {
					img = ImageDatabase.pieceFromNumber (val);
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
		
		//Draw the piece in transit
		if (transit!=null) {
			BufferedImage img = ImageDatabase.pieceFromNumber((byte) transit.getPieceFrom());
			int squareX1 = Board.positionFromIndex(transit.getFrom()).getColumn()-1;
			int x1 = squareX1 * SQUARE_SIZE + PADDING + PIECE_PADDING;
			int squareY1 = Board.positionFromIndex(transit.getFrom()).getRow()-1;
			int y1 = (7-squareY1) * SQUARE_SIZE + PADDING + PIECE_PADDING;
			int squareX2 = Board.positionFromIndex(transit.getTo()).getColumn()-1;
			int x2 = squareX2 * SQUARE_SIZE + PADDING + PIECE_PADDING;
			int squareY2 = Board.positionFromIndex(transit.getTo()).getRow()-1;
			int y2 = (7-squareY2) * SQUARE_SIZE + PADDING + PIECE_PADDING;
			int finalX = (int) (x1 + ((double)(x2-x1) * ((double) transit.getStep()/ (double) transit.getTotal())));
			int finalY = (int) (y1 + ((double)(y2-y1) * ((double) transit.getStep()/(double)transit.getTotal())));
			g.drawImage(img, finalX, finalY, SQUARE_SIZE-(2*PIECE_PADDING), SQUARE_SIZE-(2*PIECE_PADDING), this);
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

	
	public void refreshControlPanel() {
		this.whiteAuto = ctrl.getWhiteAuto();
		this.blackAuto = ctrl.getBlackAuto();
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
