package gameLogic;

import ai.IMove;


/**
 * A class representing a chess move. 
 * @author kevinshao
 *
 */
public class Move implements IMove {
	
	private int from, to;
	/**
	 * The piece making the move. 
	 */
	private int piece;
	
	
	public int getPiece() {
		return piece;
	}

	public void setPiece(int piece) {
		this.piece = piece;
	}

	public Move (int fromX, int fromY, int toX, int toY, int piece) {
		this (
			Board.indexFromPosition(fromX, fromY), 
			Board.indexFromPosition(toX, toY), 
			piece
			);
	}
	
	public Move (String from, String to, int piece) {
		this(Board.indexFromPosition(from), Board.indexFromPosition(to), piece);
	}
	
	public Move (int from, int to, int piece) {
		this.from = from;
		this.to = to;
		this.piece = piece;
	}
	
	public int getFrom () {
		return from;
	}
	
	public int getTo () {
		return to;
	}
	
	@Override
	public boolean equals (Object other) {
		Move m = (Move) other;
		return this.from == m.from && this.to == m.to;
	}
	
	@Override
	public String toString () {
		return Board.positionFromIndex(from).toString()+"-"+Board.positionFromIndex(to).toString();
	}

}
