package gameLogic;

import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * True if it's white's turn to move, false if it's black. 
	 */
	private boolean whiteMove = true;
	
	/**
	 * A number representing castling rights. 
	 * 1 - WHITE KINGSIDE
	 * 2 - WHITE QUEENSIDE
	 * 4 - BLACK KINGSIDE
	 * 8 - BLACK QUEENSIDE
	 */
	private byte castlingRights = 15;

	/**
	 * Empty constructor. This class uses the factory pattern. 
	 */
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
	 * @return An 8*8 2D byte array. 
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
	 * Performs a given move given a Move object. 
	 * NOTE: This method does NOT check whether the move is LEGAL or not!
	 * @param move The Move object. 
	 */
	public void performMove (Move move) {
		this.performMove (move.getFrom().toIndex(),move.getTo().toIndex());
	}
	
	
	/**
	 * Returns all legal moves. 
	 * @return A List of Move's, all of which are legal 
	 * to be played in the current position. 
	 */
	public List <Move> getLegalMoves () {
		List <Move> moves = new ArrayList <Move> ();
		for (int i = 0 ; i < data.length; i++) {
			byte piece = data [i];
			if ( (!inBoard(i)) && piece!=-1) {
				System.err.println("Piece in out-of-bounds area!");
			}
			if ((!isWhite(piece)&&whiteMove)||(isWhite(piece)&&!whiteMove)) {
				continue;
			}
			switch (piece) {
			case -1:break;
			case 0: case 6: 
				int [] surrounding = {-11, -10, -9, -1, 1, 9, 10, 11};
				for (int square: surrounding) {
					if (this.inBoard(i+square)) {
						moves.add(new Move(i, i+square));
					}
				}
				break;
			case 1: case 7: 
				new Thread (i, 1, this, moves).execute();
				new Thread (i, 10, this, moves).execute();
				new Thread (i, -1, this, moves).execute();
				new Thread (i, -10, this, moves).execute();
				new Thread (i, 11, this, moves).execute();
				new Thread (i, 9, this, moves).execute();
				new Thread (i, -11, this, moves).execute();
				new Thread (i, -9, this, moves).execute();
				break;
			case 2: case 8: 
				new Thread (i, 1, this, moves).execute();
				new Thread (i, 10, this, moves).execute();
				new Thread (i, -1, this, moves).execute();
				new Thread (i, -10, this, moves).execute();
				break;
			case 3: case 9:
				new Thread (i, 11, this, moves).execute();
				new Thread (i, 9, this, moves).execute();
				new Thread (i, -11, this, moves).execute();
				new Thread (i, -9, this, moves).execute();
				break;
			case 4: case 10:
				int [] squares = {-12, -21, -19, -8, 8, 12, 19, 21};
				for (int square: squares) {
					if (this.inBoard(i+square)) {
						moves.add(new Move(i, i+square));
					}
				}
				break;
			case 5:
				if (data[i+10]==-1) {
					moves.add(new Move (i, i+10));
					if (i/10==3 && data[i+20]==-1) {
						moves.add(new Move(i, i+20));
					}
				}
				if ((!this.isWhite(data[i+9])) && this.inBoard(i+9)) {
					moves.add(new Move(i, i+9));
				}
				if ((!this.isWhite(data[i+11])) && this.inBoard(i+11)) {
					moves.add(new Move(i, i+11));
				}
				
				break;
			case 11: 
				if (data[i-10]==-1) {
					moves.add(new Move (i, i-10));
					if (i/10==8 && data[i-20]==-1) {
						moves.add(new Move(i, i-20));
					}
				}
				if ((this.isWhite(data[i-9])) && this.inBoard(i-9)) {
					moves.add(new Move(i, i-9));
				}
				if ((this.isWhite(data[i-11])) && this.inBoard(i-11)) {
					moves.add(new Move(i, i-11));
				}
				break;
			}

		}
		return moves;
	}
	
	/**
	 * Returns whether or not a specified piece is white. 
	 * @param piece An integer specifying the type of a piece. 
	 * For full index, refer to the Javadoc of the data field. 
	 * @return True if the piece is white, false if it's black. 
	 */
	public boolean isWhite (int piece) {
		return piece<6;
	}
	
	/**
	 * Returns whether or not it is white's turn to move
	 * on this board. 
	 * @return True if it's white's move, false if it's black's move. 
	 */
	public boolean isWhiteMove() {
		return whiteMove;
	}

	/**
	 * Changes the turn. 
	 * Makes it black's move if it's white's, 
	 * white's move if it's black's. 
	 */
	public void changeTurn () {
		whiteMove = (whiteMove)?false:true;
	}
	
	/**
	 * Returns the castling rights. 
	 * See castlingRights for how to parse the data. 
	 * @return Castling rights for this game. 
	 */
	public byte getCastlingRights () {
		return castlingRights;
	}
	
	/**
	 * Returns the byte array of the data. 
	 * @return A byte array. 
	 */
	public byte [] getData () {
		return data;
	}
	
	/**
	 * Changes the castling rights by a specified number. 
	 * The castling rights always decreased whether the number
	 * is positive or negative. 
	 * @param increment The amount to change. 
	 */
	public void changeCastlingRights (int increment) {
		castlingRights-=Math.abs(increment);
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
	
	
	/**
	 * Performs the reverse operation of numberFromLetter. 
	 * @param number The number given. 
	 * @return The character return. 
	 */
	public static char letterFromNumber (int number) {
		switch (number) {
		case 1: return 'a';
		case 2: return 'b';
		case 3: return 'c';
		case 4: return 'd';
		case 5: return 'e';
		case 6: return 'f';
		case 7: return 'g';
		case 8: return 'h';
		default: return 'i';

		}
	}
	
	
	/**
	 * A helper class to board. 
	 * For any sliding piece (bishop, rook, queen), a thread
	 * will find its legal moves given the placement of the other pieces on the board. 
	 * @author kevinshao
	 *
	 */
	private class Thread {
		
		private int increment; //The increment for the sliding piece, eg 10 for ROOK UP, 11 for BISHOP UP RIGHT. 
		private int initialPosition; //The array index for the initial position of the piece. 
		private Board board; //The board to be considered. 
		private List <Move> list; //The list to place valid moves in. 
		
		Thread (int initial, int increment, Board board, List<Move> list) {
			this.increment = increment;
			this.board = board;
			this.list = list;
			initialPosition = initial;
		}
		
		void execute () {
			boolean running = true;
			int considering = initialPosition + increment;
			
			while (running) {
				if (board.inBoard(considering)) {
					if (board.data[considering]==-1) {
						this.addToMoves(considering);
						considering+=increment;
					} else {
						running = false;
						if (board.isWhite(board.data[considering])!=board.isWhite(board.data[initialPosition])) {
							this.addToMoves(considering);
						}
					}
				} else {
					return;
				}
				
			}
		}
		
		private void addToMoves (int position) {
			list.add(new Move(initialPosition, position));
		}

	}

}
