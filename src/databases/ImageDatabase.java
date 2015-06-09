package databases;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * The class that stores all images as either public static BufferedImages
 * or public static Maps with the values as BufferedImages. 
 * @author kevinshao
 *
 */
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
		
		
		//Initializes the pieces hashmap with a key (wk for white king, bb for black bishop etc.) 
		//and a BufferedImage
		pieces.put("wk", ImageIO.read(new File("./Resources/Images/king_white.png")));
		pieces.put("bk", ImageIO.read(new File("./Resources/Images/king_black.png")));
		pieces.put("wq", ImageIO.read(new File("./Resources/Images/queen_white.png")));
		pieces.put("bq", ImageIO.read(new File("./Resources/Images/queen_black.png")));
		pieces.put("wr", ImageIO.read(new File("./Resources/Images/rook_white.png")));
		pieces.put("br", ImageIO.read(new File("./Resources/Images/rook_black.png")));
		pieces.put("wb", ImageIO.read(new File("./Resources/Images/bishop_white.png")));
		pieces.put("bb", ImageIO.read(new File("./Resources/Images/bishop_black.png")));
		pieces.put("wn", ImageIO.read(new File("./Resources/Images/knight_white.png")));
		pieces.put("bn", ImageIO.read(new File("./Resources/Images/knight_black.png")));
		pieces.put("wp", ImageIO.read(new File("./Resources/Images/pawn_white.png")));
		pieces.put("bp", ImageIO.read(new File("./Resources/Images/pawn_black.png")));


	}

}
