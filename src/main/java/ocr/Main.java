package ocr;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


public class Main {

	public static void main(String[] args) throws Exception {

		String filePath = Objects.requireNonNull(Main.class.getResource("/images/img002.pdf")).getPath();
		if (FileExtension.isPdf(filePath)){
			ExtractImage.takeImageFromPdf(filePath);
			ExtractImage.MergePdfDocuments(filePath);

			/*
			//Loading an existing PDF document
			//Create PDFMergerUtility class object
			PDFMergerUtility PDFmerger = new PDFMergerUtility();

			//Setting the destination file path
			PDFmerger.setDestinationFileName("./merged.pdf");
			for (int i = 1; i < 4; i++) {
				File file1 = new File("./newFile_pdf_" + i + ".pdf");
				PDDocument document1 = PDDocument.load(file1);

				//adding the source files
				PDFmerger.addSource(file1);

				//Merging the documents
				PDFmerger.mergeDocuments(null);

				System.out.println("PDF Documents merged to a single file successfully");

                //Close documents
				document1.close();

			}

			 */


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
////////
			//System.out.println("getting the size and the location of the image from textonly_pdf");

			//Path path = Paths.get("textonly_pdf.pdf");
			//String ExistingPdfFilePath= path.toAbsolutePath().toString();
			//String outputFilePath = "./newFile.pdf"; // New file

			//ImageLocationsAndSize.createPdfWithOriginalImage(ExistingPdfFilePath,
				//	outputFilePath, imageNBorder);

        
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
