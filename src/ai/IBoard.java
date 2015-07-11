package ai;

import java.util.List;

/**
 * A board, to be supplied to the AI class for getting the optimal move. 
 * @author kevinshao
 *
 */
public interface IBoard {

	/**
	 * Clones the board and makes a move. 
	 * @param move The move to be executed. 
	 * @param checkLegal A boolean saying whether or not to check if the move is legal. 
	 * EG in chess it is illegal to move into check. 
	 * @return A copy of the board with the move performed. 
	 * @throws IllegalMoveException If checkLegal is true and the move is indeed illegal. 
	 */
	IBoard getBoardFromMove(IMove move, boolean checkLegal) throws IllegalMoveException;
	
	/**
	 * Tells whether this board should be a max node. 
	 * @return True if it is a max node (in chess, white to move), 
	 * and false if it's a min node (black to move)
	 */
	boolean isMax ();

	/**
	 * Simply clone the board, no modifications made. 
	 * @return A copy of the board. 
	 */
	IBoard clone();
	
	/**
	 * Clone the board and change the side to move. 
	 * @return A copy of the board with the side to move changed. 
	 */
	IBoard cloneBoardWithOppositeMove();
	
	/**
	 * Returns a list of legal moves. 
	 * @param generateCastling Used in chess, to prevent infinite recursion. 
	 * For other games, should be false. 
	 * @return A list of IMoves that are all the legal moves. 
	 */
	List <IMove> getLegalMoves (boolean generateCastling);

	/**
	 * Evaluates the board, and returns the evaluation. 
	 * @return The evaluation of the board. 
	 */
	double getEvaluation();
	
	

}
