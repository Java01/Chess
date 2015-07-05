package opening;

import gameLogic.Board;
import gameLogic.Move;

public class Entry {
	
	private Board board;
	private Move move;
	
	public Entry(String fen, int from, int to) {
		this.board = Board.boardFromFEN(fen);
		this.move = new Move (from, to, board.getData()[from]);
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}


}
