package userInterface;

/**
 * The class to store the data of a piece in transit. 
 * Used for drawing pieces sliding across the board. 
 * @author kevinshao
 *
 */
public class Transit {
	
	private int from, to, pieceFrom, pieceTo, step, total, sleepTime;
	
	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getPieceFrom() {
		return pieceFrom;
	}

	public void setPieceFrom(int pieceFrom) {
		this.pieceFrom = pieceFrom;
	}

	public int getPieceTo() {
		return pieceTo;
	}

	public void setPieceTo(int pieceTo) {
		this.pieceTo = pieceTo;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Transit (int from, int to, int pieceFrom, int pieceTo) {
		this.from = from;
		this.to = to;
		this.pieceFrom = pieceFrom;
		this.pieceTo = pieceTo;
		this.step = 0;
		this.total = 30;
		this.sleepTime = 20;
	}
	
	public void next () {
		step++;
	}

	public long getSleepTime() {
		return sleepTime;
	}

	

}
