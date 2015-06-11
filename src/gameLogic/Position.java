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

}
