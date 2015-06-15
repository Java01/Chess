package gameLogic;

public class Move {
	
	private int from, to;
	
	public Move (int fromX, int fromY, int toX, int toY) {
		this (
			Board.indexFromPosition(fromX, fromY), 
			Board.indexFromPosition(toX, toY)
			);
	}
	
	public Move (String from, String to) {
		this(Board.indexFromPosition(from), Board.indexFromPosition(to));
	}
	
	public Move (int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	public Position getFrom () {
		return Board.positionFromIndex(from);
	}
	
	public Position getTo () {
		return Board.positionFromIndex(to);
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
