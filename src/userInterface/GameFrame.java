package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import gameLogic.Board;
import main.Main;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {

	public GameFrame(String title) {
		super (title);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = .8;
		c.gridx = 0;
		c.gridy = 0;
		Board test = Board.standardBoard();
		BoardPanel pane = new BoardPanel(test);
		ControlPanel control = new ControlPanel(pane);
		pane.setControlPanel(control);
		this.add(pane, c);
		c.gridx++;
		c.weightx = .2;
		this.add(control, c);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.requestFocus();
		this.repaint();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Main.windows.add(this);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				Main.windows.remove(this);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				
			}
			
		});

	}

}
