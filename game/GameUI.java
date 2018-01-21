package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameUI extends JFrame {
	protected Game game;
	private JLabel status;
	private static Cell[][] cells;

	public GameUI(Game game) {
		this.game = game;
		this.status = new JLabel(game.getTurn());

		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponent();

		this.setVisible(true);
		this.pack();
	}

	private void initComponent() {
		JPanel statusBar = new JPanel();
		statusBar.add(status);

		cells = new Cell[game.getBoard().length][game.getBoard()[0].length];
		JPanel table = new JPanel(new GridLayout(game.getBoard().length, game.getBoard()[0].length));
		createTable(table);

		this.add(table);
		this.add(statusBar);

		int reply = JOptionPane.showConfirmDialog(null, "Who play first?\n Yes : X , No : O", "",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			game.setTurn('X');
		} else {
			game.setTurn('O');
		}
		this.status.setText(game.getTurn());
	}

	public static void updateUI(int x , int y) {
		cells[x][y].updateCell();
	}

	private void createTable(JPanel table) {
		for (int i = 0; i < game.getBoard().length; i++) {
			for (int j = 0; j < game.getBoard()[i].length; j++) {
				Cell temp = new Cell(i, j, this);
				temp.setPreferredSize(new Dimension(100, 100));
				temp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				table.add(temp);
				cells[i][j] = temp;
			}
		}
	}

	public void updateStatus() {
		if (this.game.status() == ("Playing"))
			this.status.setText(game.getTurn());
		else
			this.status.setText(this.game.status());
	}
}
