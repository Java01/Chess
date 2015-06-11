package tools;

import gameLogic.Board;

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

	protected boolean execute(String text) {
		String [] arr = text.split("-");
		pane.getBoard().performMove(Board.indexFromPosition(arr[0]), Board.indexFromPosition(arr[1]));
		
		pane.repaint();
		return true;
	}

}
