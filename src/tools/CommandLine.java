package tools;

import gameLogic.AI;
import gameLogic.Board;
import gameLogic.IllegalMoveException;
import gameLogic.Move;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

import userInterface.BoardPanel;

public class CommandLine extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7107805443231267535L;
	
	BoardPanel pane;
	String last;
	
	public CommandLine (BoardPanel pane) {
		this.pane = pane;
		
		final JTextField text = new JTextField ();
		text.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					if (CommandLine.this.execute (text.getText())) {
						text.setText("");
					}
				}
				if (e.getKeyCode()==KeyEvent.VK_DOWN||e.getKeyCode()==KeyEvent.VK_UP) {
					text.setText(last);
				}
			}


			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		this.add(text);
		
		
		this.setLocation(800,300);
		this.setSize(300,50);
		this.setTitle("Command Line");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		
	}

	protected boolean execute(String input) {
		last = input;
		String text = input.toLowerCase();
		switch (text.charAt(0)) {
		case '?':
			String strip = text.substring(1, text.length());
			System.out.println(pane.getBoard().getData()[Board.indexFromPosition(strip)]);
			break;
		case ':':
			text = text.substring(1, text.length());
			if (text.equals("ep")) {
				System.out.println(Board.positionFromIndex(pane.getBoard().getEpSquare()).toString());
			}
			if (text.equals("halfmove")) {
				System.out.println(pane.getBoard().getHalfmove());
			}
			if (text.equals("move")) {
				System.out.println(pane.getBoard().getMove());
			}
			if (text.equals("selected")) {
				System.out.println(pane.getSelectedSquare().toString());
			}
			if (text.equals("best")) {
				System.out.println(AI.getBestMove(pane.getBoard()));
			}
			if (text.equals("legal")) {
				for (Move m: pane.getBoard().getLegalMoves(true)) {
					System.out.println(m.toString());
				}
			}
			if (text.equals("turn")) {
				String s = pane.getBoard().isWhiteMove()?"White":"Black";
				System.out.println(s);
			}
			if (text.equals("dobest")) {
				try {
					pane.getBoard().performMove(AI.getBestMove(pane.getBoard()), true);
				} catch (IllegalMoveException e) {
					System.out.println("Illegal move");
				}
			}
			if (text.equals("evaluate")) {
				System.out.println(pane.getBoard().getEvaluation());
			}
			if (text.equals("fen")) {
				System.out.println(pane.getBoard().getFEN());
			}
			break;
		case '$':
			String [] array = text.substring(1).split(" ");
			String key = array[0];
			String val = array[1];
			if (key.equals("bauto")) {
				pane.setBlackAuto(Boolean.parseBoolean(val));
			}
			if (key.equals("wauto")) {
				pane.setWhiteAuto(Boolean.parseBoolean(val));
			}
			break;
		default:
			String [] arr = text.split("-");
			Move move = new Move (Board.indexFromPosition(arr[0]), Board.indexFromPosition(arr[1]), pane.getBoard().getData()[Board.indexFromPosition(arr[0])]);
			if (pane.getBoard().getLegalMoves(true).contains(move)) {
				System.out.println(move.toString());
				try {
					pane.getBoard().performMove(move, true);
				} catch (IllegalMoveException e) {
					System.out.println("Illegal move");
				}
			} else {
				System.out.println("Rejected move: " + move.toString());
			}
			break;
		}
		
		pane.repaint();
		return true;
	}

}
