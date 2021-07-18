package ocr;

import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Imgtext extends Tesseract {
	
	private String imagePath2;

	public Imgtext(String imagePath2) throws Exception {
		this.imagePath2 = imagePath2;
	}

	String text() throws TesseractException {
		this.setTessVariable("user_defined_dpi", "300");
		this.setDatapath(System.getenv("TESSDATA_PREFIX"));
		this.setLanguage("eng");//set the Eng language
		String fulltext = this.doOCR(new File(imagePath2));
		return fulltext ;		
	}

}
