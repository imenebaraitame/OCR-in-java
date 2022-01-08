package ocr;

import com.recognition.software.jdeskew.ImageDeskew;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
 
import javax.imageio.ImageIO;

public class ExtractImage extends PDFStreamEngine {

    public static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;
    public static final String IMAGE_MAGICK_PATH;

    static {
        if (Utils.isWindows()){
            IMAGE_MAGICK_PATH = "D:\\ImageMagick-7.1.0-Q16-HDRI";
        } else {
            IMAGE_MAGICK_PATH = "/usr/bin/";
        }
    }

   public ExtractImage() throws IOException {
	   	
	    }
   
   
   public int imageNumber = 1;
   
   
   @Override 
   protected void processOperator( Operator operator, List<COSBase> operands) throws IOException
   {
       String operation = operator.getName();
       if( "Do".equals(operation) )
       {
           COSName objectName = (COSName) operands.get( 0 );
           PDXObject xobject = getResources().getXObject( objectName );
           if( xobject instanceof PDImageXObject)
           {
               PDImageXObject image = (PDImageXObject)xobject;

               // save image to local
               BufferedImage bImage = image.getImage();
               File file = new File("ExtractedImage_"+imageNumber+".png");
               ImageIO.write(bImage,"PNG",file);
               System.out.println("Image saved.");

               //deskew image
               BufferedImage bi = ImageIO.read(file);
               ImageDeskew id = new ImageDeskew(bi);
               double imageSkewAngle = id.getSkewAngle(); // determine skew angle
               if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
                   bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
               }
               String straightenImgPath = "./deskewImage_"+imageNumber+".png";
               ImageIO.write(bi, "png", new File(straightenImgPath));

               //remove border
               ProcessStarter.setGlobalSearchPath(IMAGE_MAGICK_PATH);
               IMOperation ope = new IMOperation();
               ope.addImage();
               ope.density(300);
               ope.bordercolor("black").border(1).fuzz (0.95).fill("white").draw("color 0,0 floodfill");
               ope.addImage();
               ConvertCmd cmd = new ConvertCmd();
               BufferedImage images =  ImageIO.read(new File(straightenImgPath));
               String outputImgNBorder = "./borderRemoved_"+imageNumber+".png";
               ImageIO.write(images, "png", new File(outputImgNBorder));
               try {
                   cmd.run(ope,straightenImgPath,outputImgNBorder);
               } catch (InterruptedException | IM4JavaException e) {
                   e.printStackTrace();
               }
               //binaryInverse
               ProcessStarter.setGlobalSearchPath(IMAGE_MAGICK_PATH);
               // create the operation, add images and operators/options
               IMOperation oper = new IMOperation();
               oper.addImage();
               oper.density(300);
               oper.format("png").monochrome().negate().fill("white").
                       fuzz(0.11).p_opaque("#000000").blur(1d,1d);
               oper.addImage();
               // execute the operation
               ConvertCmd cmdd = new ConvertCmd();
               BufferedImage img =  ImageIO.read(new File(outputImgNBorder));
               String outputImgBI = "./binaryInverseImg_"+imageNumber+".png";
               ImageIO.write(img, "png", new File(outputImgBI));
               try {
                   cmdd.run(oper,img,outputImgBI);
               } catch (InterruptedException | IM4JavaException e) {
                   e.printStackTrace();
               }
               //imageTransparent
               ProcessStarter.setGlobalSearchPath(IMAGE_MAGICK_PATH);
               IMOperation opera = new IMOperation();
               opera.addImage();
               opera.density(300);
               opera.addImage();
               opera.density(300);
               opera.alpha("off").compose("copy_opacity").composite();
               opera.addImage();
               ConvertCmd cmd3 = new ConvertCmd();
               BufferedImage IMG1 =  ImageIO.read(new File(outputImgNBorder));
               BufferedImage IMG2 =  ImageIO.read(new File(outputImgBI));
               String outputFile = "./transparentImg"+imageNumber+".png";
               ImageIO.write(IMG1,"png", new File(outputFile));
               ImageIO.write(IMG2,"png", new File(outputFile));
               try {
                   cmd3.run(opera,outputImgNBorder,outputImgBI,outputFile);
               } catch (InterruptedException | IM4JavaException e) {
                   e.printStackTrace();
               }
               //create a searchable PDF
               List<ITesseract.RenderedFormat> formats = new ArrayList<ITesseract.RenderedFormat>
                       (Arrays.asList(ITesseract.RenderedFormat.PDF));
               try {

                   Tesseract instance = new Tesseract();
                   //mode 6: Assume a single uniform block of text.
                   instance.setPageSegMode(6);
                   instance.setTessVariable("user_defined_dpi", "300");
                   instance.setDatapath(System.getenv("TESSDATA_PREFIX"));
                   instance.setLanguage("ara+eng");//set the English and Arabic languages
                   String configfileValue = "0";
                   instance.setTessVariable("textonly_pdf",configfileValue);
                   instance.createDocuments(new String[]{outputFile}, new String[]{"./textonly_pdf_"+imageNumber}, formats);

               } catch (TesseractException te){
                   System.err.println("Error TE: " + te.getMessage());
               }
               //Merge pdf in one pdf
               PDFMergerUtility obj = new PDFMergerUtility();
               // Setting the destination file path
               obj.setDestinationFileName("./Merged.pdf");
               // Add all source files, to be merged
               obj.addSource("./textonly_pdf_"+imageNumber+".pdf");
               // Merging documents
               obj.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
               System.out.println("Documents merged");

               System.out.println(
                       "PDF Documents merged to a single file");


               imageNumber++;

           }
           else if(xobject instanceof PDFormXObject)
           {
               PDFormXObject form = (PDFormXObject)xobject;
               showForm(form);
           }
       }
       else
       {
           super.processOperator( operator, operands);
       }
   }


   public static void takeImageFromPdf (String fileName) throws IOException {
	   PDDocument document = null;
       try
       {
           document = PDDocument.load( new File(fileName) );
           ExtractImage printer = new ExtractImage();
           int pageNum = 0;
           for( PDPage page : document.getPages() )
           {
               pageNum++;
               System.out.println( "Processing page: " + pageNum );
               printer.processPage(page);
           }
       }
       finally
       {
           if( document != null )
           {
               document.close();
           }
       }
	   
   }
      
	 
}