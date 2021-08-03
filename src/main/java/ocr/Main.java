package ocr;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;




public class Main {

	public static void main(String[] args) throws Exception {
		//Image processing.
		String imagePath = Main.class.getResource("/images/test2.jpg").getPath();
		ImgProcess image = new ImgProcess(imagePath );
		String imageDeskew = image.deskewImage(imagePath);
		String imageNBorder = image.removeBorder(imageDeskew);
		String blackNWhite = image.bufferedImage(imageNBorder);
		String imageMagick = image.magickManipulation(blackNWhite);
		String finalImage = image.imageTransparent(imageNBorder,imageMagick);
	    

        //Extract text from the image.
		//Imgtext ocr = new Imgtext(finalImage);
		//String fulltext = ocr.text();
		
		
		//System.out.println("Creating pdf document...");
		//TextPdf textpdf = new TextPdf(fulltext, "./ocrDemo.pdf");
		//System.out.println("Document created.");		
		//textpdf.document();
		
		//Place an invisible text layer on the top of the image and create a searchable pdf.
		// configfileValue = 0->make the image visible, =1->make the image invisible
		TesseractCmd cmd = new TesseractCmd(finalImage,"./textonly_pdf","0");
		cmd.textOnlyPdf(finalImage);
		
		
 
        System.out.println("getting the size and the location of the image from textonly_pdf");
		PDDocument document = null;
		Path path = Paths.get("textOnly_pdf.pdf");
		String inputFilePath= path.toAbsolutePath().toString();
        
        String outputFilePath = "./newFile.pdf"; // New file
		
         try {
        	      
        	document = PDDocument.load(new File(inputFilePath));
            GetImageLocationsAndSize printer = new GetImageLocationsAndSize();
            int pageNum = 0;
            for( PDPage page : document.getPages() )
            {
                pageNum = pageNum + 1;
                
                printer.processPage(page);
            }
            document.close();
            System.out.println("Document created.");
            
         //Place the original image on top of transparent image in existing PDF.
            printer.merge(inputFilePath, outputFilePath, imageNBorder);
         }
         finally {
        	 if(document != null)
        	 {
        		 document.close();
        	 }
         }
		
         
		
		
		
		
		
		
		
       
	
	 
        
	}

}
