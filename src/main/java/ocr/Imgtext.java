package ocr;

import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Imgtext extends Tesseract {
	
	private String imagePath;

	public Imgtext(String imagePath){
		this.imagePath = imagePath;
	}
   
	   
	String text(){
		
		//mode 6: Assume a single uniform block of text.
		this.setTessVariable("user_defined_dpi", "300");
		this.setDatapath(System.getenv("TESSDATA_PREFIX"));
		this.setLanguage("eng");//set the Eng language
		
		String fulltext = null;
		try {
			fulltext = this.doOCR(new File(imagePath));
		} catch (TesseractException e) {
			e.printStackTrace();
			System.out.println("TESSERACT ERROR:" + e.getMessage());
		}
		return fulltext ;		
	
	
    
		
	}		
   
}
