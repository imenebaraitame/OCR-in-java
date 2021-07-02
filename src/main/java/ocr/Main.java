package ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws Exception {
		// if image was not in current directory copy the path of the image
		String imagePath = Main.class.getResource("/images/test2.jpg").getPath();
		Process image = new Process(imagePath, ImageIO.read(new File(imagePath)) );
	    BufferedImage img = image.bufferedImage();
	    Fenetre fen = new Fenetre(img);


		Imgtext ocr = new Imgtext(Main.class.getResource("/images/bufferedImage.jpg").getPath());
		String fulltext = ocr.text();

		TextPdf pdf = new TextPdf(fulltext, "ocrDemo.pdf");
		pdf.document();

	}

}
