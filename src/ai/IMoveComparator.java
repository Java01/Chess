package ai;

import java.util.Comparator;

public class IMoveComparator<T> implements Comparator<IMove> {
	
	private IBoard board;

	public IMoveComparator (IBoard board) {
		this.board = board;
	}
	
	
	@Override
	public int compare(IMove arg0, IMove arg1) {
		int result = 0;
		try {
			IBoard board1 = board.getBoardFromMove(arg0, false);
			IBoard board2 = board.getBoardFromMove(arg1, false);
			if (board1.isMax()!=board2.isMax()) {
				System.err.println("Incomparable boards");
			}
			if (board1.getEvaluation()>board2.getEvaluation()) {
				result = 1;
			} else 
			if (board1.getEvaluation()<board2.getEvaluation()) {
				result = -1;
			} else {
				result = 0;
			}
			if (!board1.isMax()) {
				result*=-1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
