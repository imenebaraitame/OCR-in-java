package ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws Exception {
		// if image was not in current directory copy the path of the image
		Process image = new Process("test2.jpg", ImageIO.read(new File("test2.jpg")) );
	    BufferedImage img = image.bufferedImage();
	    Fenetre fen = new Fenetre(img);
	    
		
		Imgtext ocr = new Imgtext("bufferedImage.jpg");
		String fulltext = ocr.text();
		
		TextPdf pdf = new TextPdf(fulltext, "ocrDemo.pdf");
		pdf.document();
		
	}

}
