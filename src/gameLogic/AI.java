package gameLogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class AI {
	
	
	/**
	 * Given a board, return an integer that represents a depth of 
	 * search that can be completed in a reasonable time. 
	 * @param board
	 * @return
	 */
	public static int getBestDepth (Board board) {
		int moves1 = board.getLegalMoves(true).size();
		Board board2 = Board.cloneBoard(board);
		board2.changeTurn();
		int moves2 = board2.getLegalMoves(true).size();
		int total = moves1 * moves2;
		if (total > 1000) {
			return 5;
		}
		if (total > 700) {
			return 6;
		}
		if (total > 500) {
			return 6;
		}
		if (total > 300) {
			return 7;
		}
		if (total > 100) {
			return 7;
		}
		return 8;
	}
	
	/**
	 * Returns the best move using alpha beta pruning. 
	 * @param board
	 * @return
	 */
	public static Move getBestMove (Board board) {
		PrintWriter out = null;
		try {
			out = new PrintWriter (new File ("./Resources/Debug/aidebug.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int depth = getBestDepth (board);
		boolean finished = false;
		Node parent = new Node (board);
		Node activeNode = parent;
		while (!finished) {
			
			if (activeNode.getDepth()==depth) {
				double evaluation = activeNode.getBoard().getEvaluation();
				activeNode = activeNode.getParent();
				if (activeNode.isMaxNode()) {
					if (evaluation > activeNode.getAlpha()) {
						activeNode.setAlpha(evaluation);
						activeNode.setSelected(activeNode.getChildOn()-1);
					}
				} else {
					if (evaluation < activeNode.getBeta()) {
						activeNode.setBeta(evaluation);
						activeNode.setSelected(activeNode.getChildOn()-1);
					}
				}
				continue;
			}
			
			if (activeNode.isNewNode()) {
//				System.out.println("Considering new node. Depth: " + activeNode.getDepth());
				List<Move> moves = activeNode.getBoard().getLegalMoves(true);
				activeNode.setNewNode(false);
				if (moves.size()==0) {
					//Evaluate, return to higher. 
				} else {
					activeNode.setMoves(moves);
					activeNode.setChildCount(moves.size());
					activeNode.setChildOn(0);
				}
				
			} else {
				
				if (activeNode.getAlpha()>=activeNode.getBeta()) {
					//Prune
					if (activeNode.getDepth()==1) {
						out.close();
						return activeNode.getMoves().get(activeNode.getSelected());
					}
					activeNode = activeNode.getParent();
					continue;
				}
				
				if (activeNode.getChildOn()==activeNode.getChildCount()) {
					if (activeNode.getDepth()==1) {
						out.close();
						return activeNode.getMoves().get(activeNode.getSelected());
					}
					if (activeNode.isMaxNode()) {
						double evaluation = activeNode.getAlpha();
						activeNode = activeNode.getParent();
						if (evaluation < activeNode.getBeta()) {
							activeNode.setBeta(evaluation);
							activeNode.setSelected(activeNode.getChildOn()-1);
						}
						continue;
					} else {
						double evaluation = activeNode.getBeta();
						activeNode = activeNode.getParent();
						if (evaluation > activeNode.getAlpha()) {
							if (activeNode.getDepth()==1) {
								out.println("here - beta: " + evaluation + " alpha: " + activeNode.getAlpha());
							}
							activeNode.setAlpha(evaluation);
							activeNode.setSelected(activeNode.getChildOn()-1);

						}
						continue;
					}
				} else {
					if (activeNode.getDepth()<3) {
						out.println("Next child. Depth: " + activeNode.getDepth() + " childOn: " + activeNode.getChildOn() + " total moves: " + activeNode.getMoves().size() + " children: " + activeNode.getChildCount() + " alpha: " + activeNode.getAlpha() + " beta: " + activeNode.getBeta() + " max: " + activeNode.isMaxNode()+" move: "+activeNode.getMoves().get(activeNode.getChildOn()) + " selected: " + activeNode.getSelected());
						out.flush();
					}
					
					Node n = new Node (activeNode, activeNode.getMoves().get(activeNode.getChildOn()));
					activeNode.nextChild();
					activeNode = n;
				}
			}
		}

		return parent.getMoves().get(parent.getSelected());

	}

}
