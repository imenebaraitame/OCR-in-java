package ocr;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


public class Main {

	public static void main(String[] args) throws Exception {

		String filePath = Objects.requireNonNull(Main.class.getResource("/images/img002.pdf")).getPath();
		if (FileExtension.isPdf(filePath)){
			ExtractImage.takeImageFromPdf(filePath);
			ExtractImage.MergePdfDocuments(filePath);


			//Path imgPath = Paths.get("ExtractedImage_1.png");
			//String img = imgPath.toAbsolutePath().toString();


			//Image processing.
			//String imageNBorder = ImageProcess.ImgAfterDeskewingWithoutBorder(img);
			//String finalImage = ImageProcess.ImgAfterRemovingBackground(img);

			//Extract text from the image.
			//ImageText ocr = new ImageText(finalImage);
			//String fulltext = ocr.generateText();

			//System.out.println("Creating pdf document...");
			//TextPdf textpdf = new TextPdf(fulltext, "./ocrDemo.pdf");
			//System.out.println("Document created.");
			//textpdf.generateDocument();

			// configfileValue = 0->make the image visible, =1->make the image invisible
			//CreateSearchableImagePdf createPdf = new CreateSearchableImagePdf
					//(finalImage,"./textonly_pdf","0");
			//createPdf.textOnlyPdf(finalImage);

			//System.out.println("getting the size and the location of the image from textonly_pdf");

			//Path path = Paths.get("textonly_pdf.pdf");
			//String ExistingPdfFilePath= path.toAbsolutePath().toString();
			//String outputFilePath = "./newFile.pdf"; // New file

			//ImageLocationsAndSize.createPdfWithOriginalImage(ExistingPdfFilePath,
				//	outputFilePath, imageNBorder);

        
		}else {
			//Image processing.	
			String imageNBorder = ImageProcess.ImgAfterDeskewingWithoutBorder(filePath, 1);
		    String finalImage = ImageProcess.ImgAfterRemovingBackground(filePath,1);

	        //Extract text from the image.
			//ImageText ocr = new ImageText(finalImage);
			//String fulltext = ocr.generateText();
	        
			//System.out.println("Creating pdf document...");
			//TextPdf textpdf = new TextPdf(fulltext, "./ocrDemo.pdf");
			//System.out.println("Document created.");		
			//textpdf.generateDocument();
			
			// configfileValue = 0->make the image visible, =1->make the image invisible
			CreateSearchableImagePdf createPdf = new CreateSearchableImagePdf(finalImage,"./textonly_pdf_","0");
			createPdf.textOnlyPdf(finalImage, 1);
			
	        System.out.println("getting the size and the location of the image from textonly_pdf");
			
			Path path = Paths.get("textonly_pdf_1.pdf");
			String ExistingPdfFilePath= path.toAbsolutePath().toString();
	        String outputFilePath = "./newFile_1.pdf"; // New file
	        
	        ImageLocationsAndSize.createPdfWithOriginalImage(ExistingPdfFilePath, 
	        		                                         outputFilePath, imageNBorder);
			
		}
		       

		
	}


}
