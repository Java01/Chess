package gameLogic;

import opening.Book;


public class Computer {
	
	public static Move getBestMove (Board board) {
		Move m = Book.getMove (board);
		if (m!=null) {
			return m;
		} else {
			return (Move) ai.AI.getBestMove(board);
		}
	}

}
