package ocr;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.PDFStreamEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
  
import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.contentstream.operator.state.Restore;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import org.apache.pdfbox.contentstream.operator.state.SetMatrix;


public class GetImageLocationsAndSize extends PDFStreamEngine{
    
    public GetImageLocationsAndSize() throws IOException
    {
        // preparing PDFStreamEngine
        addOperator(new Concatenate());
        addOperator(new DrawObject());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new SetMatrix());
    }
    float imageYPosition; float imageXPosition; float imageXScale; float imageYScale;
    
    //@Override
	public void processOperator( Operator operator, List<COSBase> operands) throws IOException
    {  
		String operation = operator.getName();
        if( "Do".equals(operation) )
        {  
           COSName objectName = (COSName) operands.get( 0 );
	       PDXObject xobject = getResources().getXObject( objectName );
	       
            if(xobject  instanceof PDImageXObject) {
            	getImageScaleAndPosition();
            }
            else if(xobject instanceof PDFormXObject)
            {
                PDFormXObject form = (PDFormXObject)xobject ;
                showForm(form);
            }
        }
            else
            {
                super.processOperator( operator, operands);
            } 
        }
    
	
	private void getImageScaleAndPosition() {
		 Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
         // displayed size in user space units
             imageXScale = ctmNew.getScalingFactorX();
             imageYScale = ctmNew.getScalingFactorY();
          // position of image in the pdf in terms of user space units
             imageXPosition = ctmNew.getTranslateX();
             imageYPosition = ctmNew.getTranslateY();
		
	}
    


	public void PalceImageOnExistingPdf(String inputFilePath,String outputFilePath,String imgPath ) throws DocumentException, IOException {
	    OutputStream file = new FileOutputStream(new File(outputFilePath));

	    PdfReader pdfReader = new PdfReader(inputFilePath);
	    PdfStamper pdfStamper = new PdfStamper(pdfReader, file);
	    //Image to be added in existing pdf file.
	    Image Img = Image.getInstance(imgPath);
	    //Scale image's width and height
	    Img.scaleAbsolute(imageXScale, imageYScale);
	    //Set position for image in PDF
	    Img.setAbsolutePosition(imageXPosition,imageYPosition);
	    
	    
	     // loop on all the PDF pages
	     // i is the pdfPageNumber
	     for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
	           //getOverContent() allows you to write pdfContentByte on TOP of existing pdf pdfContentByte.
	      
	           PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
	           pdfContentByte.addImage(Img);
	           
	     }
	     System.out.println("Document will be created at: "+ new File(outputFilePath).getPath() );
	     pdfStamper.close();
	   
		}
	
	public static void createPdfWithOriginalImage(String ExistingPdfFilePath, String outputFilePath, String imageNBorder) 
			           throws IOException, DocumentException {
		   PDDocument document = null;
	        try {
	       	      
	       	document = PDDocument.load(new File(ExistingPdfFilePath));
	           GetImageLocationsAndSize printer = new GetImageLocationsAndSize();
	          
	           for( PDPage page : document.getPages() )
	           { 
	               printer.processPage(page);
	           }
	           document.close();
	           System.out.println("Document created.");
	           
	        //Place the original image on top of transparent image in existing PDF.
	           printer.PalceImageOnExistingPdf(ExistingPdfFilePath, outputFilePath, imageNBorder);
	        }
	        finally {
	       	 if(document != null)
	       	 {
	       		 document.close();
	       	 }
	        }
	}
}

