package ocr;

import java.nio.file.Path;
import java.nio.file.Paths;




public class Main {

	public static void main(String[] args) throws Exception {
		
		String filePath = Main.class.getResource("/images/AraEng.pdf").getPath();
		
		if (FileExtension.isPdf(filePath)){
			ExtractImage.imageFromPdf(filePath);
			Path imgPath = Paths.get("ExtractedImage_1.png");
			String img = imgPath.toAbsolutePath().toString();
	        
		
		//Image processing.	
		String imageNBorder = ImgProcess.ImgAfterDeskewingWithoutBorder(img);
	    String finalImage = ImgProcess.ImgAfterRemovingBackground(img);

        //Extract text from the image.
		//Imgtext ocr = new Imgtext(finalImage);
		//String fulltext = ocr.ExractText();
        
		//System.out.println("Creating pdf document...");
		//TextPdf textpdf = new TextPdf(fulltext, "./ocrDemo.pdf");
		//System.out.println("Document created.");		
		//textpdf.MakeDocument();
		
		// configfileValue = 0->make the image visible, =1->make the image invisible
		CreatePdf createPdf = new CreatePdf(finalImage,"./textonly_pdf","0");
		createPdf.textOnlyPdf(finalImage);
		
        System.out.println("getting the size and the location of the image from textonly_pdf");
		
		Path path = Paths.get("textOnly_pdf.pdf");
		String ExistingPdfFilePath= path.toAbsolutePath().toString();
        String outputFilePath = "./newFile.pdf"; // New file
        
        GetImageLocationsAndSize.createPdfWithOriginalImage(ExistingPdfFilePath, outputFilePath, imageNBorder);
        
		}else {
			//Image processing.	
			String imageNBorder = ImgProcess.ImgAfterDeskewingWithoutBorder(filePath);
		    String finalImage = ImgProcess.ImgAfterRemovingBackground(filePath);

	        //Extract text from the image.
			//Imgtext ocr = new Imgtext(finalImage);
			//String fulltext = ocr.ExractText();
	        
			//System.out.println("Creating pdf document...");
			//TextPdf textpdf = new TextPdf(fulltext, "./ocrDemo.pdf");
			//System.out.println("Document created.");		
			//textpdf.MakeDocument();
			
			// configfileValue = 0->make the image visible, =1->make the image invisible
			CreatePdf createPdf = new CreatePdf(finalImage,"./textonly_pdf","0");
			createPdf.textOnlyPdf(finalImage);
			
	        System.out.println("getting the size and the location of the image from textonly_pdf");
			
			Path path = Paths.get("textOnly_pdf.pdf");
			String ExistingPdfFilePath= path.toAbsolutePath().toString();
	        String outputFilePath = "./newFile.pdf"; // New file
	        
	        GetImageLocationsAndSize.createPdfWithOriginalImage(ExistingPdfFilePath, outputFilePath, imageNBorder);
			
		}
		       
	
		
		
		
		
		
		
		
		
       
	
	 
        
	

	
		
	}

}
