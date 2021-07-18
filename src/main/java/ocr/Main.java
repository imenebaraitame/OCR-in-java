package ocr;

public class Main {

	public static void main(String[] args) throws Exception {
		//Image processing.
		String imagePath = Main.class.getResource("/images/skew.jpg").getPath();
		ImgProcess image = new ImgProcess(imagePath );
		String imageDeskew = image.deskewImage(imagePath);
		String imageMagick = image.magickManipulation(imageDeskew);
	    	String finalImage = image.bufferedImage(imageMagick);
	   

        	//Extract text from the image.
		Imgtext ocr = new Imgtext(finalImage);
		String fulltext = ocr.text();
		
		
		System.out.println("Creating pdf document...");
		TextPdf textpdf = new TextPdf(fulltext, "./ocrDemo.pdf");
		System.out.println("Document created.");		
		textpdf.document();
		
		//Place an invisible text layer on the top of the image and create a searchable pdf.
		TesseractCmd cmd = new TesseractCmd(finalImage, "./textonly_pdf", "tesseract", "pdf");
		cmd.searchableImg();
		
  
	}

}
