package game;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;

import javax.swing.SwingUtilities;

public class Game extends Observable {
	private int winCount;
	private String status;

	// X = 1 , O = 2
	protected int board[][];

	// false = "X" , true = "O"
	public boolean turn = false;

	/**
	 * create the game with width x and height y board
	 */
	public Game(int x, int y) {
		board = new int[x][y];
		initBoard();
		status = "Playing";
		if (x > y)
			winCount = y;
		else
			winCount = x;
		System.out.println("Game is created");
	}

	/**
	 * initialize the board value.
	 */
	private void initBoard() {
		// add value to all board = 0.
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = 0;
			}
		}
	}

	public void startGame() {
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * place the X or O
	 * 
	 * @param x
	 *            width start with 0
	 * @param y
	 *            height start with 0
	 */
	public void turnIn(int x, int y) {
		if (board[x][y] != 0) {
			return;
		}
		if (turn) {
			board[x][y] = 2;
		} else {
			board[x][y] = 1;
		}
		turn = !turn;
		this.status = checker();

		// try {
		// SwingUtilities.invokeAndWait(() ->);
		// } catch (InvocationTargetException | InterruptedException e) {
		//
		// }
		GameUI.updateUI(x, y);
		this.setChanged();
		this.notifyObservers(new Integer[] { x, y });
	}

	/**
	 * place the X or O
	 * 
	 * @param x
	 *            width start with 0
	 * @param y
	 *            height start with 0
	 */
	public void turnIn(int x, int y, int value) {
		if (board[x][y] != 0) {
			return;
		}
		board[x][y] = value;
		this.status = checker();
	}

	public void turnOut(int x, int y) {
		board[x][y] = 0;
		this.status = checker();
	}

	private String checker() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != 0) {
					String label = checker(board[i][j], i, j);
					if (label.equals("X win") || label.equals("O win")) {
						return label;
					}
				}
			}
		}
		if (getCounter() == board.length * board[0].length) {
			return "Tile";
		}
		return "Playing";
	}

	public int getCounter() {
		int count = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != 0) {
					count++;
				}
			}
		}
		return count;
	}

	private String checker(int marker, int x, int y) {
		if (marker == 1) {
			for (int i = 0; i < 4; i++) {
				if (checker(marker, x, y, 0, i)) {
					return "X win";
				}
			}
		} else if (marker == 2) {
			for (int i = 0; i < 4; i++) {
				if (checker(marker, x, y, 0, i)) {
					return "O win";
				}
			}
		}
		return "Playing";
	}

	private boolean checker(int marker, int x, int y, int level, int direction) {
		try {
			if (level == winCount) {
				return true;
			}
			if (board[x][y] == marker) {
				switch (direction) {
				case 0: // go right
					return checker(marker, x + 1, y, level + 1, direction);
				case 1: // go down
					return checker(marker, x, y + 1, level + 1, direction);
				case 2: // go left-down
					return checker(marker, x + 1, y + 1, level + 1, direction);
				case 3: // go right-down
					return checker(marker, x + 1, y - 1, level + 1, direction);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}

	/**
	 * get the value if this game is playing or not.
	 */
	public String status() {
		return status;
	}

	public void setTurn(char x) {
		if (x == 'X') {
			turn = false;
		} else {
			turn = true;
		}
	}

	/**
	 * get the current turn
	 * 
	 * @return current turn X or O
	 */
	public String getTurn() {
		if (!this.status.equals("Playing")) {
			return " ";
		}
		if (turn)
			return "O Turn";
		return "X Turn";
	}

	public int[][] getBoard() {
		return this.board;
	}

	public boolean lastTurn() {
		return this.getCounter() == (this.board.length * this.board[0].length) - 2;
	}

	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
}
