package ocr;

import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.ITesseract.RenderedFormat;
import net.sourceforge.tess4j.TesseractException;



public class TesseractCmd {
	String input_file; String output_file; String configfileValue;
	

	
	public TesseractCmd(String input_file, String output_file,String configfileValue){
		this.input_file = input_file;
		this.output_file = output_file;
		this.configfileValue = configfileValue;
	}
	 
	public void textOnlyPdf(String imagePath){
	 List<RenderedFormat> formats = new ArrayList<RenderedFormat>(Arrays.asList(RenderedFormat.PDF));
          try {
		
		Tesseract instance = new Tesseract();
		//mode 6: Assume a single uniform block of text.
		instance.setPageSegMode(6);
		instance.setTessVariable("user_defined_dpi", "300");
		instance.setDatapath(System.getenv("TESSDATA_PREFIX"));
		instance.setLanguage("eng");//set the Eng language
	    instance.setTessVariable("textonly_pdf",configfileValue);//0->make the image visible,1->make the image invisible
	    instance.createDocuments(new String[]{imagePath}, new String[]{output_file}, formats);
	    
		} catch (TesseractException te){
			System.err.println("Error TE: " + te.getMessage());
		}
	
    
	}
		
	
	

}
