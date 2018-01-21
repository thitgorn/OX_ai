package game;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Cell extends JPanel {
	public int x;
	public int y;
	private JLabel text = new JLabel();
	private GameUI gameUI;

	public Cell(int x, int y, GameUI gameui) {
		this.setBackground(Color.WHITE);
		this.gameUI = gameui;
		this.setLayout(new GridLayout(3, 1));
		this.x = x;
		this.y = y;
		this.add(new JLabel());
		this.text.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(text);
		this.add(new JLabel());

		this.addMouseListener(new Mouse());
	}

	public void setText(String text) {
		this.text.setText(text);
	}

	public void updateCell() {
		if (gameUI.game.getBoard()[x][y] == 1) {
			setText("X");
		} else {
			setText("O");
		}
		gameUI.updateStatus();
	}

	private class Mouse implements MouseListener {

		Color DEFAULT_COLOR;

		@Override
		public void mouseClicked(MouseEvent e) {
			if (gameUI.game.status() == ("Playing")) {
				gameUI.game.turnIn(x, y);
				updateCell();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			DEFAULT_COLOR = getBackground();
			setBackground(Color.LIGHT_GRAY);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			setBackground(DEFAULT_COLOR);
		}

	}
}
