package ai;

import java.util.List;

/**
 * A node, used for alpha-beta pruning. 
 * @author kevinshao
 *
 */
public class Node {
	
	/**
	 * Depth of the node. Root node has depth of 1. 
	 */
	private int depth;
	/**
	 * The board that this node represents. 
	 */
	private IBoard board;
	/**
	 * The alpha value of this node.
	 */
	private double alpha;
	/**
	 * The beta value of this node. 
	 */
	private double beta;
	/**
	 * The parent of this node. 
	 */
	private Node parent;
	private int childOn; //Which child this node is currently considering. 
	private boolean newNode; //Tells whether this is a new node. That is, whether or not the child count has been calculated. 
	private int childCount;
	private boolean maxNode;
	private List<IMove> moves = null;
	private int selected = -1; //Which child node is the superior one right now. 
	private boolean dead = false; //Whether or not this node is a "dead" one, ie the position is illegal therefore it should not be considered. 
	
	public Node (Node parent, IMove move) {
		this.parent = parent;
		depth = parent.depth+1;
		alpha = parent.alpha;
		beta = parent.beta;
		childOn = 0;
		newNode = true;
		maxNode = parent.maxNode?false:true;
		try {
			board = parent.board.getBoardFromMove (move, true);
		} catch (IllegalMoveException e) {
			this.dead = true;
		}
	}
	
	/**
	 * Used to initialize the root node. 
	 * @param board
	 */
	public Node (IBoard board) {
		depth = 1;
		this.board = board;
		alpha = -10000;
		beta = 10000;
		parent = null;
		childOn = 0;
		this.newNode = true;
		this.maxNode = board.isMax();
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public IBoard getBoard() {
		return board;
	}

	public void setBoard(IBoard board) {
		this.board = board;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getChildOn() {
		return childOn;
	}

	public void setChildOn(int childOn) {
		this.childOn = childOn;
	}

	public boolean isNewNode() {
		return newNode;
	}

	public void setNewNode(boolean newNode) {
		this.newNode = newNode;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	public boolean isMaxNode() {
		return maxNode;
	}

	public void setMaxNode(boolean maxNode) {
		this.maxNode = maxNode;
	}

	public List<IMove> getMoves() {
		return moves;
	}

	public void setMoves(List<IMove> moves) {
		this.moves = moves;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public void nextChild() {
		this.childOn++;
	}
	public boolean isDead () {
		return dead;
	}
	public void setDead (boolean dead) {
		this.dead = dead;
	}

}
