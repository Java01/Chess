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
		for (int i = 0 ; i < data.length; i++) { //Initializes board to array of 0s
			data [i] = 0;
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
}
