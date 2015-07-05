package databases;

import java.util.HashMap;
import java.util.Map;

public class Database {
	
	private static Map <Byte, String> pieceLetterFEN = new HashMap <Byte, String>();
	private static Map <String, Byte> FENpieceNumber = new HashMap <String, Byte> ();
	private static Map <Byte, String> pieceLetter = new HashMap <Byte, String> ();
	
	public static void initDatabase () {
		String [] fen = {"NULL", "K", "Q", "R", "B", "N", "P", "k", "q", "r", "b", "n", "p"};
		for (byte i = -1; i < 12 ; i++) {
			pieceLetterFEN.put(i, fen[i+1]);
			FENpieceNumber.put(fen[i+1], i);
		}
		String [] png = {"NULL", "K", "Q", "R", "B", "N", "", "K", "Q", "R", "B", "N", ""};
		for (byte i = -1; i < 12 ; i++) {
			pieceLetter.put(i, png[i+1]);
		}
	}


	public static String getFenLetter(byte b) {
		return pieceLetterFEN.get(b);
	}
	
	public static String getPieceLetter (byte b) {
		return pieceLetter.get(b);
	}
	
	public static byte getFenNumber (String piece) {
		return FENpieceNumber.get(piece);
	}

}
