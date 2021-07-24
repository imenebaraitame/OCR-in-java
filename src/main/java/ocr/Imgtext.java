package ocr;

import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Imgtext extends Tesseract {
	
	private String imagePath;

	public Imgtext(String imagePath) throws Exception {
		this.imagePath = imagePath;
	}

	String text() throws TesseractException {
		this.setTessVariable("user_defined_dpi", "300");
		this.setDatapath(System.getenv("TESSDATA_PREFIX"));
		this.setLanguage("eng");//set the Eng language
		String fulltext = this.doOCR(new File(imagePath));
		return fulltext ;		
	}

}
