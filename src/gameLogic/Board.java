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
	 * EG: In a position where white can castle to both sides
	 * but black can only castle queenside, this field should be
	 * 8 (black queenside) + 2 (white queenside) + 1 (white kingside) = 11. 
	 */
	private byte castlingRights = 15;

	/**
	 * The field indicating, if any, the square that is available for 
	 * en passant capture. This field will contain the index of the 
	 * square to move to if such a square exists, or 0 if the last 
	 * move does not allow for an en passant capture. 
	 */
	private int epSquare = 0;
	
	/**
	 * The halfmove clock, used for determining 50 move draw. 
	 * This is incremented every time either player makes a move, 
	 * and is reset every time a piece is captured or a pawn is advanced. 
	 */
	private int halfmoveClock = 0;
	
	/**
	 * The move counter. This starts at 1, 
	 * is incremented when Black makes a move, 
	 * and can never be reset. 
	 */
	private int move = 1;
	
	/**
	 * A flag indicating whether or not the legal moves have been generated for this board.
	 * If so, the legal moves can simply be pulled through the getLegalMoves method. 
	 */
	private boolean movesGenerated = false;
	
	/**
	 * The list of legal moves are to be stored here once generated. 
	 */
	private List<Move> legalMoves = null;
	
	private boolean evaluated = false;
	
	private double evaluation = 0d;
	

	


	/**
	 * The array holding the piece values. Indexed by the piece IDs described in the 
	 * Javadoc for the data field. 
	 */
	public static final double [] PIECE_VALUES = {1000, 9, 5, 3, 3, 1,
												-1000, -9, -5, -3, -3, -1};
	
	/**
	 * The array holding the value of each piece's available move. Indexed
	 * by the piece IDs described in the Javadoc for the data  field. 
	 */
	public static final double [] PIECE_MOVE_VALUES = {6, .5, 10, 15, 20, 8, 
													-6, -.5, -10, -15, -20, -8};

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
	 * Initializes a board given another board and a move, executing the move. 
	 * @param board The board to be copied. 
	 * @param move The move to be executed. 
	 * @return
	 */
	public static Board getBoardFromMove(Board board, Move move) {
		Board newBoard = cloneBoard (board);
		newBoard.performMove(move);
		return newBoard;
	}
	
	/**
	 * Returns a deep copy of the given board. 
	 * @param board The board to copy. 
	 * @return The deep copy. 
	 */
	public static Board cloneBoard (Board board) {
		Board newBoard = new Board();
		for (int i = 0 ; i < 120; i++) {
			newBoard.data[i] = board.data[i];
		}
		newBoard.whiteMove = board.whiteMove;
		newBoard.castlingRights = board.castlingRights;
		newBoard.epSquare = board.epSquare;
		newBoard.halfmoveClock = board.halfmoveClock;
		newBoard.move = board.move;
		newBoard.movesGenerated = false;
		newBoard.legalMoves = null;
		newBoard.evaluated = false;
		newBoard.evaluation = 0d;
		return newBoard;
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
		if (data[to]!=-1 || data[from]%6 == 5) {
			this.resetHalfmove();
		} else {
			this.incrementHalfmove();
		}

		this.changeTurn();

		data [to] = data [from];
		data [from] = -1;
		
		if (to==epSquare) {
			data[this.getEpTarget()]=-1;
		}
		
		if (data[to]%6==5) {
			if (Math.abs(from-to)==20) {
				epSquare = this.isWhite(data[to])?to-10:to+10;
			} else {
				epSquare = 0;
			}
		} else {
			epSquare = 0;
		}
		
		
		if (data[to]%6==5) {
			if (to/10==2||to/10==9) {
				if (data[to]==5) {
					data[to]=1;
				} else {
					data[to]=7;
				}
			}
		}
		//Make sure to set movesGenerated and evaluated to false. The board has changed. 
		movesGenerated = false;
		evaluated = false;

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
	public List <Move> obtainLegalMoves () {
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
				int [] king = {-11, -10, -9, -1, 1, 9, 10, 11};
				for (int square: king) {
					if (this.inBoard(i+square)&&(this.isWhite(this.data[i+square])!=this.isWhite(piece)||this.data[i+square]==-1)) {
						moves.add(new Move(i, i+square, data[i]));
					}
				}
				break;
			case 1: case 7: 
				int [] queen = {-11, -10, -9, -1, 1, 9, 10, 11};
				for (int square: queen) {
					new Thread (i, square, this, moves).execute();
				}
				break;
			case 2: case 8: 
				int [] rook = {1, 10, -1, -10};
				for (int square: rook) {
					new Thread (i, square, this, moves).execute();
				}
				break;
			case 3: case 9:
				int [] bishop = {11, 9, -11, -9};
				for (int square: bishop) {
					new Thread (i, square, this, moves).execute();
				}
				break;
			case 4: case 10:
				int [] squares = {-12, -21, -19, -8, 8, 12, 19, 21};
				for (int square: squares) {
					if (this.inBoard(i+square)&&(this.isWhite(this.data[i+square])!=this.isWhite(piece)||(this.data[i+square]==-1))) {
						moves.add(new Move(i, i+square, data[i]));
					}
				}
				break;
			case 5:
				if (data[i+10]==-1) {
					moves.add(new Move (i, i+10, data[i]));
					if (i/10==3 && data[i+20]==-1) {
						moves.add(new Move(i, i+20, data[i]));
					}
				}
				if ( ((!this.isWhite(data[i+9])) && this.inBoard(i+9) && data[i+9]!=-1) || this.epSquare==i+9) {
					moves.add(new Move(i, i+9, data[i]));
				}
				if (((!this.isWhite(data[i+11])) && this.inBoard(i+11) && data[i+11]!=-1)|| this.epSquare==i+11) {
					moves.add(new Move(i, i+11, data[i]));
				}
				
				break;
			case 11: 
				if (data[i-10]==-1) {
					moves.add(new Move (i, i-10, data[i]));
					if (i/10==8 && data[i-20]==-1) {
						moves.add(new Move(i, i-20, data[i]));
					}
				}
				if (((this.isWhite(data[i-9])) && this.inBoard(i-9))|| this.epSquare==i-9) {
					moves.add(new Move(i, i-9, data[i]));
				}
				if (((this.isWhite(data[i-11])) && this.inBoard(i-11))|| this.epSquare==i-11) {
					moves.add(new Move(i, i-11, data[i]));
				}
				break;
			}

		}
		this.legalMoves = moves;
		this.movesGenerated = true;
		return moves;
	}
	
	/**
	 * Evaluates the state of the board. Returns positive numbers for 
	 * white's advantage, negative numbers for black's advantage. 
	 * @return A double representing who's ahead, and by how much. 
	 */
	public double evaluate () {
		double total = 0d;
		for (int i = 0 ; i < data.length; i++) {
			byte b = data[i];
			if (this.inBoard(i)&&b!=-1) {
				total+=PIECE_VALUES [b];
			}
			if (b%6==5) {
				int rank;
				double coef = .1;
				if (b>6) {
					rank = 9-(i/10);
					total+=rank*coef;
				} else {
					rank = (i/10)-2;
					total+=rank*coef;
				}
			}
		}
		for (Move m: this.getLegalMoves()) {
			total+=PIECE_MOVE_VALUES[m.getPiece()]/190;
		}
		Board b = cloneBoard (this);
		b.changeTurn();
		for (Move m: b.getLegalMoves()) {
			total+=PIECE_MOVE_VALUES[m.getPiece()]/190;
		}
		evaluated = true;
		evaluation = total;
		return total;
	}
	
	
	/**
	 * Returns whether or not a specified piece is white. 
	 * @param piece An integer specifying the type of a piece. 
	 * For full index, refer to the Javadoc of the data field. 
	 * @return True if the piece is white, false if it's black. 
	 */
	public boolean isWhite (int piece) {
		return piece<6&&piece!=-1;
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
		if (!whiteMove) {
			move++;
		}
		whiteMove = (whiteMove)?false:true;
	}
	
	/**
	 * Returns the castling rights for a given side and color. 
	 * Give 0 for white kingside, 1 for white queenside, 
	 * 2 for black kingside, and 3 for black queenside. 
	 * @return Castling rights for the given side and color
	 */
	public int getCastlingRights (int position) {
		return (castlingRights >> position) & 1;
	}
	
	/**
	 * Returns the byte array of the data. 
	 * @return A byte array. 
	 */
	public byte [] getData () {
		return data;
	}
	
	
	/**
	 * Returns the en passant square, in integer form (indicating the index). 
	 * @return
	 */
	public int getEpSquare () {
		return this.epSquare;
	}
	
	/**
	 * Increments the halfmove clock. 
	 */
	public void incrementHalfmove () {
		halfmoveClock++;
	}
	
	/**
	 * Resets the halfmove clock. 
	 */
	public void resetHalfmove () {
		halfmoveClock = 0;
	}
	
	/**
	 * Returns the number of halfmoves elapsed since last pawn advance or capture. 
	 * @return The number of halfmoves. 
	 */
	public int getHalfmove () {
		return this.halfmoveClock;
	}
	
	/**
	 * Returns the number of moves played in the game. 
	 * @return The number of moves. 
	 */
	public int getMove () {
		return move;
	}
	
	/**
	 * Increments the move counter. 
	 * Call after black has made his move. 
	 */
	public void incrementMove () {
		move++;
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
	
	public List<Move> getLegalMoves () {
		if (movesGenerated) {
			return legalMoves;
		} else {
			return obtainLegalMoves();
		}
	}
	
	public double getEvaluation () {
		if (evaluated) {
			return evaluation;
		} else {
			return evaluate();
		}
	}
	
	/**
	 * Returns the piece targeted by an ep move, ie, the pawn to be taken. 
	 * @return
	 */
	public int getEpTarget () {
		return (this.epSquare/10==3)?this.epSquare+10:this.epSquare-10;
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
		return 10*row+column+10;
	}
	
	/**
	 * Takes a position and returns the array index. 
	 * @param position The position as a position object. 
	 * @return The array index of the specified position. 
	 */
	public static int indexFromPosition (Position position) {
		return indexFromPosition (position.getRow(), position.getColumn());
	}
	
	/**
	 * Returns the column number associated with a letter. 
	 * @param letter The letter of the column we are interested in. 
	 * @return The number, starting at 1 for A, of the column we are interested in. 
	 */
	public static int numberFromLetter (char letter) {
		switch (letter) {
		case 'a': return 1;
		case 'b': return 2;
		case 'c': return 3;
		case 'd': return 4;
		case 'e': return 5;
		case 'f': return 6;
		case 'g': return 7;
		case 'h': return 8;
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
		
		/**
		 * Runs the thread, and pushes any legal moves to the list. 
		 */
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
			list.add(new Move(initialPosition, position, data[initialPosition]));
		}
	}

}
