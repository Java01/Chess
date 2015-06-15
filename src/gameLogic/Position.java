package gameLogic;


/**
 * Simple wrapper class for row and column position. 
 * @author kevinshao
 *
 */
public class Position {
	
	private int row, column;
	
	public int getRow () {
		return row;
	}
	public int getColumn () {
		return column;
	}
	
	public Position (int r, int c) {
		row = r;
		column = c;
	}
	
	/**
	 * Returns the index corresponding to this position. 
	 * @return The index. 
	 */
	public int toIndex () {
		return 10*row+column+10;
	}
	
	@Override
	public String toString () {
		return Character.toString(Board.letterFromNumber(column))+row;
	}

}
