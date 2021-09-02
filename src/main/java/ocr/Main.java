package ocr;

import java.nio.file.Path;
import java.nio.file.Paths;




public class Main {

	public static void main(String[] args) throws Exception {
		
		String filePath = Main.class.getResource("/images/AraEng.pdf").getPath();
		
		if (FileExtension.isPdf(filePath)){
			ExtractImage.takeImageFromPdf(filePath);
			Path imgPath = Paths.get("ExtractedImage_1.png");
			String img = imgPath.toAbsolutePath().toString();
	        
		
		//Image processing.	
		String imageNBorder = ImageProcess.ImgAfterDeskewingWithoutBorder(img);
	    String finalImage = ImageProcess.ImgAfterRemovingBackground(img);

        //Extract text from the image.
		//ImageText ocr = new ImageText(finalImage);
		//String fulltext = ocr.generateText();
        
		//System.out.println("Creating pdf document...");
		//TextPdf textpdf = new TextPdf(fulltext, "./ocrDemo.pdf");
		//System.out.println("Document created.");		
		//textpdf.generateDocument();
		
		// configfileValue = 0->make the image visible, =1->make the image invisible
		CreateSearchableImagePdf createPdf = new CreateSearchableImagePdf(finalImage,"./textonly_pdf","0");
		createPdf.textOnlyPdf(finalImage);
		
        System.out.println("getting the size and the location of the image from textonly_pdf");
		
		Path path = Paths.get("textonly_pdf.pdf");
		String ExistingPdfFilePath= path.toAbsolutePath().toString();
        String outputFilePath = "./newFile.pdf"; // New file
        
        ImageLocationsAndSize.createPdfWithOriginalImage(ExistingPdfFilePath,
        		                                         outputFilePath, imageNBorder);
        
		}else {
			//Image processing.	
			String imageNBorder = ImageProcess.ImgAfterDeskewingWithoutBorder(filePath);
		    String finalImage = ImageProcess.ImgAfterRemovingBackground(filePath);

	        //Extract text from the image.
			//ImageText ocr = new ImageText(finalImage);
			//String fulltext = ocr.generateText();
	        
			//System.out.println("Creating pdf document...");
			//TextPdf textpdf = new TextPdf(fulltext, "./ocrDemo.pdf");
			//System.out.println("Document created.");		
			//textpdf.generateDocument();
			
			// configfileValue = 0->make the image visible, =1->make the image invisible
			CreateSearchableImagePdf createPdf = new CreateSearchableImagePdf(finalImage,"./textonly_pdf","0");
			createPdf.textOnlyPdf(finalImage);
			
	        System.out.println("getting the size and the location of the image from textonly_pdf");
			
			Path path = Paths.get("textonly_pdf.pdf");
			String ExistingPdfFilePath= path.toAbsolutePath().toString();
	        String outputFilePath = "./newFile.pdf"; // New file
	        
	        ImageLocationsAndSize.createPdfWithOriginalImage(ExistingPdfFilePath, 
	        		                                         outputFilePath, imageNBorder);
			
		}
		       

		
	}


}
