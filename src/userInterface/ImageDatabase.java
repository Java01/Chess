package userInterface;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageDatabase {
	
	public static BufferedImage blackSquare = null;
	public static BufferedImage whiteSquare = null;

	/**
	 * The map that contains images of all the pieces. They can be index using 2 letter strings,
	 * the first letter is 'w' for white or 'b' for black, the second letter for the piece. 
	 */
	public static Map <String, BufferedImage> pieces = new HashMap <String, BufferedImage> ();
	
	
	
	/**
	 * Initializes the images. 
	 * @throws IOException Throws if IOException occurs during initialization. 
	 */
	public static void initImages () throws IOException {
		blackSquare = ImageIO.read(new File("./Resources/Images/blacksquare.png"));
		whiteSquare = ImageIO.read(new File("./Resources/Images/whitesquare.png"));
		
		
		//Initializes the pieces hashmap with a key (wk for white king, bb for black bishop etc.) and a BufferedImage
		pieces.put("wk", ImageIO.read(new File("./Resources/Image/king_white")));
		pieces.put("bk", ImageIO.read(new File("./Resources/Image/king_black")));
		pieces.put("wq", ImageIO.read(new File("./Resources/Image/queen_white")));
		pieces.put("bq", ImageIO.read(new File("./Resources/Image/queen_black")));
		pieces.put("wr", ImageIO.read(new File("./Resources/Image/rook_white")));
		pieces.put("br", ImageIO.read(new File("./Resources/Image/rook_black")));
		pieces.put("wb", ImageIO.read(new File("./Resources/Image/bishop_white")));
		pieces.put("bb", ImageIO.read(new File("./Resources/Image/bishop_black")));
		pieces.put("wn", ImageIO.read(new File("./Resources/Image/knight_white")));
		pieces.put("bn", ImageIO.read(new File("./Resources/Image/knight_black")));
		pieces.put("wp", ImageIO.read(new File("./Resources/Image/pawn_white")));
		pieces.put("bq", ImageIO.read(new File("./Resources/Image/pawn_black")));


	}

}
