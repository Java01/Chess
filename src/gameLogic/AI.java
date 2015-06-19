package gameLogic;

import java.util.List;

public class AI {
	
	/**
	 * Returns the best move using alpha beta pruning. 
	 * @param board
	 * @return
	 */
	public static Move getBestMove (Board board) {
		System.out.println("Getting best move");
		int depth = 3; //This will change
		boolean finished = false;
		Node parent = new Node (board);
		Node activeNode = parent;
		while (!finished) {
			if (activeNode.getDepth()==depth) {
				System.out.println("Considering final depth node");
				double evaluation = activeNode.getBoard().getEvaluation();
				activeNode = activeNode.getParent();
				if (activeNode.isMaxNode()) {
					if (evaluation >= activeNode.getAlpha()) {
						activeNode.setAlpha(evaluation);
						activeNode.setSelected(activeNode.getChildOn());
					}
				} else {
					if (evaluation <= activeNode.getBeta()) {
						activeNode.setBeta(evaluation);
						activeNode.setSelected(activeNode.getChildOn());
					}
				}
				activeNode.nextChild();
				continue;
			}
			if (activeNode.isNewNode()) {
				System.out.println("Considering new node. Depth: " + activeNode.getDepth());
				List<Move> moves = board.getLegalMoves();
				activeNode.setNewNode(false);
				if (moves.size()==0) {
					//Evaluate, return to higher. 
				} else {
					activeNode.setMoves(moves);
					activeNode.setChildCount(moves.size());
					activeNode.setChildOn(0);
					activeNode = new Node (activeNode, moves.get(0));
				}
			} else {
				if (activeNode.getChildOn()==activeNode.getChildCount() || activeNode.getAlpha()>=activeNode.getBeta()) {
					System.out.println("Next child. Depth: " + activeNode.getDepth());
					if (activeNode.getDepth()==1) {
						return activeNode.getMoves().get(activeNode.getSelected());
					}
					if (activeNode.isMaxNode()) {
						double evaluation = activeNode.getAlpha();
						activeNode = activeNode.getParent();
						if (evaluation >= activeNode.getBeta()) {
							activeNode.setBeta(evaluation);
							activeNode.setSelected(activeNode.getChildOn());
						}
						activeNode.nextChild();
						continue;
					} else {
						double evaluation = activeNode.getBeta();
						activeNode = activeNode.getParent();
						if (evaluation <= activeNode.getAlpha()) {
							activeNode.setAlpha(evaluation);
							activeNode.setSelected(activeNode.getChildOn());
						}
						activeNode.nextChild();
						continue;
					}
				} else {
					activeNode.nextChild();
				}
			}
		}

		return parent.getMoves().get(parent.getSelected());

	}

}
