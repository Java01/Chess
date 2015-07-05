package userInterface;

import gameLogic.Board;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	
	BoardPanel board;
	JComboBox whiteAuto, blackAuto;
	JLabel whiteAutoLbl, blackAutoLbl;
	JButton newGame;
	
	public ControlPanel (final BoardPanel board) {
		this.board = board;
		String [] yesno = {"Yes", "No"};
		whiteAuto = new JComboBox (yesno);
		whiteAuto.setSelectedIndex(1);
		whiteAuto.addActionListener(new Refresher());
		blackAuto = new JComboBox (yesno);
		blackAuto.setSelectedIndex(1);
		blackAuto.addActionListener(new Refresher());
		whiteAutoLbl = new JLabel ("Automatic white move:");
		blackAutoLbl = new JLabel ("Automatic black move:");
		newGame = new JButton ("New Game");
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				board.setBoard(Board.standardBoard());
				ControlPanel.this.repaint();
				board.repaint();
			}
		});
		
		
		this.setLayout(new GridLayout(0,1));
		this.add(whiteAutoLbl);
		this.add(whiteAuto);
		this.add(blackAutoLbl);
		this.add(blackAuto);
		this.add(newGame);
	}
	
	public boolean getWhiteAuto () {
		return whiteAuto.getSelectedItem().equals("Yes");
	}
	public boolean getBlackAuto () {
		return blackAuto.getSelectedItem().equals("Yes");
	}

	private class Refresher implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ControlPanel.this.board.refreshControlPanel ();
		}
		
	}
}
