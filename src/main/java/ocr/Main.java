package ocr;

<<<<<<< HEAD
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.DocumentException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.im4java.core.IM4JavaException;

public class Main {

    public static void main(String[] args) {
        //Image processing.
        String imagePath = Main.class.getResource("/images/test2.jpg").getPath();
        ImageProcess image = new ImageProcess(imagePath);
        String imageNBorder = null;
        String finalImage = null;
        try {
            String imageDeSkew = image.deskewImage(imagePath);
            imageNBorder = image.removeBorder(imageDeSkew);
            String blackNWhite = image.bufferedImage(imageNBorder);
            String imageMagick = image.magickManipulation(blackNWhite);
            finalImage = image.imageTransparent(imageNBorder, imageMagick);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        } catch (IM4JavaException e) {
            System.err.println("IM4JavaException: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

        //Extract text from the image.
        //Imgtext ocr = new Imgtext(finalImage);
        //String fulltext = ocr.text();

        //System.out.println("Creating pdf document...");
        //TextPdf textPdf = new TextPdf(fulltext, "./ocrDemo.pdf");
        //System.out.println("Document created.");
        //textPdf.document();

        //Place an invisible text layer on the top of the image and create a searchable pdf.
        // configfileValue = 0-> make the image visible, =1-> make the image invisible
        TesseractCmd cmd = new TesseractCmd(finalImage, "./textonly_pdf", "0");
        cmd.textOnlyPdf(finalImage);
        System.out.println("getting the size and the location of the image from textonly_pdf");
        Path path = Paths.get("textOnly_pdf.pdf");
        String inputFilePath = path.toAbsolutePath().toString();
        String outputFilePath = "./newFile.pdf"; // New file

        PDDocument document = null;
        try {
            document = PDDocument.load(new File(inputFilePath));
            ImageLocationsAndSize printer = new ImageLocationsAndSize();
            int pageNum = 0;
            PDPageTree docPages = document.getPages();
            for (PDPage page : docPages) {
                pageNum = pageNum + 1;
                printer.processPage(page);
            }
            System.out.println("Document created.");
            // Place the original image on top of transparent image in existing PDF.
            printer.merge(inputFilePath, outputFilePath, imageNBorder);
        } catch (DocumentException e) {
            System.err.println("DocumentException Exception: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
=======
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
>>>>>>> master

}
