package databases;

import java.util.HashMap;
import java.util.Map;

public class Database {
	
	public static Map <Byte, String> pieceLetterFEN = new HashMap <Byte, String>();
	
	
	public static void initDatabase () {
		pieceLetterFEN.put((byte)-1, "NULL");
		pieceLetterFEN.put((byte) 0, "K");
		pieceLetterFEN.put((byte) 1, "Q");
		pieceLetterFEN.put((byte) 2, "R");
		pieceLetterFEN.put((byte) 3, "B");
		pieceLetterFEN.put((byte) 4, "N");
		pieceLetterFEN.put((byte) 5, "P");
		pieceLetterFEN.put((byte) 6, "k");
		pieceLetterFEN.put((byte) 7, "q");
		pieceLetterFEN.put((byte) 8, "r");
		pieceLetterFEN.put((byte) 9, "b");
		pieceLetterFEN.put((byte) 10,"n");
		pieceLetterFEN.put((byte) 11,"p");
	}


	public static String getFenLetter(byte b) {
		return pieceLetterFEN.get(b);
	}

}
