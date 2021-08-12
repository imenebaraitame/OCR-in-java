package ocr;

import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Imgtext extends Tesseract {
	
	private String imagePath;

	public Imgtext(String imagePath){
		this.imagePath = imagePath;
	}
   
	   
	String ExractText(){
		
		//mode 6: Assume a single uniform block of text.
		this.setTessVariable("user_defined_dpi", "300");
		this.setDatapath(System.getenv("TESSDATA_PREFIX"));
		this.setLanguage("ara+eng");//set the English and Arabic language
		
		String fulltext = null;
		try {
			fulltext = this.doOCR(new File(imagePath));
			System.out.println(fulltext);
		} catch (TesseractException e) {
			e.printStackTrace();
			System.out.println("TESSERACT ERROR:" + e.getMessage());
		}
		return fulltext ;		
	
	
    
		
	}		
   
}
