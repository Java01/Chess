package userInterface;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageDatabase {
	
	public static BufferedImage blackSquare = null;
	public static BufferedImage whiteSquare = null;

	
	public static void initImages () throws IOException {
		blackSquare = ImageIO.read(new File("./Resources/Images/blacksquare.png"));
		whiteSquare = ImageIO.read(new File("./Resources/Images/whitesquare.png"));

	}

}
