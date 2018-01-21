package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Computer implements Observer {
	private String bot_turn;
	private Game game;
	private int DEFAULT_input;
	private int DEFAULT_HUMAN_MOVE;
	private Map<Integer[], Integer> score;

	public Computer(Game game, String turn) {
		this.game = game;
		bot_turn = turn;
		if (turn.equals("X")) {
			DEFAULT_input = 1;
			DEFAULT_HUMAN_MOVE = 2;
		} else {
			DEFAULT_input = 2;
			DEFAULT_HUMAN_MOVE = 1;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (game.getTurn().equals(bot_turn + " Turn")) {
			System.out.println(bot_turn + " turn");
			score = new HashMap<>();

			Game clone1 = new Game(game.getBoard().length, game.getBoard()[0].length);
			// clone the frist board
			for (int i = 0; i < game.getBoard().length; i++) {
				for (int j = 0; j < game.getBoard()[i].length; j++) {
					clone1.board[i][j] = game.getBoard()[i][j];
				}
			}

			Game clone2 = new Game(game.getBoard().length, game.getBoard()[0].length);
			// clone the frist board
			for (int i = 0; i < game.getBoard().length; i++) {
				for (int j = 0; j < game.getBoard()[i].length; j++) {
					clone2.board[i][j] = game.getBoard()[i][j];
				}
			}

			Game clone3 = new Game(game.getBoard().length, game.getBoard()[0].length);
			// clone the frist board
			for (int i = 0; i < game.getBoard().length; i++) {
				for (int j = 0; j < game.getBoard()[i].length; j++) {
					clone3.board[i][j] = game.getBoard()[i][j];
				}
			}

			Game clone4 = new Game(game.getBoard().length, game.getBoard()[0].length);
			// clone the frist board
			for (int i = 0; i < game.getBoard().length; i++) {
				for (int j = 0; j < game.getBoard()[i].length; j++) {
					clone4.board[i][j] = game.getBoard()[i][j];
				}
			}

			// create possible map with score
			for (int i = 0; i < game.getBoard().length; i++) {
				for (int j = 0; j < game.getBoard()[i].length; j++) {
					if (game.getBoard()[i][j] != 0)
						continue;
					score.put(new Integer[] { i, j }, 0);
				}
			}

			Integer[] in = null;

			Game game = clone1;
			if (game.lastTurn()) {
				Integer[] first = null;
				Integer[] second = null;
				for (Integer[] x : score.keySet()) {
					if (first == null && second == null) {
						first = x;
					} else {
						second = x;
					}
				}

				game.turnIn(first[0], first[1], DEFAULT_input);
				game.turnIn(second[0], second[1], DEFAULT_HUMAN_MOVE);
				score.put(first, checkScore(game));
				game.turnOut(first[0], first[1]);
				game.turnOut(second[0], second[1]);

				if (second != null) {
					game.turnIn(second[0], second[1], DEFAULT_input);
					game.turnIn(first[0], first[1], DEFAULT_HUMAN_MOVE);
					score.put(second, checkScore(game));
					game.turnOut(second[0], second[1]);
					game.turnOut(first[0], first[1]);
				}
				if (score.get(first) > score.get(second)) {
					in = first;
				} else {
					in = second;
				}

			} else {
				// calculate 4 thread in a time;
				in = botTurnIn(clone1, clone2, clone3, clone4);
				// in = botTurnIn(game);
			}
			if (in != null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (Integer[] x : score.keySet()) {
					System.out.println("[" + x[0] + "," + x[1] + "] = " + score.get(x));
				}
				this.game.printBoard();
				this.game.turnIn(in[0].intValue(), in[1].intValue());
			} else {
			}
		}
	}

	private Integer[] botTurnIn(Game game) {
		Integer[] turn = null;
		for (Integer[] x : score.keySet()) {
			int i = x[0].intValue();
			int j = x[1].intValue();
			// game.turnIn(i, j, DEFAULT_input);
			score.put(x, scoreBoard(game, i, j, true));
			// game.getBoard()[i][j] = 0;
			turn = x;
		}
		for (Integer[] x : score.keySet()) {
			if (score.get(x) > score.get(turn)) {
				turn = x;
			}
		}
		return turn;
	}

	// calculate 4 thread in a time.
	private Integer[] botTurnIn(Game game1, Game game2, Game game3, Game game4) {
		Integer[] turn = null;

		ArrayList<Integer[]> first_coor = new ArrayList<>();
		ArrayList<Integer[]> second_coor = new ArrayList<>();
		ArrayList<Integer[]> third_coor = new ArrayList<>();
		ArrayList<Integer[]> fourth_coor = new ArrayList<>();

		int input = 1;
		for (Integer[] x : score.keySet()) {
			if (input > 4)
				input = 1;
			switch (input) {
			case 1:
				first_coor.add(x);
				break;
			case 2:
				second_coor.add(x);
				break;
			case 3:
				third_coor.add(x);
				break;
			case 4:
				fourth_coor.add(x);
				break;
			}
			input++;
			turn = x;
		}

		Thread th1 = new Thread(() -> {
			for (int i = 0; i < first_coor.size(); i++) {
				int x = first_coor.get(i)[0];
				int y = first_coor.get(i)[1];
				score.put(first_coor.get(i), scoreBoard(game1, x, y, true));
			}
		});

		Thread th2 = new Thread(() -> {
			for (int i = 0; i < second_coor.size(); i++) {
				int x = second_coor.get(i)[0];
				int y = second_coor.get(i)[1];
				score.put(second_coor.get(i), scoreBoard(game2, x, y, true));
			}
		});

		Thread th3 = new Thread(() -> {
			for (int i = 0; i < third_coor.size(); i++) {
				int x = third_coor.get(i)[0];
				int y = third_coor.get(i)[1];
				score.put(third_coor.get(i), scoreBoard(game3, x, y, true));
			}
		});

		Thread th4 = new Thread(() -> {
			for (int i = 0; i < fourth_coor.size(); i++) {
				int x = fourth_coor.get(i)[0];
				int y = fourth_coor.get(i)[1];
				score.put(fourth_coor.get(i), scoreBoard(game4, x, y, true));
			}
		});

		th1.start();
		th2.start();
		th3.start();
		th4.start();

		try {
			th1.join();
			th2.join();
			th3.join();
			th4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (Integer[] x : score.keySet()) {
			if (score.get(x) > score.get(turn)) {
				turn = x;
			}
		}
		return turn;
	}

	private int checkScore(Game game) {
		if (DEFAULT_input == 1) {
			switch (game.status()) {
			case "Tile":
				return 0;
			case "X win":
				return 1;
			case "O win":
				return -1;
			}
		} else {
			switch (game.status()) {
			case "Tile":
				return 0;
			case "X win":
				return -1;
			case "O win":
				return 1;
			}
		}
		return 0;
	}

	private int scoreBoard(Game game, int x, int y, boolean botTurn) {
		// count when the game end;
		if (!game.status().equals("Playing")) {
			return checkScore(game);
		}

		// bot Place
		if (botTurn) {
			game.turnIn(x, y, DEFAULT_input);
		}

		// human place
		else {
			game.turnIn(x, y, DEFAULT_HUMAN_MOVE);
		}

		int score = 0;
		// try to place another
		for (int i = 0; i < game.getBoard().length; i++) {
			for (int j = 0; j < game.getBoard()[i].length; j++) {
				if (game.getBoard()[i][j] != 0)
					continue;
				int temp = scoreBoard(game, i, j, !botTurn);
				score += temp;
			}
		}

		game.turnOut(x, y);
		return score;
	}
}
