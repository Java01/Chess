package ai;

/**
 * A move on the board, with a piece moving from a square, to a square. 
 * @author kevinshao
 *
 */
public interface IMove {
	
	/**
	 * Returns an integer representation of the move's destination. 
	 * @return The destination of the move. 
	 */
	int getTo();
	int getFrom();
	int getPiece();

}
