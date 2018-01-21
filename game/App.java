package game;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class App {
	public static void main(String[] args) {
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		Game TicTacToe = new Game(x, y);
		try {
			SwingUtilities.invokeAndWait(() -> new GameUI(TicTacToe));
		} catch (InvocationTargetException | InterruptedException e) {

		}
		switch (args[2]) {
		case "2":
			Computer comp1 = new Computer(TicTacToe, "X");
			TicTacToe.addObserver(comp1);
		case "1":
			Computer comp2 = new Computer(TicTacToe, "O");
			TicTacToe.addObserver(comp2);
		}
		TicTacToe.startGame();

	}
}
