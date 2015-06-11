package gameLogic;

public class Board {

	/**
	 * The data array. Element at index 21 corresponds to A1 square, 
	 * element at 98 corresponds to H8 square. 
	 * Any number ending in 0 is dummy padding on the left, 
	 * any number ending in 9 is dummy padding on the right. 
	 * Two dummy rows at both top and bottom. 
	 * 0: King
	 * 1: Queen
	 * 2: Rook
	 * 3: Bishop
	 * 4: Knight
	 * 5: Pawn
	 * Add 6 for black pieces, none for white
	 */
	private byte [] data = new byte [120]; 
	
	public Board () {
		
	}
	
	
	/**
	 * Initializes a standard board in the beginning position. 
	 * @return A fully initialized board object. 
	 */
	public static Board standardBoard () {
		Board board = new Board ();
		byte [] data = board.data;
		for (int i = 0 ; i < data.length; i++) { //Initializes board to array of -1s
			data [i] = -1;
		}
		//Initializing white pieces
		data [21] = 2;
		data [22] = 4;
		data [23] = 3;
		data [24] = 1;
		data [25] = 0;
		data [26] = 3;
		data [27] = 4;
		data [28] = 2;
		for (int i = 31; i < 39; i++) {
			data [i] = 5;
		}
		
		//Initializing black pieces
		data [91] = 2+6;
		data [92] = 4+6;
		data [93] = 3+6;
		data [94] = 1+6;
		data [95] = 0+6;
		data [96] = 3+6;
		data [97] = 4+6;
		data [98] = 2+6;
		for (int i = 81; i < 89; i++) {
			data [i] = 5+6;
		}
		
		return board;
	}
	
	/**
	 * Returns a 2D byte array representation of the board. 
	 * @return
	 */
	public byte [][] to2dArray () {
		byte [][] toReturn = new byte [8][8];
		int index1 = 0, index2 = 0;
		for (int i = 0 ; i < data.length; i++) {
			if (this.inBoard(i)) {
				toReturn [index1][index2] = data[i];
				if (index2!=7) {
					index2++;
				} else {
					index2=0;
					index1++;
				}
			}
			
		}
		return toReturn;
	}
	
	/**
	 * Returns whether or not the given index is a valid square in the array. 
	 * @param index The index to be tested. 
	 * @return True if the index points to a square on the playable board, 
	 * false if the index points to a dummy square. 
	 */
	public boolean inBoard (int index) {
		return (index>20 && index<99 && index%10!=0 && index%10!=9);
	}
	
	
	/**
	 * Performs a given move given two array indexes. 
	 * NOTE: This method does NOT check whether the move is LEGAL or not!
	 * @param from The array index of the square the piece should move from.
	 * @param to The array index of the square the piece should move to. 
	 */
	public void performMove (int from, int to) {
		data [to] = data [from];
		data [from] = -1;
	}
	
	/**
	 * Takes a position and returns the array index. 
	 * @param position The given position, in the form of a string. EG: E4, F5, etc. Not case sensitive. 
	 * @return The array index of the given position. 
	 */
	public static int indexFromPosition (String position) {
		position = position.toLowerCase();
		int tens = Integer.parseInt(Character.toString(position.charAt(1)))+1;
		int ones = numberFromLetter(position.charAt(0));
		return 10*tens+ones;
	}
	
	/**
	 * Takes a position and returns the array index. 
	 * @param row The row of the given position as an integer. 
	 * @param column The column of the given position as an integer. 
	 * @return The array index of the specified position. 
	 */
	public static int indexFromPosition (int row, int column) {
		return 10*row+column;
	}
	
	/**
	 * Returns the column number associated with a letter. 
	 * @param letter The letter of the column we are interested in. 
	 * @return The number, starting at 1 for A, of the column we are interested in. 
	 */
	public static int numberFromLetter (char letter) {
		switch (letter) {
		case 'a':
			return 1;
		case 'b':
			return 2;
		case 'c':
			return 3;
		case 'd':
			return 4;
		case 'e':
			return 5;
		case 'f':
			return 6;
		case 'g':
			return 7;
		case 'h':
			return 8;
		default: return 0;
		}
	}
	
	/**
	 * Takes an index of the data array and returns a corresponding position object. 
	 * @param index The given index. 
	 * @return The returned position. 
	 */
	public static Position positionFromIndex (int index) {
		int row = (index/10)-1;
		int column = index%10;
		return new Position (row, column);
	}
}
