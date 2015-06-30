package gameLogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AI {
	
	
	static PrintWriter time = null;
	
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
		time.print("Multiplied Moves: " + total);
		time.flush();
		int depth;
		if (total > 1000) {
			depth = 5;
		} else
		if (total > 700) {
			depth = 6;
		} else 
		if (total > 500) {
			depth = 6;
		} else 
		if (total > 300) {
			depth = 7;
		} else 
		if (total > 100) {
			depth = 7;
		} else {
			depth = 8;
		}
		return depth;
	}
	
	/**
	 * Returns the best move using alpha beta pruning. 
	 * @param board
	 * @return
	 */
	public static Move getBestMove (Board board) {
		try {
			time = new PrintWriter (new FileWriter(new File ("./Resources/Debug/time.txt"), true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long start = System.nanoTime();
		int depth = getBestDepth (board);
		boolean finished = false;
		Node parent = new Node (board);
		Node activeNode = parent;
		while (!finished) {
			
			if (activeNode.getDepth()==depth) {
				if (activeNode.isDead()) {
					activeNode = activeNode.getParent();
					continue;
				}
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
				if (activeNode.isDead()) {
					activeNode = activeNode.getParent();
					continue;
				}
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
						time.println(" | Depth: " + depth + " | Time: " + ((System.nanoTime()-start)/1000000000) + " seconds");
						time.flush();
						time.close();
						return activeNode.getMoves().get(activeNode.getSelected());
					}
					activeNode = activeNode.getParent();
					continue;
				}
				
				if (activeNode.getChildOn()==activeNode.getChildCount()) {
					if (activeNode.getDepth()==1) {
						time.println(" | Depth: " + depth + "| Time: " + ((System.nanoTime()-start)/1000000000) + " seconds");
						time.flush();
						time.close();
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
							activeNode.setAlpha(evaluation);
							activeNode.setSelected(activeNode.getChildOn()-1);
						}
						continue;
					}
				} else {
					Node n = new Node (activeNode, activeNode.getMoves().get(activeNode.getChildOn()));
					activeNode.nextChild();
					activeNode = n;
				}
			}
		}

		return parent.getMoves().get(parent.getSelected());

	}

}
