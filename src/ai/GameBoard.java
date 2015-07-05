package ai;

import java.util.List;

public interface GameBoard {

	GameBoard getBoardFromMove(GameMove move, boolean b) throws IllegalMoveException;
	
	boolean isMax ();

	GameBoard clone();
	
	GameBoard cloneBoardWithOppositeMove();
	
	List <GameMove> getLegalMoves (boolean b);

	double getEvaluation();
	
	

}
